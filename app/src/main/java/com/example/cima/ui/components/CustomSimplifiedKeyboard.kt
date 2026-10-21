package com.example.cima.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cima.ui.theme.CimaOutline

@Composable
fun CustomSimplifiedKeyboard(
    searchQuery: String,
    onKeyClick: (String) -> Unit,
    onBackspace: () -> Unit,
    onCloseSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keys = listOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I",
        "J", "K", "L", "M", "N", "Ñ", "O", "P", "Q",
        "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = searchQuery.ifEmpty { "Escribe para buscar..." },
                        fontSize = 18.sp,
                        color = if (searchQuery.isEmpty()) CimaOutline else MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = onCloseSearch) {
                    Text(text = "❌", fontSize = 20.sp)
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(9),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.height(170.dp)
            ) {
                items(keys) { key ->
                    Button(
                        onClick = { onKeyClick(key) },
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(text = key, fontSize = 18.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onKeyClick(" ") },
                    modifier = Modifier
                        .weight(2f)
                        .height(48.dp),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(text = "Espacio", fontSize = 16.sp)
                }

                OutlinedButton(
                    onClick = onBackspace,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(text = "⌫ Borrar", fontSize = 16.sp)
                }
            }
        }
    }
}