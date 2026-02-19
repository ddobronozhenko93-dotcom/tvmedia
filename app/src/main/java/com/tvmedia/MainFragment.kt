package com.tvmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*

private const val SETTINGS_URL = "__settings__"

class MainFragment : BrowseSupportFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        title = "TV Media"
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        val url = UrlStorage.load(requireContext())

        // ⚡ мгновенный показ кеша / fallback
        val cached = JsonLoader.load(requireContext(), url)
        if (cached.isNotEmpty()) {
            setupRows(cached)
        }

        // 🔄 фоновое обновление
JsonLoader.refreshIfNeeded(requireContext(), url) { fresh ->
    if (!isAdded) return@refreshIfNeeded

    if (fresh.isNotEmpty()) {
        setupRows(fresh)
    } else {
        showErrorScreen("Контент недоступен")
    }
}

    }

    private fun setupRows(categories: List<Category>) {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        // ⚙️ НАСТРОЙКИ
        val settingsAdapter = ArrayObjectAdapter(CardPresenter())
        settingsAdapter.add(
            Movie(
                title = "Сменить источник",
                poster = "" , // ← обязательно
                url = SETTINGS_URL
            )
        )
        rowsAdapter.add(ListRow(HeaderItem("Настройки"), settingsAdapter))

        // 📺 КОНТЕНТ
        categories.forEach { category ->
            if (category.items.isEmpty()) return@forEach

            val adapter = ArrayObjectAdapter(CardPresenter())
            category.items.forEach { adapter.add(it) }

            rowsAdapter.add(ListRow(HeaderItem(category.name), adapter))
        }

        adapter = rowsAdapter

        onItemViewClickedListener =
            OnItemViewClickedListener { _, item, _, _ ->
                val movie = item as? Movie ?: return@OnItemViewClickedListener

                if (movie.url == SETTINGS_URL) {
                    startActivity(Intent(requireContext(), SettingsActivity::class.java))
                } else {
                    playVideo(movie.url)
                }
            }
    }

    private fun playVideo(url: String) {
        try {
            startActivity(
                Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(Uri.parse(url), "video/*")
                }
            )
        } catch (_: Exception) {
            showErrorScreen("Не найден видеоплеер")
        }
    }

    private fun showErrorScreen(message: String) {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val errorAdapter = ArrayObjectAdapter(CardPresenter())

        errorAdapter.add(
            Movie(
                title = message,
                poster = "",
                url = SETTINGS_URL
            )
        )

        rowsAdapter.add(ListRow(HeaderItem("Ошибка"), errorAdapter))
        adapter = rowsAdapter

        onItemViewClickedListener =
            OnItemViewClickedListener { _, _, _, _ ->
                startActivity(Intent(requireContext(), SettingsActivity::class.java))
            }
    }



