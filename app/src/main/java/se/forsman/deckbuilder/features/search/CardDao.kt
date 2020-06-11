package se.forsman.deckbuilder.features.search

import androidx.room.*
import io.reactivex.Single
import se.forsman.deckbuilder.features.search.model.MtgCard

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

}