package com.example.cima.data.repository

import com.example.cima.data.model.Categoria
import com.example.cima.data.model.FraseRapida
import com.example.cima.data.model.Pictogram

interface PictogramRepository {
    fun getCategories(): List<Categoria>
    fun getPictograms(): List<Pictogram>
    fun getQuickPhrases(): List<FraseRapida>
    fun getPictogramsByCategory(categoryId: String): List<Pictogram>
}
