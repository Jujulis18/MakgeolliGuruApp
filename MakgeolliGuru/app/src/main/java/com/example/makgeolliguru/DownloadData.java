package com.example.makgeolliguru;

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


public class DownloadData extends AsyncTask<String,Void,String> {

    //private Context context;

    /*public DownloadData() {

    }*/

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... string) {

        try {
            URL url = new URL(string[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            //CSVFile csvFile = new CSVFile(inputStream);
            //MainActivity.makgeolliList = csvFile.ReadFileInto2DArray();
            //String[][] test = csvFile.ReadFileInto2DArray();
            String text = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            return text;
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

}
