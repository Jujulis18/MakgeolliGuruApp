package com.example.makgeolliguru.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.makgeolliguru.MainActivity
import com.example.makgeolliguru.R
import com.example.makgeolliguru.map.MakgeolliList
import com.example.makgeolliguru.map.MapFragment

class SavedFragment:Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_favorite, container, false)
            // update title
            val textView = view.findViewById<TextView>(R.id.titleText)
            textView.text = getString(R.string.saved)

            val prefs = requireContext().getSharedPreferences(MainActivity.SHARED_PREF, Context.MODE_PRIVATE)
            var savedListString = prefs.getString(MainActivity.SAVED_LIST, null)
            if (savedListString != null) {
                savedListString = savedListString.trim()
                if (!savedListString.endsWith("\n")) {
                    savedListString += "\n"
                }
            }
            val recyclerView = view.findViewById<RecyclerView>(R.id.dynamic_content)

            // two columns
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

            val savedListTab = MakgeolliList(savedListString).ReadFileInto2DArray()

            var title: String
            var localisation: String
            val saved = savedListTab?.map { saved ->
                val id = saved[0]
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

            Log.d("saved", saved.toString())
            val adapter = FavoriteAdapter(saved, requireActivity().supportFragmentManager)
            recyclerView.adapter = adapter


            return view
        }

        companion object {

            var savedListTab: List<Array<String>>? = null
        }
    }