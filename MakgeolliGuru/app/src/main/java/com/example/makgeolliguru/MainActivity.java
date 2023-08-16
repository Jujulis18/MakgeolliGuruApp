package com.example.makgeolliguru;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/*public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

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
        myMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MainActivity.this));

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

            /*String snippet = "<b>Sugar: </b>"+info[2]+ "\n"+
                            "Acidity "+info[3]+ "\n"+
                            "Texture "+info[4]+ "\n"+
                            "Smell "+info[5]+ "\n"+
                            "Flavor/ingredients "+info[6]+ "\n"+
                            "Localisation "+info[7]+ "\n"+
                            "% alcohol "+info[8]+ "\n"+
                            "favorite food "+info[9]+ "\n"+
                            "Artisanal "+info[10]+ "\n"+
                            "<a href='"+info[11]+"' target='new'>link to kakao</a>"+ "\n"+"\n"+
                            "<h3>Comment </h3>";*/

 /*           String snippet = String.format("Sugar: %s\n Acidity %s \nTexture %s\nSmell %s\nFlavor/ingredients %s\nLocalisation %s\n percent alcohol %s\nfavorite food %s\nArtisanal %s\n\n<h3>Comment </h3>", info[2], info[3], info[4], info[5], info[6], info[7], info[8], info[9], info[10]);

            int height = 100;
            int width = 100;
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

            marker = new MarkerOptions()
                    .position(latLng)
                    .title("Makgeolli name")
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

            Marker newMarker = myMap.addMarker(marker);
            //newMarker.showInfoWindow();


        }

        // Set a listener for info window events.
        myMap.setOnInfoWindowClickListener(this::onInfoWindowClick);

    }


    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();
        myMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MainActivity.this));

    }
}*/


import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    Map_Fragment mapFragment = new Map_Fragment();
    Learning_Fragment learningFragment = new Learning_Fragment();
    Profile_Fragment profileFragment = new Profile_Fragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.map);
        setCurrentFragment(mapFragment);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.mapNav:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, mapFragment)
                        .commit();
                return true;
            case R.id.learningNav:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, learningFragment)
                        .commit();
                return true;
            case R.id.profileNav:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, profileFragment)
                        .commit();
                return true;
        }
        return false;
    }
    void setCurrentFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fragment).commit();
    }

}
