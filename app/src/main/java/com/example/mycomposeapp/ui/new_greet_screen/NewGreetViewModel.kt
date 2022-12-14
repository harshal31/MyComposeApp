/**
 * Copyright 2022 Lenovo, All Rights Reserved.
 */
package com.example.mycomposeapp.ui.new_greet_screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class NewGreetViewModel: ViewModel() {
    val state = mutableStateOf(0)

    fun triggerState() {
        Log.d("ViewModel", "triggeeState: $this")
        state.value += 1
    }
}