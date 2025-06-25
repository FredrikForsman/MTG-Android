package se.forsman.deckbuilder.core.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import se.forsman.deckbuilder.core.database.Database
import se.forsman.deckbuilder.features.decks.DeckDao
import se.forsman.deckbuilder.features.decks.DeckRepository
import se.forsman.deckbuilder.features.decks.DeckRepositoryImpl
import se.forsman.deckbuilder.features.search.CardDao
import se.forsman.deckbuilder.features.search.CardRepository
import se.forsman.deckbuilder.features.search.CardRepositoryImpl
import se.forsman.deckbuilder.features.search.scryfall.CardService
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val SCRYFALL_BASE_URL = "https://api.scryfall.com/"


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SCRYFALL_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun providesCardService(retrofit: Retrofit): CardService = retrofit.create(CardService::class.java)

    @Provides
    @Singleton
    fun providesCardDao(database: Database): CardDao = database.scryfallDao()

    @Provides
    @Singleton
    fun providesDeckDao(database: Database): DeckDao = database.deckDao()

    @Provides
    @Singleton
    fun providesDataBase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, "magic_db").build()
    }

    @Provides
    @Singleton
    fun providesGson(): Gson = GsonBuilder().create()


    @Provides
    @Singleton
    fun providesCardRepository(
        cardService: CardService,
        cardDao: CardDao,
        gson: Gson
    ): CardRepository = CardRepositoryImpl(
        cardService,
        cardDao,
        gson
    )

    @Provides
    @Singleton
    fun providesDeckRepository(deckDao: DeckDao): DeckRepository = DeckRepositoryImpl(deckDao)

}
