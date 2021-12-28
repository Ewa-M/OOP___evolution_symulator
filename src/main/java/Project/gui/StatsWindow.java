package Project.gui;

import Project.maps.AbstractMap;
import Project.onmap.Animal;
import Project.onmap.Genome;
import javafx.geometry.Insets;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StatsWindow {
    Stage window;
    LineChart<String, Number> chart;
    AbstractMap map;
    XYChart.Series<String, Number> nOfAnimals;
    XYChart.Series<String, Number> nOfPlants;
    XYChart.Series<String, Number> avgEnergy;
    XYChart.Series<String, Number> avgLifeLenght;
    XYChart.Series<String, Number> avgKids;
    BufferedWriter writer;
    Label genes;
    Label test;
    List<int[]> statisticsHistory = new ArrayList<>();

    public StatsWindow(AbstractMap map) {
        this.map = map;
        chart = drawChart();

        File file = new File(map.title +".txt");
        try
        {   writer = new BufferedWriter(new FileWriter(file));
            writer.write(map.title);
            writer.newLine();
            writer.newLine();
            writer.write("age, number of animals, number of plants, average energy, average life lenght, average number of children");
            writer.newLine();
            } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public LineChart<String, Number> drawChart() {
        var xAxis = new CategoryAxis();
        xAxis.setLabel("age");

        var yAxis = new NumberAxis();
        yAxis.setLabel("numbers");

        var lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(map.title);

        nOfAnimals = new XYChart.Series<String, Number>();
        nOfAnimals.setName("number of animals");

        nOfPlants = new XYChart.Series<String, Number>();
        nOfPlants.setName("number of plants");

        avgEnergy = new XYChart.Series<String, Number>();
        avgEnergy.setName("average energy");

        avgLifeLenght = new XYChart.Series<String, Number>();
        avgLifeLenght.setName("average life lenght");

        avgKids = new XYChart.Series<String, Number>();
        avgKids.setName("average descendants");

        lineChart.setCreateSymbols(false);
        lineChart.getData().add(nOfPlants);
        lineChart.getData().add(nOfAnimals);
        lineChart.getData().add(avgEnergy);
        lineChart.getData().add(avgLifeLenght);
        lineChart.getData().add(avgKids);

        return lineChart;
    }

    public VBox drawStats() {


        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10,10,10,10)); //padding around layout
        gridPane.setVgap(8); //padding between cells
        gridPane.setHgap(10); //padding between cells
        VBox vbox = new VBox(chart, gridPane);
        gridPane.add(new Label("dominant genes:"), 0 ,0);
        genes = new Label("");
        gridPane.add(genes, 0 ,1);
        test = new Label("");
        gridPane.add(test, 0 ,2);
        vbox.setMinWidth(300.0);
        return vbox;
    }



    public void updateStats() {
        genes.setText(Genome.getDominantString(map));
        nOfAnimals.getData().add(new XYChart.Data<>(String.valueOf(map.date), map.animals.size()));
        nOfPlants.getData().add(new XYChart.Data<>(String.valueOf(map.date), map.countPlants));

        if (map.deadNumber >0) avgLifeLenght.getData().add(new XYChart.Data<>(String.valueOf(map.date), map.deadAge/map.deadNumber));

        int e = 0; int k = 0;
        for (Animal a : map.animals){ e += a.energy;  k+=a.kids;}
        if (!map.animals.isEmpty()) avgKids.getData().add(new XYChart.Data<>(String.valueOf(map.date), k/map.animals.size()));
        if (!map.animals.isEmpty()) avgEnergy.getData().add(new XYChart.Data<>(String.valueOf(map.date), e/map.animals.size()));

        if (nOfAnimals.getData().size() > 100) {
            nOfAnimals.getData().remove(0);
            nOfPlants.getData().remove(0);
            avgEnergy.getData().remove(0);
        }
        if (avgKids.getData().size() > 100) avgKids.getData().remove(0);
        if (avgLifeLenght.getData().size() > 100) avgLifeLenght.getData().remove(0);

        if(map.deadNumber > 0) {
            statisticsHistory.add(new int[]{map.date, map.animals.size(), map.countPlants, e/map.animals.size(), map.deadAge/map.deadNumber, k/map.animals.size()});
        } else {
            statisticsHistory.add(new int[]{map.date, map.animals.size(), map.countPlants, e/map.animals.size(), -1, k/map.animals.size()});
        }
    }

    public void saveToFile() {

        double a=0;
        double p=0;
        double e=0;
        double l=0;
        double c=0;
        double d=0;
        String s;

        for (int[] i : statisticsHistory) {
            a += i[1];
            p += i[2];
            e += i[3];
            c += i[5];

            if (i[4] == -1) { s = "-";}
            else {
                l += i[4];
                d++;
                s = String.valueOf(i[4]);
            }

            try {
                writer.write(i[0] + ", "
                        + i[1]  + ", "
                        + i[2] + ", "
                        + i[3] + ","
                        +s + ", "
                        +i[5]
                );
                writer.newLine();

            } catch (IOException exception) {
                System.out.println("An error occurred.");
            }
        }

        double x = statisticsHistory.size();
        try {
            writer.newLine();
            writer.write("averages, "
                    + a/x  + ", "
                    + p/x + ", "
                    + e/x + ","
                    + l/d + ", "
                    + c/x
            );
            writer.newLine();
            writer.newLine();
        } catch (IOException exception) {
            System.out.println("An error occurred.");
        }
        statisticsHistory.clear();
    }
}
