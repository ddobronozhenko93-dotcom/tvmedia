package com.tvmedia

import android.content.Context
import java.io.File

object JsonCacheManager {

    private const val CACHE_FILE = "content_cache.json"
    private const val CACHE_TTL = 6 * 60 * 60 * 1000 // 6 часов

    private fun cacheFile(context: Context): File =
        File(context.filesDir, CACHE_FILE)

    fun isCacheValid(context: Context): Boolean {
        val file = cacheFile(context)
        if (!file.exists()) return false
        return System.currentTimeMillis() - file.lastModified() < CACHE_TTL
    }

    fun read(context: Context): String? {
        val file = cacheFile(context)
        return if (file.exists()) file.readText() else null
    }

    fun write(context: Context, text: String) {
        cacheFile(context).writeText(text)
    }
}

