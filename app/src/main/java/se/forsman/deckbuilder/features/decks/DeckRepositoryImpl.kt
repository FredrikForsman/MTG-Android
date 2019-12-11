package se.forsman.deckbuilder.features.decks

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import se.forsman.deckbuilder.features.meta.Meta
import se.forsman.deckbuilder.features.meta.MtgMetaDeck
import se.forsman.deckbuilder.features.meta.MtgMetaDeckWrapper
import se.forsman.deckbuilder.features.meta.MtgMetaService
import se.forsman.deckbuilder.features.search.CardRepository
import se.forsman.deckbuilder.features.search.model.MtgCard
import java.util.concurrent.TimeUnit

class DeckRepositoryImpl(
    private val deckDao: DeckDao,
    private val mtgMetaService: MtgMetaService,
    private val cardRepository: CardRepository
) : DeckRepository {

    override fun getDecks(): Single<List<Deck>> {
        return deckDao.getDecks()
    }

    override fun getDeckById(id: Long?): Single<Deck> {
        return id?.let {
            deckDao.getDeckById(it)
        } ?: Single.error(Throwable("No deck with the given id"))
    }

    override fun saveDeck(deck: Deck): Single<Deck> {
        return Single.create {
            val id = deckDao.saveDeck(deck)
            if (id == -1L) {
                deckDao.updateDeck(deck)
                it.onSuccess(deck)
            } else {
                deckDao.getDeckById(id)
                    .subscribe { createdDeck ->
                        it.onSuccess(createdDeck)
                    }
            }
        }
    }

    override fun deleteDeck(deck: Deck): Single<List<Deck>> {
        return Completable.fromCallable {
            deckDao.delete(deck)
        }.andThen(deckDao.getDecks())
    }

    override fun getStandardMeta(): Single<Meta> {
        return mtgMetaService.getStandardMeta()
    }

    override fun getMetaDeckById(id: Int?): Single<MtgMetaDeckWrapper> {
        Log.d("DECK", "Getting meta deck by id: $id")
        return id?.let {
            mtgMetaService.getDeckById(it)
                .delay(200, TimeUnit.MILLISECONDS)
                .doAfterSuccess { wrapper ->
                    transformMetaDeck(wrapper.data)?.let { deck ->
                        Log.d("DECK", "saved deck: ${deck.name}")
                        saveDeck(deck)
                            .subscribeOn(Schedulers.io())
                            .subscribe(
                                { success -> Log.d("DECK", "successfully saved deck: ${success.name}")},
                                { error -> Log.e("DECK", "Got an error saving deck: $error")}
                            )
                    }
                }
        } ?: Single.error(Throwable("Meta deck id is null"))
    }

    private fun transformMetaDeck(deck: MtgMetaDeck?): Deck? {
        val deckList = mutableListOf<MtgCard>()
        Log.d("DECK", "Transforming deck with name: ${deck?.name}")
        deck?.deckList?.main?.let { cards ->
            if (cards.isNotEmpty()) {
                cards.forEach { card ->
                    for (i in 0 until card.quantity) {
                        val mtgCard = cardRepository.findCardByName(card.card)
                        if (mtgCard != null) {
                            deckList.add(mtgCard)
                        } else {
                            Log.d("DECK", "could not add card: ${card.card}")
                        }
                    }
                }
            }
        }
        return deck?.name?.let { name ->
            Deck(name, deckList)
        }
    }
}