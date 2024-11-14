package com.example.makgeolliguru;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.makgeolliguru.map.MapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    // Shared Preference data
    public static final String SHARED_PREF = "com.example.makgeolliguru.SHARED_PREF";
    public static final String MAK_LIST = "com.example.makgeolliguru.MAK_LIST";
    public static final String FAVORITE_LIST = "com.example.makgeolliguru.FAVORITE_LIST";
    public static final String ARTICLE_LIST = "com.example.makgeolliguru.ARTICLE_LIST";

    // Bottom navigation behavior
    BottomNavigationView bottomNavigationView;
    MapFragment mapFragment = new MapFragment();
    LearningFragment learningFragment = new LearningFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.print(FAVORITE_LIST);
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

    public void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fragment).commit();
    }
}
