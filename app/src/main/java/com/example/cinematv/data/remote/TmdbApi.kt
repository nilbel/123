package com.example.cinematv.data.remote

import com.example.cinematv.data.model.MovieDetails
import com.example.cinematv.data.model.MoviePage
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Thin wrapper around the public TMDB v3 API.
 * Docs: https://developer.themoviedb.org/reference/intro/getting-started
 * The user supplies their own free API key in Settings.
 */
interface TmdbApi {

    @GET("movie/popular")
    suspend fun popular(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ru-RU",
        @Query("page") page: Int = 1
    ): MoviePage

    @GET("movie/top_rated")
    suspend fun topRated(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ru-RU",
        @Query("page") page: Int = 1
    ): MoviePage

    @GET("trending/movie/week")
    suspend fun trending(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ru-RU"
    ): MoviePage

    @GET("search/movie")
    suspend fun search(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String = "ru-RU",
        @Query("page") page: Int = 1
    ): MoviePage

    @GET("movie/{id}")
    suspend fun details(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ru-RU",
        @Query("append_to_response") appendToResponse: String = "credits"
    ): MovieDetails
}
