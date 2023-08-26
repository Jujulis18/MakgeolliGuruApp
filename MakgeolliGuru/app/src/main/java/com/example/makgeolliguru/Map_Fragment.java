package com.example.makgeolliguru;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;


public class Map_Fragment extends Fragment {

    public Map_Fragment() {
        // require a empty public constructor
    }

    private GoogleMap myMap;
    static FavoriteDatabase favoriteTab = new FavoriteDatabase();
    ArrayList<Marker> mMarkerArray = new ArrayList<Marker>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize view
        View view = inflater.inflate(R.layout.fragment_map_, container, false);

//________________Find components____________________________________//
        // Filter
        ChipGroup chipGroup = view.findViewById(R.id.filter);

        // Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);


        if(MainActivity.makgeolliList.length==0) {
            getData();
        }
        String[][] makgeolliList = MainActivity.makgeolliList;

//_______________Put Listener for update data ______________________________//

        ImageButton updatebtn = view.findViewById(R.id.updatebtn);

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataFromAPI();
                /*private val callback = new OnMapReadyCallback() { updateMap(); };
                supportMapFragment.getMapAsync(callback);*/
                updateMap();
            }
        });

//_______________Put Listener about filter______________________________//
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {

                Chip chip = chipGroup.findViewById(i);
                if (chip != null)
                    Toast.makeText(container.getContext(), "Chip is ", Toast.LENGTH_SHORT).show();
            }
        });

        Chip chip = view.findViewById(R.id.chipFilter);
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(container.getContext(), "Close is Clicked", Toast.LENGTH_SHORT).show();
                filterMarker(chipGroup, mMarkerArray,makgeolliList);
            }
        });
//________________ Async map_________________________________________//
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                myMap = googleMap;

                LatLng SouthKorea = new LatLng(37.566535, 126.9779692);
                myMap.moveCamera(CameraUpdateFactory.newLatLng(SouthKorea));
                myMap.getUiSettings().setZoomControlsEnabled(true);
                myMap.getUiSettings().setMapToolbarEnabled(false);
                myMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(container.getContext()));

                // boucle for to display the map with markers
                MarkerOptions marker;
                for (int i = 0; i < makgeolliList.length; i++) {
                    if(makgeolliList[i][0]!=null) {
                        // Get the coordonate of each marker
                        double[] coords = {Double.parseDouble(makgeolliList[i][7]), Double.parseDouble(makgeolliList[i][6])};
                        String[] info = makgeolliList[i];
                        LatLng latLng = new LatLng(coords[1], coords[0]);

                        String snippet = String.format("%s %s %s %s", info[1], info[2], info[3], info[4]);

                        int height = 100;
                        int width = 100;
                        Bitmap b;
                        boolean fruity = (info[12].contains("Yes"));
                        boolean nuts = (info[13].contains("Yes"));
                        if(fruity){
                             b = BitmapFactory.decodeResource(getResources(), R.drawable.marker_pink);

                        } else if (nuts) {
                             b = BitmapFactory.decodeResource(getResources(), R.drawable.marker_orange);

                        } else if (Integer.parseInt(info[4]) > 3) {
                             b = BitmapFactory.decodeResource(getResources(), R.drawable.marker_green);

                        }else{
                             b = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
                        }
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                        marker = new MarkerOptions()
                                .position(latLng)
                                .title(info[0])
                                .snippet(snippet)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                        Marker newMarker = myMap.addMarker(marker);
                        mMarkerArray.add(newMarker);
                    }
                }
                // Set a listener for info window events.
                myMap.setOnInfoWindowClickListener(this::onInfoWindowClick);

            }

