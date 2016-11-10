package com.orobator.konstellations

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
      val currentDate = Date()

      val shortDateFormat = SimpleDateFormat("MMM d", Locale.US)
      val shortDate = shortDateFormat.format(currentDate)

      val longDateFormat = SimpleDateFormat("MMMM d, YYYY", Locale.US)
      val longDate = longDateFormat.format(currentDate)

      val shortcutInfo = ShortcutInfo.Builder(this, "dynamic")
          .setShortLabel(shortDate)
          .setLongLabel(longDate)
          .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher))
          .setIntent(Intent(ACTION_VIEW, null, this, MainActivity::class.java))
          .build()

      val shortcutManager = getSystemService(ShortcutManager::class.java)
      shortcutManager.dynamicShortcuts = listOf(shortcutInfo)
    }
  }
}
