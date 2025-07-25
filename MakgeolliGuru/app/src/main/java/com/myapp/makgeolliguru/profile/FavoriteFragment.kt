package com.myapp.makgeolliguru.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.myapp.makgeolliguru.MainActivity
import com.myapp.makgeolliguru.R
import com.myapp.makgeolliguru.map.MakgeolliList
import com.myapp.makgeolliguru.map.MapFragment

class FavoriteFragment:Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_favorite, container, false)

            val prefs = requireContext().getSharedPreferences(MainActivity.SHARED_PREF, Context.MODE_PRIVATE)
            var favoriteListString = prefs.getString(MainActivity.FAVORITE_LIST, null)
            if (favoriteListString != null) {
                favoriteListString = favoriteListString.trim()
                if (!favoriteListString.endsWith("\n")) {
                    favoriteListString += "\n"
                }
            }
            val recyclerView = view.findViewById<RecyclerView>(R.id.dynamic_content)
            Log.d("favoriteListString", favoriteListString.toString())
            // two columns
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

            val favoriteListTab = MakgeolliList(favoriteListString).ReadFileInto2DArray()

            var title: String
            var localisation: String
            val favorite = favoriteListTab?.map { favorite ->
                val id = favorite[0]
                var makgeolliListString = prefs.getString(MainActivity.MAK_LIST, null)
                // find the makgeolli in the list with the same id
                val makgeolliListTab = MakgeolliList(makgeolliListString).ReadFileInto2DArray()
                var mak = makgeolliListTab?.find { makgeolli -> makgeolli[0] == id }

                if (MapFragment.getCurrentLanguage() == "ko") {
                    title = mak?.getOrNull(14)?: "N/A"
                    localisation=mak?.getOrNull(21)?: "N/A"
                } else {
                    title = mak?.getOrNull(15)?: "N/A"
                    localisation=mak?.getOrNull(20)?: "N/A"
                }
                Log.d("mak", mak.toString())
                Favorite(title, imageUrl =mak?.getOrNull(19)?: "N/A", localisation, mak) } ?:
            emptyList()

            Log.d("favorite", favorite.toString())
            val adapter = FavoriteAdapter(favorite, requireActivity().supportFragmentManager)
            recyclerView.adapter = adapter


            return view
        }

        companion object {

            var favoriteListTab: List<Array<String>>? = null
        }
    }