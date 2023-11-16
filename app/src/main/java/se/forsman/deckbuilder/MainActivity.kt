package se.forsman.deckbuilder

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import se.forsman.deckbuilder.features.decks.DeckViewModel
import se.forsman.deckbuilder.features.decks.mydecks.MyDecksScreen
import se.forsman.deckbuilder.features.splash.SplashScreen

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        setContent {
            Surface {
                val deckViewModel: DeckViewModel = koinViewModel()
                val decks = deckViewModel.decks.collectAsState()
                val navController = rememberNavController()

                LaunchedEffect(Unit) {
                    deckViewModel.loadDecks()
                }

                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    startDestination = "splash",
                ) {
                    composable("decks") {
                        MyDecksScreen(decks.value)
                    }
                    composable("splash") {
                        SplashScreen {
                            navController.navigate("decks")
                        }
                    }
                }
            }
        }
    }
}
