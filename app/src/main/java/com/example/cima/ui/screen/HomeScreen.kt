package com.example.cima.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cima.data.model.FraseRapida
import com.example.cima.data.model.Pictogram
import com.example.cima.ui.components.BottomControls
import com.example.cima.ui.components.CategorySelector
import com.example.cima.ui.components.PictogramCard
import com.example.cima.ui.components.QuickPhraseCard
import com.example.cima.ui.components.SentenceBar
import com.example.cima.viewmodel.CommunicationUiState

@Composable
fun HomeScreen(
    uiState: CommunicationUiState,
    onCategorySelected: (String) -> Unit,
    onPictogramSelected: (Pictogram) -> Unit,
    onSentencePictogramSelected: (Int) -> Unit,
    onClearSentence: () -> Unit,
    onSpeak: () -> Unit,
    onRepeat: () -> Unit,
    onQuickPhraseSelected: (FraseRapida) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            BottomControls(
                canSpeak = uiState.canSpeak,
                canRepeat = uiState.canRepeat,
                onClearSentence = onClearSentence,
                onSpeak = onSpeak,
                onRepeat = onRepeat
            )
        }
    ) { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .safeDrawingPadding()
        ) {
            val gridMinimumSize = if (maxWidth >= 900.dp) 170.dp else 136.dp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                SentenceBar(
                    selectedPictograms = uiState.selectedPictograms,
                    generatedSentence = uiState.generatedSentence,
                    feedbackMessage = uiState.feedbackMessage,
                    limitWarningCounter = uiState.limitWarningCounter,
                    onPictogramClicked = onSentencePictogramSelected
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Categorías",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                CategorySelector(
                    categories = uiState.categories,
                    selectedCategoryId = uiState.selectedCategoryId,
                    onCategorySelected = onCategorySelected,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Acceso rápido",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    items(uiState.quickPhrases, key = { it.id }) { phrase ->
                        QuickPhraseCard(
                            phrase = phrase,
                            onClick = { onQuickPhraseSelected(phrase) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Pictogramas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (uiState.isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = gridMinimumSize),
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 18.dp)
                ) {
                    items(uiState.pictograms, key = { it.id }) { pictogram ->
                        PictogramCard(
                            pictogram = pictogram,
                            onClick = { onPictogramSelected(pictogram) }
                        )
                    }
                }
            }
        }
    }
}
