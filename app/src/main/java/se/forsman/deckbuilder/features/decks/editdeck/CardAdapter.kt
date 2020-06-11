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
import se.forsman.deckbuilder.features.search.model.MtgCard

class CardAdapter : ListAdapter<MtgCard, CardAdapter.ViewHolder>(DiffCallback()) {

    var onItemClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        fun bindTo(card: MtgCard) {
            Picasso.get()
                .load(card.imageUrlLarge)
                .error(R.drawable.card_back)
                .placeholder(R.drawable.card_back)
                .fit()
                .into(itemView.imageCard)

            itemView.setOnClickListener {
                onItemClick?.invoke(this.adapterPosition)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<MtgCard>() {
        override fun areItemsTheSame(oldItem: MtgCard, newItem: MtgCard): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: MtgCard, newItem: MtgCard): Boolean {
            return oldItem == newItem
        }
    }
}