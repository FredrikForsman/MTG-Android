package se.forsman.deckbuilder.features.decks

import io.reactivex.Single
import se.forsman.deckbuilder.features.meta.Meta
import se.forsman.deckbuilder.features.meta.MtgMetaDeckWrapper

interface DeckRepository {

    fun getDecks(): Single<List<Deck>>
    fun getDeckById(id: Long?): Single<Deck>
    fun saveDeck(deck: Deck): Single<Deck>
    fun deleteDeck(deck: Deck): Single<List<Deck>>
    fun getStandardMeta(): Single<Meta>
    fun getMetaDeckById(id: Int?): Single<MtgMetaDeckWrapper>
}