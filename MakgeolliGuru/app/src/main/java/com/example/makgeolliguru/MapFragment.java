package com.example.makgeolliguru;

import static android.content.Context.MODE_PRIVATE;
import static com.example.makgeolliguru.MainActivity.FAVORITE_LIST;
import static com.example.makgeolliguru.MainActivity.MAK_LIST;
import static com.example.makgeolliguru.MainActivity.SHARED_PREF;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


public class MapFragment extends Fragment {

    public MapFragment() {
        // require a empty public constructor
    }
    private GoogleMap myMap;
    static String[][] favoriteTab;
    static String[][] makgeolliListTab;
    ArrayList<Marker> mMarkerArray = new ArrayList<Marker>();

    private DataManager dataManager;
    private MapManager mapManager;

    //private MapView mapView;
    //private Button updateButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize view
        View view = inflater.inflate(R.layout.fragment_map_, container, false);

        dataManager = new DataManager(getContext());
        mapManager = new MapManager();
        String[][] makgeolliListTab = dataManager.loadMakgeolliList();
        String[][] favoriteTab = dataManager.loadFavoriteList();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    myMap = googleMap;
                    String[][] makgeolliListTab = dataManager.loadMakgeolliList();
                    mapManager.initializeMap(myMap, getContext(), makgeolliListTab);

                }
            });
        }
        setupListeners(view, container, inflater);
        return view;
    }



    private void setupListeners(View view, ViewGroup container, LayoutInflater inflater) {
        // Update data
        view.findViewById(R.id.updatebtn).setOnClickListener(v -> {
            try {
                dataManager.updateDataFromAPI();
                Toast.makeText(container.getContext(), "Refresh the page, please", Toast.LENGTH_SHORT).show();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(MapFragment.this).attach(MapFragment.this).commit();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Filter
        ChipGroup chipGroup = view.findViewById(R.id.filter);
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Handle filter change
        });

        view.findViewById(R.id.chipFilter).setOnClickListener(v -> {
            mapManager.filterMarkers(chipGroup, dataManager.loadMakgeolliList(), dataManager.loadFavoriteList());
        });

        /*myMap.setOnInfoWindowClickListener(marker -> {
            myMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(container.getContext()));
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    container.getContext(), R.style.MyTransparentBottomSheetDialogTheme);
            View bottomSheetView = inflater.inflate(R.layout.modal_bottom_sheet, container, false);

            infoWindowsDisplay(bottomSheetView, bottomSheetDialog, marker, container);
        });*/
    }

    private void infoWindowsDisplay(View bottomSheetView, BottomSheetDialog bottomSheetDialog, Marker marker, ViewGroup container){

            // open modal bottom sheet (+learn more)
            // marker id format = "m[id]"
            // Remove the first character with substring
            String idSelected = marker.getId().substring(1);
            String information = String.format("Sweet: %s\n Acidity %s \nTexture %s\nSparkling %s\nFlavor/ingredients %s\nLocalisation %s\n percent alcohol %s\nArtisanal %s\n\n<h3>Comment </h3>", makgeolliListTab[Integer.parseInt(idSelected)][1], makgeolliListTab[Integer.parseInt(idSelected)][2], makgeolliListTab[Integer.parseInt(idSelected)][3], makgeolliListTab[Integer.parseInt(idSelected)][4], makgeolliListTab[Integer.parseInt(idSelected)][5], makgeolliListTab[Integer.parseInt(idSelected)][8], makgeolliListTab[Integer.parseInt(idSelected)][9], makgeolliListTab[Integer.parseInt(idSelected)][10]);

// TODO: if makgeolli on FavoriteTab = setSelected()


            TextView makgeolliName = (TextView) bottomSheetView.findViewById(R.id.makgeolliName);
            // To show rating on RatingBar
            makgeolliName.setText(makgeolliListTab[Integer.parseInt(idSelected)][0]);

            RatingBar ratingBarSweet = (RatingBar) bottomSheetView.findViewById(R.id.ratingBarSweet);
            ratingBarSweet.setRating(Float.parseFloat(makgeolliListTab[Integer.parseInt(idSelected)][1]));

            RatingBar ratingBarAcidity = (RatingBar) bottomSheetView.findViewById(R.id.ratingBarAcidity);
            ratingBarAcidity.setRating(Float.parseFloat(makgeolliListTab[Integer.parseInt(idSelected)][2]));

            RatingBar ratingBarTexture = (RatingBar) bottomSheetView.findViewById(R.id.ratingBarTexture);
            ratingBarTexture.setRating(Float.parseFloat(makgeolliListTab[Integer.parseInt(idSelected)][3]));

            RatingBar ratingBarSparkling = (RatingBar) bottomSheetView.findViewById(R.id.ratingBarSparkling);
            ratingBarSparkling.setRating(Float.parseFloat(makgeolliListTab[Integer.parseInt(idSelected)][4]));

            TextView ingredient = (TextView) bottomSheetView.findViewById(R.id.ingredientText);
            if (getCurrentLanguage() == "kr") {
                ingredient.setText(makgeolliListTab[Integer.parseInt(idSelected)][16]);
            } else {
                ingredient.setText(makgeolliListTab[Integer.parseInt(idSelected)][9]);
            }


            TextView alcoolPercent = (TextView) bottomSheetView.findViewById(R.id.percentText);
            alcoolPercent.setText(makgeolliListTab[Integer.parseInt(idSelected)][8]);

            TextView localisation = (TextView) bottomSheetView.findViewById(R.id.localisationText);
            localisation.setText(makgeolliListTab[Integer.parseInt(idSelected)][5]);

            TextView description = (TextView) bottomSheetView.findViewById(R.id.moreInfoText);
            if (getCurrentLanguage().equals("kr")) {
                description.setText(makgeolliListTab[Integer.parseInt(idSelected)][17]);
            } else {
                description.setText(makgeolliListTab[Integer.parseInt(idSelected)][10]);
            }
            bottomSheetView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(container.getContext(), "Folder Clicked..", Toast.LENGTH_SHORT).show();
                    // dismiss closes the bottom sheet
                    bottomSheetDialog.dismiss();
                }
            });
            favoriteButton_management(idSelected, bottomSheetView, container);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }

    private void favoriteButton_management(String idSelected, View bottomSheetView, ViewGroup container){
        //_________________________________Favorite button___________________________________//
        final ImageButton favoritebtn = bottomSheetView.findViewById(R.id.favorisbtn);
        if (favoriteTab != null) {
            for (int i = 0; i < favoriteTab.length; i++) {
                if (favoriteTab[i][0].equals(makgeolliListTab[Integer.parseInt(idSelected)][0])) {
                    favoritebtn.setSelected(true);
                    favoritebtn.setImageResource(android.R.drawable.btn_star_big_on);
                    break;
                }
            }
        }

        favoritebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(container.getContext(), "Saved as favorite..", Toast.LENGTH_SHORT).show();
                // save as favorite
                favoritebtn.setSelected(!favoritebtn.isSelected());
                SharedPreferences prefs = getContext().getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
                String favoriteListString = prefs.getString(FAVORITE_LIST, null);
                if (favoriteListString == null) {
                    favoriteListString = "";
                }
                MakgeolliList makgeolliList = new MakgeolliList(favoriteListString);

                String text;

                if (favoritebtn.isSelected()) {
                    favoritebtn.setImageResource(android.R.drawable.btn_star_big_on);
                    // Save data on String
                    text = makgeolliList.addDataOnString(makgeolliListTab[Integer.parseInt(idSelected)]);

                    //favoriteTab.setVar(Integer.parseInt(idSelected),makgeolliListTab[Integer.parseInt(idSelected)]);
                } else {
                    favoritebtn.setImageResource(android.R.drawable.btn_star_big_off);
                    // delete from database
                    text = makgeolliList.deleteDataFromString(makgeolliListTab[Integer.parseInt(idSelected)]);
                }
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(FAVORITE_LIST, text);
                editor.apply();
                favoriteTab = makgeolliList.ReadFileInto2DArray();
            }
        });
    }

    public static String getCurrentLanguage() {
        Locale currentLocale = Locale.getDefault();
        return currentLocale.getLanguage();
    }



}
////////////////////////////////////////
/*

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }
}
*/
