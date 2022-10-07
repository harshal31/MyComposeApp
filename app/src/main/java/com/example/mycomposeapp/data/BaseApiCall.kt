/**
 * Copyright 2022 Lenovo, All Rights Reserved *
 */
package com.example.mycomposeapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

suspend fun <T> coroutineApiCall(retroFun: suspend () -> Response<T>, block: (ResponseState<T>) -> Unit) {
    withContext(Dispatchers.IO) {
        try {
            val response = retroFun.invoke()
            if (response.isSuccessful && response.code() == 200 && response.body() != null) {
                dispatchOnMain {  block(ResponseState.Success(response.body()!!)) }
            } else {
                dispatchOnMain { block(ResponseState.Failure(response.message(), response.code().toString())) }
            }
        } catch (e: Throwable) {
            dispatchOnMain { block(ResponseState.Failure(e.localizedMessage ?: "", "500")) }
        }
    }
}

suspend fun dispatchOnMain(block: () -> Unit) {
    withContext(Dispatchers.Main) {
        block()
    }
}