// ______________________ Click on Info window  _________________________//
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(container.getContext(), "Info window clicked",
                        Toast.LENGTH_SHORT).show();
                myMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(container.getContext()));

                // open modal bottom sheet (+learn more)
                // marker id format = "m[id]"
                // Remove the first character with substring
                String idSelected = marker.getId().substring(1);
                String information = String.format("Sweet: %s\n Acidity %s \nTexture %s\nSparkling %s\nFlavor/ingredients %s\nLocalisation %s\n percent alcohol %s\nArtisanal %s\n\n<h3>Comment </h3>", makgeolliList[Integer.parseInt(idSelected)][1], makgeolliList[Integer.parseInt(idSelected)][2], makgeolliList[Integer.parseInt(idSelected)][3], makgeolliList[Integer.parseInt(idSelected)][4], makgeolliList[Integer.parseInt(idSelected)][5], makgeolliList[Integer.parseInt(idSelected)][8], makgeolliList[Integer.parseInt(idSelected)][9], makgeolliList[Integer.parseInt(idSelected)][10]);

// TODO: if makgeolli on FavoriteTab = setSelected()

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        container.getContext(), R.style.MyTransparentBottomSheetDialogTheme);
                View bottomSheetView = inflater.inflate(R.layout.modal_bottom_sheet, container, false);

                TextView makgeolliName = (TextView) bottomSheetView.findViewById(R.id.makgeolliName);
                // To show rating on RatingBar
                makgeolliName.setText(makgeolliList[Integer.parseInt(idSelected)][0]);

                RatingBar ratingBarSweet = (RatingBar) bottomSheetView.findViewById(R.id.ratingBarSweet);
                // To show rating on RatingBar
                ratingBarSweet.setRating(Float.parseFloat(makgeolliList[Integer.parseInt(idSelected)][1]));

                RatingBar ratingBarAcidity = (RatingBar) bottomSheetView.findViewById(R.id.ratingBarAcidity);
                // To show rating on RatingBar
                ratingBarAcidity.setRating(Float.parseFloat(makgeolliList[Integer.parseInt(idSelected)][2]));

                RatingBar ratingBarTexture = (RatingBar) bottomSheetView.findViewById(R.id.ratingBarTexture);
                // To show rating on RatingBar
                ratingBarTexture.setRating(Float.parseFloat(makgeolliList[Integer.parseInt(idSelected)][3]));

                RatingBar ratingBarSparkling = (RatingBar) bottomSheetView.findViewById(R.id.ratingBarSparkling);
                // To show rating on RatingBar
                ratingBarSparkling.setRating(Float.parseFloat(makgeolliList[Integer.parseInt(idSelected)][4]));

                TextView ingredient = (TextView) bottomSheetView.findViewById(R.id.ingredientText);
                // To show rating on RatingBar
                ingredient.setText(makgeolliList[Integer.parseInt(idSelected)][9]);

                TextView alcoolPercent = (TextView) bottomSheetView.findViewById(R.id.percentText);
                // To show rating on RatingBar
                alcoolPercent.setText(makgeolliList[Integer.parseInt(idSelected)][8]);

                TextView localisation = (TextView) bottomSheetView.findViewById(R.id.localisationText);
                // To show rating on RatingBar
                localisation.setText(makgeolliList[Integer.parseInt(idSelected)][5]);

                TextView description = (TextView) bottomSheetView.findViewById(R.id.moreInfoText);
                // To show rating on RatingBar
                description.setText(makgeolliList[Integer.parseInt(idSelected)][10]);

                bottomSheetView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(container.getContext(), "Folder Clicked..", Toast.LENGTH_SHORT).show();
                        // dismiss closes the bottom sheet
                        bottomSheetDialog.dismiss();
                    }
                });

