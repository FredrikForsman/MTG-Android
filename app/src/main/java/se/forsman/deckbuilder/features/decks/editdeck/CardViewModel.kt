package se.forsman.deckbuilder.features.decks.editdeck

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import se.forsman.deckbuilder.core.app.BaseViewModel
import se.forsman.deckbuilder.core.exception.Failure
import se.forsman.deckbuilder.features.search.CardRepository
import se.forsman.deckbuilder.features.search.model.MtgCard
import se.forsman.deckbuilder.features.search.model.SearchFilter

class CardViewModel(private val cardRepository: CardRepository) : BaseViewModel() {

    private val _cards = MutableLiveData<List<MtgCard>>()
    val cards: LiveData<List<MtgCard>> = _cards

    private val compositeDisposable = CompositeDisposable()

    fun loadCards() {
        compositeDisposable.add(
            cardRepository.getAllStandardCards()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { cards -> _cards.value = cards },
                    { handleFailure(Failure.DatabaseError) },
                ),
        )
    }

    fun filter(query: String, colors: Set<String>) {
        compositeDisposable.add(
            cardRepository.filterCards(SearchFilter(query, colors))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { cards ->
                        _cards.value = cards
                    },
                    { handleFailure(Failure.DatabaseError) },
                ),
        )
    }

    fun getSymbology() {
        compositeDisposable.add(
            cardRepository.getSymbology()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { symbology -> },
                    { error -> },
                ),
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
