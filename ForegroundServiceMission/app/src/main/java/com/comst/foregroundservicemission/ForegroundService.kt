package com.comst.foregroundservicemission

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ForegroundService : Service(), CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext
        get() = Dispatchers.Main + job

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        job = Job()
        createNotification()
        launch {
            for (i in 0..1000) {
                if (!isActive) {
                    break
                }
                updateNotification(i)
                delay(1000)
            }
        }
        Log.d(TAG, "onCreate")
    }

    private fun createNotification(progress: Int = 0) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )


        val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle("Foreground Service")
            setContentText("포그라운드 서비스 진행 중...")
            setContentIntent(pendingIntent)
            setProgress(1000, progress, false)
            color = Color.RED
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "기본 채널",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }

        startForeground(NOTI_ID, builder.build())
    }

    private fun updateNotification(count: Int) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Foreground Service")
            .setContentText("Count: $count")
            .setColor(Color.RED)
            .setProgress(1000, count, false)
            .setContentIntent(pendingIntent)  // 추가한 줄
            .setOnlyAlertOnce(true)

        val notification = builder.build()
        notificationManager.notify(NOTI_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        private const val TAG = "MyServiceTag"
        private const val CHANNEL_ID = "default"
        private const val NOTI_ID = 1
    }
}