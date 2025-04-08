package com.myapp.makgeolliguru.profile

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.myapp.makgeolliguru.MainActivity
import com.myapp.makgeolliguru.R
import com.myapp.makgeolliguru.map.MakgeolliList
import com.myapp.makgeolliguru.map.MapFragment
import com.myapp.makgeolliguru.map.MapManager

class MakgeolliInfoFragment(private var item: Array<String>?) : Fragment(), Parcelable {

    constructor(parcel: Parcel) : this(parcel.createStringArray()) {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_makgeolli_info, container, false)

        item?.let { mak ->
            setupMakgeolliUI(view, mak)
        }

        setupCloseButton(view)
        setupFavoriteButton(view, requireNotNull(container))
        setupSavedButton(view, requireNotNull(container))

        return view
    }

    private fun setupMakgeolliUI(view: View, mak: Array<String>) {
        val context = view.context

        Glide.with(context)
            .load(mak.getOrNull(19))
            .placeholder(R.drawable.marker)
            .error(R.drawable.marker)
            .into(view.findViewById(R.id.bottleImg))

        view.findViewById<TextView>(R.id.tagsView).text = ""

        view.findViewById<TextView>(R.id.makgeolliName).text = when (MapFragment.getCurrentLanguage()) {
            "ko" -> mak.getOrNull(14)
            else -> mak.getOrNull(15)
        }

        view.findViewById<RatingBar>(R.id.ratingBarSweet).rating = mak.getOrNull(1)?.toFloatOrNull() ?: 0f
        view.findViewById<RatingBar>(R.id.ratingBarAcidity).rating = mak.getOrNull(2)?.toFloatOrNull() ?: 0f
        view.findViewById<RatingBar>(R.id.ratingBarTexture).rating = mak.getOrNull(3)?.toFloatOrNull() ?: 0f
        view.findViewById<RatingBar>(R.id.ratingBarSparkling).rating = mak.getOrNull(4)?.toFloatOrNull() ?: 0f

        view.findViewById<TextView>(R.id.ingredientText).text = when (MapFragment.getCurrentLanguage()) {
            "ko" -> mak.getOrNull(16)
            else -> mak.getOrNull(9)
        }

        view.findViewById<TextView>(R.id.percentText).text = mak.getOrNull(8)

        view.findViewById<TextView>(R.id.localisationText).text = when (MapFragment.getCurrentLanguage()) {
            "ko" -> mak.getOrNull(5)
            else -> mak.getOrNull(18)
        }

        view.findViewById<TextView>(R.id.moreInfoText).text = when (MapFragment.getCurrentLanguage()) {
            "ko" -> mak.getOrNull(17)
            else -> mak.getOrNull(10)
        }

        view.findViewById<ImageView>(R.id.favorisbtn).setOnClickListener {
            // TODO: implémenter logique favori
        }
    }

    private fun setupCloseButton(view: View) {
        view.findViewById<ImageView>(R.id.favoriteclosebtn).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setupFavoriteButton(view: View, container: ViewGroup) {
        val favoriteBtn = view.findViewById<ImageButton>(R.id.favorisbtn)
        favoriteBtn.isSelected = false
        favoriteBtn.setImageResource(android.R.drawable.btn_star_big_off)

        val makList = MapManager.makgeolliListTab
        val favoriteList = MapManager.favoriteTab ?: emptyList()
        val selected = item ?: return
        Log.d("setupFavoriteButton", "favoriteList: $favoriteList")
        Log.d("setupFavoriteButton", "selected: $selected")
        if (favoriteList.any { it[0] == selected[0] }) {
            favoriteBtn.isSelected = true
            favoriteBtn.setImageResource(android.R.drawable.btn_star_big_on)
        }

        favoriteBtn.setOnClickListener {
            toggleFavorite(container.context, favoriteBtn, selected)
        }
    }

    private fun toggleFavorite(context: Context, btn: ImageButton, makItem: Array<String>) {
        Toast.makeText(context, "Saved as favorite..", Toast.LENGTH_SHORT).show()
        val prefs = context.getSharedPreferences(MainActivity.SHARED_PREF, Context.MODE_PRIVATE)
        val currentData = prefs.getString(MainActivity.FAVORITE_LIST, "")?.trim() ?: ""

        val makList = MakgeolliList(currentData)
        Log.d("toggleFavorite", "makList: $makList")
        val newData = if (btn.isSelected) {
            btn.setImageResource(android.R.drawable.btn_star_big_off)
            makList.deleteDataFromString(makItem)
        } else {
            btn.setImageResource(android.R.drawable.btn_star_big_on)
            makList.addDataOnString(makItem.toList())
        }

        Log.d("toggleFavorite", "newData: $newData")
        btn.isSelected = !btn.isSelected
        prefs.edit().putString(MainActivity.FAVORITE_LIST, newData).apply()
        MapManager.favoriteTab = makList.ReadFileInto2DArray()
    }

    private fun setupSavedButton(view: View, container: ViewGroup) {
        val savedBtn = view.findViewById<ImageButton>(R.id.savedbtn)
        val context = container.context
        val selected = item ?: return

        val isAlreadySaved = MapManager.savedTab?.any { it[0] == selected[0] } ?: false

        savedBtn.isSelected = isAlreadySaved
        savedBtn.setImageResource(android.R.drawable.ic_input_get)
        savedBtn.setColorFilter(
            ContextCompat.getColor(context, if (isAlreadySaved) R.color.secondary else R.color.background_dark),
            PorterDuff.Mode.SRC_IN
        )

        savedBtn.setOnClickListener {
            toggleSaved(context, savedBtn, selected)
        }
    }

    private fun toggleSaved(context: Context, btn: ImageButton, makItem: Array<String>) {
        Toast.makeText(context, "Saved makgeolli..", Toast.LENGTH_SHORT).show()
        val prefs = context.getSharedPreferences(MainActivity.SHARED_PREF, Context.MODE_PRIVATE)
        val currentData = prefs.getString(MainActivity.SAVED_LIST, "")?.trim() ?: ""
        val makList = MakgeolliList(currentData)

        val newData = if (btn.isSelected) {
            btn.setColorFilter(ContextCompat.getColor(context, R.color.background_dark), PorterDuff.Mode.SRC_IN)
            makList.deleteDataFromString(makItem)
        } else {
            btn.setColorFilter(ContextCompat.getColor(context, R.color.secondary), PorterDuff.Mode.SRC_IN)
            makList.addDataOnString(makItem.toList())
        }

        btn.isSelected = !btn.isSelected
        prefs.edit().putString(MainActivity.SAVED_LIST, newData).apply()
        MapManager.savedTab = makList.ReadFileInto2DArray()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringArray(item)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MakgeolliInfoFragment> {
        override fun createFromParcel(parcel: Parcel): MakgeolliInfoFragment {
            return MakgeolliInfoFragment(parcel)
        }

        override fun newArray(size: Int): Array<MakgeolliInfoFragment?> {
            return arrayOfNulls(size)
        }
    }
}
