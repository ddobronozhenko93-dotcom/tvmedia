
package com.tvmedia

import android.content.Context
import org.json.JSONObject
import java.net.URL


object JsonLoader {

    fun load(context: Context): List<Category> {

        // 1️⃣ Кеш
        JsonCache.load(context)?.let {
            return parse(it)
        }

        // 2️⃣ fallback
        return loadFromAssets(context)
    }

    fun refreshIfNeeded(
        context: Context,
        url: String?,
        onUpdated: (List<Category>) -> Unit
    ) {
        if (url.isNullOrBlank()) return

        Thread {
            try {
                val text = URL(url).readText()
                JsonCache.save(context, text)
                onUpdated(parse(text))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
    private fun loadFromAssets(context: Context): List<Category> {
        val text = context.assets.open("fallback.json")
            .bufferedReader()
            .use { it.readText() }
        return parse(text)
    }

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
                        poster = m.optString("poster").takeIf { it.isNotBlank() },
                        url = m.getString("url")
                    )
                )
            }
            result.add(Category(c.getString("name"), movies))
        }
        return result
    }
}

