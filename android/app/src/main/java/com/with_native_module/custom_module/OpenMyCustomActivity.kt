package com.with_native_module.custom_module

import android.content.Intent
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.with_native_module.custom_ui.MyCustomActivity

class OpenMyCustomActivity(private val reactContext: ReactContext) : ReactContextBaseJavaModule() {
    override fun getName() = "OpenMyCustomActivity"

    @ReactMethod
    fun open() {
        val intent = Intent(reactContext, MyCustomActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        reactContext.startActivity(intent)
    }
}