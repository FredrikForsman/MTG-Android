package se.forsman.deckbuilder.features.search

import androidx.room.*
import io.reactivex.Single
import se.forsman.deckbuilder.features.search.model.MtgCard
import se.forsman.deckbuilder.features.search.scryfall.Symbology

@Dao
interface CardDao {

    @Transaction
    @Query("SELECT * FROM mtgcard")
    fun getAllCards(): Single<List<MtgCard>>

    @Transaction
    @Query("SELECT * FROM mtgcard WHERE name LIKE :query || '%'")
    fun searchCardsByName(query: String): Single<List<MtgCard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMagicCard(card: MtgCard)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSymbology(symbology: Symbology)

    @Transaction
    @Query("SELECT * from symbology")
    fun getSymbology(): Single<List<Symbology>>
}