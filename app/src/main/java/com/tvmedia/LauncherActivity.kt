package com.tvmedia

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class LauncherActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = UrlStorage.load(this)

        if (url.isNullOrBlank()) {
            // Первый запуск — отправляем в настройки
            startActivity(Intent(this, SettingsActivity::class.java))
        } else {
            // URL есть — запускаем основное приложение
            startActivity(Intent(this, MainActivity::class.java))
        }

        finish()
    }
}

