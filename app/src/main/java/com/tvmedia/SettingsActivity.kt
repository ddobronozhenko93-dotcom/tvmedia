package com.tvmedia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val input = findViewById<EditText>(R.id.urlInput)
        val save = findViewById<Button>(R.id.saveBtn)

        save.setOnClickListener {
            val url = input.text.toString()
            if (url.startsWith("http")) {
                UrlStorage.save(this, url)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}
