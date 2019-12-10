package se.forsman.deckbuilder.features.search.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface CardDao {

    @Query("SELECT * FROM mtgcard")
    fun getAllCards(): Single<List<MtgCard>>

    @Query("SELECT * FROM mtgcard WHERE name LIKE :query || '%'")
    fun searchCardsByName(query: String): Single<List<MtgCard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMagicCard(card: MtgCard)

}