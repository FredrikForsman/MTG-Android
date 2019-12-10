package se.forsman.deckbuilder.features.search

import android.content.Context
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import se.forsman.deckbuilder.features.search.model.CardDao
import se.forsman.deckbuilder.features.search.model.MtgCard
import se.forsman.deckbuilder.features.search.model.ScryfallCard

class CardRepositoryImpl(
    private val context: Context,
    private val cardDao: CardDao,
    private val gson: Gson) : CardRepository {

    override fun getAllStandardCards(): Single<List<MtgCard>> {
        return cardDao.getAllCards().flatMap { result ->
            if (result.isNullOrEmpty()) {
                getStandardCardsFromScryfall()
                    .subscribeOn(Schedulers.io())
                    .doAfterSuccess { cards ->
                        cards.forEach { card ->
                            this.cardDao.insertMagicCard(card)
                        }
                    }
            } else {
                Single.just(result)
            }
        }
    }

    override fun searchCardByName(query: String): Single<List<MtgCard>> {
        return cardDao.searchCardsByName(query)
    }

    private fun getStandardCardsFromScryfall(): Single<List<MtgCard>> {
        val list = mutableListOf<MtgCard>()
        return try {
            val jsonReader = JsonReader(
                context.assets.open("scryfall-oracle-cards.json").bufferedReader())

            jsonReader.beginArray()

            var numberOfCards = 0

            while (jsonReader.hasNext()) {
                val card = gson.fromJson<ScryfallCard>(jsonReader, ScryfallCard::class.java)
                if (card.legalities.standard.equals("legal")) {
                    list.add(MtgCard(card.name, card.set, card.cmc.toInt(), card.colors, card.color_identity, card.image_uris.small, card.image_uris.large, card.type_line, card.collector_number))
                    numberOfCards++
                }
            }
            jsonReader.endArray()

            Single.just(list)
        } catch (e: Exception) {
            Single.error(Throwable(e.localizedMessage))
        }
    }
}