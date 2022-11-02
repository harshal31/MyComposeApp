/**
 * Copyright 2022 Lenovo, All Rights Reserved *
 */
package com.example.mycomposeapp.data

import retrofit2.Response

suspend fun <T> coroutineApiCall(retroFun: suspend () -> Response<T>): ResponseState<T> {
    return try {
        val response = retroFun.invoke()
        if (response.isSuccessful && response.code() == 200 && response.body() != null) {
            ResponseState.Success(response.body()!!)
        } else {
            ResponseState.Failure(response.message(), response.code().toString())
        }
    } catch (e: Throwable) {
        ResponseState.Failure(e.localizedMessage ?: "", "500")
    }
}