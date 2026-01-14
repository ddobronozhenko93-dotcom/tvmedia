package com.tvmedia

import android.view.ViewGroup
import android.widget.ImageView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide

class CardPresenter : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup) = ViewHolder(ImageView(parent.context).apply {
        layoutParams = ViewGroup.LayoutParams(300, 450)
        scaleType = ImageView.ScaleType.CENTER_CROP
        isFocusable = true
    })

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val movie = item as Movie
        Glide.with(viewHolder.view.context)
            .load(movie.poster)
            .into(viewHolder.view as ImageView)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {}
}
