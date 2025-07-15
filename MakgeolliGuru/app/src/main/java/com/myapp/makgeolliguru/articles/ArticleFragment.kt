package com.myapp.makgeolliguru.articles

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.myapp.makgeolliguru.MainActivity
import com.myapp.makgeolliguru.R
import com.myapp.makgeolliguru.map.MakgeolliList
import com.myapp.makgeolliguru.map.MapFragment
import com.myapp.makgeolliguru.tools.PCloudData

class ArticleFragment(var itemId: Int) : Fragment() {

    private lateinit var zoomableLayout: View
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f

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

        val dateView = view.findViewById<TextView>(R.id.dateView)
        val tagsView = view.findViewById<TextView>(R.id.tagsView)

        val textView = view.findViewById<TextView>(R.id.titleTextView)
        val intro = view.findViewById<TextView>(R.id.IntroView)
        val textView1 = view.findViewById<TextView>(R.id.paragraphTitle1)
        val textView2 = view.findViewById<TextView>(R.id.paragraphText1)
        val textView3 = view.findViewById<TextView>(R.id.paragraphTitle2)
        val textView4 = view.findViewById<TextView>(R.id.paragraphText2)
        val textView5 = view.findViewById<TextView>(R.id.paragraphTitle3)
        val textView6 = view.findViewById<TextView>(R.id.paragraphText3)
        val textView7 = view.findViewById<TextView>(R.id.ConclusionView)
        val textView8 = view.findViewById<TextView>(R.id.InformationView)


        dateView.text = ""
        tagsView.text = ""

        if (MapFragment.getCurrentLanguage() == "ko") {
            textView.text = articleData[13]
            intro.text = articleData[14]
            textView1.text = articleData[15]
            textView2.text = articleData[16]
            textView3.text = articleData[17]
            textView4.text = articleData[18]
            textView5.text = articleData[19]
            textView6.text = articleData[20]
            textView7.text = articleData[21]
            textView8.text = articleData[25]
        } else {

            textView.text = articleData[1]
            intro.text = articleData[2]
            textView1.text = articleData[3]
            textView2.text = articleData[4]
            textView3.text = articleData[6]
            textView4.text = articleData[7]
            textView5.text = articleData[9]
            textView6.text = articleData[10]
            textView7.text = articleData[12]
            textView8.text = articleData[24]
        }

        val imageView1 = view.findViewById<ImageView>(R.id.imageView1)
        PCloudData.downloadAndLoadImage(imageView1.context, articleData[5], imageView1)

        val imageView2 = view.findViewById<ImageView>(R.id.imageView2)
        PCloudData.downloadAndLoadImage(imageView2.context, articleData[8], imageView2)

        val imageView3 = view.findViewById<ImageView>(R.id.imageView3)
        PCloudData.downloadAndLoadImage(imageView3.context, articleData[11], imageView3)

        val closeButton = view.findViewById<ImageView>(R.id.closebtn)
        closeButton.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }

        // Zoomable Layout
        zoomableLayout = view.findViewById(R.id.zoomableLayout)

        scaleGestureDetector = ScaleGestureDetector(zoomableLayout.context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scaleFactor *= detector.scaleFactor
                scaleFactor = scaleFactor.coerceIn(0.5f, 3.0f)

                zoomableLayout.scaleX = scaleFactor
                zoomableLayout.scaleY = scaleFactor
                return true
            }
        })

        zoomableLayout.setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)
            true
        }


        return view
    }



}