package kr.co.rolling.moment.library.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Encrypted Preference Manager
 */
object PreferenceManager {
    private const val FILENAME: String = "SettingsPreferences_enc"
    private var preference: SharedPreferences? = null

    private const val KEY_PUSH_TOKEN = "RollinMoment_KEY_PUSH_TOKEN"

    fun init(context: Context) {
        preference = initEncPreference(context)
    }

    fun setPushToken(token: String) {
        write(KEY_PUSH_TOKEN, token)
    }

    fun getPushToken(): String {
        return read(KEY_PUSH_TOKEN, "")
    }

    private fun initEncPreference(context: Context): SharedPreferences {
        val masterKeyAlias = MasterKey
            .Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            FILENAME,
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }


    @SuppressLint("UseKtx")
    private fun <T> write(key: String, value: T) {
        preference?.run {
            @Suppress("UNCHECKED_CAST")
            when (value) {
                is Boolean -> edit().putBoolean(key, value).apply()
                is String -> edit().putString(key, value).apply()
                is Int -> edit().putInt(key, value).apply()
                is Long -> edit().putLong(key, value).apply()
                is Float -> edit().putFloat(key, value).apply()
                is HashSet<*> -> edit().putStringSet(key, value as HashSet<String>).apply()
                else -> throw UnsupportedOperationException("not yet implemented type")
            }
        } ?: run {
            throw IllegalStateException("must call init")
        }
    }


    private fun <T> read(key: String, defaultValue: T): T {
        return preference?.run {
            @Suppress("UNCHECKED_CAST")
            return when (defaultValue) {
                is Boolean -> getBoolean(key, defaultValue) as T
                is String -> getString(key, defaultValue) as T
                is Int -> getInt(key, defaultValue) as T
                is Long -> getLong(key, defaultValue) as T
                is Float -> getFloat(key, defaultValue) as T
                is HashSet<*> -> getStringSet(key, defaultValue as HashSet<String>) as T
                else -> throw UnsupportedOperationException("not yet implemented type")
            }
        } ?: run {
            throw IllegalStateException("must call init")
        }
    }
}