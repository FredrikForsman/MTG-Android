package se.forsman.deckbuilder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.forsman.deckbuilder.core.exception.Failure
import se.forsman.deckbuilder.core.extension.failure
import se.forsman.deckbuilder.core.extension.observe
import se.forsman.deckbuilder.features.decks.editdeck.CardViewModel
import se.forsman.deckbuilder.features.search.model.MtgCard

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<CardViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observe(viewModel.getCards(), this::navigateToApp)
        failure(viewModel.getErrorMessage(), this::handleFailure)

        viewModel.loadCards()
        viewModel.getSymbology()
    }

    private fun navigateToApp(cards: List<MtgCard>?) {
    }

    private fun handleFailure(failure: Failure?) {
    }
}
