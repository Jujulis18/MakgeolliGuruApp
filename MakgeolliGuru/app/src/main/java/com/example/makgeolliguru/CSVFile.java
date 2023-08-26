package com.example.makgeolliguru;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.FileReader;
import java.util.stream.Collectors;

public class CSVFile {
    InputStream inputStream;

    public CSVFile(InputStream inputStream){
        this.inputStream = inputStream;
    }

    /*public String[][] read(file){
        String[][] resultList = ReadFileInto2DArray(file);
        return resultList;
    }*/

    public  String[][] ReadFileInto2DArray() {
        List<String> recordList = new ArrayList<String>();

        String delimiter = ",";
        String currentLine;

        String[][] arrayToReturn;
        try {
            //FileReader fr = new FileReader(filepath);
            //BufferedReader br = new BufferedReader(fr);
            String text = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));


            recordList = Arrays.asList(text.split("\n", -1));
            /*for(String line : lineList){
                recordList.addAll(Arrays.asList(line.split(",", -1)));
            }*/

            int recordCount = recordList.size();

            String[] firstLine = recordList.get(0).split(delimiter);

            int amountOfField = firstLine.length;

            arrayToReturn = new String[recordCount-1][amountOfField];
            String[] data;

            for (int i = 1; i < recordCount; i++) {

                data = recordList.get(i).replace(",,",",N/A,").split(delimiter);
                if(data.length==amountOfField) {
                    for (int j = 0; j < data.length; j++) {
                        arrayToReturn[i - 1][j] = data[j];
                        //System.out.println(String.join(",",arrayToReturn[i][j]));
                    }
                }
            }

        } catch (Exception e) {
            System.out.print(e);
            return null;
        }

        return arrayToReturn;
    }
}