package com.spundev.ringvolume.ui.screens.auto

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun AutoScreen(
    modifier: Modifier = Modifier,
    viewModel: AutoViewModel = hiltViewModel()
) {
    Column {
        Text("Auto: ${viewModel.fromViewModel}", modifier = modifier)
        AutoSettings()
    }
}

@Composable
fun AutoSettings() {
    Column {
        ListItem(
            headlineContent = {
                Text(text = "Notify if ")
            },
            trailingContent = {
                Switch(checked = true, onCheckedChange = {})
            }
        )
        ListItem(
            headlineContent = {
                Text(text = "Auto set")
            }, trailingContent = {
                Switch(checked = true, onCheckedChange = {})
            }
        )
    }
}