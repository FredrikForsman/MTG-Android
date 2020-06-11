package se.forsman.deckbuilder.features.decks

import androidx.room.*
import io.reactivex.Single

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