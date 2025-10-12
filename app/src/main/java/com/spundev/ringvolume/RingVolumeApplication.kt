package com.spundev.ringvolume

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RingVolumeApplication : Application() {

//    override fun onCreate() {
//        super.onCreate()
//
//        val ringVolume = PeriodicWorkRequestBuilder<RingVolumeWorker>(
//            repeatInterval = 1,
//            repeatIntervalTimeUnit = TimeUnit.DAYS
//        ).build()
//
//        WorkManager
//            .getInstance(this)
//            .enqueueUniquePeriodicWork(
//                "RING_VOLUME_WORK",
//                ExistingPeriodicWorkPolicy.UPDATE,
//                ringVolume
//            )
//    }
}