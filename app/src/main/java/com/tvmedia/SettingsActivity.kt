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
    val input = android.widget.EditText(this)
    input.hint = "Введите URL JSON"

    AlertDialog.Builder(this)
        .setTitle("Введите источник")
        .setView(input)
        .setPositiveButton("Сохранить") { _, _ ->
            val url = input.text.toString().trim()

            if (url.startsWith("http")) {
                UrlStorage.save(this, url)
                openMain()
            }
        }
        .setNegativeButton("Отмена", null)
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



