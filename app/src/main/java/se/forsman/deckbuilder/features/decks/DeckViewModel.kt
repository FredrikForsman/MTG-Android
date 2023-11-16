package se.forsman.deckbuilder.features.decks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import se.forsman.deckbuilder.core.app.BaseViewModel
import se.forsman.deckbuilder.core.exception.Failure

class DeckViewModel(private val deckRepository: DeckRepository) : BaseViewModel() {

    private val _decks = MutableStateFlow<List<Deck>>(emptyList())
    val decks: StateFlow<List<Deck>> = _decks

    private val deckToEdit = MutableLiveData<Deck>()
    fun getDeckToEdit(): LiveData<Deck> = deckToEdit

    private val compositeDisposable = CompositeDisposable()

    fun loadDecks() {
        compositeDisposable.add(
            deckRepository.getDecks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { decks -> _decks.value = decks },
                    { handleFailure(Failure.DatabaseError) },
                ),
        )
    }

    fun getDeckById(id: Long?) {
        compositeDisposable.add(
            deckRepository.getDeckById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { deck -> deckToEdit.value = deck },
                    { handleFailure(Failure.DatabaseError) },
                ),
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
                        { handleFailure(Failure.DatabaseError) },
                    ),
            )
        }
    }

    fun deleteDeck(deck: Deck?) {
        deck?.let {
            compositeDisposable.add(
                deckRepository.deleteDeck(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { decks -> _decks.value = decks },
                        { handleFailure(Failure.DatabaseError) },
                    ),
            )
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
