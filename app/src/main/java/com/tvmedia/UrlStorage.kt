package com.tvmedia

import android.content.Context

object UrlStorage {
    private const val PREF = "tv_prefs"
    private const val KEY_URL = "content_url"

    fun save(context: Context, url: String) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit().putString(KEY_URL, url).apply()
    }

    fun load(context: Context) =
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getString(KEY_URL, null)

    fun clear(context: Context) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit().remove(KEY_URL).apply()
    }
}
