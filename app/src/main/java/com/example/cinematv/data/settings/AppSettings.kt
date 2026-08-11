package com.example.cinematv.data.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "cinema_settings")

class AppSettings(private val context: Context) {

    private object Keys {
        val TORRSERVER_URL = stringPreferencesKey("torrserver_url")
        val TMDB_API_KEY = stringPreferencesKey("tmdb_api_key")
    }

    /** e.g. "http://192.168.1.50:8090/" — the user's own TorrServer instance. */
    val torrServerUrl: Flow<String> = context.dataStore.data.map { it[Keys.TORRSERVER_URL] ?: "" }

    /** Free key from https://www.themoviedb.org/settings/api */
    val tmdbApiKey: Flow<String> = context.dataStore.data.map { it[Keys.TMDB_API_KEY] ?: "" }

    suspend fun setTorrServerUrl(url: String) {
        context.dataStore.edit { it[Keys.TORRSERVER_URL] = url.trim() }
    }

    suspend fun setTmdbApiKey(key: String) {
        context.dataStore.edit { it[Keys.TMDB_API_KEY] = key.trim() }
    }
}
