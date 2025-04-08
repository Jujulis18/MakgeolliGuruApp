package com.myapp.makgeolliguru.map;

import static android.content.Context.MODE_PRIVATE;
import static android.graphics.Color.parseColor;
import static com.myapp.makgeolliguru.MainActivity.FAVORITE_LIST;
import static com.myapp.makgeolliguru.MainActivity.SAVED_LIST;
import static com.myapp.makgeolliguru.MainActivity.SHARED_PREF;
import static com.myapp.makgeolliguru.map.MapFragment.getCurrentLanguage;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.myapp.makgeolliguru.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapManager {

    private final ArrayList<Marker> mMarkerArray = new ArrayList<>();
    private ViewGroup container;

    public static List<String[]> makgeolliListTab = new ArrayList<>();
    public static List<String[]> favoriteTab = new ArrayList<>();
    public static List<String[]> savedTab = new ArrayList<>();

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

                    String markerName;
                    if (getCurrentLanguage().equals("ko")) {
                        markerName = info[14];
                    } else {
                        markerName = info[15];
                    }

                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLng)
                            .title(markerName)
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

        int idSelected = Integer.parseInt(marker.getId().substring(1))+1;

        ImageView imageView = (ImageView) bottomSheetView.findViewById(R.id.bottleImg);
        String imagePath = null;
        try {
             imagePath = makgeolliListTab.get(idSelected)[19];
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Glide.with(imageView.getContext())
                .load(imagePath)
                .placeholder(R.drawable.marker) // Image affichée pendant le chargement
                .error(R.drawable.marker) // Image affichée si une erreur survient
                .into(imageView);

        TextView makgeolliName = (TextView) bottomSheetView.findViewById(R.id.makgeolliName);
        if (getCurrentLanguage().equals("ko")) {
            makgeolliName.setText(makgeolliListTab.get(idSelected)[14]);
        } else {
            makgeolliName.setText(makgeolliListTab.get(idSelected)[15]);
        }


        RatingBar ratingBarSweet = (RatingBar) bottomSheetView.findViewById(R.id.ratingBarSweet);
        ratingBarSweet.setRating(Float.parseFloat(makgeolliListTab.get(idSelected)[1]));

        RatingBar ratingBarAcidity = (RatingBar) bottomSheetView.findViewById(R.id.ratingBarAcidity);
        ratingBarAcidity.setRating(Float.parseFloat(makgeolliListTab.get(idSelected)[2]));

        RatingBar ratingBarTexture = (RatingBar) bottomSheetView.findViewById(R.id.ratingBarTexture);
        ratingBarTexture.setRating(Float.parseFloat(makgeolliListTab.get(idSelected)[3]));

        RatingBar ratingBarSparkling = (RatingBar) bottomSheetView.findViewById(R.id.ratingBarSparkling);
        ratingBarSparkling.setRating(Float.parseFloat(makgeolliListTab.get(idSelected)[4]));

        TextView ingredient = (TextView) bottomSheetView.findViewById(R.id.ingredientText);
        if (getCurrentLanguage() == "ko") {
            ingredient.setText(makgeolliListTab.get(idSelected)[16]);
        } else {
            ingredient.setText(makgeolliListTab.get(idSelected)[9]);
        }

        TextView alcoolPercent = (TextView) bottomSheetView.findViewById(R.id.percentText);
        alcoolPercent.setText(makgeolliListTab.get(idSelected)[8]);

        TextView localisation = (TextView) bottomSheetView.findViewById(R.id.localisationText);
        if (getCurrentLanguage() == "ko") {
            localisation.setText(makgeolliListTab.get(idSelected)[5]);
        } else {
            localisation.setText(makgeolliListTab.get(idSelected)[18]);
        }

        TextView description = (TextView) bottomSheetView.findViewById(R.id.moreInfoText);
        if (getCurrentLanguage().equals("ko")) {
            description.setText(makgeolliListTab.get(idSelected)[17]);
        } else {
            description.setText(makgeolliListTab.get(idSelected)[10]);
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
        savedButtonManagement(idSelected, bottomSheetView, container);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }

    private void favoriteButtonManagement(int idSelected, View bottomSheetView, ViewGroup container) {
        //_________________________________Favorite button___________________________________//
        final ImageButton favoriteBtn = bottomSheetView.findViewById(R.id.favorisbtn);
        favoriteBtn.setSelected(false);
        favoriteBtn.setImageResource(android.R.drawable.btn_star_big_off);
        if (favoriteTab != null) {
            for (String[] strings : favoriteTab) {
                if (strings[0].equals(makgeolliListTab.get(idSelected)[0])) {
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

            favoriteListString = favoriteListString.trim();
            if (!favoriteListString.endsWith("\n")) {
                favoriteListString += "\n";
            }

            MakgeolliList makgeolliList = new MakgeolliList(favoriteListString);

            String text;

            if (favoriteBtn.isSelected()) {
                favoriteBtn.setImageResource(android.R.drawable.btn_star_big_on);
                // Save data on String
                text = makgeolliList.addDataOnString(List.of(makgeolliListTab.get(idSelected)));

                //favoriteTab.setVar(idSelected,makgeolliListTab[idSelected]);
            } else {
                favoriteBtn.setImageResource(android.R.drawable.btn_star_big_off);
                // delete from database
                text = makgeolliList.deleteDataFromString(makgeolliListTab.get(idSelected));
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(FAVORITE_LIST, text);
            editor.apply();
            favoriteTab = makgeolliList.ReadFileInto2DArray();
        });
    }

    private void savedButtonManagement(int idSelected, View bottomSheetView, ViewGroup container) {
        //_________________________________Favorite button___________________________________//
        final ImageButton savedBtn = bottomSheetView.findViewById(R.id.savedbtn);
        savedBtn.setSelected(false);
        savedBtn.setImageResource(android.R.drawable.ic_input_get);


        savedBtn.setColorFilter(parseColor("#AFADA9"));

        if (savedTab != null) {
            for (String[] strings : savedTab) {
                if (strings[0].equals(makgeolliListTab.get(idSelected)[0])) {
                    savedBtn.setSelected(true);
                    // set color of button
                    savedBtn.setColorFilter(container.getContext().getResources().getColor(R.color.secondary), PorterDuff.Mode.SRC_IN);
                    break;
                }

            }
        }

        savedBtn.setOnClickListener(view -> {
            Toast.makeText(container.getContext(), "Saved makgeolli..", Toast.LENGTH_SHORT).show();
            // save as favorite
            savedBtn.setSelected(!savedBtn.isSelected());
            SharedPreferences prefs = container.getContext().getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
            String savedListString = prefs.getString(SAVED_LIST, null);
            if (savedListString == null) {
                savedListString = "";
            }

            savedListString = savedListString.trim();
            if (!savedListString.endsWith("\n")) {
                savedListString += "\n";
            }

            MakgeolliList makgeolliList = new MakgeolliList(savedListString);

            String text;

            if (savedBtn.isSelected()) {
                savedBtn.setColorFilter(container.getContext().getResources().getColor(R.color.secondary), PorterDuff.Mode.SRC_IN);
                text = makgeolliList.addDataOnString(List.of(makgeolliListTab.get(idSelected)));
            } else {
                savedBtn.setColorFilter(parseColor("#AFADA9"));
                text = makgeolliList.deleteDataFromString(makgeolliListTab.get(idSelected));
            }



            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(SAVED_LIST, text);
            editor.apply();
            savedTab = makgeolliList.ReadFileInto2DArray();
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

        EditText searchBar = (EditText) container.findViewById(R.id.search_bar);
        String searchQuery = searchBar.getText().toString().trim();

        int visibleCount = 0;

        for (Marker marker : mMarkerArray) {
            if (!marker.getTitle().toLowerCase().contains(searchQuery.toLowerCase())) {
                marker.setVisible(false);
                continue;
            }

            if (isFavorite) {

                if (makgeolliListTab == null || favoriteTabs == null) {
                    Log.e("MapManager", "Erreur : makgeolliListTab ou favoriteTabs est null");
                    AlertDialog alertDialog = new AlertDialog.Builder(container.getContext(), R.style.CustomAlertDialog).setTitle(R.string.Error).setMessage(R.string.error_favorite_list).show();
                    alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(container.getContext().getResources().getColor(R.color.primary));
                    return;
                }

                Log.w("MakgeolliGuru", String.format("%d", favoriteTabs.size()));
                boolean visible = false;
                for (String[] tab : favoriteTabs) {
                    Log.w("MakgeolliGuru", String.format("favoriteTab: %s", tab[0]));
                    Log.w("MakgeolliGuru", String.format("completeList: %s", makgeolliListTab.get(Integer.parseInt(marker.getId().substring(1))+1)[0]));
                    if (!visible && !tab[0].equals(makgeolliListTab.get(Integer.parseInt(marker.getId().substring(1))+1)[0])) {
                        marker.setVisible(false);
                    } else {
                        Log.w("MakgeolliGuru", "visible");
                        visible = true;
                        marker.setVisible(true);
                    }
                }
            }
            if (isFruity) {
                if (!makgeolliListTab.get(Integer.parseInt(marker.getId().substring(1))+1)[12].contains("Yes")) {
                    marker.setVisible(false);
                }
            }
            if (isNuts) {
                if (!makgeolliListTab.get(Integer.parseInt(marker.getId().substring(1))+1)[13].contains("Yes")) {
                    marker.setVisible(false);
                }
            }
            if (isSparkling) {
                if (Integer.parseInt(makgeolliListTab.get(Integer.parseInt(marker.getId().substring(1))+1)[4]) <= 3) {
                    marker.setVisible(false);
                }
            }
            if (isSour) {
                if (Integer.parseInt(makgeolliListTab.get(Integer.parseInt(marker.getId().substring(1))+1)[2]) <= 3) {
                    marker.setVisible(false);
                }
            }
            if (isSweet) {
                if (Integer.parseInt(makgeolliListTab.get(Integer.parseInt(marker.getId().substring(1))+1)[1]) <= 3) {
                    marker.setVisible(false);
                }
            }

            if (marker.isVisible()) {
                visibleCount++; // Incrémentation si visible
            }
        }
        if (visibleCount == 0) {
            mMarkerArray.forEach(marker -> marker.setVisible(true));
            showNoResultsDialog();
        }
    }
    private void showNoResultsDialog() {
        AlertDialog  alertDialog =  new AlertDialog.Builder(container.getContext(), R.style.CustomAlertDialog)
                .setTitle(R.string.Error)
                .setMessage(R.string.error_makgeolli_list)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();

        alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(container.getContext().getResources().getColor(R.color.primary));


    }
}

