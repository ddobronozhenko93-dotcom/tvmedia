package com.tvmedia

import org.json.JSONObject
import java.net.URL

object JsonLoader {
    fun load(url: String): List<Category> {
        val text = URL(url).readText()
        val root = JSONObject(text)
        val cats = root.getJSONArray("categories")
        val result = mutableListOf<Category>()

        for (i in 0 until cats.length()) {
            val c = cats.getJSONObject(i)
            val items = c.getJSONArray("items")
            val movies = mutableListOf<Movie>()
            for (j in 0 until items.length()) {
                val m = items.getJSONObject(j)
                movies.add(Movie(m.getString("title"), m.getString("poster"), m.getString("url")))
            }
            result.add(Category(c.getString("name"), movies))
        }
        return result
    }
}
