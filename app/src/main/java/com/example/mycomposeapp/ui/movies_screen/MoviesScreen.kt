@file:OptIn(ExperimentalAnimationApi::class)

package com.example.mycomposeapp.ui.movies_screen

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mycomposeapp.R
import com.example.mycomposeapp.data.ResponseState
import com.example.mycomposeapp.model.MoviesResult
import com.example.mycomposeapp.ui.genericUIComposables.IconState
import com.example.mycomposeapp.ui.genericUIComposables.ScrollAnimation
import com.example.mycomposeapp.ui.genericUIComposables.TextFieldWithSwipeSuffixIcon
import com.example.mycomposeapp.ui.greet_screen.LottieAnimationAccordingToRes
import com.example.mycomposeapp.ui.greet_screen.ProgressScreen
import com.example.mycomposeapp.ui.movies_screen.movies_screen_specific_ui.GenreChip
import com.example.mycomposeapp.ui.movies_screen.repository.MovieScreenState
import com.example.mycomposeapp.ui.movies_screen.repository.MoviesViewModel
import com.example.mycomposeapp.ui.movies_screen.repository.TmdbState
import com.example.mycomposeapp.ui.utils.WebUrlConstant


@Composable
fun MoviesScreen(onItemClick: (MoviesResult) -> Unit) {
    val viewModel = hiltViewModel<MoviesViewModel>()
    val genre = viewModel.genre
    val state = rememberLazyGridState()
    val searchState = rememberLazyGridState()

    Column(modifier = Modifier.fillMaxSize()) {
        TextFieldWithSwipeSuffixIcon(
            value = viewModel.searchValue.collectAsState("").value,
            onValueChange = {
                viewModel.movieScreenState.value = if (it.isNotEmpty()) {
                    viewModel.searchResponse.value = ResponseState.Loading()
                    MovieScreenState.SEARCH
                } else {
                    viewModel.resetSearchPagination()
                    MovieScreenState.NO_SEARCH
                }
                viewModel.searchValue.value = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            iconState = IconState.TRAILING,
            onIconChanged = {
                viewModel.currentIconIndex.value = it
                if (genre.value !is ResponseState.Loading) {
                    viewModel.movieScreenState.value = MovieScreenState.NO_SEARCH
                    viewModel.resetPagination()
                    viewModel.resetSearchPagination()
                    viewModel.initiateApiAsPerApiType(TmdbState.MOVIES.getState(it) to null)
                    viewModel.callMoviesOrTvShows(TmdbState.MOVIES.getState(it))
                }
            }
        )

        if (viewModel.movieScreenState.value == MovieScreenState.NO_SEARCH) {
            when (val it = genre.value) {
                is ResponseState.Failure -> LottieAnimationAccordingToRes(if (it.responseCode == "502") R.raw.no_internet else R.raw.error_404)
                is ResponseState.Loading -> ProgressScreen()
                is ResponseState.Success -> {
                    AnimatedContent(targetState = it.response.genres, transitionSpec = { ScrollAnimation() }) { genre ->
                        GenreChip(list = genre) {
                            viewModel.resetPagination()
                            viewModel.selectedGenre.value = it
                        }
                    }
                }
            }

            viewModel.selectedGenre.value?.let {
                viewModel.callMoviesOrTvShows(TmdbState.MOVIES.getState(viewModel.currentIconIndex.value), it)
                CustomMoviesAndTvShows(
                    viewModel,
                    TmdbState.MOVIES.getState(viewModel.currentIconIndex.value),
                    state
                ) { movieResult ->
                    onItemClick(movieResult)
                }
            } ?: kotlin.run {
                CustomMoviesAndTvShows(
                    viewModel,
                    TmdbState.MOVIES.getState(viewModel.currentIconIndex.value),
                    state
                ) { movieResult ->
                    onItemClick(movieResult)
                }
            }
        }

        if (viewModel.movieScreenState.value == MovieScreenState.SEARCH) {
            when (val it = viewModel.searchResponse.value) {
                is ResponseState.Failure -> LottieAnimationAccordingToRes(if (it.responseCode == "502") R.raw.no_internet else R.raw.error_404)
                is ResponseState.Loading -> {
                    Log.d("Loading", "Loading data")
                    LottieAnimationAccordingToRes(R.raw.searching)
                }
                is ResponseState.Success -> {
                    Log.d("Success", "successresponse" + it.response.results.size)
                    if (it.response.results.isNotEmpty()) {
                        viewModel.searchMoviesAndTvShows.addAll(it.response.results.filter {
                            it.posterPath.isNullOrEmpty().not() || it.backdropPath.isNullOrEmpty().not()
                        })
                        it.response.results.clear()
                    }

                    if (viewModel.searchMoviesAndTvShows.toList().isEmpty()) {
                        LottieAnimationAccordingToRes(R.raw.no_data_found)
                    } else {
                        CustomSearchMoviesAndGenre(
                            viewModel,
                            TmdbState.MOVIES.getState(viewModel.currentIconIndex.value),
                            viewModel.searchValue.collectAsState("").value,
                            searchState
                        ) { movieResult ->
                            onItemClick(movieResult)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomSearchMoviesAndGenre(
    viewModel: MoviesViewModel,
    tmdbState: TmdbState,
    query: String,
    state: LazyGridState,
    onMovieClick: (MoviesResult) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(all = 8.dp),
        state = state
    ) {

        itemsIndexed(viewModel.searchMoviesAndTvShows.toList()) { index, item ->
            if (index == viewModel.searchMoviesAndTvShows.lastIndex) {
                Log.d("Search", "searching data")
                ++viewModel.searchPage
                viewModel.callApiWhenUserPaginatingSearch(tmdbState, query)
            }
            MovieItem(item = item, onMovieClick = onMovieClick)
        }
    }
}

@Composable
fun CustomMoviesAndTvShows(
    viewModel: MoviesViewModel,
    tmdbState: TmdbState,
    state: LazyGridState,
    onMovieClick: (MoviesResult) -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(all = 8.dp),
        state = state
    ) {

        itemsIndexed(viewModel.moviesTvShows.toList()) { index, item ->
            if (index == viewModel.moviesTvShows.lastIndex) {
                ++viewModel.page
                viewModel.callMoviesOrTvShowsAsPerPage(tmdbState, viewModel.selectedGenre.value)
            }
            MovieItem(item = item, onMovieClick = onMovieClick)
        }
    }
}

@Composable
fun MovieItem(item: MoviesResult?, onMovieClick: (MoviesResult) -> Unit) {
    Box(contentAlignment = Alignment.Center, content = {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(WebUrlConstant.IMAGE_BASE_URL.plus(item?.posterPath ?: item?.backdropPath))
                .crossfade(true)
                .build(),
            contentScale = ContentScale.FillBounds,
        )

        if (painter.state is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Image(
            modifier = Modifier
                .clip(RoundedCornerShape(15))
                .clickable {
                    onMovieClick(item!!)
                }
                .aspectRatio(9f / 16f, matchHeightConstraintsFirst = true),
            painter = painter,
            contentScale = ContentScale.FillBounds,
            contentDescription = "Cat"
        )
    })
}
