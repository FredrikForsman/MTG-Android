package se.forsman.deckbuilder.features.decks

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import se.forsman.deckbuilder.core.database.CardConverter
import se.forsman.deckbuilder.features.search.model.MtgCard

@Entity(tableName = "magicdeck")
data class Deck(
    @field:SerializedName("name")
    var name: String,
    @field:SerializedName("cards")
    @TypeConverters(CardConverter::class)
    val cards: MutableList<MtgCard> = arrayListOf()
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var id: Long? = null
}