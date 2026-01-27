package com.tvmedia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentActivity

class UrlInputActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_url_input)

        val editText = findViewById<EditText>(R.id.urlInput)
        val button = findViewById<Button>(R.id.saveButton)

        button.setOnClickListener {
            val url = editText.text.toString().trim()

            if (url.isNotEmpty()) {
                UrlStorage.save(this, url)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}

