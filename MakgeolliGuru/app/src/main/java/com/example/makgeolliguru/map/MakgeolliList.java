package com.example.makgeolliguru.map;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MakgeolliList {

    private String makgeolliListString;

    public MakgeolliList(String listString) {
        this.makgeolliListString = listString;
    }

    public List<String[]> ReadFileInto2DArray() {
        List<String[]> records = new ArrayList<>();
        String csvPattern = "\"([^\"]*)\"|([^,]+)"; // Regex to correctly extract quoted & non-quoted values
        Pattern pattern = Pattern.compile(csvPattern);

        try {
            List<String> recordList = Arrays.asList(this.makgeolliListString.split("\n", -1));

            for (String line : recordList) {
                Matcher matcher = pattern.matcher(line);
                List<String> values = new ArrayList<>();

                while (matcher.find()) {
                    if (matcher.group(1) != null) {
                        values.add(matcher.group(1)); // Quoted value
                    } else {
                        values.add(matcher.group(2)); // Unquoted value
                    }
                }

                // Skip headers based on "order" or "name" in first column
                if (!values.isEmpty() && !values.get(0).toLowerCase().contains("order") && !values.get(0).toLowerCase().contains("name")) {
                    records.add(values.toArray(new String[0]));
                }
            }
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }

        return records;
    }

    public String addDataOnString(List<String> newMakgeolli) {

        String info = newMakgeolli.toString().replace("[", "").replace("]", "").concat("");


        this.makgeolliListString = this.makgeolliListString.trim();

        if (!this.makgeolliListString.endsWith("\n")) {
            this.makgeolliListString += "\n";
        }

        this.makgeolliListString += info;
        return this.makgeolliListString;
    }

    public String deleteDataFromString(String[] newMakgeolli) {

        Log.d("deleteDataFromString", "newMakgeolli: " + Arrays.toString(newMakgeolli));
        Log.d("deleteDataFromString", "makgeolliListString: " + this.makgeolliListString);

        String info = Arrays.toString(newMakgeolli).replace("[", "").replace("]", "").concat("\n");

        Log.d("deleteDataFromString", "info: " + info);
        this.makgeolliListString = String.format("%s", this.makgeolliListString.replace(info, ""));

        this.makgeolliListString = this.makgeolliListString.trim();
        if (!this.makgeolliListString.endsWith("\n")) {
            this.makgeolliListString += "\n";
        }

        Log.d("deleteDataFromString", "makgeolliListString: " + this.makgeolliListString);
        return this.makgeolliListString;
    }

}
