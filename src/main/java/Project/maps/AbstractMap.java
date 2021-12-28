package Project.maps;

import Project.Observers.IObserveAnimal;
import Project.Observers.IObserveSquare;
import Project.Vector2d;
import Project.gui.*;
import Project.onmap.Animal;
import Project.onmap.Dna;
import Project.onmap.Genome;
import javafx.application.Platform;

import java.util.*;

public abstract class AbstractMap implements IObserveAnimal, IObserveSquare {
    public String title;
    public App app;
    public int date;
    public int countPlants = 0;
    public int width;
    public int height;
    public List<Animal> potentialDead;
    public static int moveEnergy = 15;
    public static int plantEnergy = 20;
    public Vector2d lowerLeftJungle;
    public Vector2d upperRightJungle;
    public List<Animal> animals;
    public List<Vector2d> freeStep;     // trzymanie pól na mpaie podzial na:
    public List<Vector2d> freeJungle;     // - zajete -> szybki dostep po kluczu
    public Map<Vector2d, SingleSquare> takenSpace;// - wolne -> podzial na jungle i step w listach dla latwego losowania
    public MapWindow mapWindow;
    public int deadAge = 0;
    public int deadNumber;
    public int ids = 0;
    public int magic=4;
    public Map<Dna, Genome> genepool = new HashMap<>();
    public int descendants=0;

    public AbstractMap(int width,
                       int height,
                       int widthJungle,
                       int heightJungle,
                       int startAnimals,
                       int moveEnergy,
                       int plantEnergy,
                       int maxEnergy,
                       App app,
                       String title,
                       boolean magic){
        this.title = title;
        AbstractMap.setConstants(moveEnergy, plantEnergy);
        Animal.setConstants(maxEnergy);
        this.potentialDead = new ArrayList<>();
        this.app = app;
        this.width = width;
        this.height = height;
        this.lowerLeftJungle = new Vector2d((width-widthJungle+1)/2, (height-heightJungle+1)/2);
        this.upperRightJungle = new Vector2d(widthJungle - 1 + ((width-widthJungle+1)/2), heightJungle - 1 +((height-heightJungle+1)/2));
        this.freeJungle = new ArrayList<>();
        this.freeStep = new ArrayList<>();
        this.animals = new ArrayList<>();
        this.takenSpace = new HashMap<>();
        this.mapWindow  = new MapWindow(this);

        if (magic) {this.magic=0;}


        for (int x=0; x<width; x++) { //inicjalizacja wszystkich pol jako wolnych
            for (int y=0; y<height; y++) {
                Vector2d v = new Vector2d(x, y);
                if (this.isJungle(v)) this.freeJungle.add(v);
                else this.freeStep.add(v);
            }
        }
        for (int i =0; i < startAnimals; i++) { //dodawanie zwierzatek
            new Animal(this);
        }
    }

    public static void setConstants(int moveEnergy, int plantEnergy) {
        AbstractMap.moveEnergy = moveEnergy;
        AbstractMap.plantEnergy = plantEnergy;
    }

    public final void addPlantJungle() {
        if (this.freeJungle.isEmpty()) return;
        Random r = new Random();
        int n = r.nextInt(this.freeJungle.size());
        Vector2d v = this.freeJungle.get(n);
        this.freeJungle.remove(n);
        SingleSquare s = new SingleSquare(v, this);
        s.addPlant();
        countPlants++;
        this.takenSpace.put(v, s);
    }

    public final void addPlantStep() {
        if (this.freeStep.isEmpty()) return;
        Random r = new Random();
        int n = r.nextInt(this.freeStep.size());
        Vector2d v = this.freeStep.get(n);
        this.freeStep.remove(n);
        SingleSquare s = new SingleSquare(v, this);
        s.addPlant();
        countPlants++;
        this.takenSpace.put(v, s);

    }

    public final boolean isJungle(Vector2d v) {
        return (v.precedes(upperRightJungle) && v.follows(lowerLeftJungle));
    }

    public final void removeAnimal(Animal a) {
        animals.remove(a);
        a.genes.removeFromGenepool(this);
        takenSpace.get(a.getPosition()).removeAnimal(a);
    }

    public final void moveAnimal(Animal a, Vector2d oldPosition, Vector2d newPosition) {

        takenSpace.get(oldPosition).removeAnimal(a);

        if (!takenSpace.containsKey(newPosition)) {
            SingleSquare s = new SingleSquare(newPosition, this);
            takenSpace.put(newPosition, s);
                this.freeJungle.remove(newPosition);
                this.freeStep.remove(newPosition);

        }
        takenSpace.get(newPosition).addAnimal(a);

    }

    public final void addAnimal(Animal a) {
        animals.add(a);
        if (a.isTracked) descendants++;
        a.addObserver(this);
        a.id = ids;
        ids++;
        a.map = this;
        if (!this.takenSpace.containsKey(a.getPosition())) {
            SingleSquare s = new SingleSquare(a.getPosition(), this);
            this.takenSpace.put(a.getPosition(), s);
            if (this.isJungle(a.position)) {
                this.freeJungle.remove(a.position);
            } else {
                this.freeStep.remove(a.position);
            }
        }
        takenSpace.get(a.getPosition()).addAnimal(a);

    }

    public final void removeSquare(SingleSquare s){
        if (this.isJungle(s.position)) {
            freeJungle.add(s.position);
        } else {
            freeStep.add(s.position);
        }
        this.takenSpace.remove(s.position);
    }

    public final Vector2d getFree() {
        Random r = new Random();
        int n = r.nextInt(freeJungle.size() + freeStep.size());
        if (n < freeJungle.size()) {
            return freeJungle.get(n);
        } else {
            return  freeStep.get(n-freeJungle.size());
        }
    }

    public final void addPotentialDead(Animal a){
        potentialDead.add(a);
    }

    public final void dailyMoves(){
            for (Animal a : animals) {
                ++a.age;
                a.decreaseEnergy(moveEnergy);
                a.move();
            }
    }

    public final void dailyOnSquare(){
        for(SingleSquare s : takenSpace.values()) {
            if (s.eatPlant()) countPlants--;
            s.reproduce();
        }
    }

    public final void removeDead() {
        for (Animal a : potentialDead) { //deleting dead
            if (a.isDead()) {
                deadAge += a.age;
                deadNumber++;
            }
        }
        potentialDead.clear();

        if (magic < 3 && animals.size() == 5 ) {
            magic++;
            List<Animal> copy = new ArrayList<>(animals);
            for (Animal a : copy) {new Animal(a);}

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    MagicAlert.alertMe();
                }
            });
            System.out.println("magic!");
        }
    }

    public abstract Vector2d canMoveTo(Vector2d v, int direction);
}
