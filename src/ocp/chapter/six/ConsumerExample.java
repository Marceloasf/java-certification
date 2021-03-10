package ocp.chapter.six;

import java.util.function.*;

// p.230-231
// Consumer interface - accept() is the most important method on 1Z0-815. 
// public interface Consumer<T> {
//     void accept(T t);
// }

// Good to use in cases that you need to do something using lambdas without returning a value (like printing)
public class ConsumerExample {

    private static String color = "Red";

    public static void main(String args[]) {
        
        Consumer<String> consumer = x -> System.out.println(x);

        print(consumer, "Hello World");
        // print(x -> System.out.println(x), "Hello World"); // Works too.

        System.out.println("\n");

        caw("Fulana");
    }

    public static void print(Consumer<String> consumer, String value) {

        consumer.accept(value); // Will execute the consumer passed by params and using the value param in it.
    }

    // Only works/compiles because all these local and method parameter variables that are used in the lambda are "effectively final",
    // their value will not change after being set (using final on the variables is an option to ensure they are effectively final).
    public static void caw(String name) {

        String volume = "loudly";

        // volume = "HAHA" // This would make line 41 not compile, at the variable, because the lambda sees this assignment as a problem.
        // name = "HAHA" // This would not compile for the same reason of volume assignment.

        Consumer<String> consumer = s -> System.out.print(name + " says " + volume + " that she is " + color);
        
        consumer.accept("");
    }
}