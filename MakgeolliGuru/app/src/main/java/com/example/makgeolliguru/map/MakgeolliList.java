package com.example.makgeolliguru.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public String addDataOnString(String[] newMakgeolli) {
        String info = Arrays.toString(newMakgeolli).replace("[", "").replace("]", "").concat("\n");
        this.makgeolliListString = String.format("%s %s", this.makgeolliListString, info);
        return this.makgeolliListString;
    }

    public String deleteDataFromString(String[] newMakgeolli) {
        String info = Arrays.toString(newMakgeolli).replace("[", "").replace("]", "").concat("\n");

        this.makgeolliListString = String.format("%s", this.makgeolliListString.replace(info, ""));

        return this.makgeolliListString;
    }

}
