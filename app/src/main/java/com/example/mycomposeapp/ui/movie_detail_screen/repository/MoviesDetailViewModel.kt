/**
 * Copyright 2022 Lenovo, All Rights Reserved *
 */
package com.example.mycomposeapp.ui.movie_detail_screen.repository

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycomposeapp.data.ResponseState
import com.example.mycomposeapp.model.MovieDetailResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesDetailViewModel @Inject constructor(private val moviesRepository: MovieDetailRepository) : ViewModel() {

    val movieDetailResponse = mutableStateOf<ResponseState<MovieDetailResponse>>(ResponseState.Loading())


    fun getMovieDetail(movieId: Int) {
        viewModelScope.launch {
            val movieDetail = moviesRepository.getMovieDetails(movieId)
            movieDetailResponse.value = movieDetail
        }
    }


}