package se.forsman.deckbuilder.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import se.forsman.deckbuilder.features.decks.Deck
import se.forsman.deckbuilder.features.decks.DeckDao
import se.forsman.deckbuilder.features.search.CardDao
import se.forsman.deckbuilder.features.search.model.CardTypeConverter
import se.forsman.deckbuilder.features.search.model.MtgCard

@Database(entities = [Deck::class, MtgCard::class], version = 1, exportSchema = false)
@TypeConverters(value = arrayOf(Converter::class, CardConverter::class, CardTypeConverter::class))
abstract class Database : RoomDatabase() {
    abstract fun deckDao(): DeckDao
    abstract fun scryfallDao(): CardDao
}

class Converter {

    @TypeConverter
    fun fromMagicCards(cards: List<String>?): String? {
        return cards?.let {
            val gson = Gson()
            val type = object : TypeToken<List<String>>() {

            }.type
            gson.toJson(cards, type)
        }
    }

    @TypeConverter
    fun toMagicCards(magicCardsString: String?): List<String>? {
        return magicCardsString?.let {
            val gson = Gson()
            val type = object : TypeToken<List<String>>() {

            }.type
            gson.fromJson(magicCardsString, type)
        }
    }

}

class CardConverter {

    @TypeConverter
    fun fromMagicCards(cards: List<MtgCard>?): String? {
        return cards?.let {
            val gson = Gson()
            val type = object : TypeToken<List<MtgCard>>() {

            }.type
            gson.toJson(cards, type)
        }
    }

    @TypeConverter
    fun toMagicCards(magicCardsString: String?): List<MtgCard>? {
        return magicCardsString?.let {
            val gson = Gson()
            val type = object : TypeToken<List<MtgCard>>() {

            }.type
            gson.fromJson(magicCardsString, type)
        }
    }

}