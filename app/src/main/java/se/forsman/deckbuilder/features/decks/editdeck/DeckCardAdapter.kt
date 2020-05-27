package se.forsman.deckbuilder.features.decks.editdeck

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_card.view.*
import se.forsman.deckbuilder.R
import se.forsman.deckbuilder.features.decks.CardCount

class DeckCardAdapter : ListAdapter<CardCount, DeckCardAdapter.ViewHolder>(DiffCallback()) {

    var onItemClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindTo(cardCount: CardCount) {

            Picasso.get()
                .load(cardCount.card.imageUrlLarge)
                .error(R.drawable.card_back)
                .placeholder(R.drawable.card_back)
                .fit()
                .into(itemView.imageCard)

            showNumberOfCardInDeck(cardCount.count)

            itemView.setOnClickListener {
                onItemClick?.invoke(this.adapterPosition)
            }
        }

        internal fun showNumberOfCardInDeck(count: Int?) {
            when (count) {
                0 -> {
                    itemView.imageFirst.isSelected = false
                    itemView.imageSecond.isSelected = false
                    itemView.imageThird.isSelected = false
                    itemView.imageFourth.isSelected = false
                }
                1 -> {
                    itemView.imageFirst.isSelected = true
                    itemView.imageSecond.isSelected = false
                    itemView.imageThird.isSelected = false
                    itemView.imageFourth.isSelected = false
                }
                2 -> {
                    itemView.imageFirst.isSelected = true
                    itemView.imageSecond.isSelected = true
                    itemView.imageThird.isSelected = false
                    itemView.imageFourth.isSelected = false
                }
                3 -> {
                    itemView.imageFirst.isSelected = true
                    itemView.imageSecond.isSelected = true
                    itemView.imageThird.isSelected = true
                    itemView.imageFourth.isSelected = false
                }
                4 -> {
                    itemView.imageFirst.isSelected = true
                    itemView.imageSecond.isSelected = true
                    itemView.imageThird.isSelected = true
                    itemView.imageFourth.isSelected = true
                }
                else -> {
                    itemView.imageFirst.isSelected = false
                    itemView.imageSecond.isSelected = false
                    itemView.imageThird.isSelected = false
                    itemView.imageFourth.isSelected = false
                }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<CardCount>() {
        override fun areItemsTheSame(oldItem: CardCount, newItem: CardCount): Boolean {
            return oldItem.card.name == newItem.card.name
        }

        override fun areContentsTheSame(oldItem: CardCount, newItem: CardCount): Boolean {
            return oldItem == newItem
        }
    }

}