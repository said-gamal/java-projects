package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class PyramidDao {
    private final List<Pyramid> pyramids;

    public PyramidDao() {
        pyramids = new ArrayList<>();
    }

    public List<Pyramid> readFromCSV(File csvFile) {
        List<String> lines = new ArrayList<String>();
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
            if (entries[7].equals("")) continue; // height
            pyramids.add(new Pyramid(entries[0], entries[2], entries[4], Double.parseDouble(entries[7]), entries[14]));
        }
        return pyramids;
    }

    public Pyramid addObject(String[] parms) {
        return new Pyramid(parms[0], parms[1], parms[2], Double.parseDouble(parms[3]), parms[4]);
    }
}
