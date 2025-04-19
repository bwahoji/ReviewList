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
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.BasicComponentDefaults
import top.yukonga.miuix.kmp.basic.InputField
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.basic.*
import top.yukonga.miuix.kmp.theme.Colors
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@Composable
fun FabUI(
    padding: PaddingValues,
    colors: Colors,
    finish: (() -> Unit)
) {

    val checkboxStates = remember { List(15) { mutableStateOf(false) } }
    Scaffold (
        modifier = Modifier
            .fillMaxSize(),
        containerColor = colors.surfaceContainer,
        contentColor = colors.surfaceContainer,
        topBar = {
            SmallTopAppBar(
                title = stringResource(R.string.add_new_content),
                color = colors.surfaceContainer,
                actions = {
                    top.yukonga.miuix.kmp.basic.IconButton(
                        modifier = Modifier.padding(end = 21.dp).size(40.dp),
                        onClick = finish
                    ) {
                        top.yukonga.miuix.kmp.basic.Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = "OK",
                            tint = colors.onSurface
                        )
                    }
                }
            )
        }
    ){ padding ->
        LazyColumn (
            modifier = Modifier
                .overScrollVertical(),
            contentPadding = padding
        ){
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