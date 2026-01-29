package com.tvmedia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog


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
    .setTitle("Выбор источника")
    .setItems(arrayOf("Источник 1", "Источник 2")) { _, which: Int ->
        when (which) {
            0 -> UrlStorage.save(this, "https://example.com/1.json")
            1 -> UrlStorage.save(this, "https://example.com/2.json")
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    .show()

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


