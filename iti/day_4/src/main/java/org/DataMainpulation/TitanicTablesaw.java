package org.DataMainpulation;

import java.io.IOException;

import joinery.DataFrame;
import tech.tablesaw.api.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TitanicTablesaw {
    public static void main(String[] args) {
        String filePath = "src/main/resources/files/titanic.csv";

        try {
            // load data from the CSV file
            Table titanicTable = Table.read().csv(filePath);

            // print dataFrame summary
            System.out.println ("Press any key to print the summary and the structure of the Titanic table");
            System.in.read();
            System.out.println (titanicTable.structure());
            System.out.println (titanicTable.summary());
            System.out.println ("=====================================================================================");

            // create 2 DataFrames from the titanicTable object
            Table dataFrame1 = titanicTable.select("name","pclass","survived","sex");
            Table dataFrame2 = titanicTable.select("name","age", "fare", "ticket");

            // print dataFrame1 summary
            System.out.println ("Press any key to print the head of the dataFrame1");
            System.in.read();
            System.out.println (dataFrame1.first(10));
            System.out.println ("=====================================================================================");

            // print dataFrame2 summary
            System.out.println ("Press any key to print the head of the dataFrame2");
            System.in.read();
            System.out.println (dataFrame2.first(10));
            System.out.println ("=====================================================================================");


            // Join 2 DataFrame Objects
            Table joinedDataFrame = dataFrame1.joinOn("name").inner(dataFrame2);

            // print the joinedDataFrame summary
            System.out.println ("Press any key to print the head of the joinedDataFrame");
            System.in.read();
            System.out.println (joinedDataFrame.first(10));
            System.out.println ("=====================================================================================");

            // add a column to the dataFrame
            Table newTable = mapTextColumnToNumber(titanicTable, "Sex", "Gender");

            // print the newTable head
            System.out.println ("Press any key to print the head of the newTable after adding Gender numeric column");
            System.in.read();
            System.out.println (newTable.first(10));
            System.out.println ("=====================================================================================");


        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    // map text column to numeric values
    public static Table mapTextColumnToNumber(Table table, String columnName, String newColumnName) {
        NumberColumn mappedColumn;
        StringColumn column = (StringColumn) table.column (columnName);
        List<Number> mappedValues = new ArrayList<> ();
        List<String> distinctValues = column.asList().stream().distinct().collect(Collectors.toList());
        Map<String,Integer> distinctMap = new LinkedHashMap<>();
        int i = 0;
        // store every distinct record with unique number
        for (String distinctValue:distinctValues){
            distinctMap.put(distinctValue, i++);
        }
        // map every value in the column to its unique number
        for (String value : column.asList()) {
            mappedValues.add(distinctMap.get(value));
        }
        mappedColumn = DoubleColumn.create (newColumnName, mappedValues);
        table.addColumns (mappedColumn);
        return table;
    }
}