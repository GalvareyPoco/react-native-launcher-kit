package com.launcherkit.utils

import android.content.Intent
import android.provider.Settings
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactContext

class LauncherHelper(private val reactContext: ReactContext) {

  fun setAsDefaultLauncher() {
    Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS).apply {
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      reactContext.startActivity(this)
    }
  }

  fun openSetDefaultLauncher(promise: Promise) {
    try {
      Intent(Settings.ACTION_HOME_SETTINGS).apply {
        addFlags(
          Intent.FLAG_ACTIVITY_NEW_TASK or
          Intent.FLAG_ACTIVITY_CLEAR_TASK or
          Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        )
        reactContext.startActivity(this)
      }
      promise.resolve(true)
    } catch (e: Exception) {
      promise.reject(e)
    }
  }
}
