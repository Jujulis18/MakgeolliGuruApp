package com.example.makgeolliguru.profile

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.makgeolliguru.R
import com.example.makgeolliguru.articles.Article
import com.example.makgeolliguru.articles.ArticleFragment
import com.example.makgeolliguru.profile.FavoriteAdapter
import com.example.makgeolliguru.tools.PCloudData

class FavoriteAdapter(private val favorite: List<Favorite>, private val fragmentManager: FragmentManager) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>()  {



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView? = itemView.findViewById(R.id.card_view)
        val imageView: ImageView? = itemView.findViewById(R.id.logo)
        val nameTextView: TextView? = itemView.findViewById(R.id.makgeolli_name)
        val localisationTextView: TextView? = itemView.findViewById(R.id.makgeolli_localisation)

        init {
            Log.d("ViewHolder", "ViewHolder créé. cardView=$cardView, imageView=$imageView, nameTextView=$nameTextView, localisationTextView=$localisationTextView")

            if (cardView == null || imageView == null || nameTextView == null || localisationTextView == null) {
                Log.e("ViewHolder", "ERREUR: Une des vues est null. Vérifie cardview_layout.xml")
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_layout, parent, false) // Vérifie bien le nom
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite = favorite[position]

        Log.d("FavoriteAdapter", "Binding item at position $position: ${favorite.name}")

        // Vérification des views
        if (holder.nameTextView == null || holder.localisationTextView == null || holder.imageView == null) {
            Log.e("FavoriteAdapter", "Problème: une des vues du ViewHolder est null !")
            return
        }

        holder.nameTextView.text = favorite.name
        holder.localisationTextView.text = favorite.localisation

        // Chargement de l'image avec PCloudData
        val imageUrl = favorite.imageUrl
        Log.d("FavoriteAdapter", "Loading image from: $imageUrl")

        PCloudData.downloadAndLoadImage(holder.imageView.context, imageUrl, holder.imageView)

        // Gestion du clic sur la CardView
        holder.cardView?.setOnClickListener {
            Log.d("FavoriteAdapter", "CardView clicked at position $position")

            val articleFragment = ArticleFragment(position)
            fragmentManager.beginTransaction()
                .replace(R.id.flFragment, articleFragment) // Remplacement du fragment container
                .addToBackStack(null) // Ajout à l'historique de navigation
                .commit()
        }
    }


    override fun getItemCount() = favorite.size
}