/**
 * Copyright 2022 Lenovo, All Rights Reserved.
 */
package com.example.mycomposeapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import com.example.mycomposeapp.ui.greet_screen.Greeting
import com.example.mycomposeapp.ui.greet_screen.animatedComposable
import com.example.mycomposeapp.ui.new_greet_screen.NewGreet
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun AppNavigator() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(navController, startDestination = Route.GREET) {
        animatedComposable(Route.GREET) {
            Greeting(navController)
        }
        animatedComposable(Route.NEW_GREET) {
            NewGreet()
        }
    }
}