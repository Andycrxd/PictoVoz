package com.example.cima.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.cima.data.model.Pictogram
import com.example.cima.ui.theme.CimaOutline
import com.example.cima.ui.theme.CimaSentenceSurface
import com.example.cima.ui.theme.CimaWarning
import kotlinx.coroutines.delay

@Composable
fun SentenceBar(
    selectedPictograms: List<Pictogram>,
    generatedSentence: String,
    feedbackMessage: String,
    limitWarningCounter: Int,
    onPictogramClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var warningActive by remember { mutableStateOf(false) }
    LaunchedEffect(limitWarningCounter) {
        if (limitWarningCounter > 0) {
            warningActive = true
            delay(520)
            warningActive = false
        }
    }

    val borderColor by animateColorAsState(
        targetValue = if (warningActive) CimaWarning else CimaOutline,
        label = "sentence_bar_border"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(BorderStroke(3.dp, borderColor), RoundedCornerShape(8.dp)),
        color = CimaSentenceSurface,
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Frase",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.width(120.dp)
                )
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(5) { index ->
                        val pictogram = selectedPictograms.getOrNull(index)
                        SentenceSlot(
                            pictogram = pictogram,
                            onClick = { onPictogramClicked(index) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (generatedSentence.isNotBlank() || feedbackMessage.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = generatedSentence.ifBlank { feedbackMessage },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = if (warningActive) CimaWarning else MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun SentenceSlot(
    pictogram: Pictogram?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = modifier
            .heightIn(min = 104.dp)
            .clip(shape)
            .background(Color.White)
            .border(2.dp, CimaOutline.copy(alpha = 0.45f), shape)
            .then(
                if (pictogram != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (pictogram == null) {
            Text(
                text = "+",
                style = MaterialTheme.typography.titleLarge,
                color = CimaOutline,
                textAlign = TextAlign.Center
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = pictogram.imageResId),
                    contentDescription = pictogram.nombre,
                    modifier = Modifier.size(52.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = pictogram.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
