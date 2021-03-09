package ocp.chapter.five;

import java.util.*;

public class ManipulatingArrays {

    static int[] classIntArray;

    public static void main(String... args) {

        String [] bugs = new String[] { "cricket", "beetle", "ladybug" };

        System.out.println(Arrays.toString(bugs));

        var emptyList = new ArrayList<>();
        String[] emptyArray = emptyList.toArray(new String[0]);

        System.out.println(emptyList);
        System.out.println(emptyArray);

        String[] oneArray = {"hawk", "robin"};

        List<String> oneAsList = Arrays.asList(oneArray);
        List<String> oneListOf = List.of(oneArray);

        System.out.println(oneAsList);
        System.out.println(oneListOf);

        classIntArray = new int[] {1, 2, 3}; // Need to instantiate a new object to change the default values of the array

        System.out.println(Arrays.toString(classIntArray));

        System.out.println("\n----------------------------------------\n");

        int numbers[] = new int[] {6, 9, 1};

        Arrays.sort(numbers);

        for(Integer i : numbers) // Autoboxing with the sorted int array
            System.out.println(i + " "); // 1 6 9 

        System.out.println("\n");

        String[] strings = {"123", "9", "112", "0300"};

        Arrays.sort(strings);

        for(String s : strings) // Autoboxing with the sorted int array
            System.out.println(s + " "); // "0300", "112", "123", "9" - alphabetic order - sorts from 0 to 9

        System.out.println("\n----------------------------------------\n");

        Set<Integer> set = new HashSet<>();
        
        System.out.println("Added the value? " + set.add(21)); // true
        System.out.println("Added the duplicated value? " + set.add(21)); // false
        System.out.println("Size: " + set.size()); // 1
        
        set.remove(21);
        System.out.println("isEmpty? " + set.isEmpty()); // true

        System.out.println("\n----------------------------------------\n");
        
        Map<String, String> map = new HashMap<>();
        map.put("koala", "bamboo");
        
        String food = map.get("koala"); // bamboo
        String other = map.getOrDefault("ant", "leaf"); // leaf

        for (String key: map.keySet()) 
            System.out.println(key + " " + map.get(key)); // koala bamboo (key | value)

        System.out.println("\n----------------------------------------\n");
    }
}