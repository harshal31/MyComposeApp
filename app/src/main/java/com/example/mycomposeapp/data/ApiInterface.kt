/**
 * Copyright 2022 Lenovo, All Rights Reserved *
 */
package com.example.mycomposeapp.data

import com.example.mycomposeapp.model.PostsItem
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("/posts")
    suspend fun getPosts(): Response<List<PostsItem>>

}