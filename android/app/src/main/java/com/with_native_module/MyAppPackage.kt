package com.with_native_module

import android.view.View
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager
import com.with_native_module.custom_module.FlashlightModule
import com.with_native_module.custom_module.OpenMyCustomActivity

class MyAppPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): MutableList<NativeModule> {
        return mutableListOf(
            AlarmModule(reactContext),
            FlashlightModule(reactContext),
            OpenMyCustomActivity(reactContext)
        )
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): MutableList<ViewManager<View, ReactShadowNode<*>>> {
        return mutableListOf()
    }
}