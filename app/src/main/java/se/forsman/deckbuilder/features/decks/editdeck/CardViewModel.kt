package se.forsman.deckbuilder.features.decks.editdeck

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import se.forsman.deckbuilder.core.app.BaseViewModel
import se.forsman.deckbuilder.core.exception.Failure
import se.forsman.deckbuilder.features.search.CardRepository
import se.forsman.deckbuilder.features.search.model.MtgCard
import se.forsman.deckbuilder.features.search.model.SearchFilter

class CardViewModel(private val cardRepository: CardRepository) : BaseViewModel() {

    private val cards = MutableLiveData<List<MtgCard>>()
    fun getCards(): LiveData<List<MtgCard>> = cards

    private val compositeDisposable = CompositeDisposable()

    fun loadCards() {
        compositeDisposable.add(
            cardRepository.getAllStandardCards()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { cards -> this.cards.value = cards },
                    { handleFailure(Failure.DatabaseError)}
                )
        )
    }

    fun filter(query: String, colors: Set<String>) {
        compositeDisposable.add(
            cardRepository.filterCards(SearchFilter(query, colors))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { cards ->
                        this.cards.value = cards
                    },
                    { handleFailure(Failure.DatabaseError) }
                )
        )
    }
}