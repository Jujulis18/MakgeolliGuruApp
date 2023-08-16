package com.example.makgeolliguru;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;

public class CSVFile {
    InputStream inputStream;

    public CSVFile(InputStream inputStream){
        this.inputStream = inputStream;
    }

    /*public String[][] read(file){
        String[][] resultList = ReadFileInto2DArray(file);
        return resultList;
    }*/

    public static String[][] ReadFileInto2DArray(String filepath) {
        List<String> recordList = new ArrayList<String>();

        String delimiter = ",";
        String currentLine;

        String[][] arrayToReturn;
        try {
            FileReader fr = new FileReader(filepath);
            BufferedReader br = new BufferedReader(fr);

            /*while((currentLine - br.readLine()) != null){
                recordList.add(currentLine);
            }*/
            int recordCount = recordList.size();

            String[] firstLine = recordList.get(0).split(delimiter);

            int amountOfField = firstLine.length;

            arrayToReturn = new String[recordCount][amountOfField];
            String[] data;

            for (int i = 0; i < recordCount; i++) {
                data = recordList.get(i).split(delimiter);
                for (int j = 0; j < data.length; j++) {
                    arrayToReturn[i][j] = data[j];
                    System.out.println(String.join(",",arrayToReturn[i][j]));
                }
            }

        } catch (Exception e) {
            System.out.print(e);
            return null;
        }

        return arrayToReturn;
    }
}