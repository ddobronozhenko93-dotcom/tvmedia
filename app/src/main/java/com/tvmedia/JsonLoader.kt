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
     * 1. Кеш (если валидный)
     * 2. Fallback из assets
     */
    fun load(context: Context, url: String?): List<Category> {
        return try {
            JsonCache.read(context)?.let { cached ->
                val parsed = parseSafely(cached)
                if (parsed.isNotEmpty()) return parsed
            }

            loadFromAssets(context)
        } catch (e: Exception) {
            Log.e("JsonLoader", "Load error", e)
            loadFromAssets(context)
        }
    }

    /**
     * 🔄 Фоновое обновление (никогда не ломает приложение)
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

                // Быстрая проверка — это вообще JSON?
                if (!text.trim().startsWith("{")) {
                    Log.e("JsonLoader", "Response is not JSON")
                    return@Thread
                }

                val parsed = parseSafely(text)

                // Если структура невалидная — не сохраняем
                if (parsed.isEmpty()) {
                    Log.e("JsonLoader", "Parsed empty or invalid structure")
                    return@Thread
                }

                // Сохраняем только валидный JSON
                JsonCache.save(context, text)

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

            parseSafely(text)
        } catch (e: Exception) {
            Log.e("JsonLoader", "Assets load error", e)
            emptyList()
        }
    }

    /**
     * 🌐 Скачивание JSON
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
     * 🛡 Безопасный парсинг
     */
    private fun parseSafely(text: String): List<Category> {
        return try {
            val root = JSONObject(text)

            if (!root.has("categories")) {
                Log.e("JsonLoader", "Missing 'categories'")
                return emptyList()
            }

            val cats = root.getJSONArray("categories")
            val result = mutableListOf<Category>()

            for (i in 0 until cats.length()) {
                val c = cats.getJSONObject(i)
                val items = c.optJSONArray("items") ?: continue
                val movies = mutableListOf<Movie>()

                for (j in 0 until items.length()) {
                    val m = items.getJSONObject(j)

                    val title = m.optString("title", "")
                    val url = m.optString("url", "")

                    if (title.isBlank() || url.isBlank()) continue

                    movies.add(
                        Movie(
                            title = title,
                            poster = m.optString("poster"),
                            url = url
                        )
                    )
                }

                if (movies.isNotEmpty()) {
                    result.add(
                        Category(
                            name = c.optString("name", "Category"),
                            items = movies
                        )
                    )
                }
            }

            result
        } catch (e: Exception) {
            Log.e("JsonLoader", "Parse error", e)
            emptyList()
        }
    }
}


