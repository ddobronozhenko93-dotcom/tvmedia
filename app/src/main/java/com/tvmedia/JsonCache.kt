package com.tvmedia

import android.content.Context

object JsonCache {

    private const val PREFS = "json_cache"
    private const val KEY_DATA = "data"

    fun save(context: Context, json: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_DATA, json)
            .apply()
    }

    fun load(context: Context): String? {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_DATA, null)
    }

    fun clear(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_DATA)
            .apply()
    }
}

