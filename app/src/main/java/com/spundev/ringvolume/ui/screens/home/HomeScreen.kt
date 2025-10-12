package com.spundev.ringvolume.ui.screens.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.spundev.ringvolume.R
import com.spundev.ringvolume.ui.components.RingVolumeState
import com.spundev.ringvolume.ui.components.StepVolumeSlider
import com.spundev.ringvolume.ui.components.rememberDnDPermission
import com.spundev.ringvolume.ui.components.rememberRingVolumeState
import com.spundev.ringvolume.ui.theme.OnSliderColor
import com.spundev.ringvolume.ui.theme.OnWarningSliderColor
import com.spundev.ringvolume.ui.theme.SliderColor
import com.spundev.ringvolume.ui.theme.WarningSliderColor

@Composable
fun HomeRoute(
    onNavigateToAuto: () -> Unit,
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass
) {
    HomeScreen(
        onNavigateToAuto = onNavigateToAuto,
        windowSizeClass = windowSizeClass,
        modifier = modifier.safeDrawingPadding()
    )
}

@Composable
private fun HomeScreen(
    onNavigateToAuto: () -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        HomeTopAppBar(navigateToAuto = onNavigateToAuto)
        HomeContent(windowSizeClass = windowSizeClass, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun HomeContent(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val isNotificationPolicyGranted = rememberDnDPermission()
    val ringVolumeState = rememberRingVolumeState()

    val showPermissionBanner =
        !isNotificationPolicyGranted && !ringVolumeState.wasLastChangeSuccessful

    // Checks width size class to decide the layout
    HomeLayout(
        showPermissionBanner = showPermissionBanner,
        permissionBanner = {
            DndPermissionBanner(
                onOpenSettingsRequest = {
                    val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                    context.startActivity(intent, Bundle.EMPTY)
                }
            )
        },
        volumeControl = {
            VolumeControl(state = ringVolumeState, modifier = Modifier.heightIn(max = 700.dp))
        },
        windowSizeClass = windowSizeClass,
        modifier = modifier
    )
}

@Composable
private fun HomeLayout(
    permissionBanner: @Composable () -> Unit,
    volumeControl: @Composable () -> Unit,
    showPermissionBanner: Boolean,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Medium,
        WindowWidthSizeClass.Expanded -> {
            // For Medium and Expanded screens, we use a Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                modifier = modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                volumeControl()
                AnimatedVisibility(
                    visible = showPermissionBanner,
                    enter = fadeIn() + expandHorizontally(expandFrom = Alignment.CenterHorizontally),
                    exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.CenterHorizontally),
                    modifier = Modifier.widthIn(max = 640.dp)
                ) {
                    permissionBanner()
                }
            }
        }

        else -> {
            // For compact screens, we use a Column
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                AnimatedVisibility(
                    visible = showPermissionBanner,
                    modifier = Modifier.widthIn(max = 640.dp)
                ) {
                    permissionBanner()
                }
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier.fillMaxHeight(),
                ) {
                    volumeControl()
                }
            }
        }
    }
}

@Composable
private fun DndPermissionBanner(
    onOpenSettingsRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Card(modifier = Modifier.align(Alignment.Center)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, top = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.home_permission_title),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = stringResource(R.string.home_permission_text),
                    style = MaterialTheme.typography.bodySmall,
                )
                TextButton(
                    onClick = onOpenSettingsRequest,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(stringResource(R.string.home_permission_button))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun VolumeControl(
    state: RingVolumeState,
    modifier: Modifier = Modifier
) {
    val sliderFillColor = if (state.wasLastChangeSuccessful) SliderColor else WarningSliderColor
    val sliderIconTintColor =
        if (state.wasLastChangeSuccessful) OnSliderColor else OnWarningSliderColor

    StepVolumeSlider(
        value = state.currentRingVolume,
        maxValue = state.maxVolume,
        onVolumeChange = state::setVolume,
        fillColor = sliderFillColor,
        iconTintColor = sliderIconTintColor,
        modifier = modifier
            .fillMaxHeight()
            .aspectRatio(0.3f)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppBar(
    navigateToAuto: () -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(R.string.home_title)) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        /* TODO
        actions = { TextButton(onClick = navigateToAuto) { Text("Auto") } }
        */
    )
}
