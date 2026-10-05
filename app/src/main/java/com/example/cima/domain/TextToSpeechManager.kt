package com.example.cima.domain

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TextToSpeechManager(context: Context) : TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isReady = false
    private var pendingText: String? = null
    private var lastText: String? = null

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

    fun speak(text: String) {
        val cleanText = text.trim()
        if (cleanText.isBlank()) return

        lastText = cleanText
        if (!isReady) {
            pendingText = cleanText
            return
        }

        textToSpeech?.speak(
            cleanText,
            TextToSpeech.QUEUE_FLUSH,
            null,
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
