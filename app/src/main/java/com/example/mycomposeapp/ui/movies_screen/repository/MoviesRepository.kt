
package com.example.mycomposeapp.ui.movies_screen.repository

import com.example.mycomposeapp.data.ResponseState
import com.example.mycomposeapp.model.Genre
import com.example.mycomposeapp.model.Movies
import javax.inject.Inject

class MoviesRepository @Inject constructor(private val moviesDataSource: MoviesDataSource) {

    suspend fun getGenreAsPerState(tmdbState: TmdbState): ResponseState<Genre> {
        return if (tmdbState == TmdbState.MOVIES) {
            getMoviesGenre(moviesDataSource.getGenreMap())
        } else {
            getTvShowsGenre(moviesDataSource.getGenreMap())
        }
    }

    suspend fun getMoviesOrTvShows(tmdbState: TmdbState, genreId: String? = null, page: Int = 1): ResponseState<Movies> {
        return if (tmdbState == TmdbState.MOVIES) {
            moviesDataSource.getMoviesAsPerResponse(moviesDataSource.movieListMap(page, genreId))
        } else {
            moviesDataSource.getTvShowsAsPerResponse(moviesDataSource.tvShowsMap(page, genreId))
        }
    }

    suspend fun getSearchMoviesOrTvShows(tmdbState: TmdbState, page: Int = 1, query: String): ResponseState<Movies> {
        val map = moviesDataSource.searchMap(page, query)
        return if (tmdbState == TmdbState.MOVIES) {
            moviesDataSource.getSearchMovies(map)
        } else {
            moviesDataSource.getSearchTvShows(map)
        }

        /*val map = moviesDataSource.searchMap(page, query)
        val api = ApiM.getApiInterface()
        return if (tmdbState == TmdbState.MOVIES) {
            coroutineApiCall { api.getSearchMovies(map) }
        } else {
            coroutineApiCall { api.getSearchTvShows(map) }
        }*/
    }

    private suspend fun getMoviesGenre(map: Map<String, Any?>) = moviesDataSource.getMoviesGenre(map)
    private suspend fun getTvShowsGenre(map: Map<String, Any?>) = moviesDataSource.getTvShowsGenre(map)

}