//_________________________________Favorite button___________________________________//
                final ImageButton favoritebtn = bottomSheetView.findViewById(R.id.favorisbtn);
                favoritebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(container.getContext(), "Saved as favorite..", Toast.LENGTH_SHORT).show();
                        // save as favorite
                        favoritebtn.setSelected(!favoritebtn.isSelected());
                        if( favoritebtn.isSelected()){
                            favoritebtn.setImageResource(android.R.drawable.btn_star_big_on);
                            favoriteTab.setVar(Integer.parseInt(idSelected),makgeolliList[Integer.parseInt(idSelected)]);
                        }else{
                            favoritebtn.setImageResource(android.R.drawable.btn_star_big_off);
                            // delete from database
                            favoriteTab.deleteFavorite(Integer.parseInt(idSelected));
                        }
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

            }
        });

        return view;
    }

    public Void getData(){
        InputStream inputStream = getResources().openRawResource(R.raw.makgeollidata);
        CSVFile csvFile = new CSVFile(inputStream);
        MainActivity.makgeolliList = csvFile.ReadFileInto2DArray();
        //Globals.getInstance().setMakgeolliList(makgeolliList);
        return null;
    }

    public void filterMarker(ChipGroup chipGroup, ArrayList<Marker> mMarkerArray, String[][] completeList){
        mMarkerArray.forEach(marker -> marker.setVisible(true));
        boolean isSweet = ((Chip) chipGroup.findViewById(R.id.chipSweet)).isChecked();
        boolean isSour = ((Chip) chipGroup.findViewById(R.id.chipSour)).isChecked();
        boolean isSparkling = ((Chip) chipGroup.findViewById(R.id.chipSparkling)).isChecked();
        boolean isNuts = ((Chip) chipGroup.findViewById(R.id.chipNuts)).isChecked();
        boolean isFruity = ((Chip) chipGroup.findViewById(R.id.chipFruity)).isChecked();

        for( Marker marker : mMarkerArray){
            if(isFruity){
                if (!completeList[Integer.parseInt(marker.getId().substring(1))][12].contains("Yes")) {
                    marker.setVisible(false);
                }
            }
            if(isNuts){
                if (!completeList[Integer.parseInt(marker.getId().substring(1))][13].contains("Yes")) {
                    marker.setVisible(false);
                }
            }
            if(isSparkling){
                if (Integer.parseInt(completeList[Integer.parseInt(marker.getId().substring(1))][4])<=3) {
                    marker.setVisible(false);
                }
            }
            if(isSour){
                if (Integer.parseInt(completeList[Integer.parseInt(marker.getId().substring(1))][2])<=3) {
                    marker.setVisible(false);
                }
            }
            if(isSweet){
                if (Integer.parseInt(completeList[Integer.parseInt(marker.getId().substring(1))][1])<=3) {
                    marker.setVisible(false);
                }
            }

        }


    }

    private void getDataFromAPI() {

        // creating a string variable for URL.
        String FILE_URL = "https://docs.google.com/spreadsheets/d/e/2PACX-1vSZvW6YRkTSqL5ZDTkDMG7S6xhi8J2ANRj2rGJmKFY99M8O-q2DRoleWNu_aRRiahHscoU76mVKCszt/pub?gid=0&single=true&output=csv";

        new DownloadData().execute(FILE_URL);
    }

    public void updateMap(){
        String[][] makgeolliList = MainActivity.makgeolliList;
        if(!mMarkerArray.isEmpty()) {
            mMarkerArray.clear();
            myMap.clear();
            
        }
        // boucle for to display the map with markers
        MarkerOptions marker;
        for (int i = 0; i < makgeolliList.length; i++) {
            if(makgeolliList[i][0]!=null) {
                // Get the coordonate of each marker
                double[] coords = {Double.parseDouble(makgeolliList[i][7]), Double.parseDouble(makgeolliList[i][6])};
                String[] info = makgeolliList[i];
                LatLng latLng = new LatLng(coords[1], coords[0]);

                String snippet = String.format("%s %s %s %s", info[1], info[2], info[3], info[4]);

                int height = 100;
                int width = 100;
                Bitmap b;
                boolean fruity = (info[12].contains("Yes"));
                boolean nuts = (info[13].contains("Yes"));
                if(fruity){
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.marker_pink);

                } else if (nuts) {
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.marker_orange);

                } else if (Integer.parseInt(info[4]) > 3) {
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.marker_green);

                }else{
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
                }
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                marker = new MarkerOptions()
                        .position(latLng)
                        .title(info[0])
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                Marker newMarker = myMap.addMarker(marker);
                mMarkerArray.add(newMarker);
            }
        }

    }

}

