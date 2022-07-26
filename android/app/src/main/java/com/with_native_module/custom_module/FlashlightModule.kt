package com.with_native_module.custom_module

import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.with_native_module.custom_ui.MyCustomActivity


class FlashlightModule(private val reactContext: ReactContext) : ReactContextBaseJavaModule() {
    override fun getName() = "FlashlightModule"

    @RequiresApi(Build.VERSION_CODES.M)
    @ReactMethod
    fun toggleFlashlight(status: Boolean, callback: Callback) {
        val cameraManager = reactContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        try {
            cameraManager.setTorchMode(cameraId, status)
        } catch (e: CameraAccessException) {
            callback(e)
        }
    }

}