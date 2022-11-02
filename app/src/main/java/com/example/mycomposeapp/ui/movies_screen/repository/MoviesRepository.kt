/**
 * Copyright 2022 Lenovo, All Rights Reserved *
 */
package com.example.mycomposeapp.ui.movies_screen.repository

import com.example.mycomposeapp.data.ResponseState
import com.example.mycomposeapp.model.Genre
import com.example.mycomposeapp.model.Movies
import com.example.mycomposeapp.ui.utils.RestApiKey
import com.example.mycomposeapp.ui.utils.WebUrlConstant
import javax.inject.Inject

class MoviesRepository @Inject constructor(private val moviesDataSource: MoviesDataSource) {


    suspend fun getGenreAsPerState(tmdbState: TmdbState): ResponseState<Genre> {
        return if (tmdbState == TmdbState.MOVIES) {
            getMoviesGenre(getGenreMap())
        } else {
            getTvShowsGenre(getGenreMap())
        }
    }

    suspend fun getMoviesOrTvShowsAsPerGenreId(tmdbState: TmdbState, genreId: String? = null, page: Int = 1): ResponseState<Movies> {
        return if (tmdbState == TmdbState.MOVIES) {
            getMoviesListAsPerGenre(movieListMap(page, genreId))
        } else {
            getTvShowsListAsPerGenre(tvShowsMap(page, genreId))
        }
    }

    private suspend fun getMoviesGenre(map: Map<String, Any?>) = moviesDataSource.getMoviesGenre(map)
    private suspend fun getTvShowsListAsPerGenre(map: Map<String, Any?>)  = moviesDataSource.getTvShowsListAsPerGenre(map)

    private suspend fun getMoviesListAsPerGenre(map: Map<String, Any?>) = moviesDataSource.getMoviesListAsPerGenre(map)
    private suspend fun getTvShowsGenre(map: Map<String, Any?>) = moviesDataSource.getTvShowsGenre(map)


    private fun getGenreMap() = mapOf(RestApiKey.API_KEY to WebUrlConstant.TMDB_API_KEY, RestApiKey.LANGUAGE to "en-US")

    private fun movieListMap(page: Int = 1, genreId: String? = null): Map<String, Any?> {
        val map =  mutableMapOf<String, Any?>(
            RestApiKey.API_KEY to WebUrlConstant.TMDB_API_KEY,
            RestApiKey.LANGUAGE to "en-US",
            RestApiKey.PAGE to page,
            RestApiKey.SORT_BY to "original_title.asc",
            RestApiKey.WITH_WATCH_MONETIZATION_TYPES to "flatrate"
        )

        return if (genreId.isNullOrEmpty().not()) {
            map[RestApiKey.WITH_GENRES] = genreId
            map
        } else map
    }

    private fun tvShowsMap(page: Int = 1, genreId: String? = null): Map<String, Any?> {
        val map =  mutableMapOf<String, Any?>(
            RestApiKey.API_KEY to WebUrlConstant.TMDB_API_KEY,
            RestApiKey.LANGUAGE to "en-US",
            RestApiKey.PAGE to page,
            RestApiKey.SORT_BY to "popularity.asc",
            RestApiKey.WITH_WATCH_MONETIZATION_TYPES to "flatrate"
        )

        return if (genreId.isNullOrEmpty().not()) {
            map[RestApiKey.WITH_GENRES] = genreId
            map
        } else map
    }
}