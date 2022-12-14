package com.example.mycomposeapp.model

import com.example.mycomposeapp.ui.movies_screen.repository.TmdbState
import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("genres")
    val genres: List<GenreType> = listOf()
)

data class GenreType(
    @SerializedName("id")
    val id: Int? = null, // 28
    @SerializedName("name")
    val name: String = "",  // Action
    var tmdbState: TmdbState = TmdbState.MOVIES
)