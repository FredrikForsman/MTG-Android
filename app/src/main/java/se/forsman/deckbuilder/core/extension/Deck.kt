package se.forsman.deckbuilder.core.extension

import se.forsman.deckbuilder.features.decks.CardCount
import se.forsman.deckbuilder.features.decks.Deck
import se.forsman.deckbuilder.features.search.model.MtgCard
import java.util.*

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
        card.colors?.forEach { color ->
            set.add(color)
        }
    }

    return set
}

fun Deck.addCardIfPossible(card: MtgCard): Deck? {
    val occurrences = Collections.frequency(this.cards, card)
    return card.typeLine?.let { type ->
        when {
            type.startsWith("Basic") -> {
                this.cards.add(card)
                this
            }
            occurrences < 4 -> {
                this.cards.add(card)
                this
            }
            else -> null
        }
    }
}

fun Deck.getCardsWithGivenName(name: String): List<MtgCard> {
    return this.cards.filter { card -> card.name == name }
}

fun Deck.getSortedByType(): MutableList<MtgCard> {
    return this.cards.sortedWith(compareBy({ it.cardType?.value }, { it.cmc })).toMutableList()
}

fun Deck.filterUnique(): List<CardCount> {
    val cards = mutableListOf<CardCount>()
    this.cards.forEach { card ->
        cards.add(CardCount(card, Collections.frequency(this.cards, card)))
    }
    return cards.distinct()
        .sortedWith(compareBy({it.card.cardType?.value}, {it.card.cmc}))
}