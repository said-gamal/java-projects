package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CityDao {
    private final List<City> cities;

    public CityDao() {
        cities = new ArrayList<>();
    }

    public List<City> readFromCSV(File csvFile) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(csvFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] entries = line.split(";");
            for (int j = 0; j < entries.length; j++) {
                entries[j] = entries[j].trim();
            }
            if (entries.length == 6 && !entries[4].equals(""))
                cities.add(new City(entries[0], entries[1], entries[2], entries[3], Double.parseDouble(entries[4]), entries[5]));
        }
        return cities;
    }

    public City addObject(String[] parms) {
        return new City(parms[0], parms[1], parms[2], parms[3], Double.parseDouble(parms[4]), parms[5]);
    }
}
