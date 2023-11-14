package se.forsman.deckbuilder.features.decks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import io.reactivex.rxjava3.core.Single

@Dao
interface DeckDao {

    @Transaction
    @Query("SELECT * FROM magicdeck")
    fun getDecks(): Single<List<Deck>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveDeck(deck: Deck): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateDeck(deck: Deck)

    @Query("SELECT * FROM magicdeck WHERE id = :id")
    fun getDeckById(id: Long): Single<Deck>

    @Delete
    fun delete(deck: Deck)
}
