package com.example.makgeolliguru.articles

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.makgeolliguru.MainActivity
import com.example.makgeolliguru.R
import com.example.makgeolliguru.map.MakgeolliList
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors
import kotlin.text.replace

class LearningFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_learning, container, false)

        val prefs = requireContext().getSharedPreferences(MainActivity.SHARED_PREF, Context.MODE_PRIVATE)
        var articleListString = prefs.getString(MainActivity.ARTICLE_LIST, null)

        if (articleListString == null) {
            data
            articleListString = prefs.getString(MainActivity.ARTICLE_LIST, null)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.dynamic_content)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        val articleListTab = MakgeolliList(articleListString)?.ReadFileInto2DArray()

        val articles = articleListTab?.map { article ->
            Article(article[1], R.drawable.logo) } ?:
            emptyList()

        val adapter = ArticlesAdapter(articles, requireActivity().supportFragmentManager)
        recyclerView.adapter = adapter

        return view
    }



    val data: Void?
        get() {
            val inputStream =
                resources.openRawResource(R.raw.makgeolliarticle)
            //CSVFile csvFile = new CSVFile(inputStream);
            //MainActivity.makgeolliList = csvFile.ReadFileInto2DArray();
            val editor = requireContext().getSharedPreferences(
                MainActivity.SHARED_PREF,
                Context.MODE_PRIVATE
            ).edit()
            val text = BufferedReader(
                InputStreamReader(inputStream, StandardCharsets.UTF_8)
            )
                .lines()
                .collect(Collectors.joining("\n"))
            editor.putString(MainActivity.ARTICLE_LIST, text)
            editor.apply()
            //Globals.getInstance().setMakgeolliList(makgeolliList);
            return null
        }

    companion object {
        var articleListTab: List<Array<String>>? = null
    }
}