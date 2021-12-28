package Project.gui;

import Project.maps.AbstractMap;
import Project.maps.SingleSquare;
import Project.onmap.Animal;
import Project.onmap.Genome;
import Project.simulation.Simulation;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class MapWindow {
    GridPane grid;
    AbstractMap map;
    Thread t;
    Boolean paused = false;
    TrackAnimal tracker;
    StatsWindow statistics;

    public MapWindow(AbstractMap map) {
        this.map = map;
        this.grid = new GridPane();
        this.statistics = new StatsWindow(map);
        this.createMap();
        Simulation simulation2 = new Simulation(map);
        t = new Thread(simulation2);
        t.setDaemon(true);
        t.start();
    }

    public void createMap() {
        RowConstraints y = new RowConstraints();
        y.setPercentHeight(100d / map.height);

        for (int i=0; i<map.height; i++) {
            this.grid.getRowConstraints().add(y);
        }

        ColumnConstraints x = new ColumnConstraints();
        x.setPercentWidth(100d / map.width);

        for (int i=0; i<map.width; i++) {
            this.grid.getColumnConstraints().add(x);
        }

        StackPane jungle = new StackPane();
        jungle.setBackground(new Background(new BackgroundFill(Color.YELLOWGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        this.grid.add(jungle, map.lowerLeftJungle.x, map.lowerLeftJungle.y, map.upperRightJungle.x - map.lowerLeftJungle.x+1,map.upperRightJungle.y - map.lowerLeftJungle.y+1);
        this.grid.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));

        Button pause = new Button();
        pause.setText("| |");
        pause.setOnAction(event -> {
            if (paused) t.resume();
            else t.suspend();
            paused = !paused;
        });

        Button genes = new Button();
        genes.setText("Show all with dominant gene");
        genes.setOnAction(event -> {
            if (paused) {
                Genome g = Genome.getDominant(map);
                for (Animal a : map.animals) {
                    if (!a.genes.equals(g)) continue;
                    Button b = new Button();
                    b.setStyle("-fx-background-color: rgb(0, 0, 0)");
                    b.setPrefSize(200, 100);
                    b. setMinSize(0,0);
                    this.grid.add(b, a.getPosition().x, a.getPosition().y);
                        b.setOnAction(e -> {
                            if (paused) newTracker(a);
                        });
                }
            }
        });

        Button save = new Button();
        save.setText("save to file");
        save.setOnAction(event -> {
            if (paused) statistics.saveToFile();
        });

        HBox controls = new HBox(pause, genes,  save);

        VBox vbox = new VBox(controls, statistics.drawStats());
        Stage stage = new Stage();
        HBox hbox = new HBox(grid, vbox);
        Scene sc = new Scene(hbox, 700, 500);
        stage.setTitle(map.title);
        stage.setScene(sc);
        stage.show();

        stage.setOnCloseRequest(event -> {
            statistics.saveToFile();
            if (tracker != null) tracker.window.close();
            try {
                statistics.writer.close();
            } catch (IOException e) { }
            t.stop();
            stage.close();
        });
    }

    public void drawMap() {
        t.suspend();
        statistics.updateStats();
        if (tracker != null) {tracker.updateTracker();}
        grid.getChildren().clear();
        StackPane jungle = new StackPane();
        jungle.setBackground(new Background(new BackgroundFill(Color.YELLOWGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        this.grid.add(jungle, map.lowerLeftJungle.x, map.lowerLeftJungle.y, map.upperRightJungle.x - map.lowerLeftJungle.x+1,map.upperRightJungle.y - map.lowerLeftJungle.y+1);

        this.grid.setGridLinesVisible(true);
        this.grid.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));

        for (SingleSquare s : map.takenSpace.values()) {
            StackPane picture = new StackPane();
            if (s.plant) picture.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
            this.grid.add(picture, s.position.x, s.position.y);
            if (!s.animals.isEmpty()) {
                Animal a = s.animals.get(0);
                Button b = new Button();
                b.setStyle("-fx-background-color: rgb(" +String.valueOf((225 - ((a.energy*225)/Animal.maxEnergy))) + ", 0," + String.valueOf((int)((a.energy/Animal.maxEnergy)*225)) + ")");
                b.setPrefSize(200, 100);
                b. setMinSize(0,0);
                this.grid.add(b, a.getPosition().x, a.getPosition().y);
                b.setOnAction(e -> {
                    if (paused) newTracker(a);
                });
            }
        }
       t.resume();
    }

    public void newTracker(Animal a) {
        if (tracker != null) tracker.close();
        tracker = new TrackAnimal(a);
    }


}

