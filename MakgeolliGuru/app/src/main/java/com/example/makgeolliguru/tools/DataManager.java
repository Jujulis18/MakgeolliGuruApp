package com.example.makgeolliguru.tools;

import static android.content.Context.MODE_PRIVATE;

import static com.example.makgeolliguru.MainActivity.ARTICLE_LIST;
import static com.example.makgeolliguru.MainActivity.FAVORITE_LIST;
import static com.example.makgeolliguru.MainActivity.MAK_LIST;
import static com.example.makgeolliguru.MainActivity.SHARED_PREF;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.makgeolliguru.R;
import com.example.makgeolliguru.articles.LearningFragment;
import com.example.makgeolliguru.map.MakgeolliList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class DataManager {
    private static SharedPreferences prefs;
    private static Context context;

    public DataManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
    }

    public List<String[]> loadMakgeolliList() {
        String makgeolliListString = prefs.getString(MAK_LIST, null);
        if (makgeolliListString == null) {
            makgeolliListString = loadDefaultData();
        }
        return new MakgeolliList(makgeolliListString).ReadFileInto2DArray();
    }

    public List<String[]> loadFavoriteList() {
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

    public void updateDataFromAPI() {
        String FILE_URL = "https://docs.google.com/spreadsheets/d/e/2PACX-1vSZvW6YRkTSqL5ZDTkDMG7S6xhi8J2ANRj2rGJmKFY99M8O-q2DRoleWNu_aRRiahHscoU76mVKCszt/pub?gid=0&single=true&output=csv";

        new DownloadData(new DownloadCallback() {
            @Override
            public void onDataDownloaded(String data) {
                prefs.edit().putString(MAK_LIST, data).apply();

                // Parse the new data
                // List<MapItem> mapItems = parseCSVData(data);

                // Update the map with the new data
                // updateMapWithNewData(mapItems);

                // Notify the user
                Toast.makeText(context, "Data refreshed successfully!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                // Handle error
                Toast.makeText(context, "Failed to refresh map data.", Toast.LENGTH_SHORT).show();
            }
        }).execute(FILE_URL);
    }

    public void updateArticleDataFromAPI(final LearningFragment.DataUpdateCallback callback) {
        String FILE_URL = "https://docs.google.com/spreadsheets/d/e/2PACX-1vSZvW6YRkTSqL5ZDTkDMG7S6xhi8J2ANRj2rGJmKFY99M8O-q2DRoleWNu_aRRiahHscoU76mVKCszt/pub?gid=1617702893&single=true&output=csv";

        new DownloadData(new DownloadCallback() {
            @Override
            public void onDataDownloaded(String data) {
                if (callback != null) {
                    callback.onUpdateSuccess(data);
                }
            }

            @Override
            public void onError(Exception e) {
                if (callback != null) {
                    callback.onUpdateFailure(e);
                }
            }
        }).execute(FILE_URL);
    }

}

// Define the DownloadData AsyncTask with a callback

// Define the callback interface
interface DownloadCallback {
    void onDataDownloaded(String data);
    void onError(Exception e);
}

