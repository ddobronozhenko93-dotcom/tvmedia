package com.tvmedia

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class LauncherActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = UrlStorage.load(this)

        if (url.isNullOrBlank()) {
            startActivity(
                Intent(this, SettingsActivity::class.java)
            )
        } else {
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }

        finish()
    }
}

