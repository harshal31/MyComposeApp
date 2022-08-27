/**
 * Copyright 2022 Lenovo, All Rights Reserved.
 */
package com.example.mycomposeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mycomposeapp.ui.greet_screen.Greeting
import com.example.mycomposeapp.ui.new_greet_screen.NewGreet

@Composable
fun AppNavigator() {

    val navController = rememberNavController()
    NavHost(navController, startDestination = Route.GREET) {
        composable(Route.GREET) {
            Greeting(navController)
        }

        composable(Route.NEW_GREET) {
            NewGreet()
        }
    }
}