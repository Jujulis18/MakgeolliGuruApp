package com.myapp.makgeolliguru.tools;

import com.opencsv.CSVReader;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.io.InputStream;
import java.util.ArrayList;

public class CSVFile {
    InputStream inputStream;

    public CSVFile(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String[][] ReadFileInto2DArray() {
        List<String[]> records = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            records = csvReader.readAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (records.isEmpty()) {
            return new String[0][0];  // Return an empty array if no data
        }

        String[][] arrayToReturn = new String[records.size()][records.get(0).length];

        for (int i = 0; i < records.size(); i++) {
            arrayToReturn[i] = records.get(i);
        }

        return arrayToReturn;
    }
}
