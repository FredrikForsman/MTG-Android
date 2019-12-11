package se.forsman.deckbuilder.features.search.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
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
    @field:SerializedName("colors")
    val colors: List<String>?,
    @field:SerializedName("color_identity")
    val color_identity: List<String>,
    @field:SerializedName("imageUrl")
    val imageUrl: String?,
    @field:SerializedName("imageUrlLarge")
    val imageUrlLarge: String?,
    @field:SerializedName("type_line")
    val typeLine: String?,
    @field:SerializedName("collector_number")
    val collectorNumber: String?,
    @field:SerializedName("card_type")
    @TypeConverters(CardTypeConverter::class)
    val cardType: CardType?
)

enum class CardType {
    CREATURE,
    ARTIFACT,
    ENCHANTMENT,
    INSTANT,
    SORCERY,
    LAND,
    PLANESWALKER,
    OTHER
}

class CardTypeConverter {

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