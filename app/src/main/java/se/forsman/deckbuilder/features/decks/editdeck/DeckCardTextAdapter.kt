package se.forsman.deckbuilder.features.decks.editdeck

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_card_text.view.*
import se.forsman.deckbuilder.R
import se.forsman.deckbuilder.features.decks.CardCount

class DeckCardTextAdapter : ListAdapter<CardCount, DeckCardTextAdapter.ViewHolder>(DiffCallback()) {

    var onItemClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_card_text, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindTo(cardCount: CardCount) {

            Picasso.get()
                .load(cardCount.card.imageArtOnly)
                .error(R.drawable.card_back)
                .fit()
                .centerCrop(Gravity.TOP)
                .into(itemView.imageBackground)
            itemView.textCardName.text = cardCount.card.name
            itemView.textCardCount.text = cardCount.count.toString()
            itemView.textCardManaCost.text = cardCount.card.manaCost

            itemView.setOnClickListener {
                onItemClick?.invoke(this.adapterPosition)
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
