/**
 * Copyright 2022 Lenovo, All Rights Reserved *
 */
package com.example.mycomposeapp.ui.movie_detail_screen.repository

import com.example.mycomposeapp.data.MovieDetailService
import com.example.mycomposeapp.data.coroutineApiCall
import com.example.mycomposeapp.ui.utils.RestApiKey
import com.example.mycomposeapp.ui.utils.WebUrlConstant
import javax.inject.Inject

class MovieDetailDataSource @Inject constructor(val movieDetailService: MovieDetailService) {

    suspend fun getMovieDetailResponse(movieId: Int) =
       coroutineApiCall { movieDetailService.getMovieDetails(movieId, getQueryMap()) }

    suspend fun getMovieCredits(movieId: Int) = coroutineApiCall { movieDetailService.getMovieDetailCredits(movieId, getQueryMap()) }

    suspend fun getMovieRecommendations(movieId: Int) = coroutineApiCall { movieDetailService.getMovieRecommendations(movieId, getQueryMap(1)) }

    private fun getQueryMap(page: Int = 0): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>(
            RestApiKey.API_KEY to WebUrlConstant.TMDB_API_KEY,
            RestApiKey.LANGUAGE to "en-Us",
        )
        if (page != 0) {
            map[RestApiKey.PAGE] = page
        }
        return map
    }
}