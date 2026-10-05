package com.example.cima.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cima.R
import com.example.cima.ui.theme.CimaSpeak

@Composable
fun BottomControls(
    canSpeak: Boolean,
    canRepeat: Boolean,
    onClearSentence: () -> Unit,
    onSpeak: () -> Unit,
    onRepeat: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OutlinedButton(
                onClick = onClearSentence,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 64.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_delete_all),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(text = "Borrar todo")
            }

            Button(
                onClick = onSpeak,
                enabled = canSpeak,
                modifier = Modifier
                    .weight(1.4f)
                    .heightIn(min = 74.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CimaSpeak)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_speaker),
                    contentDescription = null,
                    modifier = Modifier.size(34.dp)
                )
                Spacer(modifier = Modifier.size(12.dp))
                Text(
                    text = "HABLAR",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            OutlinedButton(
                onClick = onRepeat,
                enabled = canRepeat,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 64.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_replay),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(text = "Repetir")
            }
        }
    }
}
