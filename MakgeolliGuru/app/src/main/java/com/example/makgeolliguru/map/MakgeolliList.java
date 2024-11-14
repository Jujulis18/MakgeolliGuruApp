package com.example.makgeolliguru.map;

import java.util.Arrays;
import java.util.List;

public class MakgeolliList {

    private String makgeolliListString;

    public MakgeolliList(String listString) {
        this.makgeolliListString = listString;
    }

    public String[][] ReadFileInto2DArray() {
        String delimiter = ",";
        String[][] arrayToReturn;
        try {
            List<String> recordList = Arrays.asList(this.makgeolliListString.split("\n", -1));

            int recordCount = recordList.size();

            String[] firstLine = recordList.get(0).split(delimiter);

            int amountOfField = firstLine.length;

            arrayToReturn = new String[recordCount - 1][amountOfField];
            String[] data;
            int size = 0;
            for (int i = 0; i < recordCount; i++) {
                data = recordList.get(i).replace(",,", ",N/A,").split(delimiter);
                if (!data[0].contains("ame") && !data[0].contains("order")) {
                    if (data.length == amountOfField) {
                        for (int j = 0; j < data.length; j++) {
                            arrayToReturn[size][j] = data[j].strip();
                        }
                        size++;
                    }
                }
            }
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }

        return arrayToReturn;
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
