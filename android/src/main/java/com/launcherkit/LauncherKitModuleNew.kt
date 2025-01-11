package com.launcherkit

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.turbomodule.core.interfaces.TurboModule
import com.launcherkit.managers.AppEventManager
import com.launcherkit.utils.AppLauncher
import com.launcherkit.utils.LauncherHelper
import com.launcherkit.providers.AppInfoProvider
import com.launcherkit.utils.SystemUtility

@ReactModule(name = LauncherKitModule.NAME)
class LauncherKitModuleNew(
  reactContext: ReactApplicationContext
) : LauncherKitModule(reactContext), TurboModule {

  private val appEventManager = AppEventManager(reactContext)
  private val appLauncher = AppLauncher(reactContext)
  private val appInfoProvider = AppInfoProvider(reactContext)
  private val systemUtility = SystemUtility(reactContext)
  private val launcherHelper = LauncherHelper(reactContext)

  override fun getName(): String = NAME

  override fun getApps(includeVersion: Boolean, includeAccentColor: Boolean, promise: Promise) {
    appInfoProvider.getApps(includeVersion, includeAccentColor, promise)
  }

  override fun launchApplication(packageName: String, params: ReadableMap?) {
    appLauncher.launchApplication(packageName, params)
  }

  override fun isPackageInstalled(packageName: String, promise: Promise) {
    appInfoProvider.isPackageInstalled(packageName) { isInstalled ->
      promise.resolve(isInstalled)
    }
  }

  override fun getDefaultLauncherPackageName(promise: Promise) {
    appLauncher.getDefaultLauncherPackageName(promise)
  }

  override fun setAsDefaultLauncher() {
    launcherHelper.setAsDefaultLauncher()
  }

  override fun getBatteryStatus(promise: Promise) {
    systemUtility.getBatteryStatus(promise)
  }

  override fun goToSettings() {
    systemUtility.goToSettings()
  }

  override fun openAlarmApp() {
    systemUtility.openAlarmApp()
  }

  override fun openSetDefaultLauncher(promise: Promise) {
    launcherHelper.openSetDefaultLauncher(promise)
  }

  override fun startListeningForAppInstallations() {
    appEventManager.startListeningForAppInstallations()
  }

  override fun stopListeningForAppInstallations() {
    appEventManager.stopListeningForAppInstallations()
  }

  override fun startListeningForAppRemovals() {
    appEventManager.startListeningForAppRemovals()
  }

  override fun stopListeningForAppRemovals() {
    appEventManager.stopListeningForAppRemovals()
  }
}
