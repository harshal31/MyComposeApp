
@file:OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalAnimationApi::class)

package com.example.mycomposeapp.ui.movies_screen

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.navigation.NavController
import com.example.mycomposeapp.data.ResponseState
import com.example.mycomposeapp.ui.movies_screen.movies_screen_specific_ui.GenreChip
import com.example.mycomposeapp.ui.genericUIComposables.IconState
import com.example.mycomposeapp.ui.genericUIComposables.ScrollAnimation
import com.example.mycomposeapp.ui.genericUIComposables.TextFieldWithSwipeSuffixIcon
import com.example.mycomposeapp.ui.greet_screen.ProgressScreen
import com.example.mycomposeapp.ui.movies_screen.repository.MoviesViewModel
import com.example.mycomposeapp.ui.movies_screen.repository.TmdbState


@Composable
fun MoviesScreen(navController: NavController) {
    val viewModel = hiltViewModel<MoviesViewModel>()
    val genre = viewModel.genre

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        TextFieldWithSwipeSuffixIcon(
            value = viewModel.searchValue.observeAsState("").value,
            onValueChange = { viewModel.searchValue.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            iconState = IconState.TRAILING,
            onIconChanged = {
                if (genre.value !is ResponseState.Loading) {
                    viewModel.initiateApiAsPerApiType(TmdbState.MOVIES.getState(it) to null)
                }
            }
        )

        when (val it = genre.value) {
            is ResponseState.Failure -> Log.d("MoviesScreen", "Failure api call")
            is ResponseState.Loading -> ProgressScreen()
            is ResponseState.Success -> {

                AnimatedContent(targetState = it.response.genres, transitionSpec = { ScrollAnimation() }) { genres ->
                    GenreChip(list = genres) {

                    }

                }
            }
        }

    }

}
