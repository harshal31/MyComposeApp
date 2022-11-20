/**
 * Copyright 2022 Lenovo, All Rights Reserved *
 */
package com.example.mycomposeapp.data

import com.example.mycomposeapp.model.MovieCredits
import com.example.mycomposeapp.model.MovieDetailResponse
import com.example.mycomposeapp.model.MovieRecommendations
import com.example.mycomposeapp.ui.utils.EndPointConstant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface MovieDetailService {

    @GET(EndPointConstant.MOVIE_DETAIL_URL)
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int, @QueryMap map: Map<String, @JvmSuppressWildcards Any?>): Response<MovieDetailResponse>

    @GET(EndPointConstant.MOVIE_DETAIL_CREDITS)
    suspend fun getMovieDetailCredits(@Path("movie_id") movieId: Int,@QueryMap map: Map<String, @JvmSuppressWildcards Any?>): Response<MovieCredits>

    @GET(EndPointConstant.MOVIE_DETAIL_RECOMMENDATIONS)
    suspend fun getMovieRecommendations(@Path("movie_id") movieId: Int,@QueryMap map: Map<String, @JvmSuppressWildcards Any?>): Response<MovieRecommendations>

}