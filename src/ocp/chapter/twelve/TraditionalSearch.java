package ocp.chapter.twelve;

import java.util.*;
import java.util.function.Predicate;

class Animal {
    private String species;
    private boolean canHop;
    private boolean canSwim;

    public Animal(String speciesName, boolean hooper, boolean swimmer) {
        species = speciesName;
        canHop = hooper;
        canSwim = swimmer;
    }

    public boolean canHop() {
        return canHop;
    }

    public boolean canSwim() {
        return canSwim;
    }

    public String toString() {
        return species;
    }
}

public class TraditionalSearch {
    public static void main(String... args) {

        var animals = new ArrayList<Animal>();
        animals.add(new Animal("fish", false, true));
        animals.add(new Animal("kangaroo", true, true));
        animals.add(new Animal("rabbit", true, false));
        animals.add(new Animal("turtle", false, true));

        print(animals, a -> a.canHop()); // Will print only the hoppers
    }

    private static void print(List<Animal> animals, Predicate<Animal> checker) {
        for(Animal animal : animals) {
            if (checker.test(animal)) System.out.println(animal);
        }
    }
}