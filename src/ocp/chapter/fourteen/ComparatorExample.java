package ocp.chapter.fourteen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ComparatorExample implements Comparable<ComparatorExample> {
    private String name;
    private int weight;

    public ComparatorExample(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }
    public int getWeight() {
        return this.weight;
    }
    public String toString() {
        return name;
    }
    public int compareTo(ComparatorExample d) {
        return this.name.compareTo(d.name);
    }
    public static void main(String[] args) {
        Comparator<ComparatorExample> byWeight = Comparator.comparingInt(ComparatorExample::getWeight);
        var ducks = new ArrayList<ComparatorExample>();
        ducks.add(new ComparatorExample("Quack", 7));
        ducks.add(new ComparatorExample("Puddles", 10));
        Collections.sort(ducks); // sorts by name
        System.out.println(ducks); // [Puddles, Quack]
        Collections.sort(ducks, byWeight); // sorts by weight with Comparator
        System.out.println(ducks); // [Quack, Puddles]
    }
}