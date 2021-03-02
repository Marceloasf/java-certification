package chapter.one;

public class InstanceInitializer {
    
    public static void main(String[] args) {
        { System.out.println("Inside Main Method Code Block"); }

        new InstanceInitializer();
    }

    { System.out.println("Instance Initializer"); }
}