package com.example.myapplication.myapplication.exoplayer.callBcks

import android.app.Notification
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.myapplication.myapplication.data.Constants.NOTIFICATION_ID
import com.example.myapplication.myapplication.exoplayer.MusicService
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class MusicPlayerNotificationLIstner(
    private val Musicservice:MusicService
):PlayerNotificationManager.NotificationListener{
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)
        Musicservice.apply{
            stopForeground(true)
            isForgroundService=false
            stopSelf()

        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        super.onNotificationPosted(notificationId, notification, ongoing)
        Musicservice.apply {
            if(ongoing && !isForgroundService){
                ContextCompat.startForegroundService(this, Intent(applicationContext, this::class.java))
                startForeground(NOTIFICATION_ID, notification)
                isForgroundService=true
            }
        }
    }
}