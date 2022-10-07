/**
 * Copyright 2022 Lenovo, All Rights Reserved.
 */
package com.example.mycomposeapp.ui.greet_screen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycomposeapp.data.ApiManager
import com.example.mycomposeapp.data.ResponseState
import com.example.mycomposeapp.data.coroutineApiCall
import com.example.mycomposeapp.model.PostsItem
import kotlinx.coroutines.launch

class GreetViewModel: ViewModel() {

    init {
        Log.d("Effects", "initialize or call api")
        triggerInitialApi()
    }

    val posts: MutableState<ResponseState<List<PostsItem>>> = mutableStateOf(ResponseState.Progress())
    val searchValue = MutableLiveData<String>()

    private fun triggerInitialApi() {
        viewModelScope.launch {
            coroutineApiCall({ ApiManager.getPosts() }) {
                posts.value = it
            }
        }
    }

}