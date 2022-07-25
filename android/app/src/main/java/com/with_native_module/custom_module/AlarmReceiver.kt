package com.with_native_module.custom_module

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.with_native_module.R


class AlarmReceiver : BroadcastReceiver() {
    private val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("SSS", "1")
        context?.let {
            val title = intent?.getStringExtra("title")
            val description = intent?.getStringExtra("description")
            val actionByClick = intent?.getBooleanExtra("actionByClick", false)

            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager
            createNotificationChannel(notificationManager)

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(description)
//                .setStyle(
//                    NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line")
//                )
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setVibrate(longArrayOf(0, 500, 100))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .run {
                    if (actionByClick == true) {
                        val actionIntent = Intent(Intent.ACTION_VIEW)
                        actionIntent.data = Uri.parse("https://www.google.com.ua/")
                        actionIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                        val notifyPendingIntent = PendingIntent.getActivity(
                            context, 0, actionIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        )
                        setContentIntent(notifyPendingIntent)

                    }
                    build()
                }
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }



    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChanel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            notificationChanel.setSound(soundUri,audioAttributes)
            notificationManager.createNotificationChannel(notificationChanel)
        }
    }

    companion object {
        const val CHANNEL_ID = "test_channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 100


        fun newIntent(context: Context): Intent {
            return Intent(context, AlarmReceiver::class.java)
        }
    }
}