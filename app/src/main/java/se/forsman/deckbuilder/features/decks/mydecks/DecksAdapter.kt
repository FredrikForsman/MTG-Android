package se.forsman.deckbuilder.features.decks.mydecks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_deck.view.*
import se.forsman.deckbuilder.R
import se.forsman.deckbuilder.core.extension.getColor
import se.forsman.deckbuilder.features.decks.Deck

class DecksAdapter : ListAdapter<Deck, DecksAdapter.ViewHolder>(DiffCallback()) {

    var onItemClick: ((Deck) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindTo(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_deck, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        fun bindTo(deck: Deck) {

            itemView.textDeckName.text = deck.name
            setColorsOfDeck(deck.getColor())

            itemView.setOnClickListener {
                onItemClick?.invoke(deck)
            }
        }

        private fun setColorsOfDeck(colors: Set<String>) {
            if (colors.contains("W")) itemView.imageWhite.visibility = View.VISIBLE
            if (colors.contains("G")) itemView.imageGreen.visibility = View.VISIBLE
            if (colors.contains("U")) itemView.imageBlue.visibility = View.VISIBLE
            if (colors.contains("B")) itemView.imageBlack.visibility = View.VISIBLE
            if (colors.contains("R")) itemView.imageRed.visibility = View.VISIBLE
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Deck>() {
        override fun areItemsTheSame(oldItem: Deck, newItem: Deck): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Deck, newItem: Deck): Boolean {
            return oldItem == newItem
        }

    }
}