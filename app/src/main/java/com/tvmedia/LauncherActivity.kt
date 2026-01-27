package com.tvmedia

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

cclass LauncherActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = UrlStorage.load(this)

        if (url.isNullOrBlank()) {
            startActivity(Intent(this, UrlInputActivity::class.java))
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }

        finish()
    }
}
