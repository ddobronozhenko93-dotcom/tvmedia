package com.tvmedia

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object JsonLoader {

    /**
     * ⚡ Быстрая загрузка:
     * 1. Кеш
     * 2. Assets (fallback)
     * ❗ НИКАКОЙ сети здесь
     */
    fun load(context: Context, url: String?): List<Category> {
        // кеш
        JsonCache.read(context)?.let {
            return parse(it)
        }

        // fallback
        return loadFromAssets(context)
    }

    /**
     * 🔄 Фоновое обновление контента
     */
    fun refreshIfNeeded(
        context: Context,
        url: String?,
        onUpdated: (List<Category>) -> Unit
    ) {
        if (url.isNullOrBlank()) return

        Thread {
            try {
                val text = download(url)
                JsonCache.save(context, text)
                val parsed = parse(text)

                // ⬅ callback ВСЕГДА в UI-потоке
                Handler(Looper.getMainLooper()).post {
                    onUpdated(parsed)
                }
            } catch (e: Exception) {
                Log.e("JsonLoader", "Refresh error", e)
            }
        }.start()
    }

    /**
     * 📦 Fallback из assets
     */
    private fun loadFromAssets(context: Context): List<Category> {
        return try {
            val text = context.assets
                .open("fallback.json")
                .bufferedReader()
                .use { it.readText() }
            parse(text)
        } catch (e: Exception) {
            Log.e("JsonLoader", "Assets load error", e)
            emptyList()
        }
    }

    /**
     * 🌐 Скачивание JSON с таймаутами
     */
    private fun download(url: String): String {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.requestMethod = "GET"

        return connection.inputStream
            .bufferedReader()
            .use { it.readText() }
    }

    /**
     * 🧠 Парсинг JSON
     */
    private fun parse(text: String): List<Category> {
        val root = JSONObject(text)
        val cats = root.getJSONArray("categories")
        val result = mutableListOf<Category>()

        for (i in 0 until cats.length()) {
            val c = cats.getJSONObject(i)
            val items = c.getJSONArray("items")
            val movies = mutableListOf<Movie>()

            for (j in 0 until items.length()) {
                val m = items.getJSONObject(j)
                movies.add(
                    Movie(
                        title = m.getString("title"),
                        poster = m.optString("poster"),
                        url = m.getString("url")
                    )
                )
            }

            result.add(
                Category(
                    name = c.getString("name"),
                    items = movies
                )
            )
        }
        return result
    }
}

