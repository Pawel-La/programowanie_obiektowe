package agh.ics.oop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ToFileWriter {
    private int day, numOfAnimals, numOfGrasses, numOfFreeSpots, averageEnergyForLivingAnimals, averageAgeOfDeath;
    private int numOfFile;
    private String mostPopularGenes;
    private final List<String[]> dataLines = new ArrayList<>();
    public void setNumOfFile(int numOfFile){
        this.numOfFile = numOfFile;
    }
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

    /**
     * @param data - line of data to convert
     * @return converted line of data as CSV
     */
    private String convertToCSV(String[] data) {
        return String.join(",", data);
    }

    /**
     * saves information saved in dataLines to csv file
     * @throws FileNotFoundException if csv file doesn't exist
     */
    private void saveInfoToCSV() throws FileNotFoundException {
        File csvOutputFile = new File("src/main/resources/info"+numOfFile+".csv");
        if (!csvOutputFile.exists()){
            try {
                csvOutputFile.createNewFile();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
    }

    /**
     * Sets titles of columns in csv file
     * @throws FileNotFoundException if csv file doesn't exist
     */
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

    /**
     * save daily info to a csv file
     * @throws FileNotFoundException if csv file doesn't exist
     */
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
