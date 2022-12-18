
package com.example.mycomposeapp.data

import com.example.mycomposeapp.model.Genre
import com.example.mycomposeapp.model.Movies
import com.example.mycomposeapp.ui.utils.EndPointConstant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface TmdbService {

    @GET(EndPointConstant.DISCOVER_MOVIES_URL)
    suspend fun getMoviesAsPerGenreResponse(@QueryMap map: Map<String, @JvmSuppressWildcards Any?>): Response<Movies>

    @GET(EndPointConstant.DISCOVER_TV_URL)
    suspend fun getTvShowsAsPerGenreResponse(@QueryMap map: Map<String, @JvmSuppressWildcards Any?>): Response<Movies>

    @GET(EndPointConstant.GENRE_MOVIES_URL)
    suspend fun getMoviesGenre(@QueryMap map: Map<String, @JvmSuppressWildcards Any?>): Response<Genre>

    @GET(EndPointConstant.GENRE_TV_URL)
    suspend fun getTvShowsGenre(@QueryMap map: Map<String, @JvmSuppressWildcards Any?>): Response<Genre>

    @GET(EndPointConstant.SEARCH_MOVIES)
    suspend fun getSearchMovies(@QueryMap map: Map<String, @JvmSuppressWildcards Any?>): Response<Movies>

    @GET(EndPointConstant.SEARCH_TV_SHOWS)
    suspend fun getSearchTvShows(@QueryMap map: Map<String, @JvmSuppressWildcards Any?>): Response<Movies>
}