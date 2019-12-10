package se.forsman.deckbuilder.core.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import se.forsman.deckbuilder.core.database.Database
import se.forsman.deckbuilder.features.decks.DeckDao
import se.forsman.deckbuilder.features.decks.DeckRepository
import se.forsman.deckbuilder.features.decks.DeckRepositoryImpl
import se.forsman.deckbuilder.features.decks.DeckViewModel
import se.forsman.deckbuilder.features.decks.editdeck.CardAdapter
import se.forsman.deckbuilder.features.decks.editdeck.DeckCardAdapter
import se.forsman.deckbuilder.features.decks.editdeck.EditDeckViewModel
import se.forsman.deckbuilder.features.decks.mydecks.DecksAdapter
import se.forsman.deckbuilder.features.search.CardRepository
import se.forsman.deckbuilder.features.search.CardRepositoryImpl
import se.forsman.deckbuilder.features.search.model.CardDao

val applicationModule = module {
    single { Room.databaseBuilder((get()) as Context, Database::class.java, "magic_db").build() }
    single { Glide.with((get() as Context)) }
}

val searchModule = module {
    single { provideScryfallDao(get()) }
    single<CardRepository> { CardRepositoryImpl((get()) as Context, get(), gson) }
}

val myDecksModule = module {
    single<DeckRepository> {
        DeckRepositoryImpl(
            get()
        )
    }
    single { provideDeckDao(get()) }
    viewModel { DeckViewModel(get()) }
    factory { DecksAdapter() }
}

val editDeckModule = module {
    factory { DeckCardAdapter() }
    viewModel { EditDeckViewModel(get()) }
    factory { CardAdapter() }
}

private fun provideDeckDao(database: Database): DeckDao = database.deckDao()
private fun provideScryfallDao(database: Database): CardDao = database.scryfallDao()

private val gson: Gson = GsonBuilder().create()