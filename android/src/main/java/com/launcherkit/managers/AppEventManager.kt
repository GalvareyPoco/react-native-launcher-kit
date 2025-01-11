package com.launcherkit.managers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import com.facebook.react.bridge.ReactContext
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.launcherkit.models.AppDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppEventManager(private val reactContext: ReactContext) {
  private var appInstallReceiver: BroadcastReceiver? = null
  private var appRemovalReceiver: BroadcastReceiver? = null
  private val scope = CoroutineScope(Dispatchers.IO)

  init {
    initializeReceivers()
  }

  private fun initializeReceivers() {
    appInstallReceiver = createAppInstallReceiver()
    appRemovalReceiver = createAppRemovalReceiver()
  }

  private fun createAppInstallReceiver() = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      scope.launch {
        intent.data?.schemeSpecificPart?.let { packageName ->
          try {
            val pManager = context.packageManager
            pManager.getLaunchIntentForPackage(packageName)?.let { launchIntent ->
              pManager.resolveActivity(launchIntent, 0)?.let { resolveInfo ->
                val newApp = AppDetail(
                  ri = resolveInfo,
                  pManager = pManager,
                  context = reactContext,
                  includeVersion = true,
                  includeAccentColor = true
                )
                emitAppInstalled(newApp.toString())
              }
            }
          } catch (e: Exception) {
            e.printStackTrace()
          }
        }
      }
    }
  }

  private fun createAppRemovalReceiver() = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      scope.launch {
        intent.data?.schemeSpecificPart?.let { packageName ->
          emitAppRemoved(packageName)
        }
      }
    }
  }

  private fun emitAppInstalled(appDetails: String) {
    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit("onAppInstalled", appDetails)
  }

  private fun emitAppRemoved(packageName: String) {
    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit("onAppRemoved", packageName)
  }

  fun startListeningForAppInstallations() {
    IntentFilter(Intent.ACTION_PACKAGE_ADDED).apply {
      addDataScheme("package")
      appInstallReceiver?.let { receiver ->
        reactContext.registerReceiver(receiver, this)
      }
    }
  }

  fun stopListeningForAppInstallations() {
    try {
      appInstallReceiver?.let { receiver ->
        reactContext.unregisterReceiver(receiver)
      }
    } catch (e: IllegalArgumentException) {
      e.printStackTrace()
    }
  }

  fun startListeningForAppRemovals() {
    IntentFilter(Intent.ACTION_PACKAGE_REMOVED).apply {
      addDataScheme("package")
      appRemovalReceiver?.let { receiver ->
        reactContext.registerReceiver(receiver, this)
      }
    }
  }

  fun stopListeningForAppRemovals() {
    try {
      appRemovalReceiver?.let { receiver ->
        reactContext.unregisterReceiver(receiver)
      }
    } catch (e: IllegalArgumentException) {
      e.printStackTrace()
    }
  }

  companion object {
    private const val TAG = "AppEventManager"
  }
}
