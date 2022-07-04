package com.giniapps.servicetest

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder

class MyApplication: Application() {
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {}
        override fun onServiceDisconnected(arg0: ComponentName) {}
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannelIfNeeded()
        bindToService()
    }

    private fun createNotificationChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                NotificationChannelId,
                NotificationChannelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun bindToService() {
        Intent(this, MyService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    companion object {
        const val NotificationChannelId = "channel_id"
        const val NotificationChannelName = "channel name"
    }
}