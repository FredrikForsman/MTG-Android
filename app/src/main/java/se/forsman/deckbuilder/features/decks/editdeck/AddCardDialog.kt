package se.forsman.deckbuilder.features.decks.editdeck

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_add_card.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import se.forsman.deckbuilder.R
import se.forsman.deckbuilder.features.decks.DeckViewModel
import java.util.*

class AddCardDialog : DialogFragment() {

    private val cardPosition: Int by lazy {
        arguments?.get(ARG_CARDS_POSITION) as Int
    }

    private val editDeckViewModel by sharedViewModel<EditDeckViewModel>()
    private val deckViewModel by sharedViewModel<DeckViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setDimAmount(0.9f)
        return inflater.inflate(R.layout.dialog_add_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deckViewModel.getDeckToEdit().observe(viewLifecycleOwner, Observer { deck ->
            editDeckViewModel.getCards().value?.let { allCards ->
                val card = allCards[cardPosition]
                val occurrences = Collections.frequency(deck.cards, card)
                when (occurrences) {
                    1 -> imageFirst.isSelected = true
                    2 -> {
                        imageFirst.isSelected = true
                        imageSecond.isSelected = true
                    }
                    3 -> {
                        imageFirst.isSelected = true
                        imageSecond.isSelected = true
                        imageThird.isSelected = true
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

                buttonAddCard.setOnClickListener {
                    card.typeLine?.let { type ->
                        if (type.startsWith("Basic")) {
                            deck.cards.add(card)
                            deckViewModel.saveDeck(deck)
                        } else if (occurrences < 4) {
                            deck.cards.add(card)
                            deckViewModel.saveDeck(deck)
                        }
                    }
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