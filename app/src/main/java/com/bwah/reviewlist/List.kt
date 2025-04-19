package com.bwah.reviewlist

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.BasicComponentDefaults
import top.yukonga.miuix.kmp.basic.InputField
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.basic.*
import top.yukonga.miuix.kmp.icon.icons.useful.Search
import top.yukonga.miuix.kmp.theme.Colors
import top.yukonga.miuix.kmp.utils.overScrollHorizontal

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListPage(
    topAppBarScrollBehavior: ScrollBehavior,
    padding: PaddingValues,
    colors: Colors
) {
    val listState = rememberLazyListState()
    var searchValue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val paddingBottom = padding.calculateBottomPadding()
    val checkboxStates = remember { List(15) { mutableStateOf(false) } }
    val checkboxStatesAll = remember { List(100) { mutableStateOf(false) } }

    LaunchedEffect(Unit) {
        checkboxStates.forEach { it.value = false } // Simulate preload
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()/*.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)*/.overScrollHorizontal(),
        contentPadding = PaddingValues(
            top = padding.calculateTopPadding(),
            bottom = paddingBottom
        ),
        state = listState,
    ) {
        item {
            ListSearchBar(
                searchValue = searchValue,
                onSearchValueChange = { searchValue = it },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                colors = colors
            )
        }

        if (!expanded) {
            item {
                SmallTitle(stringResource(R.string.added_contents))
            }
            items(checkboxStates.size, key = { it }) { idx ->
                TodoItem(
                    checked = checkboxStates[idx].value,
                    text = "Item $idx",
                    colors = colors,
                    onCheckedChange = { checkboxStates[idx].value = it },
                    onClick = { checkboxStates[idx].value = !checkboxStates[idx].value }
                )
            }
            item {
                SmallTitle(stringResource(R.string.all_added_contents))
            }
            items(checkboxStatesAll.size, key = { it }) { idx ->
                TodoItem(
                    checked = checkboxStatesAll[idx].value,
                    text = "Item $idx",
                    colors = colors,
                    onCheckedChange = { checkboxStatesAll[idx].value = it },
                    onClick = { checkboxStatesAll[idx].value = !checkboxStatesAll[idx].value }
                )
            }
        }
    }
}

@Composable
fun ListSearchBar(
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    colors: Colors
) {
    SearchBar(
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