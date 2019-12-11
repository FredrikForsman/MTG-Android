package se.forsman.deckbuilder.core.extension

import android.util.Log
import se.forsman.deckbuilder.features.decks.Deck
import se.forsman.deckbuilder.features.search.model.MtgCard

fun Deck.getAverageManaCost(): String {
    var totalManaCost = 0
    val list = this.cards.filter { it.typeLine!!.contains("Land") }
    list.forEach { card ->
        card.cmc?.let { cmc ->
            totalManaCost += cmc
        }
    }

    return if (list.isEmpty()) "--" else String.format("%.1f", (totalManaCost.toDouble() / list.size.toDouble()))
}

fun Deck.getColor(): Set<String> {

    val set = mutableSetOf<String>()

    Log.d("DECK", "Got cards for deck, with name: ${this.name} ${this.cards.size}")

    this.cards.forEach { card ->
        if (card != null) {
            Log.d("DECK", "Got card for deck with name: ${card.name}")
            card.colors?.forEach { color ->
                set.add(color)
            }
        }
    }

    return set
}

fun Deck.getPrimaryCard(): MtgCard? {
    return this.cards.firstOrNull()
}