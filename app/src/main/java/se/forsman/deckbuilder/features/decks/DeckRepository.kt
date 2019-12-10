package se.forsman.deckbuilder.features.decks

import io.reactivex.Observable
import io.reactivex.Single

interface DeckRepository {

    fun getDecks(): Observable<List<Deck>>
    fun getDeckById(id: Int?): Single<Deck>
    fun saveDeck(deck: Deck): Single<Deck>
}