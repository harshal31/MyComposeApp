/**
 * Copyright 2022 Lenovo, All Rights Reserved *
 */
package com.example.mycomposeapp.ui.movies_screen.repository

import com.example.mycomposeapp.data.ResponseState
import com.example.mycomposeapp.data.TmdbService
import com.example.mycomposeapp.data.coroutineApiCall
import com.example.mycomposeapp.model.Genre
import com.example.mycomposeapp.model.Movies
import javax.inject.Inject

class MoviesDataSource @Inject constructor(private val moviesService: TmdbService) {

    suspend fun getMoviesGenre(map: Map<String, Any?>): ResponseState<Genre> {
        return coroutineApiCall { moviesService.getMoviesGenre(map) }
    }

    suspend fun getMoviesListAsPerGenre(map: Map<String, Any?>): ResponseState<Movies> {
        return coroutineApiCall { moviesService.getMoviesAsPerGenre(map) }
    }

    suspend fun getTvShowsGenre(map: Map<String, Any?>): ResponseState<Genre> {
        return coroutineApiCall { moviesService.getTvShowsGenre(map) }
    }

    suspend fun getTvShowsListAsPerGenre(map: Map<String, Any?>): ResponseState<Movies> {
        return coroutineApiCall { moviesService.getTvShowsAsPerGenre(map) }
    }

}

enum class TmdbState {
    MOVIES, TV_SHOWS;

    fun getState(index: Int): TmdbState {
        return if (index == 0) MOVIES else TV_SHOWS
    }
}