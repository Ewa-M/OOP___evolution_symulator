package Project.gui;

import Project.maps.AbstractMap;
import Project.onmap.Animal;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class TrackAnimal {
    Stage window;
    GridPane grid;
    AbstractMap map;
    Label energyValue;
    Label children;
    Label descendants;
    Label isAlive;
    Label age;
    Animal animal;
    boolean lives = true;
    int beginningChildren;

    public TrackAnimal(Animal animal) {
        this.animal = animal;
        animal.isTracked = true;
        beginningChildren = animal.kids;
        this.map = animal.map;
        this.drawTracker();
    }


    public void drawTracker() {
        grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10)); //padding around layout
        grid.setVgap(8); //padding between cells
        grid.setHgap(10); //padding between cells

        grid.add(new Label("Genome:"), 0 ,0);
        grid.add(new Label(animal.genes.toString()), 1 ,0);

        isAlive = new Label("Energy:");
        grid.add(isAlive, 0 ,2);
        energyValue = new Label(String.valueOf(animal.energy));
        grid.add(energyValue, 1 ,2);

        grid.add(new Label("Children:"), 0 ,3);
        children = new Label("0");
        grid.add(children, 1 ,3);

        grid.add(new Label("Descendants:"), 0 ,4);
        descendants = new Label("0");
        grid.add(descendants, 1 ,4);



        window = new Stage();
        Scene sc = new Scene(grid, 500, 300);
        window.setScene(sc);
        window.show();


    }

    public void updateTracker() {
        children.setText(String.valueOf(animal.kids - beginningChildren));
        descendants.setText(String.valueOf(map.descendants));
        if (lives && animal.energy <= 0) {
            energyValue.setText(String.valueOf(animal.map.date));
            isAlive.setText("Died on");
            lives =false;
        } else if (lives) {
            energyValue.setText(String.valueOf(animal.energy));
        }
    }

    public void close() {
        window.close();
        for (Animal a : animal.map.animals) {
            a.isTracked = false;
        }
        map.descendants=0;
    }
}
