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
import kotlinx.android.synthetic.main.dialog_remove_card.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import se.forsman.deckbuilder.R
import se.forsman.deckbuilder.features.decks.DeckViewModel
import se.forsman.deckbuilder.features.search.model.MtgCard

class RemoveCardDialog : DialogFragment() {

    private val cardPosition: Int by lazy {
        arguments?.get(ARG_CARDS_POSITION) as Int
    }

    private val deckViewModel by sharedViewModel<DeckViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setDimAmount(0.9f)
        return inflater.inflate(R.layout.dialog_remove_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val card = deckViewModel.getDeckToEdit().value?.cards?.get(cardPosition)

        val listOfCards = mutableListOf<MtgCard>()

        card?.let { c ->

            val deck = deckViewModel.getDeckToEdit().value
            deck?.cards?.filter { it.name == c.name }?.let {
                listOfCards.addAll(it)
            }


            Picasso.get()
                .load(c.imageUrlLarge)
                .error(R.drawable.card_back)
                .placeholder(R.drawable.card_back)
                .resize(252, 352)
                .into(imageCard)

            buttonRemoveCard.setOnClickListener {
                deck?.let { d ->
                    d.cards.remove(listOfCards.lastOrNull())
                    listOfCards.remove(listOfCards.lastOrNull())
                    deckViewModel.saveDeck(d)
                }
            }
        }

        deckViewModel.getDeckToEdit().observe(viewLifecycleOwner, Observer {
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
        })
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