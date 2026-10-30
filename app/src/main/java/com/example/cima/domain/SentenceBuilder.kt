package com.example.cima.domain

import com.example.cima.data.model.Pictogram
import com.example.cima.data.repository.AdminOptions
import com.example.cima.domain.repository.AiRepository
import java.util.Locale

interface SentenceBuilder {
    suspend fun buildSentence(pictograms: List<Pictogram>): String
}

interface ConfigurableSentenceBuilder {
    fun updateSettings(vocabularyLevel: String, grammaticalPerson: String)
}

class AiSentenceBuilder(
    private val aiRepository: AiRepository
) : SentenceBuilder, ConfigurableSentenceBuilder {

    @Volatile private var vocabularyLevel = AdminOptions.VOCABULARY_LEVELS[1]
    @Volatile private var grammaticalPerson = AdminOptions.GRAMMATICAL_PERSONS[0]

    override fun updateSettings(vocabularyLevel: String, grammaticalPerson: String) {
        if (vocabularyLevel in AdminOptions.VOCABULARY_LEVELS) this.vocabularyLevel = vocabularyLevel
        if (grammaticalPerson in AdminOptions.GRAMMATICAL_PERSONS) this.grammaticalPerson = grammaticalPerson
    }

    override suspend fun buildSentence(pictograms: List<Pictogram>): String {
        if (pictograms.isEmpty()) return ""

        if (pictograms.size == 1) {
            return singlePictogramPhrase(pictograms.first())
        }

        val labels = pictograms.map { it.nombre.trim() }.filter { it.isNotBlank() }
        val result = aiRepository.generateSentence(labels, vocabularyLevel, grammaticalPerson)
        return result.getOrElse { fallbackSentence(labels) }
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
        val secondPerson = grammaticalPerson == AdminOptions.GRAMMATICAL_PERSONS[1]
        val thirdPerson = grammaticalPerson == AdminOptions.GRAMMATICAL_PERSONS[2]
        val want = when { secondPerson -> "Quieres"; thirdPerson -> "Quiere"; else -> "Quiero" }
        val need = when { secondPerson -> "Necesitas"; thirdPerson -> "Necesita"; else -> "Necesito" }
        val be = when { secondPerson -> "Estás"; thirdPerson -> "Está"; else -> "Estoy" }

        return when (pictogram.id) {
            "needs_water", "objects_water" -> "$want agua"
            "needs_food", "objects_food" -> "$want comida"
            "needs_bathroom" -> "$want ir al baño"
            "needs_sleep", "actions_sleep" -> "$want dormir"
            "needs_help" -> "$need ayuda"
            "emotions_happy" -> "$be feliz"
            "emotions_sad" -> "$be triste"
            "emotions_angry" -> if (secondPerson) "Estás enojado" else if (thirdPerson) "Está enojado" else "Estoy enojado"
            "emotions_tired" -> if (secondPerson) "Estás cansado" else if (thirdPerson) "Está cansado" else "Estoy cansado"
            "actions_eat" -> "$want comer"
            "actions_drink" -> "$want beber"
            "actions_play" -> "$want jugar"
            "actions_go" -> "$want ir"
            else -> pictogram.nombre
        }
    }
}
