package oca.chapter.one;
import java.util.Random;

//After compiling the .java file, to execute this class type java oca.chapter.one.Zoo "San Diego" Zoo from the \src folder
public class Zoo {

    public Zoo() {
        System.out.println("Contructor");
    }

    public static void main(String[] args) {
        System.out.println("Welcome!");

        System.out.println(args[0]);
        System.out.println(args[1]);

        System.out.println(new Random().nextInt(100));

        Zoo zoo = new Zoo();
    }
}
