package se.forsman.deckbuilder.features.decks.editdeck

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_card.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import se.forsman.deckbuilder.R
import se.forsman.deckbuilder.core.extension.addCardIfPossible
import se.forsman.deckbuilder.features.search.model.MtgCard

class AddCardDialog : CardDialog() {

    override fun getCard(): MtgCard? = editDeckViewModel.getCards().value?.get(cardPosition)

    private val cardPosition: Int by lazy {
        arguments?.get(ARG_CARDS_POSITION) as Int
    }

    private val editDeckViewModel by sharedViewModel<EditDeckViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonHandleCard.text = getString(R.string.add_card_deck)

        deckViewModel.getDeckToEdit().observe(viewLifecycleOwner, Observer { deck ->
            getCard()?.let { card ->
                showNumberOfCardInDeck(deck.cards, card)
                buttonHandleCard.setOnClickListener {
                    deckViewModel.saveDeck(deck.addCardIfPossible(card))
                }

                Picasso.get()
                    .load(card.imageUrlLarge)
                    .error(R.drawable.card_back)
                    .placeholder(R.drawable.card_back)
                    .resize(252, 352)
                    .into(imageCard)
            }
        })
    }

    companion object {
        private const val ARG_CARDS_POSITION = "arg_card_position"

        fun newInstance(position: Int): AddCardDialog {
            return AddCardDialog().apply {
                arguments = Bundle().apply {
                    this.putInt(ARG_CARDS_POSITION, position)
                }
            }
        }
    }
}