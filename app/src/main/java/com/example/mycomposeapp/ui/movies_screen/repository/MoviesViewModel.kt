/**
 * Copyright 2022 Lenovo, All Rights Reserved *
 */
package com.example.mycomposeapp.ui.movies_screen.repository

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycomposeapp.data.ResponseState
import com.example.mycomposeapp.model.Genre
import com.example.mycomposeapp.model.GenreType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class MoviesViewModel @Inject constructor(private val moviesRepository: MoviesRepository) : ViewModel() {

    var genre = mutableStateOf<ResponseState<Genre>>(ResponseState.Loading())
    var tmdbMoviesTvShowsData = mutableStateOf<ResponseState<Genre>>(ResponseState.Loading())
    val searchValue = MutableLiveData<String>()

    init {
        Log.d("MoviesViewModel", "call")
        initializeTmdbApi()
    }

    private fun initializeTmdbApi() {
        initiateApiAsPerApiType(tmdbStateWithGenre = TmdbState.MOVIES to null)
    }

    fun initiateApiAsPerApiType(tmdbStateWithGenre: Pair<TmdbState, GenreType?>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                this@MoviesViewModel.genre.value = moviesRepository.getGenreAsPerState(tmdbStateWithGenre.first)
            }
        }
    }


}