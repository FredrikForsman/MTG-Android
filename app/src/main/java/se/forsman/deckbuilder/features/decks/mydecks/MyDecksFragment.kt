package se.forsman.deckbuilder.features.decks.mydecks

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_my_decks.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.forsman.deckbuilder.R
import se.forsman.deckbuilder.core.app.BaseFragment
import se.forsman.deckbuilder.core.extension.close
import se.forsman.deckbuilder.core.extension.failure
import se.forsman.deckbuilder.core.extension.navigateTo
import se.forsman.deckbuilder.core.extension.observe
import se.forsman.deckbuilder.features.decks.Deck
import se.forsman.deckbuilder.features.decks.DeckViewModel

class MyDecksFragment : BaseFragment() {

    private val viewModel by viewModel<DeckViewModel>()
    private val adapter by inject<DecksAdapter>()

    override fun layoutId(): Int = R.layout.fragment_my_decks

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.getDecks(), this::showDecks)
        failure(viewModel.getErrorMessage(), this::handleFailure)

        initView()
    }

    private fun initView() {
        viewModel.loadDecks()

        recyclerviewMyDecks.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        recyclerviewMyDecks.adapter = adapter

        buttonCreateDeck.setOnClickListener {
            parentFragmentManager.navigateTo(CreateDeckFragment(), TAG_MY_DECKS)
        }

        imageNavigateBack.setOnClickListener {
            close()
        }

        adapter.onItemClick = { deck ->
            parentFragmentManager.navigateTo(DeckFragment.newInstance(deck.id), TAG_MY_DECKS)
        }

        adapter.onItemLongClick = { deck ->
            viewModel.deleteDeck(deck)
        }
    }

    private fun showDecks(decks: List<Deck>?) {
        decks?.let {
            adapter.submitList(decks)
        }
    }

    companion object {
        private const val TAG_MY_DECKS = "MyDecksFragment"
    }
}