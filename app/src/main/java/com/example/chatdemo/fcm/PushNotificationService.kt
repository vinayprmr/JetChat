package com.example.chatdemo.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.chatdemo.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, "NOTIFICATION_CHANNEL_ID")
            .setAutoCancel(true)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
//            .setContentIntent(getMainActivityPendingIntent())
        NotificationManagerCompat.from(this).notify(1, notificationBuilder.build());
        Log.e("TagNotification", "onMessageReceived:${ message.notification?.title } ")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel =
            NotificationChannel("NOTIFICATION_CHANNEL_ID", "NOTIFICATION_CHANNEL_NAME",
                NotificationManager.IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
    }
}