package ocp.chapter.fourteen;
	
import java.util.*;

public class SetExamples {

	public static void main(String... args) {

        Set<Integer> set1 = new HashSet<>();
        boolean b11 = set1.add(66);
        boolean b12 = set1.add(10);
        boolean b13 = set1.add(66);
        boolean b14 = set1.add(8); 
        set1.forEach(System.out::println); 

        System.out.println();

        Set<Integer> set2 = new TreeSet<>();
        boolean b21 = set2.add(66);
        boolean b22 = set2.add(10);
        boolean b23 = set2.add(66);
        boolean b24 = set2.add(8); 
        set2.forEach(System.out::println);
	}
}