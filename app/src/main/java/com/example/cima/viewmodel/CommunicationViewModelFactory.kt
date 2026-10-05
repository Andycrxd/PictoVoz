package com.example.cima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cima.data.repository.PictogramRepository
import com.example.cima.domain.SentenceBuilder
import com.example.cima.domain.TextToSpeechManager

class CommunicationViewModelFactory(
    private val repository: PictogramRepository,
    private val sentenceBuilder: SentenceBuilder,
    private val textToSpeechManager: TextToSpeechManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommunicationViewModel::class.java)) {
            return CommunicationViewModel(
                repository = repository,
                sentenceBuilder = sentenceBuilder,
                textToSpeechManager = textToSpeechManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
