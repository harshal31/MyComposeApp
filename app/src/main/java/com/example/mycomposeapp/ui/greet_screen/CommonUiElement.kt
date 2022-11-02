
@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mycomposeapp.ui.greet_screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable


@Composable
fun ProgressScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.animatedComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit) {


    composable(
        route,
        arguments,
        deepLinks,
        enterTransition = {
            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(500))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(500))
        },
        popEnterTransition = {
            slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(500))
        },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(500))
        },
        content
    )
}