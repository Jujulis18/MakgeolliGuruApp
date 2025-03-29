package com.example.makgeolliguru.tools;

import static android.content.Context.MODE_PRIVATE;
import static com.example.makgeolliguru.MainActivity.MAK_LIST;
import static com.example.makgeolliguru.MainActivity.SHARED_PREF;

import static java.security.AccessController.getContext;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


public class DownloadData extends AsyncTask<String, Void, String> {
    private final DownloadCallback callback;

    public DownloadData(DownloadCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... urls) {
        // Implement the download logic here
        // Return the downloaded data as a String
        String fileUrl = urls[0];
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return content.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            callback.onDataDownloaded(result);
        } else {
            callback.onError(new Exception("Failed to download data"));
        }
    }
}
