package com.spundev.ringvolume.ui

import android.media.AudioManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.spundev.ringvolume.ui.theme.RingVolumeTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Change the volume control to offer a quick access to the ring volume when the app is open
        volumeControlStream = AudioManager.STREAM_RING

        setContent {
            RingVolumeTheme {
                Surface {
                    AppNavHost(
                        windowSizeClass = calculateWindowSizeClass(this)
                    )
                }
            }
        }
    }
}
