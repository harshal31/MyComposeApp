package com.example.mycomposeapp.model


import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
    @SerializedName("adult")
    val adult: Boolean = false, // false
    @SerializedName("backdrop_path")
    val backdropPath: String? = "", // /y5Z0WesTjvn59jP6yo459eUsbli.jpg
    @SerializedName("belongs_to_collection")
    val belongsToCollection: BelongsToCollection = BelongsToCollection(),
    @SerializedName("budget")
    val budget: Int = 0, // 250000
    @SerializedName("genres")
    val genres: List<GenreType> = listOf(),
    @SerializedName("homepage")
    val homepage: String = "", // http://www.terrifier2themovie.com/
    @SerializedName("id")
    val id: Int = 0, // 663712
    @SerializedName("imdb_id")
    val imdbId: String = "", // tt10403420
    @SerializedName("original_language")
    val originalLanguage: String = "", // en
    @SerializedName("original_title")
    val originalTitle: String = "", // Terrifier 2
    @SerializedName("overview")
    val overview: String = "", // After being resurrected by a sinister entity, Art the Clown returns to Miles County where he must hunt down and destroy a teenage girl and her younger brother on Halloween night.  As the body count rises, the siblings fight to stay alive while uncovering the true nature of Art's evil intent.
    @SerializedName("popularity")
    val popularity: Double = 0.0, // 7035.54
    @SerializedName("poster_path")
    val posterPath: String? = "", // /yB8BMtvzHlMmRT1WmTQnGv1bcOK.jpg
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany> = listOf(),
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountry> = listOf(),
    @SerializedName("release_date")
    val releaseDate: String = "", // 2022-10-06
    @SerializedName("revenue")
    val revenue: Int = 0, // 5325078
    @SerializedName("runtime")
    val runtime: Int = 0, // 138
    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage> = listOf(),
    @SerializedName("status")
    val status: String = "", // Released
    @SerializedName("tagline")
    val tagline: String = "", // Who's Laughing Now?
    @SerializedName("title")
    val title: String = "", // Terrifier 2
    @SerializedName("video")
    val video: Boolean = false, // false
    @SerializedName("vote_average")
    val voteAverage: Double = 0.0, // 7.107
    @SerializedName("vote_count")
    val voteCount: Int = 0, // 427
    var movieCredits: MovieCredits? = null,
    var movieRecommendations: MovieRecommendations? = null
)

data class BelongsToCollection(
    @SerializedName("backdrop_path")
    val backdropPath: String = "", // /xGl7uzt0JX8u2WCIKK4e9Sk5Rat.jpg
    @SerializedName("id")
    val id: Int = 0, // 727761
    @SerializedName("name")
    val name: String = "", // Terrifier Collection
    @SerializedName("poster_path")
    val posterPath: String = "" // /fTDmbVPqUfeIMKM1ThLHSquwMsm.jpg
)

data class ProductionCompany(
    @SerializedName("id")
    val id: Int = 0, // 15157
    @SerializedName("logo_path")
    val logoPath: Any = Any(), // null
    @SerializedName("name")
    val name: String = "", // Bloody Disgusting
    @SerializedName("origin_country")
    val originCountry: String = "" // US
)

data class ProductionCountry(
    @SerializedName("iso_3166_1")
    val iso31661: String = "", // US
    @SerializedName("name")
    val name: String = "" // United States of America
)


data class SpokenLanguage(
    @SerializedName("english_name")
    val englishName: String = "", // English
    @SerializedName("iso_639_1")
    val iso6391: String = "", // en
    @SerializedName("name")
    val name: String = "" // English
)