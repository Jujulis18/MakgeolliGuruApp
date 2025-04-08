package com.myapp.makgeolliguru.tools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class MakgeolliViewModel(private val dataManager: DataManagerKotlin) : ViewModel() {

    private val _makgeolliData = MutableLiveData<String?>()
    val makgeolliData: LiveData<String?> get() = _makgeolliData

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun updateMakgeolliDataFromAPI() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL(MAKGEOLLI_URL)
                val connection = url.openConnection() as HttpURLConnection
                val data = connection.inputStream.bufferedReader().use { it.readText() }
                dataManager.saveMakgeolliData(data)
                _makgeolliData.postValue(data)
            } catch (e: Exception) {
                _error.postValue("Failed to fetch makgeolli data: ${e.message}")
            }
        }
    }

    fun updateArticleDataFromAPI(callback: (String?, Exception?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL(ARTICLE_URL)
                val connection = url.openConnection() as HttpURLConnection
                val data = connection.inputStream.bufferedReader().use { it.readText() }
                callback(data, null)
            } catch (e: Exception) {
                callback(null, e)
            }
        }
    }

    companion object {
        const val MAKGEOLLI_URL = "https://docs.google.com/spreadsheets/d/e/2PACX-1vSZvW6YRkTSqL5ZDTkDMG7S6xhi8J2ANRj2rGJmKFY99M8O-q2DRoleWNu_aRRiahHscoU76mVKCszt/pub?gid=0&single=true&output=csv"
        const val ARTICLE_URL = "https://docs.google.com/spreadsheets/d/e/2PACX-1vSZvW6YRkTSqL5ZDTkDMG7S6xhi8J2ANRj2rGJmKFY99M8O-q2DRoleWNu_aRRiahHscoU76mVKCszt/pub?gid=1617702893&single=true&output=csv"
    }
}
