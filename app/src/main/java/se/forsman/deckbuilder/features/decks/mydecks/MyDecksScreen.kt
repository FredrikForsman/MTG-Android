package se.forsman.deckbuilder.features.decks.mydecks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import se.forsman.deckbuilder.R
import se.forsman.deckbuilder.features.decks.Deck

@Composable
fun MyDecksScreen(decks: List<Deck>, createNewDeckCallback: () -> Unit) {
    Box(
        modifier = with(Modifier) {
            fillMaxSize()
                .paint(
                    painterResource(id = R.drawable.chandrablur),
                    contentScale = ContentScale.FillBounds,
                )
        },
    ) {
        Button(
            onClick = { createNewDeckCallback.invoke() },
            modifier = Modifier.fillMaxWidth(0.8f).align(Alignment.BottomCenter),
            border = BorderStroke(2.dp, Color.White),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
            ),
        ) {
            Text(text = "CREATE NEW DECK", color = Color.White)
        }
    }
}

@Composable
fun CreateNewDeck(
    createDeckViewModel: CreateDeckViewModel = koinViewModel(),
    newDeckCreatedCallback: (Long) -> Unit,
) {
    val deckCreatedState = createDeckViewModel.state.collectAsState()
    if (deckCreatedState.value is DeckCreationState.Success) {
        newDeckCreatedCallback.invoke((deckCreatedState.value as DeckCreationState.Success).deckId)
    }
    Column(
        modifier = Modifier.fillMaxSize()
            .paint(
                painterResource(id = R.drawable.chandrablur),
                contentScale = ContentScale.FillBounds,
            ),
    ) {
        var textState by remember { mutableStateOf("New Deck") }
        OutlinedTextField(
            value = textState,
            onValueChange = {
                textState = it
            },
            singleLine = true,
            label = { Text(text = "Enter your decks name") },
        )
        Button(
            onClick = {
                createDeckViewModel.createDeck(
                    Deck(name = textState),
                )
            },
            modifier = Modifier.fillMaxWidth(0.8f),
            border = BorderStroke(2.dp, Color.White),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
            ),
        ) {
            Text(text = "CREATE", color = Color.White)
        }
    }
}

// @Composable
// private fun Cards(cards: List<MtgCard>?) {
//    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
//        cards?.let {
//            items(it) { card ->
//                AsyncImage(
//                    model = card.imageUrl,
//                    contentDescription = card.name,
//                    placeholder = painterResource(id = R.drawable.card_back),
//                )
//            }
//        }
//    }
// }
