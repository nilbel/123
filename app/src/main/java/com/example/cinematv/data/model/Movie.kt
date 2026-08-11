package com.example.cinematv.data.model

import com.google.gson.annotations.SerializedName

/** Lightweight card shown in carousels / search results. */
data class Movie(
    val id: Int,
    val title: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
    val overview: String?
) {
    val posterUrl: String? get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
    val backdropUrl: String? get() = backdropPath?.let { "https://image.tmdb.org/t/p/w1280$it" }
    val year: String? get() = releaseDate?.take(4)
}

data class MoviePage(
    val page: Int,
    val results: List<Movie>,
    @SerializedName("total_pages") val totalPages: Int
)

/** Full details screen: overview, genres, runtime, cast. */
data class MovieDetails(
    val id: Int,
    val title: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
    val overview: String?,
    val runtime: Int?,
    val genres: List<Genre>,
    val credits: Credits?
) {
    val posterUrl: String? get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
    val backdropUrl: String? get() = backdropPath?.let { "https://image.tmdb.org/t/p/w1280$it" }
}

data class Genre(val id: Int, val name: String)

data class Credits(val cast: List<CastMember>, val crew: List<CrewMember>)

data class CastMember(
    val id: Int,
    val name: String,
    val character: String?,
    @SerializedName("profile_path") val profilePath: String?
) {
    val photoUrl: String? get() = profilePath?.let { "https://image.tmdb.org/t/p/w185$it" }
}

data class CrewMember(
    val id: Int,
    val name: String,
    val job: String?
)
