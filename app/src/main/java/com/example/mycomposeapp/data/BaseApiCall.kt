
package com.example.mycomposeapp.data

import retrofit2.Response
import java.io.IOException

suspend fun <T> coroutineApiCall(retroFun: suspend () -> Response<T>): ResponseState<T> {
    return try {
        val response = retroFun.invoke()
        if (response.isSuccessful && response.code() == 200 && response.body() != null) {
            ResponseState.Success(response.body()!!)
        } else {
            ResponseState.Failure(response.message(), response.code().toString())
        }
    } catch (e: Throwable) {
        when(e) {
           is IOException ->  ResponseState.Failure(e.localizedMessage ?: "", "502")
           else -> ResponseState.Failure(e.localizedMessage ?: "", "500")
        }
    }
}