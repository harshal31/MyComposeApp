package com.example.mycomposeapp.ui.movies_screen.repository

import com.example.mycomposeapp.data.ResponseState
import com.example.mycomposeapp.data.TmdbService
import com.example.mycomposeapp.data.coroutineApiCall
import com.example.mycomposeapp.model.Genre
import com.example.mycomposeapp.ui.utils.RestApiKey
import com.example.mycomposeapp.ui.utils.WebUrlConstant
import javax.inject.Inject

class MoviesDataSource @Inject constructor(private val moviesService: TmdbService) {

    suspend fun getMoviesGenre(map: Map<String, Any?>): ResponseState<Genre> = coroutineApiCall { moviesService.getMoviesGenre(map) }

    suspend fun getTvShowsGenre(map: Map<String, Any?>): ResponseState<Genre> = coroutineApiCall { moviesService.getTvShowsGenre(map) }

    suspend fun getMoviesAsPerResponse(map: Map<String, Any?>) = coroutineApiCall { moviesService.getMoviesAsPerGenreResponse(map) }

    suspend fun getTvShowsAsPerResponse(map: Map<String, Any?>) = coroutineApiCall { moviesService.getTvShowsAsPerGenreResponse(map) }

    suspend fun getSearchMovies(map: Map<String, Any?>) = coroutineApiCall { moviesService.getSearchMovies(map) }

    suspend fun getSearchTvShows(map: Map<String, Any?>) = coroutineApiCall { moviesService.getSearchTvShows(map) }

    fun getGenreMap() = mapOf(RestApiKey.API_KEY to WebUrlConstant.TMDB_API_KEY, RestApiKey.LANGUAGE to "en-US")

    fun searchMap(page: Int = 1, query: String) = mapOf(
        RestApiKey.API_KEY to WebUrlConstant.TMDB_API_KEY,
        RestApiKey.QUERY to query,
        RestApiKey.PAGE to page,
    )

    fun movieListMap(page: Int = 1, genreId: String? = null): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>(
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

    fun tvShowsMap(page: Int = 1, genreId: String? = null): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>(
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

enum class TmdbState {
    MOVIES, TV_SHOWS;

    fun getState(index: Int): TmdbState {
        return if (index == 0) MOVIES else TV_SHOWS
    }
}

enum class MovieScreenState {
    SEARCH, NO_SEARCH
}