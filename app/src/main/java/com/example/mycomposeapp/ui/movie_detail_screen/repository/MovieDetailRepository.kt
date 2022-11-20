/**
 * Copyright 2022 Lenovo, All Rights Reserved *
 */
package com.example.mycomposeapp.ui.movie_detail_screen.repository

import com.example.mycomposeapp.data.ResponseState
import com.example.mycomposeapp.model.MovieDetailResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieDetailRepository @Inject constructor(private val movieDetailDataSource: MovieDetailDataSource) {

    suspend fun getMovieDetails(movieId: Int): ResponseState<MovieDetailResponse> {
        return withContext(Dispatchers.IO) {
            val state = async { movieDetailDataSource.getMovieDetailResponse(movieId) }.await()
            val movieCredits = async { movieDetailDataSource.getMovieCredits(movieId) }.await()
            val movieRecommendations = async { movieDetailDataSource.getMovieRecommendations(movieId) }.await()
            if (state is ResponseState.Success) {
                if (movieCredits is ResponseState.Success) {
                    state.response.movieCredits = movieCredits.response
                }
                if (movieRecommendations is ResponseState.Success) {
                    state.response.movieRecommendations = movieRecommendations.response
                }
            }
            state
        }
    }

}