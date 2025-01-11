package com.launcherkit

import com.launcherkit.managers.AppEventManager
import com.launcherkit.utils.AppLauncher
import com.launcherkit.utils.LauncherHelper
import com.launcherkit.providers.AppInfoProvider
import com.launcherkit.utils.SystemUtility
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.module.annotations.ReactModule

@ReactModule(name = LauncherKitModule.NAME)
class LauncherKitModuleLegacy(
  reactContext: ReactApplicationContext
) : LauncherKitModule(reactContext) {

  private val appEventManager = AppEventManager(reactContext)
  private val appLauncher = AppLauncher(reactContext)
  private val appInfoProvider = AppInfoProvider(reactContext)
  private val systemUtility = SystemUtility(reactContext)
  private val launcherHelper = LauncherHelper(reactContext)

  override fun getName(): String = NAME

  @ReactMethod
  override fun getApps(includeVersion: Boolean, includeAccentColor: Boolean, promise: Promise) {
    appInfoProvider.getApps(includeVersion, includeAccentColor, promise)
  }

  @ReactMethod
  override fun launchApplication(packageName: String, params: ReadableMap?) {
    appLauncher.launchApplication(packageName, params)
  }

  @ReactMethod
  override fun isPackageInstalled(packageName: String, promise: Promise) {
    appInfoProvider.isPackageInstalled(packageName) { isInstalled ->
      promise.resolve(isInstalled)
    }
  }

  @ReactMethod
  override fun getDefaultLauncherPackageName(promise: Promise) {
    appLauncher.getDefaultLauncherPackageName(promise)
  }

  @ReactMethod
  override fun setAsDefaultLauncher() {
    launcherHelper.setAsDefaultLauncher()
  }

  @ReactMethod
  override fun getBatteryStatus(promise: Promise) {
    systemUtility.getBatteryStatus(promise)
  }

  @ReactMethod
  override fun goToSettings() {
    systemUtility.goToSettings()
  }

  @ReactMethod
  override fun openAlarmApp() {
    systemUtility.openAlarmApp()
  }

  @ReactMethod
  override fun openSetDefaultLauncher(promise: Promise) {
    launcherHelper.openSetDefaultLauncher(promise)
  }

  @ReactMethod
  override fun startListeningForAppInstallations() {
    appEventManager.startListeningForAppInstallations()
  }

  @ReactMethod
  override fun stopListeningForAppInstallations() {
    appEventManager.stopListeningForAppInstallations()
  }

  @ReactMethod
  override fun startListeningForAppRemovals() {
    appEventManager.startListeningForAppRemovals()
  }

  @ReactMethod
  override fun stopListeningForAppRemovals() {
    appEventManager.stopListeningForAppRemovals()
  }
}
