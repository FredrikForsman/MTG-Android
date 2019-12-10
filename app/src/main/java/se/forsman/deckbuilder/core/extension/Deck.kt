package se.forsman.deckbuilder.core.extension

import se.forsman.deckbuilder.features.decks.Deck
import se.forsman.deckbuilder.features.search.model.MtgCard

fun Deck.getAverageManaCost(): String {
    var totalManaCost = 0
    val list = this.cards.filter { !it.typeLine!!.contains("Land") }
    list.forEach { card ->
        card.cmc?.let { cmc ->
            totalManaCost += cmc
        }
    }

    return if (list.isEmpty()) "--" else String.format("%.1f", (totalManaCost.toDouble() / list.size.toDouble()))
}

fun Deck.getColor(): Set<String> {

    val set = mutableSetOf<String>()

    this.cards.forEach { card ->
        card.color_identity.forEach { color ->
            set.add(color)
        }
    }

    return set
}

fun Deck.getPrimaryCard(): MtgCard? {
    return this.cards.firstOrNull()
}