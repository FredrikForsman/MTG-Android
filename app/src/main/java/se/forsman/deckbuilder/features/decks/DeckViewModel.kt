package se.forsman.deckbuilder.features.decks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import se.forsman.deckbuilder.core.app.BaseViewModel
import se.forsman.deckbuilder.core.exception.Failure

class DeckViewModel(private val deckRepository: DeckRepository) : BaseViewModel() {

    private val decks = MutableLiveData<List<Deck>>()
    fun getDecks(): LiveData<List<Deck>> = decks

    private val deckToEdit = MutableLiveData<Deck>()
    fun getDeckToEdit(): LiveData<Deck> = deckToEdit

    private val compositeDisposable = CompositeDisposable()

    fun loadDecks() {
        compositeDisposable.add(
            deckRepository.getDecks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { decks -> this.decks.value = decks },
                    { handleFailure(Failure.DatabaseError) }
                )
        )
    }

    fun getDeckById(id: Int?) {
        compositeDisposable.add(
            deckRepository.getDeckById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { deck -> deckToEdit.value = deck },
                    { deckToEdit.value = Deck(0, "Unnamed", arrayListOf()) }
                )
        )
    }

    fun saveDeck(deck: Deck?) {
        deck?.let {
            compositeDisposable.add(
                deckRepository.saveDeck(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { deck -> deckToEdit.value = deck },
                        { handleFailure(Failure.DatabaseError) }
                    )
            )
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}