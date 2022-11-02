/**
 * Copyright 2022 Lenovo, All Rights Reserved.
 */

package com.example.mycomposeapp.ui.new_greet_screen

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mycomposeapp.ui.greet_screen.ShareViewModel


@Composable
fun PostItem() {
    /**
     * shareModel between two viewModel using this we can share data across
     * two viewModel
     * e.g - NewGreetScreen: com.example.mycomposeapp.ui.greet_screen.ShareViewModel@2ac9c97
     */
    val shareModel = viewModel<ShareViewModel>(LocalContext.current as ComponentActivity)

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 16.dp),
            shape = CardDefaults.outlinedShape,
        ) {

            Column(
                modifier = Modifier
                    .padding(all = 8.dp)
            ) {

                Text(
                    text = shareModel.postItem.title,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                    color = Color.Black
                )

                Text(
                    text = shareModel.postItem.body,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                    color = Color.Black
                )

            }
        }
    }
}