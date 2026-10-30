package com.example.cima.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cima.data.repository.AdminPreferencesRepository
import com.example.cima.data.repository.UserProfileRepository
import com.example.cima.domain.TextToSpeechManager

class AdminViewModelFactory(
    private val profileRepository: UserProfileRepository,
    private val preferencesRepository: AdminPreferencesRepository,
    private val textToSpeechManager: TextToSpeechManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(AdminViewModel::class.java)) {
            throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
        return AdminViewModel(
            profileRepository,
            preferencesRepository,
            textToSpeechManager
        ) as T
    }
}
