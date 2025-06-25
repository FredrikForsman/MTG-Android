package se.forsman.deckbuilder.features.decks.mydecks

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import dagger.hilt.android.lifecycle.HiltViewModel
import se.forsman.deckbuilder.R
import se.forsman.deckbuilder.features.decks.Deck
import se.forsman.deckbuilder.features.decks.editdeck.CardViewModel
import se.forsman.deckbuilder.features.search.model.MtgCard

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
    createDeckViewModel: CreateDeckViewModel = hiltViewModel(),
    cardViewModel: CardViewModel = hiltViewModel(),
    newDeckCreatedCallback: (Long) -> Unit,
) {
    LaunchedEffect(true) {
        cardViewModel.loadCards()
    }
    val cards = cardViewModel.cards.collectAsState()
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
        Cards(cards.value)
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

 @SuppressLint("UnusedBoxWithConstraintsScope")
 @Composable
 private fun Cards(cards: List<MtgCard>?) {
     LazyVerticalGrid(
         columns = GridCells.Fixed(3),
         contentPadding = PaddingValues(16.dp),
         verticalArrangement = Arrangement.spacedBy(16.dp),
         horizontalArrangement = Arrangement.spacedBy(16.dp)
     ) {
         cards?.let {
             items(it) { card ->
                 BoxWithConstraints(
                     modifier = Modifier
                         .fillMaxWidth()
                         .aspectRatio(0.714f) // Maintain card aspect ratio
                 ) {
                     AsyncImage(
                         model = card.imageUrl,
                         contentDescription = card.name,
                         modifier = Modifier.fillMaxSize(),
                         contentScale = ContentScale.Crop, // or FillBounds, depending on preference
                         placeholder = painterResource(id = R.drawable.card_back),
                     )
                 }
             }
         }
     }

 }
