package com.myapp.makgeolliguru.tools

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.myapp.makgeolliguru.R

class PCloudData {

    companion object {
        fun downloadAndLoadImage(context: Context, imageUrl: String, imageView: ImageView) {

            Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(imageView)


        }
    }
}