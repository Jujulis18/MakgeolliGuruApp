package com.myapp.makgeolliguru.tools

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.myapp.makgeolliguru.MainActivity
import com.myapp.makgeolliguru.R
import com.myapp.makgeolliguru.articles.LearningFragment.DataUpdateCallback
import com.myapp.makgeolliguru.map.MakgeolliList
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

class DataManagerKotlin(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(MainActivity.SHARED_PREF, Context.MODE_PRIVATE)

    fun loadMakgeolliList(): List<Array<String>> {
        val makgeolliListString = prefs.getString(MainActivity.MAK_LIST, null)
            ?: loadDefaultData()
        return MakgeolliList(makgeolliListString).ReadFileInto2DArray()
    }

    fun loadFavoriteList(): List<Array<String>>? {
        return prefs.getString(MainActivity.FAVORITE_LIST, null)?.let {
            MakgeolliList(it).ReadFileInto2DArray()
        }
    }

    private fun loadDefaultData(): String {
        val inputStream: InputStream = context.resources.openRawResource(R.raw.makgeollidata)
        val text = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8))
            .lines().collect(Collectors.joining("\n"))
        prefs.edit().putString(MainActivity.MAK_LIST, text).apply()
        return text
    }

    fun saveMakgeolliData(data: String) {
        prefs.edit().putString(MainActivity.MAK_LIST, data).apply()
    }

    fun saveFavoriteList(data: String) {
        prefs.edit().putString(MainActivity.FAVORITE_LIST, data).apply()
    }
}
