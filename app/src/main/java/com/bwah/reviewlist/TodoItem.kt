package com.bwah.reviewlist

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.BasicComponentDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.theme.Colors

@Composable
fun TodoItem(
    checked: Boolean,
    text: String,
    colors: Colors,
    onCheckedChange: (Boolean) -> Unit,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        BasicComponent(
            title = text,
            rightActions = {
                Checkbox(
                    checked = checked,
                    onCheckedChange = onCheckedChange
                )
            },
            titleColor = if (checked) {
                BasicComponentDefaults.titleColor(
                    color = colors.disabledOnSecondaryVariant
                )
            } else {
                BasicComponentDefaults.titleColor(
                    color = colors.onSurface
                )
            },
            onClick = onClick
        )
    }
}
