package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CountryDao {
    private final List<Country> countries;

    public CountryDao() {
        countries = new ArrayList<>();
    }

    public List<Country> readFromCSV(File csvFile) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(csvFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] entries = line.split(",");
            for (int j = 0; j < entries.length; j++) {
                entries[j] = entries[j].trim();
            }
            countries.add(new Country(entries[0], entries[1]));
        }
        return countries;
    }

    public Country addObject(String[] parms) {
        return new Country(parms[0], parms[1]);
    }
}
