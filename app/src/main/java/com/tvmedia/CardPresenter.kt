package com.tvmedia

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide

class CardPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val context = parent.context

        val cardWidth = 300
        val cardHeight = 450

        val container = FrameLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(cardWidth, cardHeight)
            isFocusable = true
            isFocusableInTouchMode = true
            clipToOutline = true
            background = createBackground(false)
        }

        val imageView = ImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
        }

        container.addView(imageView)

        // 🎯 Фокус-анимация
        container.setOnFocusChangeListener { v, hasFocus ->
            v.animate()
                .scaleX(if (hasFocus) 1.08f else 1f)
                .scaleY(if (hasFocus) 1.08f else 1f)
                .setDuration(150)
                .start()

            v.background = createBackground(hasFocus)
        }

        return ViewHolder(container)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val movie = item as Movie
        val container = viewHolder.view as FrameLayout
        val imageView = container.getChildAt(0) as ImageView

        val poster = movie.poster

        if (poster.isNullOrBlank()) {
            // 🧩 fallback-картинка
            imageView.setImageResource(R.drawable.ic_placeholder)
            imageView.setBackgroundColor(Color.DKGRAY)
        } else {
            Glide.with(imageView.context)
                .load(poster)
                .error(R.drawable.ic_placeholder)
                .into(imageView)
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val imageView = (viewHolder.view as FrameLayout).getChildAt(0) as ImageView
        imageView.setImageDrawable(null)
    }

    private fun createBackground(focused: Boolean): GradientDrawable {
        return GradientDrawable().apply {
            cornerRadius = 24f
            setColor(if (focused) Color.parseColor("#1E88E5") else Color.parseColor("#222222"))
        }
    }
}

