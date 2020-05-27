package se.forsman.deckbuilder.features.decks.mydecks

import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.fragment_create_deck.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.forsman.deckbuilder.R
import se.forsman.deckbuilder.core.app.BaseFragment
import se.forsman.deckbuilder.core.extension.close
import se.forsman.deckbuilder.core.extension.failure
import se.forsman.deckbuilder.core.extension.observe
import se.forsman.deckbuilder.features.decks.Deck

class CreateDeckFragment : BaseFragment() {
    override fun layoutId(): Int = R.layout.fragment_create_deck

    private val viewModel by viewModel<CreateDeckViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.getDeck()) { deck ->
            deck?.let { d ->
                parentFragmentManager.apply {
                    val transaction = this.beginTransaction()
                    transaction.remove(this@CreateDeckFragment)
                    transaction.replace(R.id.fragmentContainer, DeckFragment.newInstance(d.id))
                    transaction.commit()
                }
            }
        }

        failure(viewModel.getErrorMessage(), this::handleFailure)

        initView()
    }

    private fun initView() {

        imageNavigateBack.setOnClickListener {
            close()
        }

        buttonCreateDeck.setOnClickListener {
            Log.d("DECK", "creating deck")
            viewModel.createDeck(Deck(textDeckName.text.toString(), mutableListOf()))
        }

    }


}