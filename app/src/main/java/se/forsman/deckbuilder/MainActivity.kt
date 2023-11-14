package se.forsman.deckbuilder

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.forsman.deckbuilder.core.exception.Failure
import se.forsman.deckbuilder.core.extension.failure
import se.forsman.deckbuilder.core.extension.observe
import se.forsman.deckbuilder.features.decks.editdeck.CardViewModel
import se.forsman.deckbuilder.features.search.model.MtgCard

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<CardViewModel>()
    private var glide: Glide? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observe(viewModel.cards, this::navigateToApp)
        failure(viewModel.getErrorMessage(), this::handleFailure)

        viewModel.loadCards()
        viewModel.getSymbology()
        glide = Glide.get(this)
    }

    private fun navigateToApp(cards: List<MtgCard>?) {
        setContent {
            Surface {
                val navController = rememberNavController()

                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    startDestination = "home",
                ) {
                    composable("home") {
                        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                            cards?.let {
                                items(it) { card ->
                                    AsyncImage(
                                        model = card.imageUrl,
                                        contentDescription = card.name,
                                        placeholder = painterResource(id = R.drawable.card_back),
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    private fun handleFailure(failure: Failure?) {
    }
}
