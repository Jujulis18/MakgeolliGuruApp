package com.example.makgeolliguru.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.makgeolliguru.MainActivity
import com.example.makgeolliguru.R
import com.example.makgeolliguru.map.MapFragment

class MakgeolliInfoFragment(private var item: Array<String>?) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_makgeolli_info, container, false)


        Log.e("item", " MakgeolliInfoFragment:"+item.toString());
        val mak = item
        if (mak != null) {
            // Chargement de l'image
            val imageView = view.findViewById<ImageView>(R.id.bottleImg)
            var imagePath: String? = null

            try {
                imagePath = mak[19]
            } catch (e: Exception) {
                e.printStackTrace()
            }

            Glide.with(imageView.context)
                .load(imagePath)
                .placeholder(R.drawable.marker)
                .error(R.drawable.marker)
                .into(imageView)

            // Récupération du TextView pour le nom du Makgeolli
            val makgeolliName = view.findViewById<TextView>(R.id.makgeolliName)

            if (MapFragment.getCurrentLanguage() == "ko") {
                makgeolliName.text = mak[14]
            } else {
                makgeolliName.text = mak[15]
            }

            // Mise à jour du RatingBar
            val ratingBarSweet = view.findViewById<RatingBar>(R.id.ratingBarSweet)
            ratingBarSweet.rating =
                mak[1].toFloatOrNull() ?: 0f // Gestion d'une conversion possible


            view.findViewById<RatingBar>(R.id.ratingBarAcidity).rating =
                mak[2].toFloat()

            view.findViewById<RatingBar>(R.id.ratingBarTexture).rating =
                mak[3].toFloat()

            view.findViewById<RatingBar>(R.id.ratingBarSparkling).rating =
                mak[4].toFloat()

            val ingredient = view.findViewById<TextView>(R.id.ingredientText)
            if (MapFragment.getCurrentLanguage() === "ko") {
                ingredient.text = mak[16]
            } else {
                ingredient.text = mak[9]
            }

            view.findViewById<TextView>(R.id.percentText).text =
                mak[8]

            val localisation = view.findViewById<TextView>(R.id.localisationText)
            if (MapFragment.getCurrentLanguage() === "ko") {
                localisation.text = mak[5]
            } else {
                localisation.text = mak[18]
            }

            val description = view.findViewById<TextView>(R.id.moreInfoText)
            if (MapFragment.getCurrentLanguage() == "ko") {
                description.text = mak[17]
            } else {
                description.text = mak[10]
            }


        }

        val closeButton = view.findViewById<ImageView>(R.id.favoriteclosebtn)
        closeButton.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }


        return view
    }
}
