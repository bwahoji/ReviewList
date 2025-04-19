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
import androidx.compose.animation.core.spring
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
        installSplashScreen()
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
    var targetPage by remember { mutableStateOf(0) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val homeTabs = listOf(
        NavigationItem(stringResource(R.string.today), Icons.Filled.DateRange),
        NavigationItem(stringResource(R.string.list), Icons.Filled.Menu),
        NavigationItem(stringResource(R.string.statistics), Icons.Filled.Info)
    )
    val topAppBarScrollBehavior = MiuixScrollBehavior(
        state = rememberTopAppBarState()
    )

    MiuixTheme(colors = colorMode) {
        Scaffold(
            modifier = Modifier.imePadding().fillMaxSize(),
            containerColor = colorMode.background,
            topBar = {
                TopAppBar(
                    title = homeTabs[targetPage].label,
                    color = colorMode.background,
                    scrollBehavior = topAppBarScrollBehavior
                )
            },
            bottomBar = {
                NavigationBar(
                    color = colorMode.surfaceContainer,
                    items = homeTabs,
                    selected = targetPage,
                    showDivider = false,
                    onClick = { targetPage = it }
                )
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
                when (targetPage) {
                    0 -> TodayPage(
                        topAppBarScrollBehavior = topAppBarScrollBehavior,
                        padding = padding,
                        colors = colorMode
                    )
                    1 -> ListPage(
                        topAppBarScrollBehavior = topAppBarScrollBehavior,
                        padding = padding,
                        colors = colorMode
                    )
                    2 -> AnalysisPage(
                        topAppBarScrollBehavior = topAppBarScrollBehavior,
                        padding = padding,
                        colors = colorMode
                    )
                }
//            }
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