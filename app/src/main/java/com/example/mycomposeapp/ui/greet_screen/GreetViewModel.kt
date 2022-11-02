/**
 * Copyright 2022 Lenovo, All Rights Reserved.
 */
package com.example.mycomposeapp.ui.greet_screen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GreetViewModel: ViewModel() {

    init {
        Log.d("Effects", "initialize or call api")
        triggerInitialApi()
    }


//    val posts = mutableStateOf<ResponseState<List<PostsItem>>>(ResponseState.Success())
    val searchValue = MutableLiveData<String>()

    private fun triggerInitialApi() {
//        viewModelScope.launch {
//            coroutineApiCall({ ApiManager.getPosts() }) {
//                posts.value = it
//            }
//        }
    }

}