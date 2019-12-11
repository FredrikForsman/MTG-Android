package se.forsman.deckbuilder.features.decks

import io.reactivex.Completable
import io.reactivex.Single

class DeckRepositoryImpl(private val deckDao: DeckDao) : DeckRepository {

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
}