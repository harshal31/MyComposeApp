/**
 * Copyright 2022 Lenovo, All Rights Reserved *
 */

package com.example.mycomposeapp.ui.movie_detail_screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.palette.graphics.Palette
import coil.compose.AsyncImagePainter
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
    var palette by remember { mutableStateOf<Palette?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScrollState)
    ) {

        MovieHeader(
            item,
            Modifier.aspectRatio(12f / 9f)
        ) {
            palette = it
        }

        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(Color(palette?.getLightVibrantColor(Color.Transparent.toArgb()) ?: 0))
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            val year = item.releaseDate.split("-").firstOrNull() ?: ""
            val password = "Abc@123424"
            Text(
                text = item.title.plus(if (year.isNotEmpty()) " (${year})" else ""),
                color = palette.textColorDynamically(),
                fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier
                    .padding(bottom = 2.dp)
                    .horizontalScroll(rememberScrollState())

            )

            MovieReleaseInfoGenreAndDurationDetail(
                item, Modifier
                    .fillMaxSize()
                    .horizontalScroll(rememberScrollState())
                    .padding(top = 4.dp, bottom = 2.dp), palette
            )
            MovieOverviewAndTagline(item = item, palette)
        }
    }
}

@Composable
fun MovieHeader(item: MovieDetailResponse, modifier: Modifier, block: (Palette) -> Unit) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(WebUrlConstant.IMAGE_BASE_URL.plus(item.posterPath ?: item.backdropPath))
            .crossfade(true)
            .allowHardware(false)
            .build(),
        contentScale = ContentScale.FillBounds,
    )

    if (painter.state is AsyncImagePainter.State.Success) {
        val bitmap = (painter.state as AsyncImagePainter.State.Success).result.drawable.toBitmap()
        block(Palette.from(bitmap).generate())
    }

    Image(
        modifier = modifier,
        painter = painter,
        contentScale = ContentScale.FillBounds,
        contentDescription = "Cat"
    )
}

@Composable
fun MovieReleaseInfoGenreAndDurationDetail(item: MovieDetailResponse, modifier: Modifier, palette: Palette?) {
    Row(modifier = modifier) {
        Text(
            text = item.releaseDate.plus(if (item.originalLanguage.isNotEmpty()) "(${item.originalLanguage})" else ""),
            color = palette.textColorDynamically(),
            fontSize = 16.sp,
        )

        if (item.genres.isNotEmpty()) {
            Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            Text(
                text = item.genres.joinToString { it.name },
                color = palette.textColorDynamically(),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        }

        if (item.runtime.minutes.toString().isNotEmpty()) {
            Spacer(Modifier.padding(horizontal = 2.dp))
            Text(
                text = item.runtime.minutes.toString(),
                color = palette.textColorDynamically(),
                fontSize = 16.sp,
            )
        }
    }
}

@Composable
fun MovieOverviewAndTagline(item: MovieDetailResponse, palette: Palette?) {
    if (item.tagline.isNotEmpty()) {
        Text(
            text = item.tagline,
            color = palette.textColorDynamically(),
            fontWeight = FontWeight.Light,
            fontStyle = FontStyle.Italic,
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(2.dp))
    }

    if (item.overview.isNotEmpty()) {
        Text(
            text = "Overview",
            color = palette.textColorDynamically(),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        )

        Spacer(modifier = Modifier.height(2.dp))
        ExpandableText(
            text = item.overview,
            color = palette.textColorDynamically(),
            size = 16.sp
        )
    }
}

@Composable
fun textColor(): Color {
    return if (isSystemInDarkTheme()) Color.White else Color.Black
}

@Composable
fun Palette?.textColorDynamically() = Color(this?.lightVibrantSwatch?.bodyTextColor ?: textColor().toArgb())

