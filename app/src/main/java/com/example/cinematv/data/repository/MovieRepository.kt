package com.example.cinematv.data.repository

import com.example.cinematv.data.model.Movie
import com.example.cinematv.data.model.MovieDetails
import com.example.cinematv.data.remote.TmdbApi
import com.example.cinematv.data.settings.AppSettings
import kotlinx.coroutines.flow.first

class MovieRepository(
    private val api: TmdbApi,
    private val settings: AppSettings
) {
    private suspend fun apiKey(): String {
        val key = settings.tmdbApiKey.first()
        require(key.isNotBlank()) { "TMDB API key is not set — open Settings first" }
        return key
    }

    suspend fun popular(page: Int = 1): List<Movie> = api.popular(apiKey(), page = page).results

    suspend fun topRated(page: Int = 1): List<Movie> = api.topRated(apiKey(), page = page).results

    suspend fun trending(): List<Movie> = api.trending(apiKey()).results

    suspend fun search(query: String): List<Movie> =
        if (query.isBlank()) emptyList() else api.search(apiKey(), query).results

    suspend fun details(movieId: Int): MovieDetails = api.details(movieId, apiKey())
}
