package agh.ics.oop.model.util;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CSVWriter {

    private final String filepath;

    public CSVWriter(String filepath) {
        this.filepath = filepath;
        generateCSVBeggining();
    }

    public void write(String[] data) {
        try(FileWriter writer = new FileWriter(filepath, true)) {
            String toWrite = String.join(",", data) + "\n";
            writer.write(toWrite);
        } catch(IOException ignored) {}
    }

    private void generateCSVBeggining()  {
        if(filepath == null) return;
        try {
            PrintWriter writer = new PrintWriter(filepath);
            String data = "";
            data += "numberOfLiveAnimals" + ",";
            data += "numberOfPlants" + ",";
            data += "numberOfEmptyFields" + ",";
            data += "AverageEnergy" + ",";
            data += "AverageDaysOfLiving" + ",";
            data += "AverageChildrenNumber" + "\n";
            writer.print(data);
            writer.close();

        }
        catch(FileNotFoundException ignored) {}
    }
}