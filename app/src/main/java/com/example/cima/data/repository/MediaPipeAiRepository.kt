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
            generationConfig = generationConfig {
                temperature = 0.2f
            }
        )
    }

    override suspend fun generateSentence(pictograms: List<String>): Result<String> =
        withContext(Dispatchers.IO) {
            if (pictograms.isEmpty()) {
                return@withContext Result.failure(IllegalArgumentException("Lista de pictogramas vacía"))
            }

            val inputWords = pictograms.joinToString(", ")
            val prompt = "Convierte estas palabras derivadas de pictogramas en una oración corta, natural y gramaticalmente correcta en español: $inputWords. Devuelve ÚNICAMENTE la oración final, sin explicaciones ni comillas."

            // Intentamos hasta 2 veces si el servidor devuelve un pico de demanda (503)
            repeat(2) { attempt ->
                try {
                    Log.d("PictoVoz_AI", ">>> Enviando a Gemini Cloud (Intento ${attempt + 1}): '$inputWords'")

                    val response = generativeModel.generateContent(prompt)
                    val generatedText = response.text?.trim()

                    if (!generatedText.isNullOrEmpty()) {
                        Log.d("PictoVoz_AI", "<<< ÉXITO Gemini Cloud: '$generatedText'")
                        return@withContext Result.success(generatedText)
                    }
                } catch (e: Exception) {
                    val is503Error = e.localizedMessage?.contains("503") == true || e.localizedMessage?.contains("UNAVAILABLE") == true

                    if (is503Error && attempt == 0) {
                        Log.w("PictoVoz_AI", "<<< Servidor ocupado (503). Reintentando en 500 ms...")
                        delay(500) // Espera rápida antes del segundo intento
                    } else {
                        Log.e("PictoVoz_AI", "<<< ERROR en Gemini Cloud (Intento ${attempt + 1}): ${e.localizedMessage}")
                    }
                }
            }

            // Si ambos intentos fallan, activa de forma segura el fallback local
            Result.failure(Exception("Servidor Gemini no disponible tras reintentos"))
        }
}