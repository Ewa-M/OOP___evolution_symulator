package Project.maps;

import Project.Observers.IObserveAnimal;
import Project.Vector2d;
import Project.onmap.Animal;

import java.util.*;


public class SingleSquare implements IObserveAnimal {
    public final Vector2d position;
    public boolean plant;
    public List<Animal> animals; //posortowane rosnaco po energii;
    public AbstractMap map;

    public SingleSquare(Vector2d position, AbstractMap map) {
        this.map = map;
        this.position = position;
        this.plant = false;
        this.animals = new ArrayList<>();
    }

    public SingleSquare(Vector2d position, AbstractMap map, Animal a) {
        this.map = map;
        this.position = position;
        this.plant = false;
        this.animals = new ArrayList<>();
        this.animals.add(a);

    }

    public void addAnimal(Animal a){
        this.animals.add(a);
    };

    public void addPlant(){
        this.plant = true;
    };

    public String toString() {
        return (position.toString() + " " + animals.toString());}

    @Override
    public void removeAnimal(Animal a) {
        this.animals.remove(a);
        if (this.isEmpty()) { //czy to ostatni element na polu
            map.removeSquare(this);
        }
    }

    @Override
    public void moveAnimal(Animal a, Vector2d oldPosition, Vector2d newPosition) {
        if (position.equals(oldPosition)) {
            this.removeAnimal(a);
        } else {
            this.addAnimal(a);
        }
    }

    public boolean isEmpty() {
        if (plant) return false;
        return this.animals.isEmpty();

    }

    public void reproduce() {
        if (animals.size() > 1) {
            Collections.sort(animals, Collections.reverseOrder());
            Animal a = this.animals.get(0);
            Animal b = animals.get(1);
            if (a.energy > Animal.maxEnergy/2 && b.energy > Animal.maxEnergy/2) a.reproduce(b);
            Collections.sort(animals, Collections.reverseOrder());
        }
    }

    public boolean eatPlant() {
        if (animals.isEmpty()) return false;
        if (plant) {
            Collections.sort(animals, Collections.reverseOrder());
            int i = 0;
            while (i < animals.size() && animals.get(i).energy >= animals.get(0).energy) { i++; }
            for (int j=0; j<i; j++) {
                animals.get(j).increaseEnergy(AbstractMap.plantEnergy/i);
            }

            plant = false;
            return true;
        } return false;
    }

}
