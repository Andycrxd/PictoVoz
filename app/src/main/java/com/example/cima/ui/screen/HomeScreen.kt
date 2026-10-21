package com.example.cima.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cima.data.model.FraseRapida
import com.example.cima.data.model.Pictogram
import com.example.cima.ui.components.BottomControls
import com.example.cima.ui.components.CategorySelector
import com.example.cima.ui.components.CustomSimplifiedKeyboard
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
    onToggleSearch: () -> Unit,
    onSearchKeyInput: (String) -> Unit,
    onSearchBackspace: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (uiState.isSearchActive) {
                CustomSimplifiedKeyboard(
                    searchQuery = uiState.searchQuery,
                    onKeyClick = onSearchKeyInput,
                    onBackspace = onSearchBackspace,
                    onCloseSearch = onToggleSearch
                )
            } else {
                BottomControls(
                    canSpeak = uiState.canSpeak,
                    canRepeat = uiState.canRepeat,
                    onClearSentence = onClearSentence,
                    onSpeak = onSpeak,
                    onRepeat = onRepeat
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .safeDrawingPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            SentenceBar(
                selectedPictograms = uiState.selectedPictograms,
                generatedSentence = uiState.generatedSentence,
                feedbackMessage = uiState.feedbackMessage,
                limitWarningCounter = uiState.limitWarningCounter,
                onPictogramClicked = onSentencePictogramSelected
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategorySelector(
                    categories = uiState.categories,
                    selectedCategoryId = uiState.selectedCategoryId,
                    onCategorySelected = onCategorySelected,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                FilledTonalIconButton(
                    onClick = onToggleSearch,
                    modifier = Modifier.size(56.dp)
                ) {
                    Text(text = "🔍", fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.isProcessing) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                shape = MaterialTheme.shapes.medium
            ) {
                if (uiState.selectedCategoryId == "quick_phrases" && !uiState.isSearchActive) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(12.dp)
                    ) {
                        items(uiState.quickPhrases, key = { it.id }) { phrase ->
                            QuickPhraseCard(
                                phrase = phrase,
                                onClick = { onQuickPhraseSelected(phrase) }
                            )
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(12.dp)
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
}