package se.forsman.deckbuilder.features.search

import io.reactivex.rxjava3.core.Single
import se.forsman.deckbuilder.features.search.model.MtgCard
import se.forsman.deckbuilder.features.search.model.SearchFilter
import se.forsman.deckbuilder.features.search.scryfall.Symbology

interface CardRepository {
    fun getAllStandardCards(): Single<List<MtgCard>>
    fun filterCards(filter: SearchFilter): Single<List<MtgCard>>
    fun getSymbology(): Single<List<Symbology>>
}
