package ocp.chapter.fourteen;
	
import java.util.*;
import java.util.function.*;

public class MapExamples {

	public static void main(String... args) {

        Map<String, String> map1 = new HashMap<>();
        map1.put("koala", "bamboo");
        map1.put("lion", "meat");
        map1.put("giraffe", "leaf");
        String food1 = map1.get("koala");
        for (String key: map1.keySet()) System.out.print(key + ","); 

        System.out.println();

        Map<String, String> map2 = new TreeMap<>();
        map2.put("koala", "bamboo");
        map2.put("lion", "meat");
        map2.put("giraffe", "leaf");
        map2.put("dolphin", "water");
        String food2 = map2.get("koala");
        for (String key: map2.keySet()) System.out.print(key + ",");

        System.out.println();

        Map<Integer, String> forEachMap = new HashMap<>();
        forEachMap.put(1, "a");
        forEachMap.put(2, "b");
        forEachMap.put(3, "c");
        forEachMap.forEach((k, v) -> System.out.println(v));
        forEachMap.values().forEach(System.out::println);
        forEachMap.entrySet().forEach((e) -> System.out.println(e.getKey() + e.getValue()));

        System.out.println();

        Map<Integer, Integer> map3 = new HashMap<>();
        map3.put(1, 2);
        map3.put(2, 4);
        Integer original = map3.replace(2, 10);
        System.out.println(map3);
        map3.replaceAll((k, v) -> k + v);
        System.out.println(map3);

        Map<String, String> favorites = new HashMap<>();
        favorites.put("Jenny", "Bus Tour");
        favorites.put("Tom", null);
        favorites.putIfAbsent("Jenny", "Tram");
        favorites.putIfAbsent("Sam", "Tram");
        favorites.putIfAbsent("Tom", "Tram");
        System.out.println(favorites);

        System.out.println();
        
        BiFunction<String, String, String> mapper = (v1, v2) -> v1.length() > v2.length() ? v1 : v2;
    
        Map<String, String> favorites2 = new HashMap<>();
        favorites2.put("Jenny", "Bus Tour");
        favorites2.put("Tom", "Tram");
    
        String jenny = favorites2.merge("Jenny", "Skyride", mapper);
        String tom = favorites2.merge("Tom", "Skyride", mapper);
    
        System.out.println(favorites2);
        System.out.println(jenny);
        System.out.println(tom);
	}
}