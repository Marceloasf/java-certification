package ocp.chapter.fourteen;
	
import java.util.*;

public class ListExamples {

    public static void main(String... args) {

        List<String> list = new ArrayList<>();
        list.add("SD");
        list.add(0, "NY");
        System.out.println(list.get(0)); 

        list.set(1, "FL");
        System.out.println(list); 
                
        list.remove("NY");
        list.remove(0);
        System.out.println(list); 

        List<Integer> numbers = Arrays.asList(1, 2, 3);
        numbers.replaceAll(x -> x * 3);
        System.out.println(numbers);
	}
}