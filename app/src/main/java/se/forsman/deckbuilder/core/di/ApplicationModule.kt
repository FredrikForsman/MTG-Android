package se.forsman.deckbuilder.core.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import se.forsman.deckbuilder.core.database.Database
import se.forsman.deckbuilder.features.decks.DeckDao
import se.forsman.deckbuilder.features.decks.DeckRepository
import se.forsman.deckbuilder.features.decks.DeckRepositoryImpl
import se.forsman.deckbuilder.features.decks.DeckViewModel
import se.forsman.deckbuilder.features.decks.editdeck.CardAdapter
import se.forsman.deckbuilder.features.decks.editdeck.CardViewModel
import se.forsman.deckbuilder.features.decks.editdeck.DeckCardAdapter
import se.forsman.deckbuilder.features.decks.mydecks.CreateDeckViewModel
import se.forsman.deckbuilder.features.decks.mydecks.DecksAdapter
import se.forsman.deckbuilder.features.search.CardRepository
import se.forsman.deckbuilder.features.search.CardRepositoryImpl
import se.forsman.deckbuilder.features.search.CardDao
import se.forsman.deckbuilder.features.search.scryfall.CardService
import java.util.concurrent.TimeUnit

val applicationModule = module {
    single { Room.databaseBuilder((get()) as Context, Database::class.java, "magic_db").build() }
    single { Glide.with((get() as Context)) }
}

val networkModule = module {
    factory { provideOkHttpClient() }
}

val searchModule = module {
    single { provideRetrofit(get(), SCRYFALL_BASE_URL) }
    factory { createWebService<CardService>(get()) }
    single { provideScryfallDao(get()) }
    single<CardRepository> { CardRepositoryImpl(get(), get(), gson) }
}

val decksModule = module {
    single<DeckRepository> { DeckRepositoryImpl(get()) }
    single { provideDeckDao(get()) }
    viewModel { DeckViewModel(get()) }
    viewModel { CardViewModel(get()) }
    viewModel { CreateDeckViewModel(get()) }

    factory { DecksAdapter() }
    factory { DeckCardAdapter() }
    factory { CardAdapter() }
}

inline fun <reified T> createWebService(retrofit: Retrofit): T = retrofit.create(T::class.java)

private fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}

fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .build()
}

private fun provideDeckDao(database: Database): DeckDao = database.deckDao()
private fun provideScryfallDao(database: Database): CardDao = database.scryfallDao()

private val gson: Gson = GsonBuilder().create()

private const val SCRYFALL_BASE_URL = "https://api.scryfall.com/bulk-data/"