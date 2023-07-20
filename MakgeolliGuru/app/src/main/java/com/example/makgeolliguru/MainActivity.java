package com.example.makgeolliguru;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        // Create Map center on South Korea
        LatLng SouthKorea = new LatLng(37.566535, 126.9779692);
        myMap.moveCamera(CameraUpdateFactory.newLatLng(SouthKorea));
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setMapToolbarEnabled(false);
        //myMap.moveCamera(CameraUpdateFactory.zoomTo(10));


        // Get data
        //results = {"features":[{"type":"Feature","properties":{"mag":3,"place":"34km"},"geometry":{"type":"Point","coordinates":[127,35,8.7]} }]};

        double[][] results = {{127, 35},{129.0756416 , 35.1795543 },{126.9779692, 37.566535},{128.9779692, 36.566535}};
        String[][] infos = {{"127", "35", "2", "3", "3", "3","rice, corn", "Jeollanamdo", "14%", "???", "Yes", "Link" },{"129.0756416" , "35.1795543", "1", "1", "4", "3","rice", "Busan", "14%", "???", "No", "Link" },{"126.9779692", "37.566535", "2", "3", "3", "3","rice, nuts", "Seoul", "14%", "???", "Yes", "Link"},{"128.9779692", "36.566535", "2", "3", "3", "3","rice, corn", "Daegu", "14%", "???", "Yes", "Link"}};
        // boucle for to display the map with markers
        MarkerOptions marker;
        for (int i = 0; i < results.length; i++) {

            // Get the coordonate of each marker
            double[] coords = results[i];
            //double[] coords = {127, 35};
            String[] info = infos[i];

            LatLng latLng = new LatLng(coords[1], coords[0]);

            String contentString =
                    "<div class='info-window-content'>"+
                            "<h3>Sugar: </h3>"+info[2]+
                            "<h3>Acidity </h3>"+info[3]+
                            "<h3>Texture </h3>"+info[4]+
                            "<h3>Smell </h3>"+info[5]+
                            "<h3>Flavor/ingredients </h3>"+info[6]+
                            "<h3>Localisation </h3>"+info[7]+
                            "<h3>% alcohol </h3>"+info[8]+
                            "<h3>favorite food </h3>"+info[9]+
                            "<h3>Artisanal </h3>"+info[10]+
                            "<a href='"+info[11]+"' target='new'>link to kakao</a>"+
                            "</div>" +
                            "<div>"+
                            "<h3>Comment </h3>";

            marker = new MarkerOptions()
                    .position(latLng)
                    .title("Makgeolli name")
                    .snippet(contentString);

            Marker newMarker = myMap.addMarker(marker);
            //newMarker.showInfoWindow();


        }

        // Set a listener for info window events.
        myMap.setOnInfoWindowClickListener(this::onInfoWindowClick);

    }


    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();
    }
}
