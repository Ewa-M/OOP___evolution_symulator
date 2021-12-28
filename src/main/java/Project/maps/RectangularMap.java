package Project.maps;

import Project.Vector2d;
import Project.gui.App;
import Project.onmap.Animal;

public class RectangularMap extends AbstractMap{




    public RectangularMap(int width, int height,
                          int widthJungle, int heightJungle,
                          int startAnimals,
                          int moveEnergy,
                          int plantEnergy,
                          int maxEnergy,
                          App app,
                          boolean magic) {
        super(width,
                height,
                widthJungle,
                heightJungle,
                startAnimals,
                moveEnergy,
                plantEnergy,
                maxEnergy,
                app,
                "RectangularMap",
                magic);

        }

    @Override
    public Vector2d canMoveTo(Vector2d v, int direction) {
        Vector2d u = v.forward(direction);
        if (u.follows(new Vector2d(0,0)) && u.precedes(new Vector2d(width-1, height-1))) return u;
        return v;
    }
}
