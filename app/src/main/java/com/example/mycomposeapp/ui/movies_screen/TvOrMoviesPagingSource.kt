/**
 * Copyright 2022 Lenovo, All Rights Reserved *
 */
package com.example.mycomposeapp.ui.movies_screen

/*
class TvOrMoviesPagingSource(
    private val moviesRepository: MoviesRepository,
    private val tmdbState: TmdbState,
    private val genreId: String? = null
) :
    PagingSource<Int, MoviesResult>() {

    override fun getRefreshKey(state: PagingState<Int, MoviesResult>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviesResult> {
        return try {
            val page = params.key ?: 1
            val moviesTvShowsResponse = moviesRepository.getMoviesOrTvShowsAsPerGenreId(tmdbState, genreId, page)
            if (moviesTvShowsResponse.results.isNotEmpty()) {
                LoadResult.Page(
                    data = moviesTvShowsResponse.results.filter {
                        it.posterPath.isNullOrEmpty().not() || it.backdropPath.isNullOrEmpty().not()
                    },
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = page.plus(1)
                )
            } else {
                LoadResult.Error(NoDataFountException())
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

class NoDataFountException(val msg: String = "No Data Available"): Exception(msg)*/
