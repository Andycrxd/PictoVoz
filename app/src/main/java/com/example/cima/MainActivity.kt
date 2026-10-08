package com.example.cima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cima.data.repository.InMemoryPictogramRepository
import com.example.cima.domain.TextToSpeechManager
import com.example.cima.ui.screen.HomeScreen
import com.example.cima.ui.theme.CimaTheme
import com.example.cima.viewmodel.CommunicationViewModel
import com.example.cima.viewmodel.CommunicationViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var textToSpeechManager: TextToSpeechManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textToSpeechManager = TextToSpeechManager(applicationContext)

        // Se pasa la fábrica usando los parámetros exactos definidos en CommunicationViewModelFactory
        val viewModelFactory = CommunicationViewModelFactory(
            context = applicationContext,
            pictogramRepository = InMemoryPictogramRepository(),
            textToSpeechManager = textToSpeechManager
        )

        setContent {
            val viewModel: CommunicationViewModel = viewModel(factory = viewModelFactory)
            val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

            CimaTheme {
                HomeScreen(
                    uiState = uiState,
                    onCategorySelected = viewModel::selectCategory,
                    onPictogramSelected = viewModel::addPictogram,
                    onSentencePictogramSelected = viewModel::removePictogramAt,
                    onClearSentence = viewModel::clearSentence,
                    onSpeak = viewModel::speakCurrentSentence,
                    onRepeat = viewModel::repeatLastSentence,
                    onQuickPhraseSelected = viewModel::speakQuickPhrase
                )
            }
        }
    }

    override fun onDestroy() {
        textToSpeechManager.shutdown()
        super.onDestroy()
    }
}