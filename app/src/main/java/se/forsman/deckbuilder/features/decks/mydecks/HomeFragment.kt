package se.forsman.deckbuilder.features.decks.mydecks

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.forsman.deckbuilder.R
import se.forsman.deckbuilder.core.app.BaseFragment
import se.forsman.deckbuilder.core.extension.failure
import se.forsman.deckbuilder.core.extension.navigateTo
import se.forsman.deckbuilder.core.extension.observe
import se.forsman.deckbuilder.features.decks.Deck
import se.forsman.deckbuilder.features.decks.DeckViewModel

class HomeFragment : BaseFragment() {

    private val viewModel by viewModel<DeckViewModel>()
    private val adapter by inject<DecksAdapter>()

    override fun layoutId(): Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.observe(viewModel.getDecks()) {
            if (it.isNullOrEmpty()) {
                noDecks()
            } else {
                showDecks(it)
            }
        }

        this.failure(viewModel.getErrorMessage(), this::handleFailure)

        initView()
    }

    private fun initView() {

        viewModel.loadDecks()

        recyclerviewMyDecks.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerviewMyDecks.adapter = adapter
    }

    private fun noDecks() {
        buttonCreateDecks.visibility = View.VISIBLE
        recyclerviewMyDecks.visibility = View.GONE
        textShowAllDecks.visibility = View.GONE

        buttonCreateDecks.setOnClickListener {
            fragmentManager?.navigateTo(CreateDeckFragment(), TAG_HOME)
        }
    }

    private fun showDecks(decks: List<Deck>) {
        buttonCreateDecks.visibility = View.GONE
        recyclerviewMyDecks.visibility = View.VISIBLE
        adapter.onItemClick = { deck ->
            fragmentManager?.navigateTo(DeckFragment.newInstance(deck.id), TAG_HOME)
        }

        textShowAllDecks.setOnClickListener {
            fragmentManager?.navigateTo(MyDecksFragment(), TAG_HOME)
        }

        adapter.submitList(decks)
    }

    companion object {
        private const val TAG_HOME = "HomeFragment"
    }
}