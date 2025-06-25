package se.forsman.deckbuilder.features.search.scryfall

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class SymbologyBulk(
    val `object`: String?,
    val has_more: Boolean?,
    val data: List<Symbology>? = listOf()
)

@Entity(tableName = "symbology")
data class Symbology(
    @field:SerializedName("object")
    val objectType: String?,  // ✅ safe and works with Gson
    val symbol: String?,
    @PrimaryKey
    val svg_uri: String
)