package com.spundev.ringvolume.ui.components

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberDnDPermission(): Boolean {
    val context = LocalContext.current
    val notificationManager = remember(context) {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    var isNotificationPolicyGranted by remember(notificationManager) {
        mutableStateOf(notificationManager.isNotificationPolicyAccessGranted)
    }

    // Listen to permission changes
    DisposableEffect(context, notificationManager) {
        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                isNotificationPolicyGranted = notificationManager.isNotificationPolicyAccessGranted
            }
        }

        val intentFilter =
            IntentFilter(NotificationManager.ACTION_NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED)
        context.registerReceiver(broadcastReceiver, intentFilter)

        onDispose {
            context.unregisterReceiver(broadcastReceiver)
        }
    }

    return isNotificationPolicyGranted
}
