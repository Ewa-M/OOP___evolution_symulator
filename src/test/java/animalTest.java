import Project.Vector2d;
import Project.onmap.Animal;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class animalTest {
    @Test
    void genesTest() {
        List<Vector2d> list = new ArrayList<>();
        Vector2d v1 = new Vector2d(1,1);
        list.add(v1);
        System.out.println(list);

        list.remove(new Vector2d(1,1));

        System.out.println(list);

    }
}
