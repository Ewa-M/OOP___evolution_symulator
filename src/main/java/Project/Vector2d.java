package Project;
import java.util.Objects;

public class Vector2d {
    public final int x;
    public  final int y;

    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return("("+ this.x+ ", "+ this.y+ ")");
    }

    public boolean precedes(Vector2d other) {
        if (this.x <= other.x && this.y <= other.y) {
            return true;
        }
        return false;
    }

    public boolean follows(Vector2d other) {
        if (this.x >= other.x && this.y >= other.y) {
            return true;
        }
        return false;
    }

    public Vector2d upperRight(Vector2d other){
        if (other == null) return this;
        return new Vector2d(Math.max(this.x, other.x), Math.max(this.y, other.y));
    }

    public Vector2d lowerLeft(Vector2d other){
        if (other == null) return this;
        return new Vector2d(Math.min(this.x, other.x), Math.min(this.y, other.y));
    }

    public Vector2d add(Vector2d other){
        return new Vector2d(other.x + this.x, other.y + this.y);
    }

    public Vector2d subtract(Vector2d other){
        return new Vector2d(this.x-other.x, this.y- other.y);
    }

    public boolean equals(Object other){
        if (other == this){
            return true;
        }
        if (!(other instanceof Vector2d)){
            return false;
        }
        Vector2d that = (Vector2d) other;

        if (this.x == that.x && this.y == that.y){
            return true;
        }
        return false;
    }

    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    public  static void main(String[] args){
        Vector2d position1 = new Vector2d(1,2);
        System.out.println(position1);
        Vector2d position2 = new Vector2d(-2,1);
        System.out.println(position2);
        System.out.println(position1.add(position2));
    }

    public Vector2d forward(int x) {
        return switch (x) {
            case 0 -> this.add(new Vector2d(0, 1));
            case 1 -> this.add(new Vector2d(1, 1));
            case 2 -> this.add(new Vector2d(1, 0));
            case 3 -> this.add(new Vector2d(1, -1));
            case 4 -> this.add(new Vector2d(0, -1));
            case 5 -> this.add(new Vector2d(-1, -1));
            case 6 -> this.add(new Vector2d(-1, 0));
            case 7 -> this.add(new Vector2d(-1, 1));
            default -> this;
        };
    }
}
