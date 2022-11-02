
package com.example.mycomposeapp.data

sealed class ResponseState<T> {

    data class Success<T>(val response: T): ResponseState<T>()

    data class Failure<T>(val failureMsg: String, val responseCode: String): ResponseState<T>()

    class Loading<T>: ResponseState<T>()

}
