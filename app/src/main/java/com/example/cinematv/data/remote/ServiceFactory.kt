package com.example.cinematv.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private fun baseClient(): OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(15, TimeUnit.SECONDS)
    .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
    .build()

object TmdbServiceFactory {
    private const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"

    fun create(): TmdbApi = Retrofit.Builder()
        .baseUrl(TMDB_BASE_URL)
        .client(baseClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TmdbApi::class.java)
}

object TorrServerServiceFactory {
    /**
     * Rebuilt every time the user changes the server address in Settings.
     * baseUrl must end with '/', e.g. "http://192.168.1.50:8090/".
     */
    fun create(baseUrl: String): TorrServerApi {
        val normalized = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
        return Retrofit.Builder()
            .baseUrl(normalized)
            .client(baseClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TorrServerApi::class.java)
    }
}
