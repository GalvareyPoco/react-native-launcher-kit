package com.launcherkit.providers

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactContext
import com.launcherkit.models.AppDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppInfoProvider(private val reactContext: ReactContext) {
  private val scope = CoroutineScope(Dispatchers.IO)
  private val packageManager: PackageManager
    get() = reactContext.packageManager

  fun getApps(includeVersion: Boolean, includeAccentColor: Boolean, promise: Promise) {
    scope.launch {
      try {
        val apps = getAppsList(includeVersion, includeAccentColor)
        promise.resolve(apps.toString())
      } catch (e: Exception) {
        promise.reject("ERROR", "Failed to get apps list", e)
      }
    }
  }

  private fun getAppsList(includeVersion: Boolean, includeAccentColor: Boolean): List<AppDetail> {
    val addedPackages = mutableSetOf<String>()
    val pManager = reactContext.currentActivity?.packageManager ?: return emptyList()

    return Intent(Intent.ACTION_MAIN).apply {
      addCategory(Intent.CATEGORY_LAUNCHER)
    }.let { intent ->
      pManager.queryIntentActivities(intent, 0)
    }.mapNotNull { resolveInfo ->
      resolveInfo.activityInfo.packageName.takeUnless {
        addedPackages.contains(it)
      }?.let { packageName ->
        addedPackages.add(packageName)
        AppDetail(
          ri = resolveInfo,
          pManager = pManager,
          context = reactContext,
          includeVersion = includeVersion,
          includeAccentColor = includeAccentColor
        )
      }
    }
  }

  fun getAllApps(): List<String> =
    packageManager.getInstalledPackages(0).map { it.packageName }

  fun getNonSystemApps(): List<String> =
    packageManager.getInstalledPackages(0)
      .filter { (it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }
      .map { it.packageName }

  fun isPackageInstalled(packageName: String, callback: Callback) {
    try {
      packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
      callback.invoke(true)
    } catch (e: Exception) {
      callback.invoke(false)
    }
  }

  companion object {
    private const val TAG = "AppInfoProvider"
  }
}
