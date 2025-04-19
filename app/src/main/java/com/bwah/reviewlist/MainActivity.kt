package com.bwah.reviewlist

import top.yukonga.miuix.kmp.basic.TopAppBar
import androidx.compose.ui.unit.dp
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import com.bwah.reviewlist.ui.theme.ReviewListTheme
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.*
import top.yukonga.miuix.kmp.theme.*
import kotlin.math.abs
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false // Xiaomi moment, this code must be here
        }
        setContent {
            HomeActivity()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeActivity(
    isDarkMode: Boolean = isSystemInDarkTheme()
) {
    val colorMode = remember(isDarkMode) {
        if (isDarkMode) darkColorScheme() else lightColorScheme()
    }
    val showFAB by remember { mutableStateOf(true) }
    val pagerState = rememberPagerState(pageCount = { 3 })
    var targetPage by remember { mutableStateOf(pagerState.currentPage) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val homeTabs = listOf(
        NavigationItem(stringResource(R.string.today), Icons.Filled.DateRange),
        NavigationItem(stringResource(R.string.list), Icons.Filled.Menu),
        NavigationItem(stringResource(R.string.statistics), Icons.Filled.Info)
    )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .debounce(100) // 延长 debounce 时间减少触发频率
            .collectLatest { targetPage = it }
    }

    val topAppBarScrollBehavior = MiuixScrollBehavior()

    MiuixTheme(colors = colorMode) {
        Scaffold(
            modifier = Modifier.imePadding(),
            containerColor = colorMode.background,
            topBar = {
//                AnimatedVisibility(
//                    visible = showTopAppBar,
//                    enter = fadeIn() + expandVertically(),
//                    exit = fadeOut() + shrinkVertically()
//                ) {
                TopAppBar(
                    title = homeTabs[targetPage].label,
                    color = colorMode.background,
                    scrollBehavior = MiuixScrollBehavior()
                )
//                }
            },
            bottomBar = {
//                AnimatedVisibility(
//                    visible = showBottomBar,
//                    enter = fadeIn() + expandVertically(),
//                    exit = fadeOut() + shrinkVertically()
//                ) {
                NavigationBar(
//                        modifier = Modifier.navigationBarsPadding(),
                    color = colorMode.surfaceContainer,
                    items = homeTabs,
                    selected = targetPage,
                    onClick = { index ->
                        if (index in homeTabs.indices) {
                            coroutineScope.launch { pagerState.animateScrollToPage(index) }
                        }
                    }
                )
//                }
            },
            floatingActionButton = {
                if (showFAB) {
                    FloatingActionButton(
                        onClick = { showBottomSheet = true },
                        containerColor = Color(255, 175, 3),
                        shadowElevation = 0.dp
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add",
                            tint = Color.White
                        )
                    }
                }
            }
        ) { padding ->
            HorizontalHomePage(
                modifier = Modifier
                    .imePadding()
                ,
                pagerState = pagerState,
                padding = padding,
                colors = colorMode,
                enablePageUserScroll = true,
                topAppBarScrollBehavior = topAppBarScrollBehavior
            )
            if (showBottomSheet) {
                ModalBottomSheet(
                    modifier = Modifier
                        .fillMaxHeight()
                        .statusBarsPadding()
                        .imePadding(),
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = rememberModalBottomSheetState(),
                    containerColor = colorMode.surfaceContainer
                ) {
                    FabUI(
                        padding = padding,
                        colors = colorMode,
                        finish = {
                            showBottomSheet = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalHomePage(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    enablePageUserScroll: Boolean,
    colors: Colors,
    padding: PaddingValues,
    topAppBarScrollBehavior: ScrollBehavior
) {
    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        pageContent = { page ->
            when (page) {
                0 -> TodayPage(
                    topAppBarScrollBehavior = topAppBarScrollBehavior,
                    padding = padding,
                    colors = colors
                )
                1 -> ListPage(
                    topAppBarScrollBehavior = topAppBarScrollBehavior,
                    padding = padding,
                    colors = colors
                )
                2 -> AnalysisPage(
                    topAppBarScrollBehavior = topAppBarScrollBehavior,
                    padding = padding,
                    colors = colors
                )
            }
        }
    )
}