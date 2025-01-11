package com.launcherkit

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager

class LauncherKitPackage : ReactPackage {
  override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
    return if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
      listOf(LauncherKitModuleNew(reactContext))
    } else {
      listOf(LauncherKitModuleLegacy(reactContext))
    }
  }

  override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
    return emptyList()
  }
}
