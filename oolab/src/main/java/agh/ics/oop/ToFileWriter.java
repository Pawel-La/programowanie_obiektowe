package agh.ics.oop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ToFileWriter {
    private int day, numOfAnimals, numOfGrasses, numOfFreeSpots, averageEnergyForLivingAnimals, averageAgeOfDeath;
    private String mostPopularGenes;
    private final List<String[]> dataLines = new ArrayList<>();

    public void setDay(int day){
        this.day = day;
    }
    public void setNumOfAnimals(int numOfAnimals) {
        this.numOfAnimals = numOfAnimals;
    }
    public void setNumOfGrasses(int numOfGrasses) {
        this.numOfGrasses = numOfGrasses;
    }
    public void setNumOfFreeSpots(int numOfFreeSpots) {
        this.numOfFreeSpots = numOfFreeSpots;
    }
    public void setAverageEnergyForLivingAnimals(int averageEnergyForLivingAnimals) {
        this.averageEnergyForLivingAnimals = averageEnergyForLivingAnimals;
    }
    public void setAverageAgeOfDeath(int averageAgeOfDeath) {
        this.averageAgeOfDeath = averageAgeOfDeath;
    }
    public void setMostPopularGenes(String mostPopularGenes) {
        this.mostPopularGenes = mostPopularGenes;
    }

    private String convertToCSV(String[] data) {
        return String.join(",", data);
    }
    private void saveInfoToCSV() throws FileNotFoundException {
        File csvOutputFile = new File("src/main/resources/info.csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
    }
    public void setTitles() throws FileNotFoundException {
        dataLines.add(new String[]{
                "DZIEN",
                "LICZBA ZWIERZAT",
                "LICZBA ROSLIN",
                "LICZBA WOLNYCH POL",
                "NAJPOPULARNIEJSZY GENOM",
                "SREDNIA ENERGIA DLA ZYJACYCH ZWIERZAT",
                "SREDNIA DLUGOSC ZYCIA DLA ZMARLYCH ZWIERZAT"
        });
        saveInfoToCSV();
    }


    public void saveDailyInfo() throws FileNotFoundException {
        dataLines.add(new String[]
                {
                        Integer.toString(day),
                        Integer.toString(numOfAnimals),
                        Integer.toString(numOfGrasses),
                        Integer.toString(numOfFreeSpots),
                        mostPopularGenes,
                        Integer.toString(averageEnergyForLivingAnimals),
                        Integer.toString(averageAgeOfDeath)
                });
        saveInfoToCSV();
    }
}
