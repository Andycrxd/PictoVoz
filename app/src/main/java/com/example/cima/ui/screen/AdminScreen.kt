package com.example.cima.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.cima.data.repository.AdminOptions
import com.example.cima.ui.viewmodel.AdminUiState

@Composable
fun AdminScreen(
    state: AdminUiState,
    onPinInputChanged: (String) -> Unit,
    onPinConfirmationChanged: (String) -> Unit,
    onSubmitPin: () -> Unit,
    onVocabularyLevelSelected: (String) -> Unit,
    onGrammaticalPersonSelected: (String) -> Unit,
    onVolumeChanged: (Float) -> Unit,
    onOpenProfiles: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Administración", style = MaterialTheme.typography.headlineMedium)
                OutlinedButton(onClick = onBack) { Text("Volver") }
            }

            if (!state.isUnlocked) {
                PinGate(
                    state = state,
                    onPinInputChanged = onPinInputChanged,
                    onPinConfirmationChanged = onPinConfirmationChanged,
                    onSubmitPin = onSubmitPin
                )
            } else {
                SettingsContent(
                    state = state,
                    onVocabularyLevelSelected = onVocabularyLevelSelected,
                    onGrammaticalPersonSelected = onGrammaticalPersonSelected,
                    onVolumeChanged = onVolumeChanged,
                    onOpenProfiles = onOpenProfiles
                )
            }
        }
    }
}

@Composable
private fun PinGate(
    state: AdminUiState,
    onPinInputChanged: (String) -> Unit,
    onPinConfirmationChanged: (String) -> Unit,
    onSubmitPin: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                if (state.needsPinSetup) "Crea un PIN de administrador" else "Ingresa el PIN de administrador",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                if (state.needsPinSetup) "El PIN tendrá 4 dígitos y protegerá esta sección en este dispositivo."
                else "Se requiere el PIN de 4 dígitos para abrir la configuración."
            )
            OutlinedTextField(
                value = state.pinInput,
                onValueChange = onPinInputChanged,
                label = { Text(if (state.needsPinSetup) "Nuevo PIN" else "PIN") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (state.needsPinSetup) {
                OutlinedTextField(
                    value = state.pinConfirmationInput,
                    onValueChange = onPinConfirmationChanged,
                    label = { Text("Confirma el PIN") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (state.pinError.isNotBlank()) {
                Text(state.pinError, color = MaterialTheme.colorScheme.error)
            }
            Button(
                onClick = onSubmitPin,
                enabled = !state.isCheckingPin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.isCheckingPin) "Comprobando…" else if (state.needsPinSetup) "Guardar PIN" else "Desbloquear")
            }
        }
    }
}

@Composable
private fun SettingsContent(
    state: AdminUiState,
    onVocabularyLevelSelected: (String) -> Unit,
    onGrammaticalPersonSelected: (String) -> Unit,
    onVolumeChanged: (Float) -> Unit,
    onOpenProfiles: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Configuración de IA y voz", style = MaterialTheme.typography.titleLarge)
        PreferenceDropdown(
            label = "Nivel de vocabulario",
            selected = state.vocabularyLevel,
            options = AdminOptions.VOCABULARY_LEVELS,
            onSelected = onVocabularyLevelSelected
        )
        PreferenceDropdown(
            label = "Persona gramatical",
            selected = state.grammaticalPerson,
            options = AdminOptions.GRAMMATICAL_PERSONS,
            onSelected = onGrammaticalPersonSelected
        )
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Volumen de voz: ${(state.ttsVolume * 100).toInt()}%", style = MaterialTheme.typography.titleMedium)
                Slider(
                    value = state.ttsVolume,
                    onValueChange = onVolumeChanged,
                    valueRange = 0f..1f
                )
                Text("El cambio se guarda automáticamente.", style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(Modifier.height(4.dp))
        Text("Perfiles", style = MaterialTheme.typography.titleLarge)
        Text(
            state.profiles.firstOrNull { it.id == state.activeProfileId }?.let { "Perfil activo: ${it.nombre}" }
                ?: "Todavía no hay un perfil activo."
        )
        Button(onClick = onOpenProfiles, modifier = Modifier.fillMaxWidth()) {
            Text("Administrar perfiles (${state.profiles.size})")
        }
    }
}

@Composable
private fun PreferenceDropdown(
    label: String,
    selected: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text("$label: $selected")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        expanded = false
                        onSelected(option)
                    }
                )
            }
        }
    }
}
