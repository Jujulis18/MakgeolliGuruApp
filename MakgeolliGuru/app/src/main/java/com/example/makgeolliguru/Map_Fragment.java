package com.example.makgeolliguru;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.util.List;


public class Map_Fragment extends Fragment {

    public Map_Fragment() {
        // require a empty public constructor
    }

    private GoogleMap myMap;

    double[][] results = {{127, 35}, {129.0756416, 35.1795543}, {126.9779692, 37.566535}, {128.9779692, 36.566535}};
    String[][] infos = {{"Corn makgeolli", "127", "35", "2", "3", "3", "3", "rice, corn", "Jeollanamdo", "14%", "???", "Yes", "Link"}, {"Busan makgeolli", "129.0756416", "35.1795543", "1", "1", "4", "3", "rice", "Busan", "14%", "???", "No", "Link"}, {"Nuts makgeolli", "126.9779692", "37.566535", "2", "3", "3", "3", "rice, nuts", "Seoul", "14%", "???", "Yes", "Link"}, {"Daegu makgeolli","128.9779692", "36.566535", "2", "3", "3", "3", "rice, corn", "Daegu", "14%", "???", "Yes", "Link"}};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize view
        View view = inflater.inflate(R.layout.fragment_map_, container, false);

        // Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);

        // Get data
        InputStream inputStream = getResources().openRawResource(R.raw.makgeollidata);
        CSVFile csvFile = new CSVFile(inputStream);
        String[][] makgeolliList = csvFile.ReadFileInto2DArray("makgeollidata");


        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                myMap = googleMap;

                // Create Map center on South Korea
                LatLng SouthKorea = new LatLng(37.566535, 126.9779692);
                myMap.moveCamera(CameraUpdateFactory.newLatLng(SouthKorea));
                myMap.getUiSettings().setZoomControlsEnabled(true);
                myMap.getUiSettings().setMapToolbarEnabled(false);
                //myMap.moveCamera(CameraUpdateFactory.zoomTo(10));
                myMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(container.getContext()));

                // Get data
                //results = {"features":[{"type":"Feature","properties":{"mag":3,"place":"34km"},"geometry":{"type":"Point","coordinates":[127,35,8.7]} }]};


                // boucle for to display the map with markers
                MarkerOptions marker;
                for (int i = 0; i < results.length; i++) {

                    // Get the coordonate of each marker
                    double[] coords = results[i];
                    String[] info = infos[i];
                    LatLng latLng = new LatLng(coords[1], coords[0]);

                    String snippet = String.format("%s %s %s %s", info[3], info[4], info[5], info[6]);

                    int height = 100;
                    int width = 100;
                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                    marker = new MarkerOptions()
                            .position(latLng)
                            .title(info[0])
                            .snippet(snippet)
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                    Marker newMarker = myMap.addMarker(marker);
                    //newMarker.showInfoWindow();


                }

                // Set a listener for info window events.
                myMap.setOnInfoWindowClickListener(this::onInfoWindowClick);


            }
            // ______________________ Info window custom events _________________________//
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(container.getContext(), "Info window clicked",
                        Toast.LENGTH_SHORT).show();
                myMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(container.getContext()));

                // open modal bottom sheet (+learn more)
                // marker id format = "m[id]"
                // Remove the first character with substring
                String idSelected = marker.getId().substring(1);
                String information = String.format("Sweet: %s\n Acidity %s \nTexture %s\nSparkling %s\nFlavor/ingredients %s\nLocalisation %s\n percent alcohol %s\nArtisanal %s\n\n<h3>Comment </h3>", infos[Integer.parseInt(idSelected)][3], infos[Integer.parseInt(idSelected)][4], infos[Integer.parseInt(idSelected)][5], infos[Integer.parseInt(idSelected)][6], infos[Integer.parseInt(idSelected)][7], infos[Integer.parseInt(idSelected)][8], infos[Integer.parseInt(idSelected)][9], infos[Integer.parseInt(idSelected)][10]);


                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        container.getContext(), R.style.MyTransparentBottomSheetDialogTheme);
                View bottomSheetView = inflater.inflate(R.layout.modal_bottom_sheet, container, false);

                RatingBar ratingBarSweet = (RatingBar) bottomSheetView.findViewById(R.id.ratingBarSweet);
                // To show rating on RatingBar
                ratingBarSweet.setRating(Float.parseFloat(infos[Integer.parseInt(idSelected)][3]));

                RatingBar ratingBarAcidity = (RatingBar) bottomSheetView.findViewById(R.id.ratingBarAcidity);
                // To show rating on RatingBar
                ratingBarAcidity.setRating(Float.parseFloat(infos[Integer.parseInt(idSelected)][4]));

                RatingBar ratingBarTexture = (RatingBar) bottomSheetView.findViewById(R.id.ratingBarTexture);
                // To show rating on RatingBar
                ratingBarTexture.setRating(Float.parseFloat(infos[Integer.parseInt(idSelected)][5]));

                RatingBar ratingBarSparkling = (RatingBar) bottomSheetView.findViewById(R.id.ratingBarSparkling);
                // To show rating on RatingBar
                ratingBarSparkling.setRating(Float.parseFloat(infos[Integer.parseInt(idSelected)][6]));

                TextView ingredient = (TextView) bottomSheetView.findViewById(R.id.ingredientText);
                // To show rating on RatingBar
                ingredient.setText(infos[Integer.parseInt(idSelected)][7]);

                TextView alcoolPercent = (TextView) bottomSheetView.findViewById(R.id.percentText);
                // To show rating on RatingBar
                alcoolPercent.setText(infos[Integer.parseInt(idSelected)][9]);

                TextView localisation = (TextView) bottomSheetView.findViewById(R.id.localisationText);
                // To show rating on RatingBar
                localisation.setText(infos[Integer.parseInt(idSelected)][8]);

                /*bottomSheetView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(container.getContext(), "Folder Clicked..", Toast.LENGTH_SHORT).show();
                        // dismiss closes the bottom sheet
                        bottomSheetDialog.dismiss();
                    }
                });*/
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

            }
        });
        return view;
    }


}

