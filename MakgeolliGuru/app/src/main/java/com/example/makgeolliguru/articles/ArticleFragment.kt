package com.example.makgeolliguru.articles

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.makgeolliguru.MainActivity
import com.example.makgeolliguru.R
import com.example.makgeolliguru.map.MakgeolliList
import com.example.makgeolliguru.tools.PCloudData
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ArticleFragment(var itemId: Int) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_article, container, false)

        val articleListTab: List<Array<String>>
        val prefs = requireContext().getSharedPreferences(MainActivity.SHARED_PREF, Context.MODE_PRIVATE)
        val articleListString = prefs.getString(MainActivity.ARTICLE_LIST, null)
        articleListTab = MakgeolliList(articleListString).ReadFileInto2DArray()

        //if (itemId != -1 && articleListTab != null && itemId < articleListTab.length) {
        val articleData = articleListTab[itemId]

        // Display the ID or use it to load data
        val textView = view.findViewById<TextView>(R.id.titleTextView)
        textView.text = articleData[1]
        val intro = view.findViewById<TextView>(R.id.IntroView)
        intro.text = articleData[2]
        val textView1 = view.findViewById<TextView>(R.id.paragraphTitle1)
        textView1.text = articleData[3]
        val textView2 = view.findViewById<TextView>(R.id.paragraphText1)
        textView2.text = articleData[4]
        val textView3 = view.findViewById<TextView>(R.id.paragraphTitle2)
        textView3.text = articleData[6]
        val textView4 = view.findViewById<TextView>(R.id.paragraphText2)
        textView4.text = articleData[7]
        val textView5 = view.findViewById<TextView>(R.id.paragraphTitle3)
        textView5.text = articleData[9]
        val textView6 = view.findViewById<TextView>(R.id.paragraphText3)
        textView6.text = articleData[10]
        val textView7 = view.findViewById<TextView>(R.id.ConclusionView)
        textView7.text = articleData[12]

        val imageView1 = view.findViewById<ImageView>(R.id.imageView1)
        PCloudData.downloadAndLoadImage(imageView1.context, articleData[5], imageView1)

        val imageView2 = view.findViewById<ImageView>(R.id.imageView2)
        PCloudData.downloadAndLoadImage(imageView2.context, articleData[8], imageView2)

        val imageView3 = view.findViewById<ImageView>(R.id.imageView3)
        PCloudData.downloadAndLoadImage(imageView3.context, articleData[11], imageView3)



        return view
    }



}