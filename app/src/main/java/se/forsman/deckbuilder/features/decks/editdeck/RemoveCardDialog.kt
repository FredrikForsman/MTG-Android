package se.forsman.deckbuilder.features.decks.editdeck

import android.os.Bundle
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_card.*
import se.forsman.deckbuilder.R
import se.forsman.deckbuilder.core.extension.filterUnique
import se.forsman.deckbuilder.core.extension.getCardsWithGivenName
import se.forsman.deckbuilder.core.extension.observe
import se.forsman.deckbuilder.features.search.model.MtgCard

class RemoveCardDialog : CardDialog() {

    private val cardPosition: Int by lazy {
        arguments?.get(ARG_CARDS_POSITION) as Int
    }

    override fun getCard(): MtgCard? = deckViewModel.getDeckToEdit().value?.filterUnique()?.get(cardPosition)?.card

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonHandleCard.text = getString(R.string.remove_card_deck)

        val listOfCards = mutableListOf<MtgCard>()

        getCard()?.let { card ->

            val deck = deckViewModel.getDeckToEdit().value
            deck?.let {
                listOfCards.addAll(it.getCardsWithGivenName(card.name))
            }

            Picasso.get()
                .load(card.imageUrlLarge)
                .error(R.drawable.card_back)
                .placeholder(R.drawable.card_back)
                .resize(252, 352)
                .into(imageCard)

            buttonHandleCard.setOnClickListener {
                deck?.let { d ->
                    d.cards.remove(listOfCards.lastOrNull())
                    listOfCards.remove(listOfCards.lastOrNull())
                    deckViewModel.saveDeck(d)
                }
            }
        }

        observe(deckViewModel.getDeckToEdit()) {
            when (listOfCards.size) {
                0 -> {
                    dismiss()
                }
                1 -> {
                    imageFirst.isSelected = true
                    imageSecond.isSelected = false
                    imageThird.isSelected = false
                    imageFourth.isSelected = false
                }
                2 -> {
                    imageFirst.isSelected = true
                    imageSecond.isSelected = true
                    imageThird.isSelected = false
                    imageFourth.isSelected = false
                }
                3 -> {
                    imageFirst.isSelected = true
                    imageSecond.isSelected = true
                    imageThird.isSelected = true
                    imageFourth.isSelected = false
                }
                4 -> {
                    imageFirst.isSelected = true
                    imageSecond.isSelected = true
                    imageThird.isSelected = true
                    imageFourth.isSelected = true
                }
                else -> {
                    imageFirst.isSelected = false
                    imageSecond.isSelected = false
                    imageThird.isSelected = false
                    imageFourth.isSelected = false
                }
            }
        }
    }

    companion object {
        private const val ARG_CARDS_POSITION = "arg_card_position"

        fun newInstance(position: Int): RemoveCardDialog {
            return RemoveCardDialog().apply {
                arguments = Bundle().apply {
                    this.putInt(ARG_CARDS_POSITION, position)
                }
            }
        }
    }
}