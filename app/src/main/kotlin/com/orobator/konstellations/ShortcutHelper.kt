package com.orobator.konstellations

import android.annotation.TargetApi
import android.content.pm.ShortcutManager
import android.os.Build
import android.os.Build.VERSION_CODES.N_MR1
import android.preference.PreferenceManager
import com.orobator.konstellations.KonstellationsApplication.Companion.APP_CONTEXT

/**
 * Helper method to pass in usages of the shortcut api without having to do an
 * explicit version check every time
 */
inline fun shortcutAction(action: ShortcutManager.() -> Unit): Unit {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
    val shortcutManager = APP_CONTEXT.getSystemService(ShortcutManager::class.java)
    shortcutManager.action()
  }
}

/**
 * Returns the number of times a particular constellation has been visited
 * */
private fun getConstellationVisitedCount(constellation: Constellation): Int {
  val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(APP_CONTEXT)
  return sharedPrefs.getInt(constellation.name, 0)
}

/**
 * Sets the new Shortcut List by providing the three most visited
 * constellations
 * */
@TargetApi(N_MR1)
fun updateShortcuts(shortcutManager: ShortcutManager) {
  shortcutManager.dynamicShortcuts =
      Constellation
          .values()
          .sortedWith(compareBy { -getConstellationVisitedCount(it) })
          .map { it.toShortcutInfo() }
          .subList(0, 3)
}

@TargetApi(N_MR1)
fun trackShortcutUsed(shortcutManager: ShortcutManager, constellation: Constellation) {
  shortcutManager.reportShortcutUsed(constellation.name)

  val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(APP_CONTEXT)
  val seenCount = sharedPrefs.getInt(constellation.name, 0)
  sharedPrefs
      .edit()
      .putInt(constellation.name, seenCount + 1)
      .apply()

  updateShortcuts(shortcutManager)
}