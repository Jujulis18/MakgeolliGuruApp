package com.example.makgeolliguru;

import android.os.AsyncTask;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadData extends AsyncTask<String,Void,String[][]> {

    //private Context context;

    /*public DownloadData() {

    }*/

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String[][] doInBackground(String... string) {

        try {
            URL url = new URL(string[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            CSVFile csvFile = new CSVFile(inputStream);
            MainActivity.makgeolliList = csvFile.ReadFileInto2DArray();
            String[][] test = csvFile.ReadFileInto2DArray();
            return test;
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

}
