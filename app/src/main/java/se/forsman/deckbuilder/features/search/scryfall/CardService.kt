package se.forsman.deckbuilder.features.search.scryfall

import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface CardService {

    @GET("bulk-data/oracle-cards")
    fun getScryfallBulkData(): Single<BulkData>

    @Streaming
    @GET
    fun getCardsFromScryfall(@Url uri: String): Single<ResponseBody>

    @GET("symbology")
    fun getSymbology(): Single<SymbologyBulk>
}
