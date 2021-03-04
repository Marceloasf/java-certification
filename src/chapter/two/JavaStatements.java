package chapter.two;

public class JavaStatements {

    public static void main(String[] args) {
        int x = 5;

        if (x == 5) {
            System.out.println("if-then block");
        }

        if (x == 5)
            System.out.println("if-then single line");

        if (x == 5) {
            System.out.println("if-then-else true branch");    
        } else {
            System.out.println("if-then-else false branch");    
        }

        if (x != 5) 
            System.out.println("if-then-else single line true branch");
        else
            System.out.println("if-then-else single line false branch");

        if (x > 5) {
            System.out.println("if-then-else-if-then first if true branch");
        } else if (x == 5) {
            System.out.println("if-then-else-if-then second if true branch");
        } else {
            System.out.println("if-then-else-if-then block false branch");
        }

        int y = x == 5 ? 10 : 0; // () on the operands are optional;

        System.out.println("ternary result = " + y);

        switch(y) {
            case 0:                              // If the value is 0, it will print all the cases 
                System.out.println("Monday..."); // until it finds a break statement or finishes the structure.
            default:                             
                System.out.println("Weekday.");  // Prints only Friday because the value of y is 10, but if the default block was called,
            case 10:                             // in case of no match, Weekday and Friday would be printed (no break on the default).
                System.out.println("Friday!");
                break;
        }

        int bitesOfCheese = 10;
        int roomInBelly = 5;

        while (bitesOfCheese > 0 && roomInBelly > 0) {
            --bitesOfCheese;
            --roomInBelly;
        }

        System.out.println(bitesOfCheese + " pieces of cheese left.");

        int chips = 10;
        int roomInPackage = 5;

        do {
            --chips;
            --roomInPackage;
        } while (chips > 0 && roomInPackage > 0);

        System.out.println(chips + " chips left.");

        int f = 0;

        for(long i = 0, p = 4; f < 5 && i < 10; f++, i++) { 
            System.out.println(i + " ");
        }
    }
}