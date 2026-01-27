package com.tvmedia

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.loading)

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, MainFragment())
            .commit()
    }
}


