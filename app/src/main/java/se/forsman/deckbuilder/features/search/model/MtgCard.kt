package se.forsman.deckbuilder.features.search.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

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
    var typeLine: String?,
    @field:SerializedName("collector_number")
    var collectorNumber: String?
)