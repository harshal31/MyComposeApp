
package com.example.mycomposeapp.ui.movies_screen.repository

import com.example.mycomposeapp.data.ResponseState
import com.example.mycomposeapp.data.TmdbService
import com.example.mycomposeapp.data.coroutineApiCall
import com.example.mycomposeapp.model.Genre
import javax.inject.Inject

class MoviesDataSource @Inject constructor(private val moviesService: TmdbService) {

    suspend fun getMoviesGenre(map: Map<String, Any?>): ResponseState<Genre> {
        return coroutineApiCall { moviesService.getMoviesGenre(map) }
    }

    suspend fun getMoviesListAsPerGenre(map: Map<String, Any?>) = moviesService.getMoviesAsPerGenre(map)

    suspend fun getTvShowsGenre(map: Map<String, Any?>): ResponseState<Genre> {
        return coroutineApiCall { moviesService.getTvShowsGenre(map) }
    }

    suspend fun getTvShowsListAsPerGenre(map: Map<String, Any?>) = moviesService.getTvShowsAsPerGenre(map)

    suspend fun getMoviesAsPerResponse(map: Map<String, Any?>) = coroutineApiCall { moviesService.getMoviesAsPerGenreResponse(map) }
    suspend fun getTvShowsAsPerResponse(map: Map<String, Any?>) = coroutineApiCall { moviesService.getTvShowsAsPerGenreResponse(map) }



}

enum class TmdbState {
    MOVIES, TV_SHOWS;

    fun getState(index: Int): TmdbState {
        return if (index == 0) MOVIES else TV_SHOWS
    }
}