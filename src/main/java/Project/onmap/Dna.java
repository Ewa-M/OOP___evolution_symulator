package Project.onmap;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Dna {
    private int[] genes;

    public Dna() {
        Random r = new Random();
        genes = new int[32];
        for (int i=0; i<32; i++) { genes[i] = r.nextInt(8);}
        Arrays.sort(genes);
    }

    public Dna(int x, Dna d1, Dna d2) {
        Random r = new Random();
        genes = new int[32];
        if (r.nextBoolean()) {
            for(int i=0; i<x; i++){
                genes[i] = d1.genes[i];
            }
            for(int i=x; i<32; i++){
                genes[i] = d2.genes[i];
            }
        }
        else {
            for(int i=0; i<(32-x); i++){
                genes[i] = d2.genes[i];
            }
            for(int i=32-x; i<32; i++){
                genes[i] = d1.genes[i];
            }
        }
        Arrays.sort(genes);
    }

    public int getRandom() {
        Random r = new Random();
        return genes[r.nextInt(32)];
    }

    public String toString() {
        return Arrays.toString(genes);
    }

    public boolean equals(Object other){
        if (other == this){
            return true;
        }
        if (!(other instanceof Dna)){
            return false;
        }
        Dna that = (Dna) other;

        return Arrays.equals(that.genes, genes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(this.genes));
    }

}
