package com.spundev.ringvolume.works

import android.content.Context
import android.media.AudioManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class RingVolumeWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        try {
            // TODO: If volume is at 0, setStreamVolume will throw a SecurityException
            //  To increase the volume when the device is in "Do not disturb" we need to ask the user
            //  to give the permission. We should show a notification with a description.

            val audioManager =
                context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

            val maxRingVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)
            val targetRingVolume = (maxRingVolume * 0.9).toInt()
            audioManager.setStreamVolume(AudioManager.STREAM_RING, targetRingVolume, 0)
        } catch (e: SecurityException) {
            // Not allowed to change Do Not Disturb state
            // We cannot do anything until the user gives the app permission
        }

        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }
}
