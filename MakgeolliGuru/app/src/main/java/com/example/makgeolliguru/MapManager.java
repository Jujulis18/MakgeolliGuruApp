package com.example.makgeolliguru;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;

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

import java.util.ArrayList;

public class MapManager {

    private Context context;
    private GoogleMap myMap;
    private ArrayList<Marker> mMarkerArray = new ArrayList<>();

    public void initializeMap( GoogleMap googleMap, Context context, String[][] makgeolliListTab) {
        this.context = context;
        /*supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {*/
                this.myMap = googleMap;
                LatLng SouthKorea = new LatLng(37.566535, 126.9779692);
                myMap.moveCamera(CameraUpdateFactory.newLatLng(SouthKorea));
                myMap.getUiSettings().setZoomControlsEnabled(true);
                myMap.getUiSettings().setMapToolbarEnabled(false);
                myMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(context));

                addMarkers(makgeolliListTab);
           /* }

        });*/
    }

    private void addMarkers(String[][] makgeolliListTab) {
        for (String[] info : makgeolliListTab) {
            if (info[0] != null) {
                try {
                    double lat = Double.parseDouble(info[7]);
                    double lng = Double.parseDouble(info[6]);

                    LatLng latLng = new LatLng(lat, lng);
                    String snippet = String.format("%s %s %s %s", info[1], info[2], info[3], info[4]);
                    Bitmap smallMarker = getMarkerBitmap(info);

                    MarkerOptions markerOptions = new MarkerOptions()
                                .position(latLng)
                                .title(info[0])
                                .snippet(snippet)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                    Marker newMarker = myMap.addMarker(markerOptions);

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

    private Bitmap getMarkerBitmap(String[] info) {
        int height = 100;
        int width = 100;
        Bitmap b=null;
        try {
            int resourceId=-1;
            if (info[12].contains("Yes")) {
                resourceId = R.drawable.marker_pink;
            } else if (info[13].contains("Yes")) {
                resourceId = R.drawable.marker_orange;
            } else if (Integer.parseInt(info[4]) >= 3) {
                resourceId = R.drawable.marker_green;
            } else {
                resourceId = R.drawable.marker;
            }
            b = BitmapFactory.decodeResource(this.context.getResources(), resourceId);

            if (b != null) {
                b = Bitmap.createScaledBitmap(b, width, height, false);
            } else {
                Log.e("MapManager", "Failed to decode resource for: " + info[0]);
            }
        } catch (Exception e) {
            Log.e("MapManager", "Error loading bitmap for: " + info[0], e);
        }
        return b;
    }



    public void filterMarkers(ChipGroup chipGroup, String[][] makgeolliListTab, String[][] favoriteTab) {
        mMarkerArray.forEach(marker -> marker.setVisible(true));
        boolean isSweet = ((Chip) chipGroup.findViewById(R.id.chipSweet)).isChecked();
        boolean isSour = ((Chip) chipGroup.findViewById(R.id.chipSour)).isChecked();
        boolean isSparkling = ((Chip) chipGroup.findViewById(R.id.chipSparkling)).isChecked();
        boolean isNuts = ((Chip) chipGroup.findViewById(R.id.chipNuts)).isChecked();
        boolean isFruity = ((Chip) chipGroup.findViewById(R.id.chipFruity)).isChecked();
        boolean isFavorite = ((Chip) chipGroup.findViewById(R.id.favorite)).isChecked();

        for (Marker marker : mMarkerArray) {
            if (isFavorite) {

                Log.w("MakgeolliGuru", String.format("%d", favoriteTab.length));
                boolean visible = false;
                for (int i = 0; i < favoriteTab.length; i++) {
                    Log.w("MakgeolliGuru", String.format("favoriteTab: %s", favoriteTab[i][0]));
                    Log.w("MakgeolliGuru", String.format("completeList: %s", makgeolliListTab[Integer.parseInt(marker.getId().substring(1))][0]));
                    if (!visible && !favoriteTab[i][0].contains(makgeolliListTab[Integer.parseInt(marker.getId().substring(1))][0])) {
                        marker.setVisible(false);
                    } else {
                        Log.w("MakgeolliGuru", "visible");
                        visible = true;
                        marker.setVisible(true);
                    }
                }
            }
            if (isFruity) {
                if (!makgeolliListTab[Integer.parseInt(marker.getId().substring(1))][12].contains("Yes")) {
                    marker.setVisible(false);
                }
            }
            if (isNuts) {
                if (!makgeolliListTab[Integer.parseInt(marker.getId().substring(1))][13].contains("Yes")) {
                    marker.setVisible(false);
                }
            }
            if (isSparkling) {
                if (Integer.parseInt(makgeolliListTab[Integer.parseInt(marker.getId().substring(1))][4]) <= 3) {
                    marker.setVisible(false);
                }
            }
            if (isSour) {
                if (Integer.parseInt(makgeolliListTab[Integer.parseInt(marker.getId().substring(1))][2]) <= 3) {
                    marker.setVisible(false);
                }
            }
            if (isSweet) {
                if (Integer.parseInt(makgeolliListTab[Integer.parseInt(marker.getId().substring(1))][1]) <= 3) {
                    marker.setVisible(false);
                }
            }


        }
    }
}

