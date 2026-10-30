package com.example.cima.domain

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import java.util.Locale

class TextToSpeechManager(
    context: Context,
    initialVolume: Float = 1f
) : TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isReady = false
    private var pendingText: String? = null
    private var lastText: String? = null
    private var volume = initialVolume.coerceIn(0f, 1f)

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val spanishMexico = Locale("es", "MX")
            val languageResult = textToSpeech?.setLanguage(spanishMexico)
            isReady = languageResult != TextToSpeech.LANG_MISSING_DATA &&
                languageResult != TextToSpeech.LANG_NOT_SUPPORTED
            textToSpeech?.setSpeechRate(0.92f)
            textToSpeech?.setPitch(1.0f)

            pendingText?.let { queued ->
                pendingText = null
                speak(queued)
            }
        }
    }

    fun setVolume(value: Float) {
        volume = value.coerceIn(0f, 1f)
    }

    fun speak(text: String) {
        val cleanText = text.trim()
        if (cleanText.isBlank()) return

        lastText = cleanText
        if (!isReady) {
            pendingText = cleanText
            return
        }

        val parameters = Bundle().apply {
            putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, volume)
        }
        textToSpeech?.speak(
            cleanText,
            TextToSpeech.QUEUE_FLUSH,
            parameters,
            "cima_utterance_${System.currentTimeMillis()}"
        )
    }

    fun repeatLast() {
        lastText?.let(::speak)
    }

    fun stop() {
        textToSpeech?.stop()
    }

    fun shutdown() {
        stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        isReady = false
    }
}
