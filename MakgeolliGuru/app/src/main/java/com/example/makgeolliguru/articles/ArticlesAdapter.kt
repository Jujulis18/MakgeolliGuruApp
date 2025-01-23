package com.example.makgeolliguru.articles

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import com.example.makgeolliguru.R

class ArticlesAdapter(private val articles: List<Article>, private val fragmentManager: FragmentManager) : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.card_view)
        val logoImageView: ImageView = itemView.findViewById(R.id.logo)
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
        holder.logoImageView.setImageResource(article.logoResource)

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