package se.forsman.deckbuilder.features.decks.editdeck

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_add_card.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import se.forsman.deckbuilder.R
import se.forsman.deckbuilder.core.app.BaseFragment
import se.forsman.deckbuilder.core.extension.afterTextChanged
import se.forsman.deckbuilder.core.extension.failure
import se.forsman.deckbuilder.core.extension.observe
import se.forsman.deckbuilder.features.search.model.MtgCard
import java.util.concurrent.TimeUnit

class AddCardDeckFragment : BaseFragment() {

    private val cardViewModel by sharedViewModel<CardViewModel>()
    private val adapter by inject<CardAdapter>()
    private val compositeDisposable = CompositeDisposable()

    override fun layoutId(): Int = R.layout.fragment_add_card

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(cardViewModel.getCards(), this::showCards)
        failure(cardViewModel.getErrorMessage(), this::handleFailure)

        initView()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private fun initView() {
        cardViewModel.loadCards()

        recyclerviewCards.adapter = adapter
        recyclerviewCards.layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)

        adapter.onItemClick = { position ->
            fragmentManager?.let {
                AddCardDialog.newInstance(position).show(it, "add_card_dialog")
            }
        }

        compositeDisposable.add(editTextSearch.afterTextChanged()
            .debounce(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { query ->
                cardViewModel.search(query)
            })
    }

    private fun showCards(cards: List<MtgCard>?) {
        cards?.let {
            adapter.submitList(cards)
        }
    }

    companion object {
        private const val ARG_DECK_NAME = "arg_deck_name"

        fun newInstance(deckId: Long?): AddCardDeckFragment {
            return AddCardDeckFragment().apply {
                deckId?.let { id ->
                    arguments = Bundle().apply {
                        this.putLong(ARG_DECK_NAME, id)
                    }
                }
            }
        }
    }

}