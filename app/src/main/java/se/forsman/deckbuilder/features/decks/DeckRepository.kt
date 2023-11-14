package se.forsman.deckbuilder.features.decks

import io.reactivex.rxjava3.core.Single

interface DeckRepository {
    fun getDecks(): Single<List<Deck>>
    fun getDeckById(id: Long?): Single<Deck>
    fun saveDeck(deck: Deck): Single<Deck>
    fun deleteDeck(deck: Deck): Single<List<Deck>>
}
