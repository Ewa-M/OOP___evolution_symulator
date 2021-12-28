package Project.onmap;

import Project.maps.AbstractMap;

import java.util.*;

public class Genome {
    public Dna dna;
    public int repetition;
    public AbstractMap map;

    private  Genome(Dna dna, AbstractMap map) {
        this.dna = dna;
        this.map = map;
        this.repetition = 1;
    }

    public static Genome createGenome(AbstractMap map) {
        Dna dna = new Dna();
        return addToGenepool(dna, map);
    }

    public Genome copyGenome() {
        repetition++;
        return this;
    }

    public static Genome reproduceGenome(Animal parent1, Animal parent2) {

        int x = (32* parent2.energy)/(parent1.energy + parent2.energy); //zawsze zaokragla na korzysc dominujacego rodzica
        Dna dna = new Dna(x, parent1.genes.dna, parent2.genes.dna);
        return addToGenepool(dna, parent1.map);
    }


    private static Genome addToGenepool(Dna dna, AbstractMap map) {

        if (map.genepool.containsKey(dna)) {
            Genome g = map.genepool.get(dna);
            g.repetition++;
            return g;
        } else {
            Genome g = new Genome(dna, map);
            map.genepool.put(dna, g);
            return g;
        }
    }

    public  void removeFromGenepool(AbstractMap map) {
            this.repetition--;
    }

    public static void showAll(AbstractMap map) {
        int r=0;
        for(Genome g : map.genepool.values()) {
            if (g.repetition == 0) continue;
            System.out.println(g.toString());
            r += g.repetition;
        }
        System.out.println();
        System.out.println(r + "    " + map.animals.size());
        System.out.println();
        System.out.println();
        System.out.println();

    }


    public static Genome getDominant(AbstractMap map) {
        int n=0;
        Genome greatest = null;
        for (Genome g : map.genepool.values()) {
            if ( n < g.repetition) {
                greatest = g;
                n = g.repetition;
            }
        }
        return greatest;
    }

    public static String getDominantString(AbstractMap map) {
        Genome greatest = getDominant(map);
        if (greatest != null) return greatest.toString();
        return "-";
    }

    public int getRandom() {
        return dna.getRandom();
    }

    public String toString(){
        return dna.toString();
    }

    public boolean equals(Object other){
        if (other == this){
            return true;
        }
        if (!(other instanceof Genome)){
            return false;
        }
        Genome that = (Genome) other;

        return dna.equals(that.dna);
    }
}
