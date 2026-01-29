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

        val btn = findViewById<Button>(R.id.setUrlBtn)
        btn.requestFocus()

        btn.setOnClickListener {
            showUrlDialog()
        }
    }

    private fun showUrlDialog() {
        val input = EditText(this).apply {
            hint = "https://example.com"
            setSingleLine(true)
        }

        AlertDialog.Builder(this)
            .setTitle("Введіть URL")
            .setView(input)
            .setPositiveButton("Зберегти") { _, _ ->
                val url = input.text.toString().trim()
                if (url.startsWith("http")) {
                    UrlStorage.save(this, url)
                    openMain()
                }
            }
            .setNegativeButton("Скасувати", null)
            .show()
    }

    private fun openMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_NEW_TASK
            )
        }
        startActivity(intent)
        finish()
    }
}


