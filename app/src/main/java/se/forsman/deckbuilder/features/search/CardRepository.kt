package se.forsman.deckbuilder.features.search

import io.reactivex.Single
import se.forsman.deckbuilder.features.search.model.MtgCard

interface CardRepository {
    fun getAllStandardCards(): Single<List<MtgCard>>
    fun searchCardByName(query: String): Single<List<MtgCard>>
    fun findCardByName(name: String): MtgCard?
}