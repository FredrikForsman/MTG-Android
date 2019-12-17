package se.forsman.deckbuilder.features.search

import android.content.Context
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import se.forsman.deckbuilder.features.search.model.*

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

    override fun filterCards(filter: SearchFilter): Single<List<MtgCard>> {
        return cardDao.searchCardsByName(filter.query).map { cards ->
            if (filter.colors.isEmpty()) {
                cards
            } else {
                val listOfCards = mutableListOf<MtgCard>()
                cards.forEach { card ->
                    if (card.color_identity.any { it in filter.colors }) {
                        listOfCards.add(card)
                    }
                }
                listOfCards
            }
        }
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
                if (card.legalities.standard == "legal") {
                    val cardType = card.type_line
                    when {
                        cardType.contains("Creature") -> {
                            list.add(MtgCard(card.name, card.set, card.cmc.toInt(), card.colors, card.color_identity, card.image_uris.small, card.image_uris.large, cardType, card.collector_number, CardType.CREATURE))
                        }
                        cardType.contains("Artifact") -> {
                            list.add(MtgCard(card.name, card.set, card.cmc.toInt(), card.colors, card.color_identity, card.image_uris.small, card.image_uris.large, cardType, card.collector_number, CardType.ARTIFACT))
                        }
                        cardType.contains("Enchantment") -> {
                            list.add(MtgCard(card.name, card.set, card.cmc.toInt(), card.colors, card.color_identity, card.image_uris.small, card.image_uris.large, cardType, card.collector_number, CardType.ENCHANTMENT))
                        }
                        cardType.contains("Instant") -> {
                            list.add(MtgCard(card.name, card.set, card.cmc.toInt(), card.colors, card.color_identity, card.image_uris.small, card.image_uris.large, cardType, card.collector_number, CardType.INSTANT))
                        }
                        cardType.contains("Sorcery") -> {
                            list.add(MtgCard(card.name, card.set, card.cmc.toInt(), card.colors, card.color_identity, card.image_uris.small, card.image_uris.large, cardType, card.collector_number, CardType.SORCERY))
                        }
                        cardType.contains("Land") -> {
                            list.add(MtgCard(card.name, card.set, card.cmc.toInt(), card.colors, card.color_identity, card.image_uris.small, card.image_uris.large, cardType, card.collector_number, CardType.LAND))
                        }
                        cardType.contains("Planeswalker") -> {
                            list.add(MtgCard(card.name, card.set, card.cmc.toInt(), card.colors, card.color_identity, card.image_uris.small, card.image_uris.large, cardType, card.collector_number, CardType.PLANESWALKER))
                        }
                        else -> {
                            list.add(MtgCard(card.name, card.set, card.cmc.toInt(), card.colors, card.color_identity, card.image_uris.small, card.image_uris.large, cardType, card.collector_number, CardType.OTHER))
                        }
                    }
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