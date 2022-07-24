package com.with_native_module

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.facebook.react.bridge.*
import com.google.gson.Gson
import java.util.*


data class DateProps(
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
)

data class ScheduleAlarmProps(
    val date: DateProps,
    val title: String,
    val description: String,
)

class AlarmModule(private val reactContext: ReactContext) : ReactContextBaseJavaModule() {
    override fun getName() = "AlarmModule"

    @ReactMethod
    fun makeScheduleAlarm(props: ReadableMap) {
        val gson = Gson()
        val hashMap = props.toHashMap().toString()
        val data = gson.fromJson(hashMap, ScheduleAlarmProps::class.java)

        val dueDate = Calendar.getInstance()
        dueDate.set(Calendar.HOUR_OF_DAY, data.date.hours)
        dueDate.set(Calendar.MINUTE, data.date.minutes)
        dueDate.set(Calendar.SECOND, data.date.seconds)
        dueDate.set(Calendar.MILLISECOND, 0)

        val alarmManager =
            reactContext.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = AlarmReceiver.newIntent(reactContext)
        intent.putExtra("title", data.title)
        intent.putExtra("description", data.description)

        val pendingIntent =
            PendingIntent.getBroadcast(reactContext, 101, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            dueDate.timeInMillis,
            pendingIntent
        )
    }

    @ReactMethod
    fun makeAlarm(delay: Int, title: String, description: String) {
        val alarmManager =
            reactContext.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = AlarmReceiver.newIntent(reactContext)
        intent.putExtra("title", title)
        intent.putExtra("description", description)
        intent.putExtra("actionByClick", true)

        val dueDate = Calendar.getInstance()
        dueDate.add(Calendar.MILLISECOND, 300)

        val pendingIntent =
            PendingIntent.getBroadcast(reactContext, 100, intent, PendingIntent.FLAG_IMMUTABLE)
        // use setAlarmClock because I want precise invoke time. If use setExact on small time we have additionally delay (around 5 sec)
        alarmManager.setAlarmClock(
            AlarmClockInfo(System.currentTimeMillis() + delay, pendingIntent),
            pendingIntent
        )
        // We of course could use Handler().postDelayed({...}, 1000) and invoke notification without receiver, but I don't want this :)
        // If real project setAlarmClock better not use
    }
}