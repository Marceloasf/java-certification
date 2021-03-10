package ocp.chapter.six;

import java.util.*;
import java.util.function.*;

// p.231
// Supplier interface - has only one method. 
// public interface Supplier<T> {
//     T get();
// }

public class SupplierExample {

    public static void main(String args[]) {
        
        // A good use case for a Supplier is when generating values.
        Supplier<Integer> number = () -> 42;
        Supplier<Integer> random = () -> new Random().nextInt();

        System.out.println(number.get());
        System.out.println(random.get());

        System.out.println("\n");

        System.out.println(returnNumber(number));
        System.out.println(returnNumber(random));

        System.out.println("\n");

        System.out.println(returnNumber(() -> 42));
        System.out.println(returnNumber(() -> new Random().nextInt()));
    }

    private static int returnNumber(Supplier<Integer> supplier) {

        return supplier.get();
    }
}