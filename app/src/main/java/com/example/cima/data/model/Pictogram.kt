package com.example.cima.data.model

import androidx.annotation.DrawableRes

data class Pictogram(
    val id: String,
    val nombre: String,
    val categoriaId: String,
    @DrawableRes val imageResId: Int
)
