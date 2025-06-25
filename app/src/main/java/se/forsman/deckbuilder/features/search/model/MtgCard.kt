package se.forsman.deckbuilder.features.search.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

@Entity(tableName = "mtgcard")
data class MtgCard(
    @PrimaryKey
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("set")
    val `set`: String?,
    @field:SerializedName("cmc")
    val cmc: Int?,
    @field:SerializedName("mana_cost")
    val manaCost: String?,
    @field:SerializedName("colors")
    val colors: List<String>?,
    @field:SerializedName("color_identity")
    val color_identity: List<String>,
    @field:SerializedName("art_crop")
    val imageArtOnly: String?,
    @field:SerializedName("imageUrl")
    val imageUrl: String?,
    @field:SerializedName("imageUrlLarge")
    val imageUrlLarge: String?,
    @field:SerializedName("type_line")
    val typeLine: String?,
    @field:SerializedName("collector_number")
    val collectorNumber: String?,
    @field:SerializedName("card_type")
    val cardType: CardType?
)

enum class CardType(val value: Int) {
    CREATURE(0),
    ARTIFACT(4),
    ENCHANTMENT(3),
    INSTANT(2),
    SORCERY(2),
    LAND(5),
    PLANESWALKER(1),
    OTHER(6)
}

object CardTypeConverter {

    @TypeConverter
    fun fromCardType(cardType: CardType?): String? {
        return cardType?.let {
            val gson = Gson()
            val type = object : TypeToken<CardType>() {

            }.type
            gson.toJson(cardType, type)
        }
    }

    @TypeConverter
    fun toCardType(cardTypeString: String?): CardType? {
        return cardTypeString?.let {
            val gson = Gson()
            val type = object : TypeToken<CardType>() {

            }.type
            gson.fromJson(cardTypeString, type)
        }
    }

}