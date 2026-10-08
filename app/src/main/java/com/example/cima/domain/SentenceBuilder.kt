package com.example.cima.domain

import com.example.cima.data.model.Pictogram
import com.example.cima.domain.repository.AiRepository
import java.util.Locale

interface SentenceBuilder {
    suspend fun buildSentence(pictograms: List<Pictogram>): String
}

class AiSentenceBuilder(
    private val aiRepository: AiRepository
) : SentenceBuilder {

    override suspend fun buildSentence(pictograms: List<Pictogram>): String {
        if (pictograms.isEmpty()) return ""

        // Para 1 solo pictograma, mantenemos la frase rápida directa por regla
        if (pictograms.size == 1) {
            return singlePictogramPhrase(pictograms.first())
        }

        val labels = pictograms.map { it.nombre.trim() }.filter { it.isNotBlank() }

        // Intentamos generar la oración procesando la secuencia con el modelo de IA local o cloud
        val result = aiRepository.generateSentence(labels)

        // RF19 / RC2: Si la IA genera un resultado válido se utiliza; de lo contrario, aplica el fallback de reglas locales
        return result.getOrElse {
            fallbackSentence(labels)
        }
    }

    private fun fallbackSentence(labels: List<String>): String {
        val rawSentence = labels.joinToString(separator = " ") { label ->
            label.lowercase(Locale("es", "MX"))
        }

        return rawSentence.replaceFirstChar { first ->
            if (first.isLowerCase()) first.titlecase(Locale("es", "MX")) else first.toString()
        }
    }

    private fun singlePictogramPhrase(pictogram: Pictogram): String {
        return when (pictogram.id) {
            "needs_water", "objects_water" -> "Quiero agua"
            "needs_food", "objects_food" -> "Quiero comida"
            "needs_bathroom" -> "Quiero ir al baño"
            "needs_sleep", "actions_sleep" -> "Quiero dormir"
            "needs_help" -> "Necesito ayuda"
            "emotions_happy" -> "Estoy feliz"
            "emotions_sad" -> "Estoy triste"
            "emotions_angry" -> "Estoy enojado"
            "emotions_tired" -> "Estoy cansado"
            "actions_eat" -> "Quiero comer"
            "actions_drink" -> "Quiero beber"
            "actions_play" -> "Quiero jugar"
            "actions_go" -> "Quiero ir"
            else -> pictogram.nombre
        }
    }
}