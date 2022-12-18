@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class, ExperimentalAnimationApi::class)

package com.example.mycomposeapp.ui.genericUIComposables

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycomposeapp.R
import com.example.mycomposeapp.ui.theme.Purple40
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState

val list = listOf(R.drawable.ic_outline_movie_24, R.drawable.ic_baseline_tv_24)
val map = mapOf(0 to "Search for movies", 1 to "Search for Tv shows")

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TextFieldWithSwipeSuffixIcon(
    value: String,
    modifier: Modifier = Modifier,
    roundedCornerSize: Dp = 26.dp,
    iconState: IconState = IconState.LEADING,
    onValueChange: (String) -> Unit,
    maxLines: Int = 1,
    onIconChanged: ((Int) -> Unit)? = null
) {
    val pagerState = rememberPagerState()
    var oldValue by rememberSaveable { mutableStateOf(-1) }

    LaunchedEffect(key1 = pagerState.currentPage) {
        if (oldValue != pagerState.currentPage) {
            oldValue = pagerState.currentPage
            onIconChanged?.invoke(pagerState.currentPage)
        }
    }

    when (iconState) {
        IconState.LEADING -> {
            TextField(
                value = value,
                modifier = modifier,
                shape = RoundedCornerShape(roundedCornerSize),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                placeholder = { AnimatedPlaceHolder(pagerState = pagerState) },
                onValueChange = onValueChange,
                maxLines = maxLines,
                leadingIcon = { IconBox(pagerState = pagerState) },
            )
        }
        IconState.TRAILING -> {
            TextField(
                value = value,
                modifier = modifier,
                shape = RoundedCornerShape(roundedCornerSize),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                placeholder = { AnimatedPlaceHolder(pagerState = pagerState) },
                onValueChange = onValueChange,
                maxLines = maxLines,
                trailingIcon = { IconBox(pagerState = pagerState) }
            )
        }
    }
}

@Composable
fun IconBox(pagerState: PagerState) {
    var verticalDragState by remember { mutableStateOf(0f) }
    var index by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .size(50.dp)
            .padding(end = 8.dp)
    ) {
        VerticalPager(count = list.count(), state = pagerState) {
            VerticalTab(drawableId = list[currentPage], modifier = Modifier
                .pointerInput(Unit) {
                    detectVerticalDragGestures { change, dragAmount ->
                        change.consume()
                        verticalDragState = dragAmount
                    }
                }
                .draggable(reverseDirection = true,
                    startDragImmediately = true,
                    state = DraggableState { verticalDragState = it },
                    orientation = Orientation.Vertical,
                    onDragStopped = {
                        Log.d("scroll", "scroll $verticalDragState")
                        if (verticalDragState < 0) {
                            ++index
                        } else if (verticalDragState > 0) {
                            --index
                        }
                        if (index < 0) {
                            index = 1f
                        } else if (index > list.lastIndex) {
                            index = 0f
                        }
                        pagerState.animateScrollToPage(index.toInt())
                    })
                .size(40.dp)
                .background(Purple40, CircleShape)
            )
        }
    }
}


@Composable
fun AnimatedPlaceHolder(pagerState: PagerState) {
    AnimatedContent(targetState = map[pagerState.currentPage] ?: "", transitionSpec = { ScrollAnimation() }) { str ->
        Text(
            text = str,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            color = if (isSystemInDarkTheme()) Color.White else Color.Black
        )
    }
}

@Composable
fun VerticalTab(@DrawableRes drawableId: Int, modifier: Modifier) {
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = drawableId),
            contentDescription = "Movies",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

enum class IconState { LEADING, TRAILING }

object ScrollAnimation {
    operator fun invoke(): ContentTransform {
        return slideInVertically(
            initialOffsetY = { 60 }, animationSpec = tween()
        ) + fadeIn() with slideOutVertically(targetOffsetY = { -60 }, animationSpec = tween()) + fadeOut()
    }
}
