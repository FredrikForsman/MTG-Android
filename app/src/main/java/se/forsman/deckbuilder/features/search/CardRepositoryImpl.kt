package se.forsman.deckbuilder.features.search

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import se.forsman.deckbuilder.features.search.model.CardType
import se.forsman.deckbuilder.features.search.model.MtgCard
import se.forsman.deckbuilder.features.search.model.ScryfallCard
import se.forsman.deckbuilder.features.search.model.SearchFilter
import se.forsman.deckbuilder.features.search.scryfall.CardService
import se.forsman.deckbuilder.features.search.scryfall.Symbology

class CardRepositoryImpl(
    private val cardService: CardService,
    private val cardDao: CardDao,
    private val gson: Gson,
) : CardRepository {

    override fun getAllStandardCards(): Single<List<MtgCard>> {
        return cardDao.getAllCards().flatMap { result ->
            if (result.isNullOrEmpty()) {
                getJsonFromScryfall()
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

    override fun getSymbology(): Single<List<Symbology>> {
        return cardDao.getSymbology()
    }

    private fun getJsonFromScryfall(): Single<List<MtgCard>> {
        return cardService.getScryfallBulkData().flatMap {
            cardService.getCardsFromScryfall(it.downloadUri).flatMap { response ->
                JsonReader(response.charStream())
                    .use { reader ->
                        parseJson(reader)?.let { cards ->
                            Single.just(cards)
                        } ?: Single.error(Throwable("Failed to fetch data"))
                    }
            }
        }
    }

    private fun parseJson(reader: JsonReader): List<MtgCard>? {
        return try {
            val list = mutableListOf<MtgCard>()

            reader.beginArray()

            var numberOfCards = 0

            while (reader.hasNext()) {
                val card = gson.fromJson<ScryfallCard>(
                    reader,
                    ScryfallCard::class.java,
                )
                if (card.legalities.legacy == "legal") {
                    val cardType = card.type_line
                    when {
                        cardType.contains("Creature") -> {
                            list.add(
                                MtgCard(
                                    card.name,
                                    card.set,
                                    card.cmc.toInt(),
                                    card.mana_cost,
                                    card.colors,
                                    card.color_identity,
                                    card.image_uris?.art_crop,
                                    card.image_uris?.small,
                                    card.image_uris?.border_crop,
                                    cardType,
                                    card.collector_number,
                                    CardType.CREATURE,
                                ),
                            )
                        }

                        cardType.contains("Artifact") -> {
                            list.add(
                                MtgCard(
                                    card.name,
                                    card.set,
                                    card.cmc.toInt(),
                                    card.mana_cost,
                                    card.colors,
                                    card.color_identity,
                                    card.image_uris?.art_crop,
                                    card.image_uris?.small,
                                    card.image_uris?.border_crop,
                                    cardType,
                                    card.collector_number,
                                    CardType.ARTIFACT,
                                ),
                            )
                        }

                        cardType.contains("Enchantment") -> {
                            list.add(
                                MtgCard(
                                    card.name,
                                    card.set,
                                    card.cmc.toInt(),
                                    card.mana_cost,
                                    card.colors,
                                    card.color_identity,
                                    card.image_uris?.art_crop,
                                    card.image_uris?.small,
                                    card.image_uris?.border_crop,
                                    cardType,
                                    card.collector_number,
                                    CardType.ENCHANTMENT,
                                ),
                            )
                        }

                        cardType.contains("Instant") -> {
                            list.add(
                                MtgCard(
                                    card.name,
                                    card.set,
                                    card.cmc.toInt(),
                                    card.mana_cost,
                                    card.colors,
                                    card.color_identity,
                                    card.image_uris?.art_crop,
                                    card.image_uris?.small,
                                    card.image_uris?.border_crop,
                                    cardType,
                                    card.collector_number,
                                    CardType.INSTANT,
                                ),
                            )
                        }

                        cardType.contains("Sorcery") -> {
                            list.add(
                                MtgCard(
                                    card.name,
                                    card.set,
                                    card.cmc.toInt(),
                                    card.mana_cost,
                                    card.colors,
                                    card.color_identity,
                                    card.image_uris?.art_crop,
                                    card.image_uris?.small,
                                    card.image_uris?.border_crop,
                                    cardType,
                                    card.collector_number,
                                    CardType.SORCERY,
                                ),
                            )
                        }

                        cardType.contains("Land") -> {
                            list.add(
                                MtgCard(
                                    card.name,
                                    card.set,
                                    card.cmc.toInt(),
                                    card.mana_cost,
                                    card.colors,
                                    card.color_identity,
                                    card.image_uris?.art_crop,
                                    card.image_uris?.small,
                                    card.image_uris?.border_crop,
                                    cardType,
                                    card.collector_number,
                                    CardType.LAND,
                                ),
                            )
                        }

                        cardType.contains("Planeswalker") -> {
                            list.add(
                                MtgCard(
                                    card.name,
                                    card.set,
                                    card.cmc.toInt(),
                                    card.mana_cost,
                                    card.colors,
                                    card.color_identity,
                                    card.image_uris?.art_crop,
                                    card.image_uris?.small,
                                    card.image_uris?.border_crop,
                                    cardType,
                                    card.collector_number,
                                    CardType.PLANESWALKER,
                                ),
                            )
                        }

                        else -> {
                            list.add(
                                MtgCard(
                                    card.name,
                                    card.set,
                                    card.cmc.toInt(),
                                    card.mana_cost,
                                    card.colors,
                                    card.color_identity,
                                    card.image_uris?.art_crop,
                                    card.image_uris?.small,
                                    card.image_uris?.border_crop,
                                    cardType,
                                    card.collector_number,
                                    CardType.OTHER,
                                ),
                            )
                        }
                    }
                    numberOfCards++
                }
            }
            reader.endArray()
            list
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
