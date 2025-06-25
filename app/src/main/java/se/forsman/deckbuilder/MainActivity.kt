package se.forsman.deckbuilder

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import se.forsman.deckbuilder.core.app.Routing
import se.forsman.deckbuilder.features.decks.Deck
import se.forsman.deckbuilder.features.decks.DeckViewModel
import se.forsman.deckbuilder.features.decks.mydecks.CreateNewDeck
import se.forsman.deckbuilder.features.decks.mydecks.MyDecksScreen
import se.forsman.deckbuilder.features.splash.SplashScreen

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val deckViewModel: DeckViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        setContent {
            Surface {
                val decks = deckViewModel.decks.collectAsState()
                val navController = rememberNavController()

                LaunchedEffect(Unit) {
                    deckViewModel.loadDecks()
                }

                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    startDestination = Routing.SPLASH,
                ) {
                    composable(Routing.MY_DECKS) {
                        MyDecksScreen(decks.value) {
                            navController.navigate(Routing.CREATE_NEW_DECK)
                        }
                    }
                    composable(
                        route = Routing.SPLASH,
                    ) {
                        SplashScreen {
                            navController.navigate(Routing.MY_DECKS)
                        }
                    }
                    composable(
                        route = Routing.CREATE_NEW_DECK,
                    ) {
                        CreateNewDeck {
                            navController.navigate(Routing.MY_DECKS)
                        }
                    }
                }
            }
        }
    }
}
