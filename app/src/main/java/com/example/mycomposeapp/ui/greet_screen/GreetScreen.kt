/**
 * Copyright 2022 Lenovo, All Rights Reserved.
 */
@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mycomposeapp.ui.greet_screen

import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mycomposeapp.data.ResponseState
import com.example.mycomposeapp.model.PostsItem
import com.example.mycomposeapp.navigation.Route
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
fun Greeting(navController: NavController) {
    /**
     * viewModel is used to hold data for the current compose screen
     * when compose screen is destroy view model is also destroy
     */
    val viewModel = viewModel<GreetViewModel>()

    /**
     * shareModel between two viewModel using this we can share data across
     * two viewModel
     * e.g - GreetScreen: com.example.mycomposeapp.ui.greet_screen.ShareViewModel@2ac9c97
     */

    val shareModel = viewModel<ShareViewModel>(LocalContext.current as ComponentActivity)

    PostsScreenWithSearch(viewModel = viewModel) {
        navController.navigate(Route.NEW_GREET)
    }
}


@ExperimentalFoundationApi
@Composable
fun PostsScreenWithSearch(viewModel: GreetViewModel, onPostItemClick: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {

        TextField(value = viewModel.searchValue.observeAsState("").value, onValueChange = {
            viewModel.searchValue.postValue(it)
        }, trailingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "searchIcon")
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
            label = { Text(text = "Search") },
            placeholder = { Text(text = "Search for title") }, shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            )
        )

        val search = viewModel.searchValue.observeAsState().value ?: ""
        when (val state = viewModel.posts.value) {
            is ResponseState.Success -> PostsScreen(state.response.filter { it.title.contains(search) }) {
                onPostItemClick()
            }
            is ResponseState.Progress -> ProgressScreen()
            else -> {}
        }
    }
}


@ExperimentalFoundationApi
@Composable
fun PostsScreen(posts: List<PostsItem>, click: () -> Unit) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        state = listState, modifier = Modifier
            .fillMaxSize()
    ) {

        itemsIndexed(posts) { index, item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = click)
                    .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
                    .animateItemPlacement(),
                shape = CardDefaults.outlinedShape,
            ) {
                Column {
                    Text(
                        text = item.title,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement()
                            .padding(start = 16.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                        color = Color.Black
                    )

                    Text(
                        text = item.body,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement()
                            .padding(start = 16.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                        color = Color.Black
                    )
                }
            }
            if (index == posts.lastIndex) {
                Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
                    Button(onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    }) {
                        Text(text = "GoToTop")
                    }
                }
            }
        }
    }
}

