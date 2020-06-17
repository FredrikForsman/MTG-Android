package se.forsman.deckbuilder.features.search.scryfall

import androidx.room.Entity
import androidx.room.PrimaryKey

data class SymbologyBulk(
    val `object`: String?,
    val has_more: Boolean?,
    val data: List<Symbology>? = listOf()
)

@Entity(tableName = "symbology")
data class Symbology(
    val `object`: String?,
    val symbol: String?,
    @PrimaryKey
    val svg_uri: String
)