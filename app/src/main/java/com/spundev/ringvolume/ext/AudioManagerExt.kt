package com.spundev.ringvolume.ext

import android.media.AudioManager

/**
 * Modify ring volume value
 * This method tries different values to mute the device when DnD permission is not granted.
 */
fun AudioManager.setRingVolumeCompat(volume: Int): Boolean {
    return if (volume != 0) {
        setRingVolume(volume = volume)
    } else {
        // Some devices are able to mute the ring volume without the DnD permission if -1 is used.
        // This is not referenced in the AudioManager docs.
        setRingVolume(0) || setRingVolume(-1)
    }
}

/**
 * Alternative to [AudioManager.setStreamVolume] that changes the [AudioManager.STREAM_RING] volume
 * if possible.
 * @return A boolean indicating if the operation was successful.
 */
private fun AudioManager.setRingVolume(volume: Int): Boolean {
    return try {
        setStreamVolume(
            AudioManager.STREAM_RING,
            volume,
            AudioManager.FLAG_SHOW_UI
        )
        true
    } catch (_: SecurityException) {
        false
    }
}

/**
 * Current ring volume
 */
val AudioManager.ringVolume: Int
    get() = this.getStreamVolume(AudioManager.STREAM_RING)

/**
 * Max ring volume
 */
val AudioManager.maxRingVolume: Int
    get() = this.getStreamMaxVolume(AudioManager.STREAM_RING)