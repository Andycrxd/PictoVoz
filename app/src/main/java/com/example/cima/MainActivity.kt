package com.example.cima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.cima.data.repository.AdminPreferencesRepository
import com.example.cima.data.repository.InMemoryPictogramRepository
import com.example.cima.data.repository.PictoVozDatabase
import com.example.cima.data.repository.RoomUserProfileRepository
import com.example.cima.domain.TextToSpeechManager
import com.example.cima.ui.screen.AdminScreen
import com.example.cima.ui.screen.HomeScreen
import com.example.cima.ui.screen.ProfileScreen
import com.example.cima.ui.theme.CimaTheme
import com.example.cima.ui.viewmodel.AdminViewModel
import com.example.cima.ui.viewmodel.AdminViewModelFactory
import com.example.cima.viewmodel.CommunicationViewModel
import com.example.cima.viewmodel.CommunicationViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var textToSpeechManager: TextToSpeechManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adminPreferencesRepository = AdminPreferencesRepository(applicationContext)
        textToSpeechManager = TextToSpeechManager(
            applicationContext,
            initialVolume = adminPreferencesRepository.settings.value.ttsVolume
        )
        val profileRepository = RoomUserProfileRepository(
            PictoVozDatabase.getInstance(applicationContext).userProfileDao()
        )
        val communicationFactory = CommunicationViewModelFactory(
            context = applicationContext,
            pictogramRepository = InMemoryPictogramRepository(),
            textToSpeechManager = textToSpeechManager,
            adminPreferencesRepository = adminPreferencesRepository
        )
        val adminFactory = AdminViewModelFactory(
            profileRepository = profileRepository,
            preferencesRepository = adminPreferencesRepository,
            textToSpeechManager = textToSpeechManager
        )

        setContent {
            val communicationViewModel: CommunicationViewModel = viewModel(factory = communicationFactory)
            val communicationState = communicationViewModel.uiState.collectAsStateWithLifecycle().value
            val adminViewModel: AdminViewModel = viewModel(factory = adminFactory)
            val adminState = adminViewModel.uiState.collectAsStateWithLifecycle().value
            val route = rememberSaveable { mutableStateOf(SCREEN_HOME) }

            BackHandler(enabled = route.value != SCREEN_HOME) {
                if (route.value == SCREEN_PROFILES) {
                    route.value = SCREEN_ADMIN
                } else {
                    adminViewModel.lock()
                    route.value = SCREEN_HOME
                }
            }

            CimaTheme {
                when (route.value) {
                    SCREEN_ADMIN -> AdminScreen(
                        state = adminState,
                        onPinInputChanged = adminViewModel::updatePinInput,
                        onPinConfirmationChanged = adminViewModel::updatePinConfirmation,
                        onSubmitPin = adminViewModel::submitPin,
                        onVocabularyLevelSelected = adminViewModel::setVocabularyLevel,
                        onGrammaticalPersonSelected = adminViewModel::setGrammaticalPerson,
                        onVolumeChanged = adminViewModel::setTtsVolume,
                        onOpenProfiles = { if (adminState.isUnlocked) route.value = SCREEN_PROFILES },
                        onBack = {
                            adminViewModel.lock()
                            route.value = SCREEN_HOME
                        }
                    )

                    SCREEN_PROFILES -> ProfileScreen(
                        profiles = adminState.profiles,
                        activeProfileId = adminState.activeProfileId,
                        profileError = adminState.profileError,
                        onSelectProfile = adminViewModel::selectProfile,
                        onCreateProfile = adminViewModel::createProfile,
                        onBack = { route.value = SCREEN_ADMIN }
                    )

                    else -> HomeScreen(
                        uiState = communicationState,
                        onCategorySelected = communicationViewModel::selectCategory,
                        onPictogramSelected = communicationViewModel::addPictogram,
                        onSentencePictogramSelected = communicationViewModel::removePictogramAt,
                        onClearSentence = communicationViewModel::clearSentence,
                        onSpeak = communicationViewModel::speakCurrentSentence,
                        onRepeat = communicationViewModel::repeatLastSentence,
                        onQuickPhraseSelected = communicationViewModel::speakQuickPhrase,
                        onToggleSearch = communicationViewModel::toggleSearch,
                        onSearchKeyInput = communicationViewModel::onSearchKeyInput,
                        onSearchBackspace = communicationViewModel::onSearchBackspace,
                        onOpenAdmin = { route.value = SCREEN_ADMIN }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        textToSpeechManager.shutdown()
        super.onDestroy()
    }

    private companion object {
        const val SCREEN_HOME = "home"
        const val SCREEN_ADMIN = "admin"
        const val SCREEN_PROFILES = "profiles"
    }
}


