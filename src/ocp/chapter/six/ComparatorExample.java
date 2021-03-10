package ocp.chapter.six;

import java.util.*;

// p.230-231
// Comparator interface - this is a functional interface since it has only one unimplemented method.
// It has many static and default methods to facilitate writing complex comparators.
// public interface Comparator<T> {
//     int compare(T o1, T 02); - Most important method for the exam.
// }

public class ComparatorExample {

    public static void main(String args[]) {
        
        Comparator<Integer> ints = (i1, i2) -> i1 - i2;

        System.out.println(ints.compare(5, 3)); // 2 - The ints comparator uses natural sort order. If it returns a positive number
                                                // that means the first number is bigger and we are sorting in ascending order.

        System.out.println();

        Comparator<String> strings = (s1, s2) -> s2.compareTo(s1); // Does the sorting in descending order because the call is "backwards".
        Comparator<String> moreStrings = (s1, s2) -> - s1.compareTo(s2); // This call uses the default order (ascending); however, it applies
                                                                         // a negative sign to the result, which reverses it to descending.

        System.out.println(strings.compare("bc", "az")); // -1 - descending
        System.out.println(moreStrings.compare("a", "az")); // 1 - descending
    }
}