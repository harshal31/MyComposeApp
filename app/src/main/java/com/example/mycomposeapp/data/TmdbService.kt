
package com.example.mycomposeapp.data

import com.example.mycomposeapp.model.Genre
import com.example.mycomposeapp.model.Movies
import com.example.mycomposeapp.model.PostsItem
import com.example.mycomposeapp.ui.utils.EndPointConstant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface TmdbService {

    @GET("/posts")
    suspend fun getPosts(): Response<List<PostsItem>>

    @GET(EndPointConstant.DISCOVER_MOVIES_URL)
    suspend fun getMoviesAsPerGenre(@QueryMap map: Map<String, @JvmSuppressWildcards Any?>): Response<Movies>

    @GET(EndPointConstant.DISCOVER_TV_URL)
    suspend fun getTvShowsAsPerGenre(@QueryMap map: Map<String, @JvmSuppressWildcards Any?>): Response<Movies>

    @GET(EndPointConstant.GENRE_MOVIES_URL)
    suspend fun getMoviesGenre(@QueryMap map: Map<String, @JvmSuppressWildcards Any?>): Response<Genre>

    @GET(EndPointConstant.GENRE_TV_URL)
    suspend fun getTvShowsGenre(@QueryMap map: Map<String, @JvmSuppressWildcards Any?>): Response<Genre>
}