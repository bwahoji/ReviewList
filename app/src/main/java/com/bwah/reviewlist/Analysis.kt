package com.bwah.reviewlist

import androidx.compose.runtime.Composable
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.BasicComponentDefaults
import top.yukonga.miuix.kmp.basic.InputField
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.basic.*
import top.yukonga.miuix.kmp.theme.Colors

@Composable
fun AnalysisPage(
    topAppBarScrollBehavior: ScrollBehavior,
    padding: PaddingValues,
    colors: Colors
) {
    val paddingBottom = padding.calculateBottomPadding()
    LazyColumn (
        modifier = Modifier.fillMaxSize().nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(
            top = padding.calculateTopPadding(),
            bottom = paddingBottom
        ),
        state = rememberLazyListState(),
    ){
        item {
            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 12.dp)
                    .fillMaxWidth()
            ) {
                top.yukonga.miuix.kmp.basic.Text(
                    text = stringResource(R.string.this_page_is_not_available) + '\n' + stringResource(R.string.we_are_trying_to_finish_it_soon),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
                )
            }
        }
    }
}