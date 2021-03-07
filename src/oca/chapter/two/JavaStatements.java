package oca.chapter.two;

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

        if (x != 5) 
            System.out.println("if-then-else single line true branch");
        else {
            System.out.println("if-then-else single line false branch");
        }

        if (x > 5) {
            System.out.println("if-then-else-if-then first if true branch");
        } else if (x == 5) {
            System.out.println("if-then-else-if-then second if true branch");
        } else {
            System.out.println("if-then-else-if-then block false branch");
        }

        int y = x == 5 ? 10 : 0; // () on the operands are optional;

        System.out.println("ternary result = " + y);
        
        System.out.println("\n----------------------------------------\n");

        switch(y) {
            case 0:                              // If the value is 0, it will print all the cases 
                System.out.println("Monday..."); // until it finds a break statement or finishes the structure.
            default:                             
                System.out.println("Weekday.");  // Prints only Friday because the value of y is 10, but if the default block was called,
            case 10:                             // in case of no match, Weekday and Friday would be printed (no break on the default).
                System.out.println("Friday!");
                break;
        }
        
        System.out.println("\n----------------------------------------\n");

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

        System.out.println("\n----------------------------------------\n");

        int f = 0;

        for(long i = 0, p = 4; f < 5 && i < 10; f++, i++) { 
            System.out.println(i + " ");
        }

        String[] names = new String[3];

        // Prints 3 null values
        for (String name : names) {
            System.out.println(name + " ");
        }

        java.util.List<String> values = new java.util.ArrayList<>();

        // Doesn't iterate over this empty ArrayList.
        for (String value : values) {
            System.out.println(value + " ");
        }

        System.out.println("\n----------------------------------------\n");

        int [][] myComplexArray = new int[][] {{5,2,1,3}, {3,9,8,9}, {5,7,12,7}};
        // int [][] myComplexArray = {{5,2,1,3}, {3,9,8,9}, {5,7,12,7}}; Both compile 

        for (int[] mySimpleArray : myComplexArray) {
            for (int i = 0; i < mySimpleArray.length; i++) {
                System.out.println(mySimpleArray[i] + "\t");
            }
            System.out.println();
        }

        int nestedWhileX = 20;

        while (nestedWhileX > 0) {

            do {
                nestedWhileX -= 2;
            } while (nestedWhileX > 5);

            nestedWhileX--;
            System.out.println(nestedWhileX + "\t");
        }
    }
}