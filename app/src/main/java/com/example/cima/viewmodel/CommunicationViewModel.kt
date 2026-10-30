
package com.example.cima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cima.data.model.FraseRapida
import com.example.cima.data.model.Pictogram
import com.example.cima.data.repository.PictogramRepository
import com.example.cima.data.repository.AdminPreferencesRepository
import com.example.cima.domain.ConfigurableSentenceBuilder
import com.example.cima.domain.SentenceBuilder
import com.example.cima.domain.TextToSpeechManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommunicationViewModel(
    private val repository: PictogramRepository,
    private val sentenceBuilder: SentenceBuilder,
    private val textToSpeechManager: TextToSpeechManager,
    private val adminPreferencesRepository: AdminPreferencesRepository? = null
) : ViewModel() {

    private val maxPictograms = 5
    private val initialCategory = repository.getCategories().firstOrNull()?.id.orEmpty()

    private val _uiState = MutableStateFlow(
        CommunicationUiState(
            categories = repository.getCategories(),
            selectedCategoryId = initialCategory,
            pictograms = repository.getPictogramsByCategory(initialCategory),
            quickPhrases = repository.getQuickPhrases()
        )
    )
    val uiState: StateFlow<CommunicationUiState> = _uiState.asStateFlow()

    init {
        adminPreferencesRepository?.let { preferences ->
            viewModelScope.launch {
                preferences.settings.collect { settings ->
                    (sentenceBuilder as? ConfigurableSentenceBuilder)?.updateSettings(
                        settings.vocabularyLevel,
                        settings.grammaticalPerson
                    )
                    textToSpeechManager.setVolume(settings.ttsVolume)
                }
            }
        }
    }

    fun selectCategory(categoryId: String) {
        _uiState.update { state ->
            state.copy(
                selectedCategoryId = categoryId,
                pictograms = repository.getPictogramsByCategory(categoryId),
                feedbackMessage = "",
                isSearchActive = false,
                searchQuery = ""
            )
        }
    }

    fun addPictogram(pictogram: Pictogram) {
        _uiState.update { state ->
            if (state.selectedPictograms.size >= maxPictograms) {
                state.copy(
                    feedbackMessage = "La barra ya tiene 5 pictogramas.",
                    limitWarningCounter = state.limitWarningCounter + 1
                )
            } else {
                state.copy(
                    selectedPictograms = state.selectedPictograms + pictogram,
                    generatedSentence = "",
                    feedbackMessage = ""
                )
            }
        }
    }

    fun removePictogramAt(index: Int) {
        _uiState.update { state ->
            if (index !in state.selectedPictograms.indices) return@update state
            state.copy(
                selectedPictograms = state.selectedPictograms.filterIndexed { currentIndex, _ ->
                    currentIndex != index
                },
                generatedSentence = "",
                feedbackMessage = ""
            )
        }
    }

    fun clearSentence() {
        _uiState.update { state ->
            state.copy(
                selectedPictograms = emptyList(),
                generatedSentence = "",
                feedbackMessage = "Barra limpia."
            )
        }
    }

    fun speakCurrentSentence() {
        val selected = _uiState.value.selectedPictograms
        if (selected.isEmpty() || _uiState.value.isProcessing) return

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, feedbackMessage = "Procesando mensaje con IA...") }

            val sentence = sentenceBuilder.buildSentence(selected)

            _uiState.update { state ->
                state.copy(
                    generatedSentence = sentence,
                    lastSpokenSentence = sentence,
                    isProcessing = false,
                    feedbackMessage = "Frase lista."
                )
            }

            textToSpeechManager.speak(sentence)
        }
    }

    fun speakQuickPhrase(fraseRapida: FraseRapida) {
        val text = fraseRapida.texto
        _uiState.update { state ->
            state.copy(
                generatedSentence = text,
                lastSpokenSentence = text,
                feedbackMessage = "Frase rápida reproducida.",
                isProcessing = false
            )
        }
        textToSpeechManager.speak(text)
    }

    fun repeatLastSentence() {
        val lastSentence = _uiState.value.lastSpokenSentence
        if (lastSentence.isBlank()) return
        textToSpeechManager.speak(lastSentence)
        _uiState.update { it.copy(feedbackMessage = "Repitiendo última frase.") }
    }

    fun toggleSearch() {
        _uiState.update { state ->
            val nextSearchState = !state.isSearchActive
            state.copy(
                isSearchActive = nextSearchState,
                searchQuery = "",
                pictograms = if (!nextSearchState) {
                    repository.getPictogramsByCategory(state.selectedCategoryId)
                } else {
                    state.pictograms
                }
            )
        }
    }

    fun onSearchKeyInput(char: String) {
        _uiState.update { state ->
            val newQuery = state.searchQuery + char
            performSearch(state, newQuery)
        }
    }

    fun onSearchBackspace() {
        _uiState.update { state ->
            if (state.searchQuery.isEmpty()) return@update state
            val newQuery = state.searchQuery.dropLast(1)
            performSearch(state, newQuery)
        }
    }

    private fun performSearch(currentState: CommunicationUiState, query: String): CommunicationUiState {
        val filteredList = if (query.isBlank()) {
            repository.getPictogramsByCategory(currentState.selectedCategoryId)
        } else {
            repository.getPictograms().filter {
                it.nombre.contains(query, ignoreCase = true)
            }
        }
        return currentState.copy(
            searchQuery = query,
            pictograms = filteredList
        )
    }

    override fun onCleared() {
        textToSpeechManager.stop()
        super.onCleared()
    }
}
