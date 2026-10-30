package com.example.cima.ui.viewmodel

import com.example.cima.data.model.UserProfile
import com.example.cima.data.repository.AdminOptions

 data class AdminUiState(
    val isUnlocked: Boolean = false,
    val needsPinSetup: Boolean = true,
    val pinInput: String = "",
    val pinConfirmationInput: String = "",
    val pinError: String = "",
    val isCheckingPin: Boolean = false,
    val ttsVolume: Float = 1f,
    val vocabularyLevel: String = AdminOptions.VOCABULARY_LEVELS[1],
    val grammaticalPerson: String = AdminOptions.GRAMMATICAL_PERSONS[0],
    val profiles: List<UserProfile> = emptyList(),
    val activeProfileId: Int? = null,
    val profileError: String = ""
)

