package ocp.chapter.fourteen;
	
import java.util.*;

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
	}
}