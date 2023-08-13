package com.example.makgeolliguru;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.widget.TextViewKt;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void randomWindowText(Marker marker, View view){
        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        if(!title.equals("")){
            tvTitle.setText(title);
        }

        String snippet = marker.getSnippet();
        String[] ratingTab = snippet.split(" ");

        RatingBar ratingBarSweet = (RatingBar) view.findViewById(R.id.ratingBarSweet);
        // To show rating on RatingBar
        ratingBarSweet.setRating(Float.parseFloat(ratingTab[0]));

        RatingBar ratingBarAcidity = (RatingBar) view.findViewById(R.id.ratingBarAcidity);
        // To show rating on RatingBar
        ratingBarAcidity.setRating(Float.parseFloat(ratingTab[1]));

        RatingBar ratingBarTexture = (RatingBar) view.findViewById(R.id.ratingBarTexture);
        // To show rating on RatingBar
        ratingBarTexture.setRating(Float.parseFloat(ratingTab[2]));

        RatingBar ratingBarSparkling = (RatingBar) view.findViewById(R.id.ratingBarSparkling);
        // To show rating on RatingBar
        ratingBarSparkling.setRating(Float.parseFloat(ratingTab[3]));
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        randomWindowText(marker, mWindow);
        return mWindow;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        randomWindowText(marker, mWindow);
        return mWindow;
    }
}
