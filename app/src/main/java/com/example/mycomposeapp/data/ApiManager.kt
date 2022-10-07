package com.example.mycomposeapp.data

import com.example.mycomposeapp.model.PostsItem
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiManager: ApiInterface {

    private val apiService by lazy {
        val gson = GsonBuilder().setLenient().setPrettyPrinting().create()
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiInterface::class.java)
    }

    override suspend fun getPosts(): Response<List<PostsItem>> {
        return apiService.getPosts()
    }

}