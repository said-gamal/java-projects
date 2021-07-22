package org.DataMainpulation;

import joinery.DataFrame;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TitanicJoinery {

    public static void main(String args[]){
        String filePath = "src/main/resources/files/titanic.csv";

        try {
            // load Data from the CSV file
            DataFrame<Object> dataFrame = DataFrame.readCsv(filePath);

            // print dataFrame summary
            System.out.println ("Press any key to print the summary and the head of the Titanic dataFrame");
            System.in.read();
            System.out.println (dataFrame.head());
            System.out.println (dataFrame.describe());
            System.out.println ("=====================================================================================");


            // create 2 DataFrames from the dataFrame object
            DataFrame<Object> dataFrame1 = dataFrame.retain("name","pclass","survived","sex");
            DataFrame<Object> dataFrame2 = dataFrame.retain("name","age", "fare", "ticket");

            // print dataFrame1 summary
            System.out.println ("Press any key to print the head of the dataFrame1");
            System.in.read();
            System.out.println (dataFrame1.head());
            System.out.println ("=====================================================================================");

            // print dataFrame2 summary
            System.out.println ("Press any key to print the head of the dataFrame2");
            System.in.read();
            System.out.println (dataFrame2.head());
            System.out.println ("=====================================================================================");

            // join 2 DataFrames
            DataFrame<Object> joinedDataFrame = dataFrame1.join(dataFrame2);

            // print the joinedDataFrame summary
            System.out.println ("Press any key to print the head of the joinedDataFrame");
            System.in.read();
            System.out.println (joinedDataFrame.head());
            System.out.println ("=====================================================================================");

            // add a column to the dataFrame
            DataFrame<Object> newDataFrame = mapTextColumnToNumber(dataFrame, 3, "Gender");

            // print the newDataFrame summary
            System.out.println ("Press any key to print the head of the newDataFrame after adding Gender numeric column");
            System.in.read();
            System.out.println (newDataFrame.head());
            System.out.println ("=====================================================================================");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // map text column to numeric values
    public static DataFrame<Object> mapTextColumnToNumber(DataFrame<Object> dataFrame, Integer columnIndex, String newColumnName) {
        List<Integer> mappedValues = new ArrayList<> ();
        List<String> column = dataFrame.cast(String.class).col(columnIndex);
        List<String> distinctValues = column.stream().distinct().collect(Collectors.toList());
        Map<String,Integer> distinctMap = new LinkedHashMap<>();
        int i = 0;
        // store every distinct record with unique number
        for (String distinctValue:distinctValues){
            distinctMap.put(distinctValue, i++);
        }
        // map every value in the column to its unique number
        for (String value : column) {
            mappedValues.add(distinctMap.get(value));
        }
        // add the new column
        dataFrame.add(newColumnName, new ArrayList<>(mappedValues));
        return dataFrame;
    }

}