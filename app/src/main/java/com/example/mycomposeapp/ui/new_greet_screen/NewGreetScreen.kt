/**
 * Copyright 2022 Lenovo, All Rights Reserved.
 */
package com.example.mycomposeapp.ui.new_greet_screen

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mycomposeapp.ui.greet_screen.GreetViewModel
import com.example.mycomposeapp.ui.greet_screen.ShareViewModel


@Composable
fun NewGreet() {
    /**
     * viewModel is used to hold data for the current compose screen
     */
    val viewModel = viewModel<GreetViewModel>()
    /**
     * shareModel between two viewModel using this we can share data across
     * two viewModel
     * e.g - NewGreetScreen: com.example.mycomposeapp.ui.greet_screen.ShareViewModel@2ac9c97
     */
    val shareModel = viewModel<ShareViewModel>(LocalContext.current as ComponentActivity)
    Log.d("ShareViewModel", "NewGreetScreen: $shareModel")

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(all = 10.dp), verticalAlignment = Alignment.Top) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .background(color = Color.Green)
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(6.dp))
            .padding(all = 10.dp)
        ) {
            /*Text(text = "Hello ${viewModel.state.value}!")
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {
                viewModel.triggerState()
            }) {
                Text(text = "Click me")
            }*/
        }
    }
}