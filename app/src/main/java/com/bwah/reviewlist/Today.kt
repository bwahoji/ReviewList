package com.bwah.reviewlist

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChangeIgnoreConsumed
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.BasicComponentDefaults
import top.yukonga.miuix.kmp.basic.InputField
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.theme.Colors
import top.yukonga.miuix.kmp.basic.*
import top.yukonga.miuix.kmp.icon.icons.useful.Search
import top.yukonga.miuix.kmp.utils.overScrollVertical
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TodayPage(
    topAppBarScrollBehavior: ScrollBehavior,
    padding: PaddingValues,
    colors: Colors
) {
    var searchValue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // 使用 List 缓存状态，避免每次重建
    val checkboxStates = remember { List(15) { mutableStateOf(false) } }
    LazyColumn(
        modifier = Modifier.fillMaxSize().nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)/*.overScrollVertical()*/,
        contentPadding = padding/*PaddingValues(
            top = padding.calculateTopPadding(),
            bottom = padding.calculateBottomPadding()
        )*/,
//        state = listState,
    ) {
        // 搜索栏部分
        item {
            TodaySearchBar(
                searchValue = searchValue,
                onSearchValueChange = { searchValue = it },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                colors = colors
            )
        }

        // 列表部分
        if (!expanded) {
            items(checkboxStates.size, key = { it }) { idx ->
                TodoItem(
                    checked = checkboxStates[idx].value,
                    text = "Item $idx",
                    colors = colors,
                    onCheckedChange = { checkboxStates[idx].value = it },
                    onClick = { checkboxStates[idx].value = !checkboxStates[idx].value }
                )
            }
        }
    }
}

@Composable
fun TodaySearchBar(
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    colors: Colors
) {
    // 将搜索栏的组件逻辑简单化
    top.yukonga.miuix.kmp.basic.SearchBar(
        modifier = Modifier.padding(12.dp),
        inputField = {
            InputField(
                query = searchValue,
                onQueryChange = onSearchValueChange,
                onSearch = { onExpandedChange(false) },
                expanded = expanded,
                onExpandedChange = onExpandedChange,
                label = stringResource(R.string.searchBarKeyToday),
                leadingIcon = {
                    top.yukonga.miuix.kmp.basic.Icon(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        imageVector = top.yukonga.miuix.kmp.icon.MiuixIcons.Useful.Search,
                        tint = colors.onSurfaceContainer,
                        contentDescription = null
                    )
                }
            )
        },
        outsideRightAction = {
            top.yukonga.miuix.kmp.basic.Text(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .clickable {
                        onExpandedChange(false)
                        onSearchValueChange("")
                    },
                text = stringResource(R.string.cancel),
                color = colors.primary
            )
        },
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        // 空白占位符，防止未展开时的异常行为
        Spacer(modifier = Modifier.fillMaxSize())
    }
}

