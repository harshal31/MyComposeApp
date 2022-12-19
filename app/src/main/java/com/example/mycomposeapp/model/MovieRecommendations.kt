package com.example.mycomposeapp.model


import com.google.gson.annotations.SerializedName

data class MovieRecommendations(
    @SerializedName("page")
    val page: Int = 0, // 1
    @SerializedName("results")
    val recommendedMovies: List<RecommendedMovies> = listOf(),
    @SerializedName("total_pages")
    val totalPages: Int = 0, // 9
    @SerializedName("total_results")
    val totalResults: Int = 0 // 168
)

data class RecommendedMovies(
    @SerializedName("adult")
    val adult: Boolean = false, // false
    @SerializedName("backdrop_path")
    val backdropPath: String? = "", // null
    @SerializedName("genre_ids")
    val genreIds: List<Int> = listOf(),
    @SerializedName("id")
    val id: Int = 0, // 106912
    @SerializedName("original_language")
    val originalLanguage: String = "", // en
    @SerializedName("original_title")
    val originalTitle: String = "", // Darna! Ang Pagbabalik
    @SerializedName("overview")
    val overview: String = "", // Valentina, Darna's snake-haired arch enemy, is trying to take over the Phillipines through subliminal messages on religious TV shows. Darna has her own problems, however, as she has lost her magic pearl and with it the ability to transform into her scantily clad super self. Trapped as her alter-ego, the plucky reporter Narda, she must try to regain the pearl and foil Valentina's plans.
    @SerializedName("popularity")
    val popularity: Double = 0.0, // 1.012564
    @SerializedName("poster_path")
    val posterPath: String? = "", // null
    @SerializedName("release_date")
    val releaseDate: String = "", // 1994-05-09
    @SerializedName("title")
    val title: String = "", // Darna: The Return
    @SerializedName("video")
    val video: Boolean = false, // false
    @SerializedName("vote_average")
    val voteAverage: Int = 0, // 0
    @SerializedName("vote_count")
    val voteCount: Int = 0 // 0
)