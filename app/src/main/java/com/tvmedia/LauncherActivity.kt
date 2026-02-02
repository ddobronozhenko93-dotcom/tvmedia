package com.tvmedia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class LauncherActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Пустой layout (обязательно для Android TV)
           setContentView(R.layout.activity_launcher)
    }

    override fun onResume() {
        super.onResume()

        Handler(Looper.getMainLooper()).postDelayed({
            val url = UrlStorage.load(this)

            val intent = if (url.isNullOrBlank()) {
                Intent(this, SettingsActivity::class.java)
            } else {
                Intent(this, MainActivity::class.java)
            }

            startActivity(intent)
            finish()
        }, 300) // небольшая пауза для ТВ
    }
}

