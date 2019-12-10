package se.forsman.deckbuilder.features.search.model

data class Legalities(
    val brawl: String,
    val commander: String,
    val duel: String,
    val future: String,
    val historic: String,
    val legacy: String,
    val modern: String,
    val oldschool: String,
    val pauper: String,
    val penny: String,
    val pioneer: String,
    val standard: String,
    val vintage: String
)