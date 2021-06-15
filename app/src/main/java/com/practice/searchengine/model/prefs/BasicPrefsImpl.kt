package com.practice.searchengine.model.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

abstract class BasicPrefsImpl(protected val ctx: Context) {
    abstract val defaultPrefsFileName: String
    protected val prefs: SharedPreferences
        protected get() = getPrefs(defaultPrefsFileName)

    protected fun getPrefs(fileName: String): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            fileName,
            masterKeyAlias,
            ctx,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
    }

    protected val editor: SharedPreferences.Editor
        protected get() = getEditor(defaultPrefsFileName)

    protected fun getEditor(fileName: String): SharedPreferences.Editor {
        return getPrefs(fileName).edit()
    }

    operator fun get(fileName: String, key: String, defaultVal: String): String {
        return getPrefs(fileName).getString(key, defaultVal).toString()
    }

    operator fun get(key: String, defaultVal: String): String {
        return getPrefs(defaultPrefsFileName).getString(key, defaultVal).toString()
    }

    fun put(fileName: String, key: String, value: String) {
        getEditor(fileName).putString(key, value).apply()
    }

    fun put(key: String, value: String) {
        getEditor(defaultPrefsFileName).putString(key, value).apply()
    }

    operator fun get(fileName: String, key: String, defaultVal: Int): Int {
        return getPrefs(fileName).getInt(key, defaultVal)
    }

    operator fun get(key: String, defaultVal: Int): Int {
        return getPrefs(defaultPrefsFileName).getInt(key, defaultVal)
    }

    fun put(fileName: String, key: String, value: Int) {
        getEditor(fileName).putInt(key, value).apply()
    }

    fun put(key: String, value: Int) {
        getEditor(defaultPrefsFileName).putInt(key, value).apply()
    }

    operator fun get(fileName: String, key: String, defaultVal: Boolean): Boolean {
        return getPrefs(fileName).getBoolean(key, defaultVal)
    }

    operator fun get(key: String, defaultVal: Boolean): Boolean {
        return getPrefs(defaultPrefsFileName).getBoolean(key, defaultVal)
    }

    fun put(fileName: String, key: String, value: Boolean) {
        getEditor(fileName).putBoolean(key, value).apply()
    }

    fun put(key: String, value: Boolean) {
        getEditor(defaultPrefsFileName).putBoolean(key, value).apply()
    }

    operator fun get(fileName: String, key: String, defaultVal: Long): Long {
        return getPrefs(fileName).getLong(key, defaultVal)
    }

    operator fun get(key: String, defaultVal: Long): Long {
        return getPrefs(defaultPrefsFileName).getLong(key, defaultVal)
    }

    fun put(fileName: String, key: String, value: Long) {
        getEditor(fileName).putLong(key, value).apply()
    }

    fun put(key: String, value: Long) {
        getEditor(defaultPrefsFileName).putLong(key, value).apply()
    }

    operator fun get(fileName: String, key: String, defaultVal: Float): Float {
        return getPrefs(fileName).getFloat(key, defaultVal)
    }

    operator fun get(key: String, defaultVal: Float): Float {
        return getPrefs(defaultPrefsFileName).getFloat(key, defaultVal)
    }

    fun put(fileName: String, key: String, value: Float) {
        getEditor(fileName).putFloat(key, value).apply()
    }

    fun put(key: String, value: Float) {
        getEditor(defaultPrefsFileName).putFloat(key, value).apply()
    }

    operator fun get(fileName: String, key: String, defaultVal: Set<String>): Set<String>? {
        return getPrefs(fileName).getStringSet(key, defaultVal)
    }

    operator fun get(key: String, defaultVal: Set<String>): Set<String>? {
        return getPrefs(defaultPrefsFileName).getStringSet(key, defaultVal)
    }

    fun put(fileName: String, key: String, value: Set<String>) {
        getEditor(fileName).putStringSet(key, value).apply()
    }

    fun put(key: String, value: Set<String>) {
        getEditor(defaultPrefsFileName).putStringSet(key, value).apply()
    }

    operator fun get(fileName: String): Map<String, *> {
        return getPrefs(fileName).all
    }

    fun get(): Map<String, *> {
        return getPrefs(defaultPrefsFileName).all
    }
}
