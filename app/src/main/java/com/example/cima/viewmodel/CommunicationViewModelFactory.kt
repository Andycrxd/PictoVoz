package com.example.cima.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cima.data.repository.MediaPipeAiRepository
import com.example.cima.data.repository.PictogramRepository
import com.example.cima.domain.AiSentenceBuilder
import com.example.cima.domain.TextToSpeechManager

class CommunicationViewModelFactory(
    private val context: Context,
    private val pictogramRepository: PictogramRepository,
    private val textToSpeechManager: TextToSpeechManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommunicationViewModel::class.java)) {
            val aiRepository = MediaPipeAiRepository()
            val sentenceBuilder = AiSentenceBuilder(aiRepository)

            return CommunicationViewModel(
                repository = pictogramRepository,
                sentenceBuilder = sentenceBuilder,
                textToSpeechManager = textToSpeechManager
            ) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida: ${modelClass.name}")
    }
}