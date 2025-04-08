package com.myapp.makgeolliguru.articles

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myapp.makgeolliguru.R
import com.myapp.makgeolliguru.MainActivity
import com.myapp.makgeolliguru.map.MakgeolliList
import com.myapp.makgeolliguru.map.MapFragment
import com.myapp.makgeolliguru.tools.DataManager
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

class LearningFragment : Fragment() {
    private lateinit var prefs: SharedPreferences
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_learning, container, false)

        prefs = requireContext().getSharedPreferences(MainActivity.SHARED_PREF, Context.MODE_PRIVATE)
        recyclerView = view.findViewById(R.id.dynamic_content)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Charger les données initiales
        loadArticles()

        // Vérifier et potentiellement mettre à jour en arrière-plan
        checkAndUpdateArticles()

        return view
    }

    private fun loadArticles() {
        // Charger les articles depuis les préférences partagées
        val articleListString = prefs.getString(MainActivity.ARTICLE_LIST, null)

        // Si aucune donnée n'existe, charger depuis le fichier raw
        val finalArticleListString = articleListString ?: run {
            val inputStream = resources.openRawResource(R.raw.makgeolliarticle)
            BufferedReader(
                InputStreamReader(inputStream, StandardCharsets.UTF_8)
            ).lines().collect(Collectors.joining("\n")).also {
                prefs.edit().putString(MainActivity.ARTICLE_LIST, it).apply()
            }
        }

        // Afficher les articles
        displayArticles(finalArticleListString)
    }

    private fun displayArticles(articleListString: String) {
        // Ajouter des logs de débogage
        Log.d("LearningFragment", "Raw article list string: $articleListString")

        val articleListTab = MakgeolliList(articleListString).ReadFileInto2DArray()

        // Vérification supplémentaire
        if (articleListTab.isNullOrEmpty()) {
            Log.e("LearningFragment", "No articles found")
            Toast.makeText(requireContext(), "No articles available", Toast.LENGTH_SHORT).show()
            return
        }

        val articles = articleListTab.mapNotNull { article ->
            try {
                // Ajouter des logs de débogage
                Log.d("LearningFragment", "Article array size: ${article.size}")

                // Vérifier que l'article a suffisamment d'éléments
                if (article.size > 13) {
                    val title = if (MapFragment.getCurrentLanguage() == "ko") article[13] else article[1]
                    val imageUrl = if (article.size > 5) article[5] else ""
                    Article(title, imageUrl = imageUrl)
                } else {
                    Log.e("LearningFragment", "Article array too small: ${article.size}")
                    null
                }
            } catch (e: Exception) {
                Log.e("LearningFragment", "Error processing article", e)
                null
            }
        }

        // Vérifier que la liste d'articles n'est pas vide
        if (articles.isNotEmpty()) {
            val adapter = ArticlesAdapter(articles, requireActivity().supportFragmentManager)
            recyclerView.adapter = adapter
        } else {
            Log.e("LearningFragment", "No valid articles after processing")
            Toast.makeText(requireContext(), "No valid articles", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAndUpdateArticles() {
        val lastUpdateTime = prefs.getLong("LAST_ARTICLE_UPDATE", 0)
        val currentTime = System.currentTimeMillis()
        val oneWeekInMillis = 1 * 7 * 24 * 60 * 60 * 1000L

        // Ne mettre à jour que si plus d'une heure s'est écoulée
        if (currentTime - lastUpdateTime >= oneWeekInMillis) {
            updateArticlesFromAPI()
        }
    }

    private fun updateArticlesFromAPI() {
        val dataManager = DataManager(requireContext())
        dataManager.updateArticleDataFromAPI(object : DataUpdateCallback {
            override fun onUpdateSuccess(updatedData: String) {
                // Mettre à jour les préférences partagées
                prefs.edit().apply {
                    putString(MainActivity.ARTICLE_LIST, updatedData)
                    putLong("LAST_ARTICLE_UPDATE", System.currentTimeMillis())
                }.apply()

                // Mettre à jour l'affichage sur le thread principal
                activity?.runOnUiThread {
                    displayArticles(updatedData)
                }
            }

            override fun onUpdateFailure(exception: Exception) {
                // Gérer l'échec de mise à jour
                activity?.runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        "Échec de la mise à jour des articles",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    // Interface pour gérer le callback de mise à jour
    interface DataUpdateCallback {
        fun onUpdateSuccess(updatedData: String)
        fun onUpdateFailure(exception: Exception)
    }
}

