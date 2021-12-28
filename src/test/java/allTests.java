import Project.gui.App;
import Project.maps.AbstractMap;
import Project.maps.RectangularMap;
import Project.onmap.Animal;
import Project.simulation.Simulation;
import org.junit.jupiter.api.Test;

import java.util.SortedSet;
import java.util.TreeSet;

public class allTests {

    @Test
    public void mapWorks() {
        AbstractMap map = new RectangularMap();
        System.out.println("elo");
        Simulation s =  new Simulation(map);
       for (Animal a : map.animals) {
           System.out.println(a.getPosition().toString() + "," + a.direction);
       }
        System.out.println("elo");
        s.singleDay();
        for (Animal a : map.animals) {
            System.out.println(a.getPosition().toString() + "," + a.direction);

        }
        System.out.println("elo");
        System.out.println("elo");
    }


    @Test
    public void mapNotWorks() {
        AbstractMap map = new RectangularMap();
        while (true) {
            map.date++;
            map.removeDead();
            map.dailyMoves();
            map.dailyOnSquare();
            map.addPlantStep();
            map.addPlantJungle();

        }
    }

    @Test
    public void sortedTestWork() {
        SortedSet<String> fart;
        fart = new TreeSet<String>();

        fart.add(new String("a"));
        fart.add(new String("a"));
        fart.add(new String("b"));

        for(String i : fart) {
            System.out.println(i);
        }
    }

}
