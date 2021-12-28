package Project.maps;

import Project.Vector2d;
import Project.gui.App;

public class TorusMap extends AbstractMap{
    public TorusMap(int width, int height,
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
        "TorusMap",
                magic);

    }

    @Override
    public Vector2d canMoveTo(Vector2d v, int direction) {
        Vector2d u = v.forward(direction);
        return new Vector2d((u.x+width)%width,(u.y+height)%height);
    }
}
