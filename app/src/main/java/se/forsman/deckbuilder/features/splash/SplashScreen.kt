package se.forsman.deckbuilder.features.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import org.koin.androidx.compose.koinViewModel
import se.forsman.deckbuilder.R
import se.forsman.deckbuilder.features.decks.editdeck.CardViewModel

@Composable
fun SplashScreen(
    viewModel: CardViewModel = koinViewModel(),
    cardsLoadedCallback: () -> Unit,
) {
    val cards = viewModel.cards.collectAsState()
    if (cards.value.isNotEmpty()) {
        cardsLoadedCallback.invoke()
    }
    LaunchedEffect(Unit) {
        viewModel.loadCards()
    }
    Box(
        modifier = Modifier.fillMaxSize()
            .paint(
                painterResource(id = R.drawable.chandrablur),
                contentScale = ContentScale.FillBounds,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Setting up app", color = Color.White)
    }
}
