
@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)

package com.example.mycomposeapp.ui.movies_screen.movies_screen_specific_ui

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycomposeapp.model.GenreType
import com.example.mycomposeapp.ui.theme.Purple40

@Composable
fun GenreChip(list: List<GenreType>, onSelectedChip: (GenreType) -> Unit) {
    val listState = rememberLazyListState()
    val previousSelectedIndex = remember { mutableStateOf(-1) }
    val currentSelectedIndex = remember { mutableStateOf(-1) }
    val selectedGenre = remember { mutableStateOf<GenreType?>(null) }

    LaunchedEffect(key1 = selectedGenre.value) {
        selectedGenre.value?.let {
            Log.d("GenreChipppppp", "genre chip selected ${selectedGenre.value.hashCode()}")
            onSelectedChip.invoke(it)
        }
    }

    LazyRow(
        state = listState, modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        itemsIndexed(list) { index, item ->
            SuggestionChip(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = 4.dp),
                label = {
                    CreateChipLabel(
                        item.name,
                        currentSelectedIndex.value == index || previousSelectedIndex.value == index
                    )
                },
                shape = RoundedCornerShape(20.dp),
                colors = SuggestionChipDefaults.suggestionChipColors(containerColor = animateColorAsState(targetValue = if (currentSelectedIndex.value == index || previousSelectedIndex.value == index) Purple40 else MaterialTheme.colorScheme.secondary).value),
                border = SuggestionChipDefaults.suggestionChipBorder(
                    borderWidth = 2.dp,
                    borderColor = animateColorAsState(targetValue = if (currentSelectedIndex.value == index || previousSelectedIndex.value == index) MaterialTheme.colorScheme.inverseSurface else Color.Transparent).value
                ),
                onClick = {
                    currentSelectedIndex.value = index
                    selectedGenre.value = list[index]
                    if (previousSelectedIndex.value != index) {
                        previousSelectedIndex.value = index
                    }
                })
        }
    }
}

@Composable
fun CreateChipLabel(value: String, isSelected: Boolean = false) {
    Text(
        text = value,
        color = animateColorAsState(targetValue = if (isSelected) Color.White else MaterialTheme.colorScheme.onSecondary).value,
        fontSize = 16.sp,
        textAlign = TextAlign.Center
    )
}

