package se.forsman.deckbuilder.features.decks.mydecks

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_deck.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import se.forsman.deckbuilder.R
import se.forsman.deckbuilder.core.app.BaseFragment
import se.forsman.deckbuilder.core.extension.*
import se.forsman.deckbuilder.features.decks.Deck
import se.forsman.deckbuilder.features.decks.DeckViewModel
import se.forsman.deckbuilder.features.decks.editdeck.DeckCardAdapter
import se.forsman.deckbuilder.features.decks.editdeck.DeckCardTextAdapter
import se.forsman.deckbuilder.features.decks.editdeck.EditDeckFragment
import se.forsman.deckbuilder.features.decks.editdeck.RemoveCardDialog

class DeckFragment : BaseFragment() {

    private val deckViewModel by sharedViewModel<DeckViewModel>()
    private val deckCardAdapter by inject<DeckCardAdapter>()
    private val deckCardTextAdapter by inject<DeckCardTextAdapter>()

    private val deckId: Long? by lazy {
        arguments?.get(ARG_DECK_NAME) as Long?
    }

    override fun layoutId(): Int = R.layout.fragment_deck

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(deckViewModel.getDeckToEdit(), this::renderDeck)
        failure(deckViewModel.getErrorMessage(), this::handleFailure)

        initView()
    }

    private fun initView() {

        deckViewModel.getDeckById(deckId)

        recyclerviewDeckList.adapter = deckCardTextAdapter
        deckCardTextAdapter.onItemClick = { position ->
            RemoveCardDialog.newInstance(position)
                .show(parentFragmentManager, "remove_card_dialog")
        }

        recyclerviewDeck.adapter = deckCardAdapter
        recyclerviewDeck.layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        deckCardAdapter.onItemClick = { position ->
            RemoveCardDialog.newInstance(position)
                .show(parentFragmentManager, "remove_card_dialog")
        }

        textEditDeck.setOnClickListener {
            parentFragmentManager.navigateTo(EditDeckFragment.newInstance(deckId), TAG_DECK)
        }

        imageNavigateBack.setOnClickListener {
            close()
        }
    }

    override fun onPause() {
        deckViewModel.getDeckToEdit().value?.let { deck ->
            deck.name = textDeckName.text.toString()
            deckViewModel.saveDeck(deck)
        }
        super.onPause()
    }

    private fun renderDeck(deck: Deck?) {
        deck?.let {
            textDeckName.setText(it.name)
            textCardsInDeck.text = String.format(getString(R.string.cards_in_deck), it.cards.size)
            textAverageValue.text = it.getAverageManaCost()
            setColorsOfDeck(it.getColor())
            deckCardAdapter.submitList(it.filterUnique())
            deckCardAdapter.notifyDataSetChanged()
            deckCardTextAdapter.submitList(it.filterUnique())
            deckCardTextAdapter.notifyDataSetChanged()
        }
    }

    private fun setColorsOfDeck(colors: Set<String>) {
        if (colors.contains("W")) imageWhite.visibility = View.VISIBLE else imageRed.visibility =
            View.GONE
        if (colors.contains("G")) imageGreen.visibility = View.VISIBLE else imageGreen.visibility =
            View.GONE
        if (colors.contains("U")) imageBlue.visibility = View.VISIBLE else imageBlue.visibility =
            View.GONE
        if (colors.contains("B")) imageBlack.visibility = View.VISIBLE else imageBlack.visibility =
            View.GONE
        if (colors.contains("R")) imageRed.visibility = View.VISIBLE else imageRed.visibility =
            View.GONE
    }

    companion object {
        private const val TAG_DECK = "DeckFragment"
        private const val ARG_DECK_NAME = "arg_deck_name"

        fun newInstance(deckId: Long?): DeckFragment {
            return DeckFragment().apply {
                deckId?.let { id ->
                    arguments = Bundle().apply {
                        this.putLong(ARG_DECK_NAME, id)
                    }
                }
            }
        }
    }
}