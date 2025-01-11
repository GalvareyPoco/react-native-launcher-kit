package com.launcherkit

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.NativeModule

abstract class LauncherKitModule(
  reactContext: ReactApplicationContext
) : NativeModule {
  abstract fun getApps(includeVersion: Boolean, includeAccentColor: Boolean, promise: Promise)
  abstract fun launchApplication(packageName: String, params: ReadableMap?)
  abstract fun isPackageInstalled(packageName: String, promise: Promise)
  abstract fun getDefaultLauncherPackageName(promise: Promise)
  abstract fun setAsDefaultLauncher()
  abstract fun getBatteryStatus(promise: Promise)
  abstract fun goToSettings()
  abstract fun openAlarmApp()
  abstract fun openSetDefaultLauncher(promise: Promise)
  abstract fun startListeningForAppInstallations()
  abstract fun stopListeningForAppInstallations()
  abstract fun startListeningForAppRemovals()
  abstract fun stopListeningForAppRemovals()

  override fun initialize() {
    // Optional initialization
  }

  override fun invalidate() {
    // Cleanup if needed
  }

  companion object {
    const val NAME = "LauncherKit"
  }
}
