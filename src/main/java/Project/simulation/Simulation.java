package Project.simulation;

import Project.maps.AbstractMap;
import Project.maps.SingleSquare;
import Project.onmap.Animal;
import javafx.application.Platform;

public class Simulation implements Runnable {
    public AbstractMap map;

    public Simulation(AbstractMap map){
        this.map = map;

    }

    public  void singleDay() {

        map.date++;

        map.removeDead();









        map.dailyMoves();










        map.dailyOnSquare();










        map.addPlantStep();










        map.addPlantJungle();

       
       
       
       
       
       
       
       

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                map.mapWindow.drawMap();
            }
        });

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            System.out.println("ups?");
        }

    }

    @Override
    public void run() {


        while (true) {
            this.singleDay();
            if (map.animals.isEmpty()) break;
        }

    }
}
