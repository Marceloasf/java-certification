package ocp.chapter.six;

import java.util.*;
import java.util.function.*;

// p.230
// Predicate interface - Good for conditional and validation (returns boolean).
// public interface Predicate<T> {
//     boolean test(T t);
// }

public class PredicateSearch {

    public static void main(String... args) {

        List<Animal> animals = new ArrayList<>();
        animals.add(new Animal("Fish", true));

        Predicate<Animal> predicate = a -> a.canHop();

        print(animals, predicate);
        // print(animals, a -> a.canHop()); // Works too.
    }

    private static void print(List<Animal> animals, Predicate<Animal> checker) {

        for (Animal animal : animals) {

            if (checker.test(animal)) // a -> a.canHop() 
                System.out.println(animal + " ");
        }

        System.out.println();
    }
}