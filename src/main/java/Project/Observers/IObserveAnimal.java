package Project.Observers;

import Project.Vector2d;
import Project.onmap.Animal;

public interface IObserveAnimal {
    void addAnimal(Animal a);
    void removeAnimal(Animal a);
    void moveAnimal(Animal a, Vector2d oldPosition, Vector2d newPosition);
}
