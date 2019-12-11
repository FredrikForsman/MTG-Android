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
import se.forsman.deckbuilder.features.decks.editdeck.DeckCardAdapter
import se.forsman.deckbuilder.features.decks.editdeck.EditDeckViewModel
import se.forsman.deckbuilder.features.decks.mydecks.CreateDeckViewModel
import se.forsman.deckbuilder.features.decks.mydecks.DecksAdapter
import se.forsman.deckbuilder.features.meta.MtgMetaService
import se.forsman.deckbuilder.features.search.CardRepository
import se.forsman.deckbuilder.features.search.CardRepositoryImpl
import se.forsman.deckbuilder.features.search.model.CardDao
import java.util.concurrent.TimeUnit

val applicationModule = module {
    single { Room.databaseBuilder((get()) as Context, Database::class.java, "magic_db").build() }
    single { Glide.with((get() as Context)) }
    single { provideRetrofit(get()) }
    factory { provideOkHttpClient() }
    factory<MtgMetaService> { (get() as Retrofit).create(MtgMetaService::class.java) }
}

val searchModule = module {
    single { provideScryfallDao(get()) }
    single<CardRepository> { CardRepositoryImpl((get()) as Context, get(), gson) }
}

val decksModule = module {
    single<DeckRepository> { DeckRepositoryImpl(get(), get(), get()) }
    single { provideDeckDao(get()) }
    viewModel { DeckViewModel(get()) }
    viewModel { EditDeckViewModel(get()) }
    viewModel { CreateDeckViewModel(get()) }

    factory { DecksAdapter() }
    factory { DeckCardAdapter() }
    factory { CardAdapter() }
}


private fun provideDeckDao(database: Database): DeckDao = database.deckDao()
private fun provideScryfallDao(database: Database): CardDao = database.scryfallDao()
private val gson: Gson = GsonBuilder().create()
private fun provideRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(MTGMETA_BASE_URL)
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}
private fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
//        .addInterceptor(HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
}

private const val MTGMETA_BASE_URL = "https://mtgmeta.io/api/"