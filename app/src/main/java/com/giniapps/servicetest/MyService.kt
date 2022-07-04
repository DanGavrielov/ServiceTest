package com.giniapps.servicetest

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.*
import android.widget.Toast
import androidx.core.app.NotificationCompat

class MyService : Service() {
    private lateinit var handler: Handler
    private val runnable: Runnable = object : Runnable {
        override fun run() {
            Toast.makeText(this@MyService, "Service is alive!", Toast.LENGTH_SHORT).show()
            handler.postDelayed(this, 5_000)
        }
    }

    override fun onBind(intent: Intent): IBinder {
        startForeground(100, getNotification())
        startSendingLifeSignals()
        return MyServiceBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun getNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )

        return NotificationCompat.Builder(this, MyApplication.NotificationChannelId)
            .setContentTitle("Service is running")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun startSendingLifeSignals() {
        handler = Handler(Looper.getMainLooper())
            .also {
                it.postDelayed(runnable, 5_000)
            }
    }

    inner class MyServiceBinder : Binder() {
        fun getService() = this@MyService
    }
}
