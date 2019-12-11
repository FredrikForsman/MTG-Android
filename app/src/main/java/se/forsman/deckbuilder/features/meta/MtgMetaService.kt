package se.forsman.deckbuilder.features.meta

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface MtgMetaService {

    @GET("decks/standard")
    fun getStandardMeta(): Single<Meta>

    @GET("decks/{id}")
    fun getDeckById(@Path("id") deckId: Int): Single<MtgMetaDeckWrapper>
}