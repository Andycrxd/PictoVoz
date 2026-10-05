package com.example.cima.domain

import com.example.cima.data.model.Pictogram
import java.util.Locale

interface SentenceBuilder {
    fun buildSentence(pictograms: List<Pictogram>): String
}

class RuleBasedSentenceBuilder : SentenceBuilder {

    override fun buildSentence(pictograms: List<Pictogram>): String {
        if (pictograms.isEmpty()) return ""

        if (pictograms.size == 1) {
            return singlePictogramPhrase(pictograms.first())
        }

        val labels = pictograms.map { it.nombre.trim() }.filter { it.isNotBlank() }
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
