package com.example.cima.data.model

import androidx.annotation.DrawableRes

data class FraseRapida(
    val id: String,
    val texto: String,
    @DrawableRes val imageResId: Int
)
