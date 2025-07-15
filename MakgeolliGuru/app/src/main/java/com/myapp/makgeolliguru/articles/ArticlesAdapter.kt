package com.myapp.makgeolliguru.articles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import com.myapp.makgeolliguru.R
import com.myapp.makgeolliguru.tools.PCloudData

class ArticlesAdapter(private val articles: List<Article>, private val fragmentManager: FragmentManager) : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.card_view)
        val imageView: ImageView = itemView.findViewById(R.id.logo)
        val titleTextView: TextView = itemView.findViewById(R.id.articleTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        holder.titleTextView.text = article.title

        // image

        val imageUrl = article.imageUrl
        PCloudData.downloadAndLoadImage(holder.imageView.context, imageUrl, holder.imageView)


        holder.cardView.setOnClickListener {
            val articleFragment = ArticleFragment(position)
            fragmentManager.beginTransaction()
                .replace(R.id.flFragment, articleFragment) // Replace the fragment container
                .addToBackStack(null) // Add to back stack for navigation history
                .commit()
        }

    }



    override fun getItemCount() = articles.size
}