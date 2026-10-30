package com.example.cima.data.repository

import android.content.Context
import android.util.Log
import com.example.cima.BuildConfig
import com.example.cima.domain.repository.AiRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class MediaPipeAiRepository(
    private val apiKey: String = BuildConfig.GEMINI_API_KEY
) : AiRepository {

    constructor(context: Context, modelFileName: String = "") : this(
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    private val generativeModel by lazy {
        GenerativeModel(
            modelName = "gemini-3.8-flash",
            apiKey = apiKey,
            generationConfig = generationConfig { temperature = 0.2f }
        )
    }

    override suspend fun generateSentence(pictograms: List<String>): Result<String> =
        generateSentence(pictograms, "Intermedio", "Primera persona (yo)")

    override suspend fun generateSentence(
        pictograms: List<String>,
        vocabularyLevel: String,
        grammaticalPerson: String
    ): Result<String> = withContext(Dispatchers.IO) {
        if (pictograms.isEmpty()) {
            return@withContext Result.failure(IllegalArgumentException("Lista de pictogramas vacía"))
        }

        val inputWords = pictograms.joinToString(", ")
        val prompt = """
            Eres el motor de generación de oraciones para PictoVoz, una aplicación AAC de comunicación aumentativa y alternativa.

            Convierte las palabras/pictogramas en una única oración breve, natural y gramaticalmente correcta en español.
            Nivel de vocabulario: $vocabularyLevel.
            Persona gramatical: $grammaticalPerson.

            Instrucciones:
            1. Responde únicamente con la oración final, sin explicaciones ni comillas.
            2. Usa palabras adecuadas al nivel de vocabulario elegido.
            3. Usa la persona gramatical elegida y mantén concordancia de género, número y conjugación.
            4. Conserva el significado de los pictogramas y sé conciso.

            Entrada: $inputWords
            Salida:
        """.trimIndent()

        repeat(2) { attempt ->
            try {
                Log.d("PictoVoz_AI", ">>> Enviando a Gemini Cloud (Intento ${attempt + 1}): '$inputWords'")
                val generatedText = generativeModel.generateContent(prompt).text?.trim()
                if (!generatedText.isNullOrEmpty()) {
                    Log.d("PictoVoz_AI", "<<< ÉXITO Gemini Cloud: '$generatedText'")
                    return@withContext Result.success(generatedText)
                }
            } catch (error: Exception) {
                val unavailable = error.localizedMessage?.contains("503") == true ||
                    error.localizedMessage?.contains("UNAVAILABLE") == true
                if (unavailable && attempt == 0) {
                    Log.w("PictoVoz_AI", "Servidor ocupado; reintentando en 500 ms")
                    delay(500)
                } else {
                    Log.e("PictoVoz_AI", "Error en Gemini: ${error.localizedMessage}")
                }
            }
        }
        Result.failure(Exception("Servidor Gemini no disponible tras reintentos"))
    }
}
