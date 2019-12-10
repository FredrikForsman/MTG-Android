package se.forsman.deckbuilder.features.decks

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class DeckRepositoryImpl(private val deckDao: DeckDao) :
    DeckRepository {

    override fun getDecks(): Observable<List<Deck>> {
        return deckDao.getDecks()
    }

    override fun getDeckById(id: Int?): Single<Deck> {
        return id?.let {
            deckDao.getDeckById(it)
        } ?: Single.error(Throwable("No deck with the given id"))
    }

    override fun saveDeck(deck: Deck): Single<Deck> {
        return Completable.fromCallable {
            val id = deckDao.saveDeck(deck)
            if (id == -1L) {
                deckDao.updateDeck(deck)
            }
        }.andThen(deckDao.getDeckById(deck.id))
    }
}