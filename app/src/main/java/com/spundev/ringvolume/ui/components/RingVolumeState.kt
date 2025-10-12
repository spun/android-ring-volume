package com.spundev.ringvolume.ui.components

import android.content.Context
import android.database.ContentObserver
import android.media.AudioManager
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.spundev.ringvolume.ext.maxRingVolume
import com.spundev.ringvolume.ext.ringVolume
import com.spundev.ringvolume.ext.setRingVolumeCompat

@Composable
fun rememberRingVolumeState(): RingVolumeState {
    val context = LocalContext.current
    val audioManager = remember(context) {
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    val ringVolumeState = remember(audioManager) {
        RingVolumeState(
            audioManager = audioManager,
            initialVolume = audioManager.ringVolume,
            maxVolume = audioManager.maxRingVolume
        )
    }

    // Listen to ring volume changes
    DisposableEffect(context) {
        val settingsContentObserver = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean, uri: Uri?, flags: Int) {
                if (uri.toString() == "content://settings/system/volume_ring_speaker") {
                    ringVolumeState.currentRingVolume = audioManager.ringVolume
                }
            }
        }

        context.contentResolver.registerContentObserver(
            Settings.System.CONTENT_URI,
            true,
            settingsContentObserver
        )
        onDispose {
            context.contentResolver.unregisterContentObserver(settingsContentObserver)
        }
    }

    return ringVolumeState
}

@Stable
class RingVolumeState internal constructor(
    private val audioManager: AudioManager,
    initialVolume: Int,
    val maxVolume: Int
) {
    /**
     * Indicates if the last call to [setVolume] was successful.
     * A volume change can fail if the app doesn't have permissions to modify the current DnD/Modes
     * state. The UI should use this value and the permission status to display a message explaining
     * why we couldn't change the volume.
     */
    var wasLastChangeSuccessful by mutableStateOf(true)
        private set

    private var _currentRingVolume = mutableIntStateOf(initialVolume)

    /**
     * Current volume for the ring speaker.
     * NOTE: Do not use this value setter to request a ring volume change. To do that, use
     *   [setVolume] instead.
     */
    var currentRingVolume: Int
        get() = _currentRingVolume.intValue
        // This should only be called from the DisposableEffect in charge of listening to the
        // volume_ring_speaker values using a content observer.
        internal set(value) {
            _currentRingVolume.intValue = value
            wasLastChangeSuccessful = true
        }

    /**
     * Request a ring value change
     */
    fun setVolume(volume: Int) {
        wasLastChangeSuccessful = audioManager.setRingVolumeCompat(volume)
    }
}