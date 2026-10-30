package com.example.cima.domain.repository

interface AiRepository {
    /** Procesa las etiquetas usando la configuración de vocabulario y persona seleccionada. */
    suspend fun generateSentence(pictograms: List<String>): Result<String>

    suspend fun generateSentence(
        pictograms: List<String>,
        vocabularyLevel: String,
        grammaticalPerson: String
    ): Result<String> = generateSentence(pictograms)
}
