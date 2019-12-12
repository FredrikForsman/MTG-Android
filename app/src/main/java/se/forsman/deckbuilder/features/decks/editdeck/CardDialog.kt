package se.forsman.deckbuilder.features.decks.editdeck

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_card.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import se.forsman.deckbuilder.R
import se.forsman.deckbuilder.features.decks.DeckViewModel
import se.forsman.deckbuilder.features.search.model.MtgCard
import java.util.*

abstract class CardDialog : DialogFragment() {

    internal val deckViewModel by sharedViewModel<DeckViewModel>()

    abstract fun getCard(): MtgCard?

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setDimAmount(0.9f)
        return inflater.inflate(R.layout.dialog_card, container, false)
    }

    internal fun showNumberOfCardInDeck(cardsInDeck: List<MtgCard>, card: MtgCard) {
        when (Collections.frequency(cardsInDeck, card)) {
            0 -> {
                imageFirst.isSelected = false
                imageSecond.isSelected = false
                imageThird.isSelected = false
                imageFourth.isSelected = false
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