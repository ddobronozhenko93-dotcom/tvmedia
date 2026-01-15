package com.tvmedia

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = UrlStorage.load(this)
        if (url == null) {
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
            return
        }

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, MainFragment())
            .commit()
    }
}

