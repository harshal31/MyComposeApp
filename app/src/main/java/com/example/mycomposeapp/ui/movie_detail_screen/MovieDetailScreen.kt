/**
 * Copyright 2022 Lenovo, All Rights Reserved *
 */

package com.example.mycomposeapp.ui.movie_detail_screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mycomposeapp.R
import com.example.mycomposeapp.data.ResponseState
import com.example.mycomposeapp.model.MovieDetailResponse
import com.example.mycomposeapp.ui.greet_screen.ExpandableText
import com.example.mycomposeapp.ui.greet_screen.LottieAnimationAccordingToRes
import com.example.mycomposeapp.ui.greet_screen.ProgressScreen
import com.example.mycomposeapp.ui.movie_detail_screen.repository.MoviesDetailViewModel
import com.example.mycomposeapp.ui.utils.WebUrlConstant
import kotlin.time.Duration.Companion.minutes

@Composable
fun MovieDetailScreen(movieId: Int) {
    val movieDetailViewModel = hiltViewModel<MoviesDetailViewModel>()
    movieDetailViewModel.getMovieDetail(movieId)

    when (val item = movieDetailViewModel.movieDetailResponse.value) {
        is ResponseState.Failure -> LottieAnimationAccordingToRes(if (item.responseCode == "502") R.raw.no_internet else R.raw.error_404)
        is ResponseState.Loading -> ProgressScreen()
        is ResponseState.Success -> {
            MovieDetailSection(
                item = item.response
            )
        }
    }
}

@Composable
fun MovieDetailSection(item: MovieDetailResponse) {
    val verticalScrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScrollState)
    ) {

        MovieHeader(
            item,
            Modifier
                .aspectRatio(12f / 9f)
                .padding(bottom = 8.dp)
        )

        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 8.dp)
        ) {
            val year = item.releaseDate.split("-").firstOrNull() ?: ""
            Text(
                text = item.title.plus(if (year.isNotEmpty()) " (${year})" else ""),
                color = textColor(),
                fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier
                    .padding(all = 2.dp)
                    .horizontalScroll(rememberScrollState())

            )

            MovieReleaseInfoGenreAndDurationDetail(
                item, Modifier
                    .fillMaxSize()
                    .horizontalScroll(rememberScrollState())
                    .padding(top = 4.dp, bottom = 2.dp)
            )
            MovieOverviewAndTagline(item = item)
        }
    }
}

@Composable
fun MovieHeader(item: MovieDetailResponse, modifier: Modifier) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(WebUrlConstant.IMAGE_BASE_URL.plus(item.posterPath ?: item.backdropPath))
            .crossfade(true)
            .build(),
        contentScale = ContentScale.FillBounds,
    )
    Image(
        modifier = modifier,
        painter = painter,
        contentScale = ContentScale.FillBounds,
        contentDescription = "Cat"
    )
}

@Composable
fun MovieReleaseInfoGenreAndDurationDetail(item: MovieDetailResponse, modifier: Modifier) {
    Row(modifier = modifier) {
        Text(
            text = item.releaseDate.plus(if (item.originalLanguage.isNotEmpty()) "(${item.originalLanguage})" else ""),
            color = textColor(),
            fontSize = 16.sp,
        )

        if (item.genres.isNotEmpty()) {
            Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            Text(
                text = item.genres.joinToString { it.name },
                color = textColor(),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        }

        if (item.runtime.minutes.toString().isNotEmpty()) {
            Spacer(Modifier.padding(horizontal = 2.dp))
            Text(
                text = item.runtime.minutes.toString(),
                color = textColor(),
                fontSize = 16.sp,
            )
        }
    }
}

@Composable
fun MovieOverviewAndTagline(item: MovieDetailResponse) {
    if (item.tagline.isNotEmpty()) {
        Text(
            text = item.tagline,
            color = textColor(),
            fontWeight = FontWeight.Light,
            fontStyle = FontStyle.Italic,
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(2.dp))
    }

    if (item.overview.isNotEmpty()) {
        Text(
            text = "Overview",
            color = textColor(),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        )

        Spacer(modifier = Modifier.height(2.dp))
        ExpandableText(text = item.overview, color = textColor(), size = 16.sp)
    }
}

@Composable
fun textColor(): Color {
    return if (isSystemInDarkTheme()) Color.White else Color.Black
}
