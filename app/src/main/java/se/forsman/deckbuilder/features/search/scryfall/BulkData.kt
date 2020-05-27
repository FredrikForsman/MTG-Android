package se.forsman.deckbuilder.features.search.scryfall

import com.google.gson.annotations.SerializedName

data class BulkData(
    val `object`: String,
    val id: String,
    val type: String,
    @field:SerializedName("updated_at")
    val updatedAt: String,
    val uri: String,
    val name: String,
    val description: String,
    @field:SerializedName("compressed_size")
    val size: Double,
    @field:SerializedName("download_uri")
    val downloadUri: String,
    @field:SerializedName("content_type")
    val contentType: String,
    @field:SerializedName("content_encoding")
    val contentEncoding: String
)