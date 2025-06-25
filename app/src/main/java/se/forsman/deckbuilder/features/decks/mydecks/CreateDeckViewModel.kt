package se.forsman.deckbuilder.features.decks.mydecks

import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import se.forsman.deckbuilder.core.app.BaseViewModel
import se.forsman.deckbuilder.features.decks.Deck
import se.forsman.deckbuilder.features.decks.DeckRepository
import javax.inject.Inject

@HiltViewModel
class CreateDeckViewModel @Inject constructor(private val deckRepository: DeckRepository) :
    BaseViewModel() {

    private val _state = MutableStateFlow<DeckCreationState>(DeckCreationState.Loading)
    val state: StateFlow<DeckCreationState> = _state

    private val compositeDisposable = CompositeDisposable()

    fun createDeck(deck: Deck) {
        compositeDisposable.add(
            deckRepository.saveDeck(deck)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { createdDeck ->
                        createdDeck.id?.let { deckId ->
                            _state.value = DeckCreationState.Success(deckId)
                        } ?: kotlin.run {
                            _state.value =
                                DeckCreationState.Error(Throwable("Failed to created deck"))
                        }
                    },
                    { error ->
                        _state.value = DeckCreationState.Error(error)
                    },
                ),
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}

sealed class DeckCreationState {
    object Loading : DeckCreationState()
    data class Success(val deckId: Long) : DeckCreationState()
    data class Error(val error: Throwable) : DeckCreationState()
}
