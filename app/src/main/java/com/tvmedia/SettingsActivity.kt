package com.tvmedia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
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
        AlertDialog.Builder(this)
            .setTitle("Выбор источника")
            .setItems(
                arrayOf(
                    "Источник 1",
                    "Источник 2"
                )
            ) { _, which ->
                when (which) {
                    0 -> UrlStorage.save(this, "https://example.com/1.json")
                    1 -> UrlStorage.save(this, "https://example.com/2.json")
                }

                openMain()
            }
            .setCancelable(true)
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



