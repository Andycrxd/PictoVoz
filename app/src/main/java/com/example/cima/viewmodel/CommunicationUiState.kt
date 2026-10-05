package com.example.cima.viewmodel

import com.example.cima.data.model.Categoria
import com.example.cima.data.model.FraseRapida
import com.example.cima.data.model.Pictogram

data class CommunicationUiState(
    val categories: List<Categoria> = emptyList(),
    val selectedCategoryId: String = "",
    val pictograms: List<Pictogram> = emptyList(),
    val selectedPictograms: List<Pictogram> = emptyList(),
    val quickPhrases: List<FraseRapida> = emptyList(),
    val generatedSentence: String = "",
    val lastSpokenSentence: String = "",
    val feedbackMessage: String = "",
    val isProcessing: Boolean = false,
    val limitWarningCounter: Int = 0
) {
    val canSpeak: Boolean
        get() = selectedPictograms.isNotEmpty() && !isProcessing

    val canRepeat: Boolean
        get() = lastSpokenSentence.isNotBlank() && !isProcessing
}
