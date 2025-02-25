package com.example.makgeolliguru.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.makgeolliguru.MainActivity
import com.example.makgeolliguru.R
import com.example.makgeolliguru.articles.Article
import com.example.makgeolliguru.articles.ArticlesAdapter
import com.example.makgeolliguru.map.MakgeolliList
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

class FavoriteFragment:Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_favorite, container, false)

            val prefs = requireContext().getSharedPreferences(MainActivity.SHARED_PREF, Context.MODE_PRIVATE)
            var favoriteListString = prefs.getString(MainActivity.FAVORITE_LIST, null)

            val recyclerView = view.findViewById<RecyclerView>(R.id.dynamic_content)

            // two columns
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

            //val favoriteListTab = MakgeolliList(favoriteListString)?.ReadFileInto2DArray()

            // favoriteListTab list of 4 examples contains name, imageUrl, localisation
            val favoriteListTab = listOf(arrayOf("name1", "imageUrl", "localisation1"),
                arrayOf("name2", "imageUrl", "localisation2"),
                arrayOf("name3", "imageUrl", "localisation3"),
                arrayOf("name4", "imageUrl", "localisation4"))

            val favorite = favoriteListTab?.map { favorite ->
                Favorite(favorite[0], imageUrl=favorite[1], localisation=favorite[2]) } ?:
            emptyList()

            val adapter = FavoriteAdapter(favorite, requireActivity().supportFragmentManager)
            recyclerView.adapter = adapter

            return view
        }

        companion object {

            var favoriteListTab: List<Array<String>>? = null
        }
    }