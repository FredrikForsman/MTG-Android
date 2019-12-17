package se.forsman.deckbuilder.features.search

import io.reactivex.Single
import se.forsman.deckbuilder.features.search.model.MtgCard
import se.forsman.deckbuilder.features.search.model.SearchFilter

interface CardRepository {
    fun getAllStandardCards(): Single<List<MtgCard>>
    fun filterCards(filter: SearchFilter): Single<List<MtgCard>>
}