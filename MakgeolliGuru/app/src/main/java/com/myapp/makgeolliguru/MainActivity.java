package com.myapp.makgeolliguru;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.myapp.makgeolliguru.articles.LearningFragment;
import com.myapp.makgeolliguru.map.MapFragment;
import com.myapp.makgeolliguru.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    // Shared Preference data
    public static final String SHARED_PREF = "com.myapp.makgeolliguru.SHARED_PREF";
    public static final String MAK_LIST = "com.myapp.makgeolliguru.MAK_LIST";
    public static final String FAVORITE_LIST = "com.myapp.makgeolliguru.FAVORITE_LIST";
    public static final String SAVED_LIST = "com.myapp.makgeolliguru.SAVED_LIST";
    public static final String ARTICLE_LIST = "com.myapp.makgeolliguru.ARTICLE_LIST";
    public static final String MAK_PROFILE = "com.myapp.makgeolliguru.MAK_PROFILE";

    public static final String LANGUAGE = "com.myapp.makgeolliguru.LANGUAGE";

    public static final String FIRST_TIME = "com.myapp.makgeolliguru.FIRST_TIME";


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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

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
