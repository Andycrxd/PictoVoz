package com.example.cima.data.repository

import android.content.Context
import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

data class AdminPreferences(
    val ttsVolume: Float = 1f,
    val vocabularyLevel: String = AdminOptions.VOCABULARY_LEVELS[1],
    val grammaticalPerson: String = AdminOptions.GRAMMATICAL_PERSONS[0],
    val activeProfileId: Int? = null,
    val pinConfigured: Boolean = false
)

object AdminOptions {
    val VOCABULARY_LEVELS = listOf("Básico", "Intermedio", "Avanzado")
    val GRAMMATICAL_PERSONS = listOf(
        "Primera persona (yo)",
        "Segunda persona (tú)",
        "Tercera persona (él/ella)"
    )
}

/** Preferencias locales del dispositivo: volumen, ajustes de generación, perfil activo y PIN. */
class AdminPreferencesRepository(context: Context) {
    private val preferences = context.applicationContext
        .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val mutableSettings = MutableStateFlow(readSettings())
    val settings: StateFlow<AdminPreferences> = mutableSettings.asStateFlow()

    fun setTtsVolume(value: Float) {
        preferences.edit().putFloat(KEY_TTS_VOLUME, value.coerceIn(0f, 1f)).apply()
        publishSettings()
    }

    fun setVocabularyLevel(value: String) {
        if (value !in AdminOptions.VOCABULARY_LEVELS) return
        preferences.edit().putString(KEY_VOCABULARY_LEVEL, value).apply()
        publishSettings()
    }

    fun setGrammaticalPerson(value: String) {
        if (value !in AdminOptions.GRAMMATICAL_PERSONS) return
        preferences.edit().putString(KEY_GRAMMATICAL_PERSON, value).apply()
        publishSettings()
    }

    fun setActiveProfileId(profileId: Int) {
        preferences.edit().putInt(KEY_ACTIVE_PROFILE_ID, profileId).apply()
        publishSettings()
    }

    suspend fun saveAdminPin(pin: String) = withContext(Dispatchers.Default) {
        require(pin.matches(Regex("\\d{4}"))) { "El PIN debe tener 4 dígitos." }
        val salt = ByteArray(SALT_BYTES).also(SecureRandom()::nextBytes)
        val hash = derivePinHash(pin, salt)
        preferences.edit()
            .putString(KEY_PIN_SALT, Base64.encodeToString(salt, Base64.NO_WRAP))
            .putString(KEY_PIN_HASH, Base64.encodeToString(hash, Base64.NO_WRAP))
            .apply()
        publishSettings()
    }

    suspend fun verifyAdminPin(pin: String): Boolean = withContext(Dispatchers.Default) {
        if (!pin.matches(Regex("\\d{4}"))) return@withContext false
        val saltText = preferences.getString(KEY_PIN_SALT, null) ?: return@withContext false
        val hashText = preferences.getString(KEY_PIN_HASH, null) ?: return@withContext false
        val salt = runCatching { Base64.decode(saltText, Base64.NO_WRAP) }.getOrNull()
            ?: return@withContext false
        val expected = runCatching { Base64.decode(hashText, Base64.NO_WRAP) }.getOrNull()
            ?: return@withContext false
        MessageDigest.isEqual(expected, derivePinHash(pin, salt))
    }

    private fun publishSettings() {
        mutableSettings.value = readSettings()
    }

    private fun readSettings() = AdminPreferences(
        ttsVolume = preferences.getFloat(KEY_TTS_VOLUME, 1f).coerceIn(0f, 1f),
        vocabularyLevel = preferences.getString(
            KEY_VOCABULARY_LEVEL,
            AdminOptions.VOCABULARY_LEVELS[1]
        )?.takeIf { it in AdminOptions.VOCABULARY_LEVELS }
            ?: AdminOptions.VOCABULARY_LEVELS[1],
        grammaticalPerson = preferences.getString(
            KEY_GRAMMATICAL_PERSON,
            AdminOptions.GRAMMATICAL_PERSONS[0]
        )?.takeIf { it in AdminOptions.GRAMMATICAL_PERSONS }
            ?: AdminOptions.GRAMMATICAL_PERSONS[0],
        activeProfileId = preferences.getInt(KEY_ACTIVE_PROFILE_ID, NO_PROFILE)
            .takeIf { it != NO_PROFILE },
        pinConfigured = preferences.contains(KEY_PIN_SALT) && preferences.contains(KEY_PIN_HASH)
    )

    private fun derivePinHash(pin: String, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(pin.toCharArray(), salt, PBKDF2_ITERATIONS, HASH_BITS)
        return try {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(spec)
                .encoded
        } finally {
            spec.clearPassword()
        }
    }

    private companion object {
        const val PREFERENCES_NAME = "pictovoz_admin_preferences"
        const val KEY_TTS_VOLUME = "tts_volume"
        const val KEY_VOCABULARY_LEVEL = "vocabulary_level"
        const val KEY_GRAMMATICAL_PERSON = "grammatical_person"
        const val KEY_ACTIVE_PROFILE_ID = "active_profile_id"
        const val KEY_PIN_SALT = "admin_pin_salt"
        const val KEY_PIN_HASH = "admin_pin_hash"
        const val NO_PROFILE = -1
        const val SALT_BYTES = 16
        const val PBKDF2_ITERATIONS = 120_000
        const val HASH_BITS = 256
    }
}
