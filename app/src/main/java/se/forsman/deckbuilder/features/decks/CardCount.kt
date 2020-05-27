package se.forsman.deckbuilder.features.decks

import se.forsman.deckbuilder.features.search.model.MtgCard

data class CardCount(
    val card: MtgCard,
    val count: Int? = 0
)