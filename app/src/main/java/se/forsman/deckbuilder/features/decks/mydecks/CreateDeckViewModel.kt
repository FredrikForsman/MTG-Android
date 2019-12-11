package se.forsman.deckbuilder.features.decks.mydecks

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import se.forsman.deckbuilder.core.app.BaseViewModel
import se.forsman.deckbuilder.core.exception.Failure
import se.forsman.deckbuilder.features.decks.Deck
import se.forsman.deckbuilder.features.decks.DeckRepository

class CreateDeckViewModel(private val deckRepository: DeckRepository) : BaseViewModel() {

    private val deck = MutableLiveData<Deck>()
    fun getDeck(): LiveData<Deck> = deck

    private val compositeDisposable = CompositeDisposable()

    fun createDeck(deck: Deck) {
        compositeDisposable.add(
            deckRepository.saveDeck(deck)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { createdDeck -> this.deck.value = createdDeck },
                    { error ->
                        Log.e("DECK", "error creating deck: $error")
                        handleFailure(Failure.DatabaseError)
                     }
                )
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}