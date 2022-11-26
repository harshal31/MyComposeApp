@file:OptIn(ExperimentalAnimationApi::class)

package com.example.mycomposeapp.ui.movies_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.mycomposeapp.ui.movies_screen.repository.MoviesViewModel
import com.example.mycomposeapp.ui.movies_screen.repository.TmdbState
import com.example.mycomposeapp.ui.utils.WebUrlConstant


@Composable
fun MoviesScreen(onItemClick: (MoviesResult) -> Unit) {
    val viewModel = hiltViewModel<MoviesViewModel>()
    val genre = viewModel.genre
    var currentIconIndex by remember { mutableStateOf(0) }

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
                currentIconIndex = it
                if (genre.value !is ResponseState.Loading) {
                    viewModel.resetPagination()
                    viewModel.initiateApiAsPerApiType(TmdbState.MOVIES.getState(it) to null)
                    viewModel.callMoviesOrTvShows(TmdbState.MOVIES.getState(it))
                }
            }
        )

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
            viewModel.callMoviesOrTvShows(TmdbState.MOVIES.getState(currentIconIndex), it)
            CustomMoviesAndTvShows(viewModel, TmdbState.MOVIES.getState(currentIconIndex)) { movieResult ->
                onItemClick(movieResult)
            }
        } ?: kotlin.run {
            CustomMoviesAndTvShows(viewModel, TmdbState.MOVIES.getState(currentIconIndex)) { movieResult ->
                onItemClick(movieResult)
            }
        }
    }
}

@Composable
fun CustomMoviesAndTvShows(viewModel: MoviesViewModel, tmdbState: TmdbState, onMovieClick: (MoviesResult) -> Unit) {
    val state = rememberLazyGridState()

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

/*
@Composable
fun LoadingItem() {
    CircularProgressIndicator(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentWidth(
                Alignment.CenterHorizontally
            )
    )
}


@Composable
fun MoviesAndTvShowsWithPagingLibrary(tmdbMovies: Flow<PagingData<MoviesResult>>, onMovieClick: (MoviesResult) -> Unit) {
    val state = rememberLazyGridState()
    val movies = tmdbMovies.collectAsLazyPagingItems()

    Log.d("Statetetete", "listState ${state.firstVisibleItemIndex}")
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

        items(movies.itemCount) { index ->
            val item = movies[index]
            MovieItem(item = item, onMovieClick = onMovieClick)
        }
        movies.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    Log.d("Loading state", "loading")
                    this@LazyVerticalGrid.item { LoadingItem() }
                    this@LazyVerticalGrid.item { LoadingItem() }
                }
                loadState.append is LoadState.Loading -> {
                    Log.d("Loading state2", "loading")
                    this@LazyVerticalGrid.item { LoadingItem() }
                    this@LazyVerticalGrid.item { LoadingItem() }
                }
                loadState.refresh is LoadState.Error -> {
                    if ((loadState.refresh as LoadState.Error).error is NoDataFountException) {
                        this@LazyVerticalGrid.item { LottieAnimationAccordingToRes(res = R.raw.no_data_available) }
                    }
                }
                loadState.append is LoadState.Error -> {
                    if ((loadState.append as LoadState.Error).error is NoDataFountException) {
                        this@LazyVerticalGrid.item { LottieAnimationAccordingToRes(res = R.raw.no_data_available) }
                    }
                }
            }
        }
    }
}
*/


