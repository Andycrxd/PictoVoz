package com.example.cima.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .safeDrawingPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // 1. BARRA DE CONSTRUCCIÓN DE FRASE (RF1, RU2)
            SentenceBar(
                selectedPictograms = uiState.selectedPictograms,
                generatedSentence = uiState.generatedSentence,
                feedbackMessage = uiState.feedbackMessage,
                limitWarningCounter = uiState.limitWarningCounter,
                onPictogramClicked = onSentencePictogramSelected
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2. NAVEGACIÓN POR CATEGORÍAS (RF4, RF4.1)
            CategorySelector(
                categories = uiState.categories,
                selectedCategoryId = uiState.selectedCategoryId,
                onCategorySelected = onCategorySelected,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3. INDICADOR DE CARGA / PROCESAMIENTO (RF20)
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

            // 4. GRILLA PRINCIPAL DE PICTOGRAMAS O FRASES RÁPIDAS (RU1: 9-12 elementos visibles)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                shape = MaterialTheme.shapes.medium
            ) {
                if (uiState.selectedCategoryId == "quick_phrases") {
                    // Si seleccionó la pestaña de Acceso Rápido
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
                    // Grilla normal de pictogramas por categoría
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4), // 4 columnas garantizan ~12 elementos visibles en tablet
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
