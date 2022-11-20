package com.example.mycomposeapp.ui.movies_screen.repository

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mycomposeapp.data.ResponseState
import com.example.mycomposeapp.model.Genre
import com.example.mycomposeapp.model.GenreType
import com.example.mycomposeapp.model.MoviesResult
import com.example.mycomposeapp.ui.movies_screen.TvOrMoviesPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class MoviesViewModel @Inject constructor(private val moviesRepository: MoviesRepository) : ViewModel() {

    var genre = mutableStateOf<ResponseState<Genre>>(ResponseState.Loading())
    val searchValue = MutableLiveData<String>()
    var selectedGenre = mutableStateOf<GenreType?>(null)
    val selectedMovie = mutableStateOf<MoviesResult?>(null)
    val moviesTvShows = mutableStateListOf<MoviesResult>()
    var page = 1

    init {
        Log.d("MoviesViewModel", "call")
        initializeTmdbApi()
    }

    private fun initializeTmdbApi() {
        callMoviesOrTvShows(TmdbState.MOVIES)
        initiateApiAsPerApiType(tmdbStateWithGenre = TmdbState.MOVIES to null)
    }

    fun initiateApiAsPerApiType(tmdbStateWithGenre: Pair<TmdbState, GenreType?>) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = moviesRepository.getGenreAsPerState(tmdbStateWithGenre.first)
            withContext(Dispatchers.Main) {
                genre.value = response
            }
        }
    }

    fun getMoviesOrGenresAsPerTmdbState(
        tmdbState: TmdbState = TmdbState.MOVIES,
        genre: GenreType? = null
    ): SharedFlow<PagingData<MoviesResult>> {
        return Pager(PagingConfig(pageSize = 20)) {
            TvOrMoviesPagingSource(moviesRepository, tmdbState, genre?.id?.toString())
        }.flow.cachedIn(viewModelScope).shareIn(viewModelScope, SharingStarted.WhileSubscribed(5_000))
    }

    fun callMoviesOrTvShows(tmdbState: TmdbState = TmdbState.MOVIES, genreId: GenreType? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = moviesRepository.getMoviesOrTvShows(tmdbState, genreId?.id.toString())
            withContext(Dispatchers.Main) {
                when (response) {
                    is ResponseState.Failure -> Log.d("fail", "failure")
                    is ResponseState.Loading -> Log.d("load", "loading")
                    is ResponseState.Success -> moviesTvShows.addAll(response.response.results.filter {
                        it.posterPath.isNullOrEmpty().not() || it.backdropPath.isNullOrEmpty().not()
                    })
                }
            }
        }
    }

    fun callMoviesOrTvShowsAsPerPage(tmdbState: TmdbState = TmdbState.MOVIES, genreId: GenreType? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = moviesRepository.getMoviesOrTvShows(tmdbState, genreId?.id.toString(), page)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ResponseState.Failure -> Log.d("fail", "failure")
                    is ResponseState.Loading -> Log.d("load", "loading")
                    is ResponseState.Success -> moviesTvShows.addAll(response.response.results.filter {
                        it.posterPath.isNullOrEmpty().not() || it.backdropPath.isNullOrEmpty().not()
                    })
                }
            }
        }
    }

    fun resetPagination() {
        page = 1
        moviesTvShows.clear()
    }
}