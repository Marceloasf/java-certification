package ocp.chapter.six;

import java.util.*;

public class LambdaApiUsage {

    public static void main(String[] args) {

        System.out.println("\n----------------removeIf()----------------\n");
        
        List<String> bunnies = new ArrayList<>();
        bunnies.add("long ear");
        bunnies.add("floppy");
        bunnies.add("hoppy");

        System.out.println(bunnies); // [long ear, floppy, hoppy]

        bunnies.removeIf(s -> s.charAt(0) != 'h');

        System.out.println(bunnies); // [hoppy]

        System.out.println("\n----------------sort()----------------\n");
    
        bunnies.add(0, "long ear");
        bunnies.add(1, "floppy");

        System.out.println(bunnies); // [long ear, floppy, hoppy]
        
        bunnies.sort((b1, b2) -> b1.compareTo(b2)); // default ascending order

        System.out.println(bunnies); // [floppy, hoppy, long ear]
        
        System.out.println("\n----------------forEach()----------------\n");
        
        bunnies.forEach(b -> System.out.println(b)); // floppy hoppy long ear
        System.out.println(bunnies); // [floppy, hoppy, long ear]

        System.out.println("\n--------------------------------\n");

        Set<String> dogs = Set.of("fred", "bolt", "luke");
        dogs.forEach(d -> System.out.println(d));

        System.out.println("\n--------------------------------\n");

        Map<String, Integer> cats = new HashMap<>(); 
        cats.put("meow", 3);
        cats.put("elis", 8);
        cats.put("hop", 1);

        // For a map, you have to choose whether you want to go through the keys or values.
        cats.keySet().forEach(c -> System.out.println(c)); // prints keys
        cats.values().forEach(c -> System.out.println(c)); // prints values

        System.out.println("\n----------------forEach()-BiConsumer-Map----------------\n");

        // BiConsumer works just like a Consumer, but it can take two parameters. This functional interface 
        // allows you to use forEach() with key/value pairs from Map.
        cats.forEach((k, v) -> System.out.println(k + " " + v)); // prints keys and values
    } 
}