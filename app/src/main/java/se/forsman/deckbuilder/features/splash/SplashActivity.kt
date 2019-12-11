package se.forsman.deckbuilder.features.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import se.forsman.deckbuilder.MainActivity
import se.forsman.deckbuilder.R
import se.forsman.deckbuilder.core.exception.Failure
import se.forsman.deckbuilder.core.extension.failure
import se.forsman.deckbuilder.core.extension.makeStatusBarTransparent
import se.forsman.deckbuilder.core.extension.observe
import se.forsman.deckbuilder.features.decks.editdeck.EditDeckViewModel
import se.forsman.deckbuilder.features.search.model.MtgCard

class SplashActivity : AppCompatActivity(), KoinComponent {

    private val viewModel by viewModel<EditDeckViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        this.makeStatusBarTransparent()

        this.observe(viewModel.getCards(), this::navigateToApp)
        this.failure(viewModel.getErrorMessage(), this::handleFailure)

        viewModel.loadCards()
    }

    private fun navigateToApp(cards: List<MtgCard>?) {
        cards?.let {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.DatabaseError -> {
                Snackbar.make(layoutSplashRoot, getString(R.string.failure_database), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}