package Project.onmap;

import Project.Observers.IObserveAnimal;
import Project.Vector2d;
import Project.maps.AbstractMap;

import java.util.*;

public class Animal implements Comparable<Animal>{
    public int id;
    public Vector2d position;
    public int energy;
    public int direction;
    public Genome genes;
    public static int maxEnergy =200;
    public AbstractMap map;
    public Random r = new Random();
    public Integer age = 0;
    public int kids = 0;
    public List<IObserveAnimal> observers = new ArrayList<>();
    public boolean isTracked=false;


    //spawning new animal on a map
    public Animal(AbstractMap map) {
        this.energy = this.r.nextInt(maxEnergy);
        this.position = map.getFree();
        this.genes = Genome.createGenome(map);
        this.direction = r.nextInt(8);
        map.addAnimal(this);
    }

    //result of reproduction -- parent1 is dominant
    public Animal(Animal parent1, Animal parent2) {
        position = parent1.position;
        direction = r.nextInt(8);
        energy = parent1.energy/4 + parent2.energy/4;
        isTracked = parent1.isTracked || parent2.isTracked;
        parent1.map.addAnimal(this);
        genes = Genome.reproduceGenome(parent1, parent2);
    }

    //result of magic
    public Animal(Animal parent) {
        position = map.getFree();
        direction = r.nextInt(8);
        energy = maxEnergy;
        parent.map.addAnimal(this);
        genes = parent.genes.copyGenome();
    }

    public String toString() {
        return String.valueOf(id) + ":e"+ String.valueOf(energy);
    }

    public static void setConstants(int maxEnergy) {
        Animal.maxEnergy = maxEnergy;
    }

    public void addObserver(IObserveAnimal observer) {this.observers.add(observer);}

    public void removeObserver(IObserveAnimal observer) {this.observers.remove(observer);}

    public void turn(int x) {
        this.direction += x;
        this.direction %= 8;
    }

    public void decreaseEnergy(int x) {
        if (x < energy) {
            energy -= x;
        }
        else {
            energy -= x;
            map.addPotentialDead(this);
        }
    }

    public void increaseEnergy(int x){
        energy = Math.min(energy+x, maxEnergy);
    }

    public void reproduce(Animal other) {
        if (this.energy < other.energy) {
            other.reproduce(this);
            return;
        }
        if (this.energy < maxEnergy*0.5  || other.energy < maxEnergy*0.5) return;
        Animal child = new Animal(this, other);
        this.decreaseEnergy(energy/4);
        other.decreaseEnergy(other.energy/4);
        this.kids++;
        other.kids++;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public boolean isDead() {
        if (this.energy <= 0) {
            this.died();
            return true;
        }
        return false;
    }

    public void died() {
        for(IObserveAnimal oia : this.observers) {
            oia.removeAnimal(this);
        }
    }

    public void move() {
        int n = genes.getRandom();
        switch (n){
            case 0:{
                Vector2d v = map.canMoveTo(position, direction);
                if (!v.equals(position)) {
                    for (IObserveAnimal oia : this.observers) {
                        oia.moveAnimal(this, position, v);
                    }
                    position = v;
                }
                break;
            }
            case (4): {
                Vector2d v = map.canMoveTo(position, (direction+4)%8);
                if (!v.equals(position)) {
                    for (IObserveAnimal oia : this.observers) {
                        oia.moveAnimal(this, position, v);
                    }
                    position = v;
                }
                break;
            }
            default: this.turn(n);
        }
    }

    @Override
    public int compareTo(Animal o) {
        if (o == this) return 0;
        if (energy < o.energy) return -1;
        if (energy > o.energy) return 1;
        if (id < o.id) return -1;
        return 1;

    }
}
