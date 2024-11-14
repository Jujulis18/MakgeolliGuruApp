package com.example.makgeolliguru.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.ViewGroup;

import com.example.makgeolliguru.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapManager {

    private final ArrayList<Marker> mMarkerArray = new ArrayList<>();
    private ViewGroup container;

    public void initializeMap(GoogleMap googleMap, ViewGroup container, String[][] makgeolliListTab) {
        this.container = container;
        LatLng SouthKoreaPos = new LatLng(37.566535, 126.9779692);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SouthKoreaPos));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this.container.getContext()));

        addMarkers(Arrays.asList(makgeolliListTab), googleMap);
    }

    private void addMarkers(List<String[]> makgeolliListTab, GoogleMap googleMap) {
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

    public void filterMarkers(ChipGroup chipGroup, String[][] makgeolliListTab, String[][] favoriteTabs) {
        mMarkerArray.forEach(marker -> marker.setVisible(true));
        boolean isSweet = ((Chip) chipGroup.findViewById(R.id.chipSweet)).isChecked();
        boolean isSour = ((Chip) chipGroup.findViewById(R.id.chipSour)).isChecked();
        boolean isSparkling = ((Chip) chipGroup.findViewById(R.id.chipSparkling)).isChecked();
        boolean isNuts = ((Chip) chipGroup.findViewById(R.id.chipNuts)).isChecked();
        boolean isFruity = ((Chip) chipGroup.findViewById(R.id.chipFruity)).isChecked();
        boolean isFavorite = ((Chip) chipGroup.findViewById(R.id.favorite)).isChecked();

        for (Marker marker : mMarkerArray) {
            if (isFavorite) {

                Log.w("MakgeolliGuru", String.format("%d", favoriteTabs.length));
                boolean visible = false;
                for (String[] tab : favoriteTabs) {
                    Log.w("MakgeolliGuru", String.format("favoriteTab: %s", tab[0]));
                    Log.w("MakgeolliGuru", String.format("completeList: %s", makgeolliListTab[Integer.parseInt(marker.getId().substring(1))][0]));
                    if (!visible && !tab[0].contains(makgeolliListTab[Integer.parseInt(marker.getId().substring(1))][0])) {
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

