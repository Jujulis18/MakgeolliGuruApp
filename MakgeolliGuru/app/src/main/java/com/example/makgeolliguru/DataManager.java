package com.example.makgeolliguru;

import static android.content.Context.MODE_PRIVATE;

import static com.example.makgeolliguru.MainActivity.FAVORITE_LIST;
import static com.example.makgeolliguru.MainActivity.MAK_LIST;
import static com.example.makgeolliguru.MainActivity.SHARED_PREF;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class DataManager {
    private SharedPreferences prefs;
    private Context context;

    public DataManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
    }

    public String[][] loadMakgeolliList() {
        String makgeolliListString = prefs.getString(MAK_LIST, null);
        if (makgeolliListString == null) {
            makgeolliListString = loadDefaultData();
        }
        return new MakgeolliList(makgeolliListString).ReadFileInto2DArray();
    }

    public String[][] loadFavoriteList() {
        String favoriteListString = prefs.getString(FAVORITE_LIST, null);
        return favoriteListString != null ? new MakgeolliList(favoriteListString).ReadFileInto2DArray() : null;
    }

    private String loadDefaultData() {
        // Load default data
        InputStream inputStream = context.getResources().openRawResource(R.raw.makgeollidata);
        String text = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));
        prefs.edit().putString(MAK_LIST, text).apply();
        return text;
    }

    public void updateDataFromAPI() throws ExecutionException, InterruptedException {
        String FILE_URL = "https://docs.google.com/spreadsheets/d/e/2PACX-1vSZvW6YRkTSqL5ZDTkDMG7S6xhi8J2ANRj2rGJmKFY99M8O-q2DRoleWNu_aRRiahHscoU76mVKCszt/pub?gid=0&single=true&output=csv";
        String text = new DownloadData().execute(FILE_URL).get();
        prefs.edit().putString(MAK_LIST, text).apply();
    }
}

