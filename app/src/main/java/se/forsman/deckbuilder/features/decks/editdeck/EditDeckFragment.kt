package se.forsman.deckbuilder.features.decks.editdeck

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_edit_deck.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import se.forsman.deckbuilder.R
import se.forsman.deckbuilder.core.app.BaseFragment
import se.forsman.deckbuilder.core.extension.*
import se.forsman.deckbuilder.features.decks.Deck
import se.forsman.deckbuilder.features.decks.DeckViewModel

class EditDeckFragment : BaseFragment() {

    private val deckViewModel by sharedViewModel<DeckViewModel>()
    private val adapter by inject<DeckCardAdapter>()
    private val deckId: Long? by lazy {
        arguments?.get(ARG_DECK_NAME) as Long?
    }

    override fun layoutId(): Int = R.layout.fragment_edit_deck

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(deckViewModel.getDeckToEdit(), this::renderDeck)
        failure(deckViewModel.getErrorMessage(), this::handleFailure)

        initView()
    }

    private fun initView() {

        deckViewModel.getDeckById(deckId)

        recyclerviewDeck.adapter = adapter
        recyclerviewDeck.layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)

        adapter.onItemClick = { position ->
            fragmentManager?.let {
                RemoveCardDialog.newInstance(position).show(it, "remove_card_dialog")
            }
        }

        buttonAddCards.setOnClickListener {
            fragmentManager?.navigateTo(AddCardDeckFragment.newInstance(deckId), TAG_EDIT_DECK)
        }

        imageNavigateBack.setOnClickListener {
            close()
        }
    }

    private fun renderDeck(deck: Deck?) {
        deck?.let {
            textCardsInDeck.text = String.format(getString(R.string.cards_in_deck), it.cards.size)
            adapter.submitList(it.getSortedByType())
            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        private const val TAG_EDIT_DECK = "EditDeckFragment"
        private const val ARG_DECK_NAME = "arg_deck_name"

        fun newInstance(deckId: Long?): EditDeckFragment {
            return EditDeckFragment().apply {
                deckId?.let { id ->
                    arguments = Bundle().apply {
                        this.putLong(ARG_DECK_NAME, id)
                    }
                }
            }
        }
    }

}