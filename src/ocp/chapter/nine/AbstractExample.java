package ocp.chapter.nine;

abstract class Animal {

    protected abstract void makeNoise(String a);
}

abstract class Bird extends Animal { // This class isn't instantiable

    public abstract String getName();

    protected void makeNoise(String a) { // The first concrete subclass of Bird and Animal (Stork) will not need to override this method, since it's 
                                         // overriden here as a nonabstract method. It could be redeclared here as an abstract method too.
        System.out.println(a);
    }

    public void printName() {
        System.out.println(getName());
    }
}

class Stork extends Bird {

    public String getName() { // Must override the parent's abstract methods, or else the class will not compile (l.12).
        return "STORK!";
    }
}

public class AbstractExample {

    public static void main(String... args) {
        Stork stork = new Stork();
        stork.printName();
        stork.makeNoise("Piu piu");
    }
}