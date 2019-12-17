package se.forsman.deckbuilder.features.search.model

data class SearchFilter(val query: String, val colors: Set<String>)