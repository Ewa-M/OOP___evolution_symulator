package Project.gui;
import Project.Observers.IObserveAnimal;
import Project.Vector2d;
import Project.maps.AbstractMap;
import Project.maps.RectangularMap;
import Project.maps.SingleSquare;
import Project.maps.TorusMap;
import Project.onmap.Animal;
import Project.simulation.Simulation;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.State.RUNNABLE;

public class App extends Application implements EventHandler<ActionEvent> {

    Stage window;
    VBox vbox;
    Scene scene;
    GridPane grid;
    Button button;
    AbstractMap map1;
    AbstractMap map2;
    Thread t1;
    Thread t2;
    boolean ispausedt1 = false;
    boolean ispausedt2 = false;
    TextField startAnimalsInput;
    TextField mapWidthInput;
    TextField mapHeightInput;
    TextField jungleWidthInput;
    TextField jungleHeightInput;
    TextField startEnergyInput;
    TextField moveEnergyInput;
    TextField plantEnergyInput;
    RadioButton magicRadio1;
    RadioButton magicRadio2;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.window = primaryStage;
        primaryStage.setTitle("Projekt 1");

        this.drawOpeningWindow();

        scene = new Scene(grid, 500, 500);

        window.setScene(scene);

        window.show();
    }

    private void drawOpeningWindow(){
        this.grid = new GridPane();
        this.grid.setPadding(new Insets(10,10,10,10)); //padding around layout
        this.grid.setVgap(8); //padding between cells
        this.grid.setHgap(10); //padding between cells

        //zwierzeta na poczatku
        Label startAnimalsLabel = new Label("number of start animals:");
        grid.add(startAnimalsLabel, 0,0);
        this.startAnimalsInput = new TextField("100");
        grid.add(this.startAnimalsInput, 1, 0, 2,1);

        //rozmiar mapy
        Label mapSizeLabel = new Label("map width and height:");
        grid.add(mapSizeLabel, 0 ,1);
        this.mapWidthInput = new TextField("100");
        grid.add(this.mapWidthInput, 1, 1);
        this.mapHeightInput = new TextField("100");
        grid.add(this.mapHeightInput, 2, 1);

        //rozmiar jungli
        Label jungleSizeLabel = new Label("jungle width and height:");
        grid.add(jungleSizeLabel, 0 ,2);
        this.jungleWidthInput = new TextField("30");
        grid.add(this.jungleWidthInput, 1, 2);
        this.jungleHeightInput = new TextField("30");
        grid.add(this.jungleHeightInput, 2, 2);

        //energia poczatkowa
        Label startEnergyLabel = new Label("starting energy:");
        grid.add(startEnergyLabel, 0,3);
        this.startEnergyInput = new TextField("100");
        grid.add(this.startEnergyInput, 1, 3, 2,1);

        //energia tracona w dniu
        Label moveEnergyLabel = new Label("daily energy loss:");
        grid.add(moveEnergyLabel, 0,4);
        this.moveEnergyInput = new TextField("1");
        grid.add(this.moveEnergyInput, 1, 4, 2,1);

        //energia zyskana z roslin
        Label plantEnergyLabel = new Label("plant energy:");
        grid.add(plantEnergyLabel, 0,5);
        this.plantEnergyInput = new TextField("50");
        grid.add(this.plantEnergyInput, 1, 5, 2,1);

        //tryb
        grid.add(new Label("first map mode:"), 0,6);
        RadioButton normalRadio1 = new RadioButton("Normal");
        normalRadio1.setSelected(true);
        this.magicRadio1 = new RadioButton("Magic");
        ToggleGroup radioGroup1 = new ToggleGroup();
        normalRadio1.setToggleGroup(radioGroup1);
        magicRadio1.setToggleGroup(radioGroup1);
        grid.add(normalRadio1, 1, 6);
        grid.add(magicRadio1, 2, 6);

        Label modeLabel = new Label("second map mode:");
        grid.add(modeLabel, 0,7);
        RadioButton normalRadio2 = new RadioButton("Normal");
        normalRadio2.setSelected(true);
        this.magicRadio2 = new RadioButton("Magic");
        ToggleGroup radioGroup2 = new ToggleGroup();
        normalRadio2.setToggleGroup(radioGroup2);
        magicRadio2.setToggleGroup(radioGroup2);
        grid.add(normalRadio2, 1, 7);
        grid.add(magicRadio2, 2, 7);

        //guzik
        button = new Button();
        button.setText("continue");
        this.grid.add(button, 0, 8);
        button.setOnAction(this);

    }

    private static boolean isPosInt(TextField input) {
        int n = 0;
        try{
            n = Integer.parseInt(input.getText());
        }catch(NumberFormatException e) {
            return false;
        }
        if (n < 0) {
            return false;
        }
        input.setStyle(null);
        return true;
    }

    public boolean validateInput() {
        if (isPosInt(startAnimalsInput)
                && isPosInt(mapWidthInput)
                && isPosInt(mapHeightInput)
                && isPosInt(jungleWidthInput)
                && (Integer.parseInt(mapWidthInput.getText()) >= Integer.parseInt(jungleWidthInput.getText()))
                && isPosInt(jungleHeightInput)
                && (Integer.parseInt(mapHeightInput.getText()) >= Integer.parseInt(jungleHeightInput.getText()))
                && (Integer.parseInt(startAnimalsInput.getText()) < (Integer.parseInt(mapWidthInput.getText()) * Integer.parseInt(mapHeightInput.getText())))
                && isPosInt(startEnergyInput)
                && isPosInt(moveEnergyInput)
                && isPosInt(plantEnergyInput)) return true;
        else {
            button.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            return false;
        }
    }

    @Override
    public void handle(ActionEvent event) {
        if (this.validateInput()) {
            map1 = new TorusMap(Integer.parseInt(mapWidthInput.getText()),
                    Integer.parseInt(mapHeightInput.getText()),
                    Integer.parseInt(jungleWidthInput.getText()),
                    Integer.parseInt(jungleHeightInput.getText()),
                    Integer.parseInt(startAnimalsInput.getText()),
                    Integer.parseInt(moveEnergyInput.getText()),
                    Integer.parseInt(plantEnergyInput.getText()),
                    Integer.parseInt(startEnergyInput.getText()),
                    this,
                    magicRadio1.isSelected());

            map2 = new RectangularMap(Integer.parseInt(mapWidthInput.getText()),
                    Integer.parseInt(mapHeightInput.getText()),
                    Integer.parseInt(jungleWidthInput.getText()),
                    Integer.parseInt(jungleHeightInput.getText()),
                    Integer.parseInt(startAnimalsInput.getText()),
                    Integer.parseInt(moveEnergyInput.getText()),
                    Integer.parseInt(plantEnergyInput.getText()),
                    Integer.parseInt(startEnergyInput.getText()),
                    this,
                    magicRadio2.isSelected());

        window.close();
        }
    }


}
