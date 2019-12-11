package se.forsman.deckbuilder.features.meta

import com.google.gson.annotations.SerializedName

data class Meta(
    val version: Int,
    val data: List<MetaDeck>)

data class MetaDeck(
    val name: String?,
    val did: Int?,
    val format: String?,
    val metashare: Double?,
    val performance: Double?,
    val price: Int?,
    val total: Int?)

data class MtgMetaDeckWrapper(
    val version: Int?,
    val data: MtgMetaDeck?
)

data class MtgMetaDeck(
    val name: String?,
    val metashare: Double?,
    val performance: Double?,
    @SerializedName("startdate")
    val startDate: String?,
    @SerializedName("enddate")
    val endDate: String,
    val format: String?,
    @SerializedName("latest_decklist")
    val deckList: DeckList
)

data class DeckList(
    @SerializedName("price_eur")
    val priceEuro: Double?,
    @SerializedName("price_usd")
    val priceDollar: Double?,
    val main: List<MtgMetaCard>?,
    val sideboard: List<MtgMetaCard>?
)

data class MtgMetaCard(val card: String, val quantity: Int)