package com.example.makgeolliguru.map;

import static android.content.Context.MODE_PRIVATE;
import static com.example.makgeolliguru.MainActivity.FAVORITE_LIST;
import static com.example.makgeolliguru.MainActivity.SHARED_PREF;
import static com.example.makgeolliguru.map.MapFragment.getCurrentLanguage;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.makgeolliguru.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapManager {

    private final ArrayList<Marker> mMarkerArray = new ArrayList<>();
    private ViewGroup container;

    static List<String[]> favoriteTab;
    static List<String[]> makgeolliListTab;

    public void initializeMap(GoogleMap googleMap, ViewGroup container, List<String[]> makgeolliListTab, List<String[]> favoriteTab, View bottomSheetView ) {
        this.container = container;
        this.favoriteTab = favoriteTab;
        this.makgeolliListTab = makgeolliListTab;
        LatLng SouthKoreaPos = new LatLng(37.566535, 126.9779692);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SouthKoreaPos));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this.container.getContext()));

        addMarkers(googleMap);
        addMoreInfoDisplay(googleMap, bottomSheetView);
    }

    private void addMarkers(GoogleMap googleMap) {
        for (String[] info : makgeolliListTab) {
            if (info[0] != null) {
                try {
                    double lat = Double.parseDouble(info[6]);
                    double lng = Double.parseDouble(info[7]);

                    LatLng latLng = new LatLng(lat, lng);
                    String snippet = String.format("%s %s %s %s", info[1], info[2], info[3], info[4]);
                    Bitmap smallMarker = getMarkerBitmap(Arrays.asList(info));

                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLng)
                            .title(info[0])
                            .snippet(snippet)
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                            .visible(true);

                    Marker newMarker = googleMap.addMarker(markerOptions);

                    if (newMarker != null) {
                        mMarkerArray.add(newMarker);
                        Log.d("MapManager", "Marker added at: " + latLng.toString());
                    } else {
                        Log.e("MapManager", "Failed to add marker for: " + info[0]);
                    }

                } catch (NumberFormatException e) {
                    Log.e("MapManager", "Invalid coordinates for: " + info[0], e);
                } catch (Exception e) {
                    Log.e("MapManager", "Error adding marker for: " + info[0], e);
                }

            }
        }
    }

    private void addMoreInfoDisplay(GoogleMap googleMap, View bottomSheetView){


        googleMap.setOnInfoWindowClickListener(marker -> {
            googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(container.getContext()));
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    container.getContext(), R.style.MyTransparentBottomSheetDialogTheme);
            //View bottomSheetView = inflater.inflate(R.layout.modal_bottom_sheet, container, false);

            infoWindowsDisplay(bottomSheetView, bottomSheetDialog, marker, container);
        });
    }

    private Bitmap getMarkerBitmap(List<String> info) {
        int height = 100;
        int width = 100;
        Bitmap b = null;
        try {
            int resourceId;
            if (info.get(12).contains("Yes")) {
                resourceId = R.drawable.marker_pink;
            } else if (info.get(13).contains("Yes")) {
                resourceId = R.drawable.marker_orange;
            } else if (Integer.parseInt(info.get(4)) >= 3) {
                resourceId = R.drawable.marker_green;
            } else {
                resourceId = R.drawable.marker;
            }
            b = BitmapFactory.decodeResource(this.container.getResources(), resourceId);

            if (b != null) {
                b = Bitmap.createScaledBitmap(b, width, height, false);
            } else {
                Log.e("MapManager", "Failed to decode resource for: " + info.get(0));
            }
        } catch (Exception e) {
            Log.e("MapManager", "Error loading bitmap for: " + info.get(0), e);
        }
        return b;
    }

    private void infoWindowsDisplay(View bottomSheetView, BottomSheetDialog bottomSheetDialog, Marker marker, ViewGroup container) {

        // Vérifiez si la vue a un parent et détachez-la si nécessaire
        ViewGroup parent = (ViewGroup) bottomSheetView.getParent();
        if (parent != null) {
            parent.removeView(bottomSheetView);
        }

        // open modal bottom sheet (+learn more)
        // marker id format = "m[id]"
        // Remove the first character with substring
        String idSelected = marker.getId().substring(1);
        String information = String.format("Sweet: %s\n Acidity %s \nTexture %s\nSparkling %s\nFlavor/ingredients %s\nLocalisation %s\n percent alcohol %s\nArtisanal %s\n\n<h3>Comment </h3>",
                makgeolliListTab.get(Integer.parseInt(idSelected))[1],
                makgeolliListTab.get(Integer.parseInt(idSelected))[2],
                makgeolliListTab.get(Integer.parseInt(idSelected))[3],
                makgeolliListTab.get(Integer.parseInt(idSelected))[4],
                makgeolliListTab.get(Integer.parseInt(idSelected))[5],
                makgeolliListTab.get(Integer.parseInt(idSelected))[8],
                makgeolliListTab.get(Integer.parseInt(idSelected))[9],
                makgeolliListTab.get(Integer.parseInt(idSelected))[10]);

// TODO: if makgeolli on FavoriteTab = setSelected()

        TextView makgeolliName = (TextView) bottomSheetView.findViewById(R.id.makgeolliName);
        // To show rating on RatingBar
        makgeolliName.setText(makgeolliListTab.get(Integer.parseInt(idSelected))[0]);

        RatingBar ratingBarSweet = (RatingBar) bottomSheetView.findViewById(R.id.ratingBarSweet);
        ratingBarSweet.setRating(Float.parseFloat(makgeolliListTab.get(Integer.parseInt(idSelected))[1]));

        RatingBar ratingBarAcidity = (RatingBar) bottomSheetView.findViewById(R.id.ratingBarAcidity);
        ratingBarAcidity.setRating(Float.parseFloat(makgeolliListTab.get(Integer.parseInt(idSelected))[2]));

        RatingBar ratingBarTexture = (RatingBar) bottomSheetView.findViewById(R.id.ratingBarTexture);
        ratingBarTexture.setRating(Float.parseFloat(makgeolliListTab.get(Integer.parseInt(idSelected))[3]));

        RatingBar ratingBarSparkling = (RatingBar) bottomSheetView.findViewById(R.id.ratingBarSparkling);
        ratingBarSparkling.setRating(Float.parseFloat(makgeolliListTab.get(Integer.parseInt(idSelected))[4]));

        TextView ingredient = (TextView) bottomSheetView.findViewById(R.id.ingredientText);
        if (getCurrentLanguage() == "kr") {
            ingredient.setText(makgeolliListTab.get(Integer.parseInt(idSelected))[16]);
        } else {
            ingredient.setText(makgeolliListTab.get(Integer.parseInt(idSelected))[9]);
        }

        TextView alcoolPercent = (TextView) bottomSheetView.findViewById(R.id.percentText);
        alcoolPercent.setText(makgeolliListTab.get(Integer.parseInt(idSelected))[8]);

        TextView localisation = (TextView) bottomSheetView.findViewById(R.id.localisationText);
        localisation.setText(makgeolliListTab.get(Integer.parseInt(idSelected))[5]);

        TextView description = (TextView) bottomSheetView.findViewById(R.id.moreInfoText);
        if (getCurrentLanguage().equals("kr")) {
            description.setText(makgeolliListTab.get(Integer.parseInt(idSelected))[17]);
        } else {
            description.setText(makgeolliListTab.get(Integer.parseInt(idSelected))[10]);
        }
        bottomSheetView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(container.getContext(), "Folder Clicked..", Toast.LENGTH_SHORT).show();
                // dismiss closes the bottom sheet
                bottomSheetDialog.dismiss();

            }
        });
        favoriteButtonManagement(idSelected, bottomSheetView, container);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }

    private void favoriteButtonManagement(String idSelected, View bottomSheetView, ViewGroup container) {
        //_________________________________Favorite button___________________________________//
        final ImageButton favoriteBtn = bottomSheetView.findViewById(R.id.favorisbtn);
        favoriteBtn.setSelected(false);
        favoriteBtn.setImageResource(android.R.drawable.btn_star_big_off);
        if (favoriteTab != null) {
            for (String[] strings : favoriteTab) {
                if (strings[0].equals(makgeolliListTab.get(Integer.parseInt(idSelected))[0])) {
                    favoriteBtn.setSelected(true);
                    favoriteBtn.setImageResource(android.R.drawable.btn_star_big_on);
                    break;
                }

            }
        }

        favoriteBtn.setOnClickListener(view -> {
            Toast.makeText(container.getContext(), "Saved as favorite..", Toast.LENGTH_SHORT).show();
            // save as favorite
            favoriteBtn.setSelected(!favoriteBtn.isSelected());
            SharedPreferences prefs = container.getContext().getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
            String favoriteListString = prefs.getString(FAVORITE_LIST, null);
            if (favoriteListString == null) {
                favoriteListString = "";
            }
            MakgeolliList makgeolliList = new MakgeolliList(favoriteListString);

            String text;

            if (favoriteBtn.isSelected()) {
                favoriteBtn.setImageResource(android.R.drawable.btn_star_big_on);
                // Save data on String
                text = makgeolliList.addDataOnString(makgeolliListTab.get(Integer.parseInt(idSelected)));

                //favoriteTab.setVar(Integer.parseInt(idSelected),makgeolliListTab[Integer.parseInt(idSelected)]);
            } else {
                favoriteBtn.setImageResource(android.R.drawable.btn_star_big_off);
                // delete from database
                text = makgeolliList.deleteDataFromString(makgeolliListTab.get(Integer.parseInt(idSelected)));
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(FAVORITE_LIST, text);
            editor.apply();
            favoriteTab = makgeolliList.ReadFileInto2DArray();
        });
    }


    public void filterMarkers(ChipGroup chipGroup, List<String[]> makgeolliListTab, List<String[]> favoriteTabs) {
        mMarkerArray.forEach(marker -> marker.setVisible(true));
        boolean isSweet = ((Chip) chipGroup.findViewById(R.id.chipSweet)).isChecked();
        boolean isSour = ((Chip) chipGroup.findViewById(R.id.chipSour)).isChecked();
        boolean isSparkling = ((Chip) chipGroup.findViewById(R.id.chipSparkling)).isChecked();
        boolean isNuts = ((Chip) chipGroup.findViewById(R.id.chipNuts)).isChecked();
        boolean isFruity = ((Chip) chipGroup.findViewById(R.id.chipFruity)).isChecked();
        boolean isFavorite = ((Chip) chipGroup.findViewById(R.id.favorite)).isChecked();

        for (Marker marker : mMarkerArray) {
            if (isFavorite) {

                Log.w("MakgeolliGuru", String.format("%d", favoriteTabs.size()));
                boolean visible = false;
                for (String[] tab : favoriteTabs) {
                    Log.w("MakgeolliGuru", String.format("favoriteTab: %s", tab[0]));
                    Log.w("MakgeolliGuru", String.format("completeList: %s", makgeolliListTab.get(Integer.parseInt(marker.getId().substring(1)))[0]));
                    if (!visible && !tab[0].contains(makgeolliListTab.get(Integer.parseInt(marker.getId().substring(1)))[0])) {
                        marker.setVisible(false);
                    } else {
                        Log.w("MakgeolliGuru", "visible");
                        visible = true;
                        marker.setVisible(true);
                    }
                }
            }
            if (isFruity) {
                if (!makgeolliListTab.get(Integer.parseInt(marker.getId().substring(1)))[12].contains("Yes")) {
                    marker.setVisible(false);
                }
            }
            if (isNuts) {
                if (!makgeolliListTab.get(Integer.parseInt(marker.getId().substring(1)))[13].contains("Yes")) {
                    marker.setVisible(false);
                }
            }
            if (isSparkling) {
                if (Integer.parseInt(makgeolliListTab.get(Integer.parseInt(marker.getId().substring(1)))[4]) <= 3) {
                    marker.setVisible(false);
                }
            }
            if (isSour) {
                if (Integer.parseInt(makgeolliListTab.get(Integer.parseInt(marker.getId().substring(1)))[2]) <= 3) {
                    marker.setVisible(false);
                }
            }
            if (isSweet) {
                if (Integer.parseInt(makgeolliListTab.get(Integer.parseInt(marker.getId().substring(1)))[1]) <= 3) {
                    marker.setVisible(false);
                }
            }
        }
    }
}

