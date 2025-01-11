package com.launcherkit.utils

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.provider.AlarmClock
import android.provider.Settings
import android.util.Log
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.Arguments

class SystemUtility(private val reactContext: ReactContext) {

  fun getBatteryStatus(promise: Promise) {
    reactContext.currentActivity?.let { activity ->
      activity.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))?.let { batteryIntent ->
        val level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)

        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
          status == BatteryManager.BATTERY_STATUS_FULL

        val batteryLevel = when {
          level == -1 || scale == -1 -> 0f
          else -> (level.toFloat() / scale.toFloat()) * 100f
        }

        // Create a WritableMap to send back to JS
        val batteryData: WritableMap = Arguments.createMap().apply {
          putInt("level", batteryLevel.toInt())  // Changed to putInt since we're dealing with percentage
          putBoolean("isCharging", isCharging)
        }

        promise.resolve(batteryData)
      } ?: run {
        Log.e(TAG, "Failed to get battery intent")
        promise.reject("ERROR", "Failed to get battery intent")
      }
    } ?: run {
      Log.e(TAG, "Current activity is null")
      promise.reject("ERROR", "Current activity is null")
    }
  }

  fun goToSettings() {
    Intent(Settings.ACTION_SETTINGS).apply {
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      reactContext.startActivity(this)
    }
  }

  fun openAlarmApp() {
    try {
      Intent(AlarmClock.ACTION_SHOW_ALARMS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        reactContext.packageManager.resolveActivity(this, 0)?.let {
          reactContext.startActivity(this)
        } ?: run {
          Log.e(TAG, "No activity found to handle SHOW_ALARMS intent")
        }
      }
    } catch (e: ActivityNotFoundException) {
      Log.e(TAG, "Alarm app not found", e)
    } catch (e: Exception) {
      Log.e(TAG, "Failed to open alarm app", e)
    }
  }

  companion object {
    private const val TAG = "SystemUtility"
  }
}
