/**
 * Copyright 2022 Lenovo, All Rights Reserved.
 */
package com.example.mycomposeapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.mycomposeapp.ui.greet_screen.animatedComposable
import com.example.mycomposeapp.ui.movie_detail_screen.MovieDetailScreen
import com.example.mycomposeapp.ui.movies_screen.MoviesScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigator() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(navController, startDestination = Route.MOVIES) {
        animatedComposable(Route.MOVIES) {
            MoviesScreen {
                navController.navigate(Route.MOVIES_DETAIL.plus("/${it.id}"))
            }
        }
        animatedComposable(Route.MOVIES_DETAIL.plus("/{id}"), arguments = listOf(navArgument("id") { type = NavType.IntType })) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("id") ?: error("provide movie_id")
            MovieDetailScreen(movieId = movieId)
        }
    }
}