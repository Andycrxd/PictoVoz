package com.example.cima.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cima.data.repository.AdminPreferencesRepository
import com.example.cima.data.repository.UserProfileRepository
import com.example.cima.domain.TextToSpeechManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminViewModel(
    private val profileRepository: UserProfileRepository,
    private val preferencesRepository: AdminPreferencesRepository,
    private val textToSpeechManager: TextToSpeechManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        AdminUiState(
            needsPinSetup = !preferencesRepository.settings.value.pinConfigured,
            ttsVolume = preferencesRepository.settings.value.ttsVolume,
            vocabularyLevel = preferencesRepository.settings.value.vocabularyLevel,
            grammaticalPerson = preferencesRepository.settings.value.grammaticalPerson,
            activeProfileId = preferencesRepository.settings.value.activeProfileId
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        textToSpeechManager.setVolume(preferencesRepository.settings.value.ttsVolume)
        viewModelScope.launch {
            combine(profileRepository.observeProfiles(), preferencesRepository.settings) { profiles, prefs ->
                profiles to prefs
            }.collect { (profiles, prefs) ->
                _uiState.update {
                    it.copy(
                        needsPinSetup = !prefs.pinConfigured,
                        ttsVolume = prefs.ttsVolume,
                        vocabularyLevel = prefs.vocabularyLevel,
                        grammaticalPerson = prefs.grammaticalPerson,
                        profiles = profiles,
                        activeProfileId = prefs.activeProfileId
                    )
                }
            }
        }
    }

    fun updatePinInput(value: String) {
        _uiState.update { it.copy(pinInput = value.filter(Char::isDigit).take(4), pinError = "") }
    }

    fun updatePinConfirmation(value: String) {
        _uiState.update {
            it.copy(pinConfirmationInput = value.filter(Char::isDigit).take(4), pinError = "")
        }
    }

    fun submitPin() {
        val state = _uiState.value
        if (state.isCheckingPin) return
        if (state.pinInput.length != 4) {
            _uiState.update { it.copy(pinError = "Escribe los 4 dígitos del PIN.") }
            return
        }
        if (state.needsPinSetup && state.pinConfirmationInput != state.pinInput) {
            _uiState.update { it.copy(pinError = "Los PIN no coinciden.") }
            return
        }

        _uiState.update { it.copy(isCheckingPin = true, pinError = "") }
        viewModelScope.launch(Dispatchers.IO) {
            val accepted = if (state.needsPinSetup) {
                runCatching { preferencesRepository.saveAdminPin(state.pinInput) }.isSuccess
            } else {
                preferencesRepository.verifyAdminPin(state.pinInput)
            }
            _uiState.update {
                it.copy(
                    isCheckingPin = false,
                    isUnlocked = accepted,
                    needsPinSetup = if (accepted) false else it.needsPinSetup,
                    pinInput = "",
                    pinConfirmationInput = "",
                    pinError = if (accepted) "" else if (state.needsPinSetup) {
                        "No se pudo guardar el PIN. Intenta de nuevo."
                    } else {
                        "PIN incorrecto. Intenta de nuevo."
                    }
                )
            }
        }
    }

    fun lock() {
        _uiState.update {
            it.copy(
                isUnlocked = false,
                pinInput = "",
                pinConfirmationInput = "",
                pinError = ""
            )
        }
    }

    fun setTtsVolume(value: Float) {
        val volume = value.coerceIn(0f, 1f)
        preferencesRepository.setTtsVolume(volume)
        textToSpeechManager.setVolume(volume)
        _uiState.update { it.copy(ttsVolume = volume) }
    }

    fun setVocabularyLevel(value: String) = preferencesRepository.setVocabularyLevel(value)

    fun setGrammaticalPerson(value: String) = preferencesRepository.setGrammaticalPerson(value)

    fun selectProfile(profileId: Int) = preferencesRepository.setActiveProfileId(profileId)

    fun createProfile(name: String, avatarResId: Int) {
        val cleanName = name.trim()
        if (cleanName.isBlank()) {
            _uiState.update { it.copy(profileError = "Escribe un nombre para el perfil.") }
            return
        }
        viewModelScope.launch {
            runCatching { profileRepository.createProfile(cleanName, avatarResId) }
                .onSuccess { id ->
                    if (_uiState.value.activeProfileId == null) {
                        preferencesRepository.setActiveProfileId(id)
                    }
                    _uiState.update { it.copy(profileError = "") }
                }
                .onFailure {
                    _uiState.update { it.copy(profileError = "No se pudo crear el perfil.") }
                }
        }
    }
}


