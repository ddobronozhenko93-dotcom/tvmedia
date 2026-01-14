package com.tvmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*

class MainFragment : BrowseSupportFragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        title = "TV Media"
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        val url = UrlStorage.load(requireContext()) ?: return
        val categories = JsonLoader.load(url)

        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        categories.forEach { category ->
            val listAdapter = ArrayObjectAdapter(CardPresenter())
            category.items.forEach { listAdapter.add(it) }
            rowsAdapter.add(ListRow(HeaderItem(category.name), listAdapter))
        }

        adapter = rowsAdapter

        onItemViewClickedListener =
            OnItemViewClickedListener { _, item, _, _ ->
                val movie = item as Movie
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(Uri.parse(movie.url), "video/*")
                    setPackage("com.mxtech.videoplayer.ad")
                }
                startActivity(intent)
            }
    }
}
