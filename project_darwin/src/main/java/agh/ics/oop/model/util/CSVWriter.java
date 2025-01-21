package agh.ics.oop.model.util;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CSVWriter {

    private final String filepath;
    private final String separator;

    public CSVWriter(String filepath, String separator) {
        this.filepath = filepath;
        this.separator = separator;
        generateCSVBeggining();
    }

    public void write(String[] data) {
        try(FileWriter writer = new FileWriter(filepath, true)) {
            String toWrite = String.join(separator, data) + "\n";
            writer.write(toWrite);
        } catch(IOException ignored) {}
    }

    private void generateCSVBeggining()  {
        if(filepath == null || separator == null) return;
        try {
            PrintWriter writer = new PrintWriter(filepath);
            String data = "";
            data += "numberOfLiveAnimals" + separator;
            data += "numberOfPlants" + separator;
            data += "numberOfEmptyFields" + separator;
            data += "AverageEnergy" + separator;
            data += "AverageDaysOfLiving" + separator;
            data += "AverageChildrenNumber" + "\n";
            writer.print(data);
            writer.close();

        }
        catch(FileNotFoundException ignored) {}
    }
}