package ocp.chapter.ten;

class NoMoreCarrotsException extends Exception {
    NoMoreCarrotsException() {
        super("NO MORE CARROTS!");
    }
 }

interface Animal {
    void good() throws NoMoreCarrotsException;
}

public class ExceptionExamples implements Animal {

    private String string;

    public static void main(String[] args) {
        bad();
    }

    private static void bad() throws Error {
        try {
            nice();
        } catch (NoMoreCarrotsException e) {
            System.out.println(e.getMessage());
            System.out.println("HA NoMoreCarrots...");
        } catch (java.io.IOException e) {
            System.out.println("IO? Not printed...");
        }
        
        try {
            eatCarrot();
        } catch (NullPointerException | IllegalArgumentException | Error e) { // Handling and declaring Error and its subclasses should not be done, but it works.
            System.out.println(e);
            System.out.println("HA NullPointer!");
        }

        throw new Error("OH NO");
    }

    public static void nice() throws NoMoreCarrotsException, java.io.IOException {
        System.out.println("NICE");

        throw new NoMoreCarrotsException();
    }

    public void good() {
        System.out.println("GOOD");
    }

    private static void eatCarrot() {
        ExceptionExamples exceptionExamples = new ExceptionExamples();
        exceptionExamples.good();
        exceptionExamples.string.length();
    }
}