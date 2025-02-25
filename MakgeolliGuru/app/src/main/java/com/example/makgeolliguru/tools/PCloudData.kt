package com.example.makgeolliguru.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.makgeolliguru.R
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

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