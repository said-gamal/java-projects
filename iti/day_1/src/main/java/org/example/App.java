package org.example;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class App 
{
    public static void main(String[] args) throws IOException {
        String path = "src/main/resources/pyramids.csv";
        File pyramidsCsvFile = new File(path);
        PyramidDao pyramidDao = new PyramidDao();
        List<Pyramid> pyramids = pyramidDao.readFromCSV(pyramidsCsvFile);
        List<Double> pyramidsHeights ;

        System.out.println("press any key to print all pyramids");
        System.in.read();
        for (Pyramid pyramid : pyramids) {
            System.out.println(pyramid);
        }
        System.out.println("*******************************************************************************************");

        System.out.println("press any key to print 1st, 2nd and 3rd quartiles of pyramids heights");
        System.in.read();
        pyramidsHeights = pyramids.stream()
                .map(Pyramid::getHeight)
                .sorted()
                .collect(Collectors.toList());
        int midIndex = pyramidsHeights.size() / 2;
        System.out.println("First quartile: " + median(pyramidsHeights.subList(0, midIndex)));
        System.out.println("Second quartile (median): " + median(pyramidsHeights));
        System.out.println("Third quartile: " + median(pyramidsHeights.subList(midIndex, pyramidsHeights.size())));
    }


    static double median(List<Double> list) {
        int midIndex = list.size() / 2;
        if (list.size() % 2 == 0) {
            return (list.get(midIndex) + list.get(midIndex + 1)) / 2;
        } else {
            return list.get(midIndex);
        }
    }
}
