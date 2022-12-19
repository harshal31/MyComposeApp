package com.example.mycomposeapp.ui.movies_screen.repository

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycomposeapp.data.ResponseState
import com.example.mycomposeapp.model.Genre
import com.example.mycomposeapp.model.GenreType
import com.example.mycomposeapp.model.Movies
import com.example.mycomposeapp.model.MoviesResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class MoviesViewModel @Inject constructor(private val moviesRepository: MoviesRepository) : ViewModel() {

    val genre = mutableStateOf<ResponseState<Genre>>(ResponseState.Loading())
    val searchValue = MutableStateFlow("")
    val selectedGenre = mutableStateOf<GenreType?>(null)
    val currentIconIndex = mutableStateOf(0)
    val moviesTvShows = mutableStateListOf<MoviesResult>()
    val searchResponse = mutableStateOf<ResponseState<Movies>>(ResponseState.Loading())
    val searchMoviesAndTvShows = mutableStateListOf<MoviesResult>()
    val movieScreenState = mutableStateOf(MovieScreenState.NO_SEARCH)

    var page = 1
    var searchPage = 1

    init {
        callApiWhenUserIsSearching()
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

    private fun callApiWhenUserIsSearching() {
        viewModelScope.launch(Dispatchers.IO) {
            searchValue.debounce(800)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .flatMapLatest { flowOf(it) }
                .collect { query ->
                    Log.d("Success", "successresponse"+query)
                    val response = moviesRepository.getSearchMoviesOrTvShows(TmdbState.MOVIES.getState(currentIconIndex.value), searchPage, query.lowercase())
                    searchResponse.value = response
                }
        }
    }

    fun callApiWhenUserPaginatingSearch(tmdbState: TmdbState, query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = moviesRepository.getSearchMoviesOrTvShows(tmdbState, searchPage, query.lowercase())
            withContext(Dispatchers.Main) {
                when (response) {
                    is ResponseState.Failure -> Log.d("fail", "failure")
                    is ResponseState.Loading -> Log.d("load", "loading")
                    is ResponseState.Success -> searchMoviesAndTvShows.addAll(response.response.results.filter {
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

    fun resetSearchPagination() {
        searchPage = 1
        searchMoviesAndTvShows.clear()
        searchValue.value = ""
    }
}