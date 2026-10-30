package com.example.cima.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cima.R
import com.example.cima.data.model.UserProfile

private val profileAvatars = listOf(
    R.drawable.ic_me,
    R.drawable.ic_mom,
    R.drawable.ic_dad,
    R.drawable.ic_happy,
    R.drawable.ic_love,
    R.drawable.ic_teacher
)

@Composable
fun ProfileScreen(
    profiles: List<UserProfile>,
    activeProfileId: Int?,
    profileError: String,
    onSelectProfile: (Int) -> Unit,
    onCreateProfile: (String, Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateDialog by rememberSaveable { mutableStateOf(false) }
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Perfiles", style = MaterialTheme.typography.headlineMedium)
                OutlinedButton(onClick = onBack) { Text("Volver") }
            }
            Button(onClick = { showCreateDialog = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Crear perfil")
            }
            if (profileError.isNotBlank()) {
                Text(profileError, color = MaterialTheme.colorScheme.error)
            }
            if (profiles.isEmpty()) {
                Text("Crea un perfil para guardar la persona que usa PictoVoz.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(profiles, key = { it.id }) { profile ->
                        ProfileRow(
                            profile = profile,
                            isActive = profile.id == activeProfileId,
                            onClick = { onSelectProfile(profile.id) }
                        )
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateProfileDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { name, avatarId ->
                onCreateProfile(name, avatarId)
                showCreateDialog = false
            }
        )
    }
}

@Composable
private fun ProfileRow(profile: UserProfile, isActive: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Image(
                painter = painterResource(profile.avatarResId),
                contentDescription = "Avatar de ${profile.nombre}",
                modifier = Modifier.size(56.dp).clip(CircleShape)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(profile.nombre, style = MaterialTheme.typography.titleMedium)
                if (isActive) Text("Perfil activo", color = MaterialTheme.colorScheme.primary)
            }
            if (!isActive) Text("Seleccionar", color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun CreateProfileDialog(onDismiss: () -> Unit, onCreate: (String, Int) -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    var avatarResId by rememberSaveable { mutableIntStateOf(profileAvatars.first()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo perfil") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it.take(40) },
                    label = { Text("Nombre") },
                    singleLine = true
                )
                Text("Elige un avatar")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    profileAvatars.forEach { avatar ->
                        Image(
                            painter = painterResource(avatar),
                            contentDescription = "Elegir avatar",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable { avatarResId = avatar }
                        )
                    }
                }
                if (name.isBlank()) Text("Escribe un nombre para continuar.", style = MaterialTheme.typography.bodySmall)
            }
        },
        confirmButton = {
            TextButton(onClick = { onCreate(name.trim(), avatarResId) }, enabled = name.isNotBlank()) {
                Text("Guardar")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
