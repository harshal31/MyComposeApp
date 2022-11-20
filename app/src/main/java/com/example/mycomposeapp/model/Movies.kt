package com.example.mycomposeapp.model


import com.google.gson.annotations.SerializedName

data class Movies(
    @SerializedName("page")
    val page: Int = 0, // 1
    @SerializedName("results")
    val results: List<MoviesResult> = listOf(),
    @SerializedName("total_pages")
    val totalPages: Int = 0, // 35665
    @SerializedName("total_results")
    val totalResults: Int = 0 // 713291
)


data class MoviesResult(
    @SerializedName("adult")
    val adult: Boolean = false, // false
    @SerializedName("backdrop_path")
    val backdropPath: String? = "", // /y5Z0WesTjvn59jP6yo459eUsbli.jpg
    @SerializedName("genre_ids")
    val genreIds: List<Int> = listOf(),
    @SerializedName("id")
    val id: Int = 0, // 663712
    @SerializedName("original_language")
    val originalLanguage: String = "", // en
    @SerializedName("original_title")
    val originalTitle: String = "", // Terrifier 2
    @SerializedName("overview")
    val overview: String = "", // After being resurrected by a sinister entity, Art the Clown returns to Miles County where he must hunt down and destroy a teenage girl and her younger brother on Halloween night.  As the body count rises, the siblings fight to stay alive while uncovering the true nature of Art's evil intent.
    @SerializedName("popularity")
    val popularity: Double = 0.0, // 5162.285
    @SerializedName("poster_path")
    val posterPath: String? = "", // /yw8NQyvbeNXoZO6v4SEXrgQ27Ll.jpg
    @SerializedName("release_date")
    val releaseDate: String = "", // 2022-10-06
    @SerializedName("title")
    val title: String = "", // Terrifier 2
    @SerializedName("video")
    val video: Boolean = false, // false
    @SerializedName("vote_average")
    val voteAverage: Double = 0.0, // 7.4
    @SerializedName("vote_count")
    val voteCount: Int = 0, // 176
    @SerializedName("name")
    val name: String = "", // Club América vs. Club América
    @SerializedName("origin_country")
    val originCountry: List<String> = listOf()
)