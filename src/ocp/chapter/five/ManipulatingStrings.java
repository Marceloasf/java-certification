package ocp.chapter.five;

public class ManipulatingStrings {

    public static void main(String[] args) {

        System.out.println("\n----------------String----------------\n");

        String string = "animals";

        System.out.println(string.charAt(0)); // a
        System.out.println(string.charAt(6)); // s

        // System.out.println(string.charAt(7)); // THROWS EXCEPTION

        System.out.println("\n----------------------------------------\n");

        System.out.println(string.indexOf('a')); // 0
        System.out.println(string.indexOf("al")); // 4
        System.out.println(string.indexOf('a', 4)); // 4
        System.out.println(string.indexOf("al", 5)); // -1
        
        System.out.println("\n----------------------------------------\n");

        /*
        / String 'array' structure
        /  [a][n][i][m][a][l][s]
        /  |  |  |  |  |  |  |  |
        /  0  1  2  3  4  5  6  7
        / If a method specifies a starting index of 0, 0 will be considered.
        / If a method specifies an ending index of 6, 6 won't be considered.
        / Index 7 will not give an out-of-bounds exception.
        / It looks the characters in between. Including the starting index and stoping before the ending index.
        */
        
        System.out.println(string.substring(3)); // mals
        System.out.println(string.substring(string.indexOf('m'))); // mals
        System.out.println(string.substring(3, 4)); // m
        System.out.println(string.substring(3, 7)); // mals 
        System.out.println(string.substring(3, 3)); // Empty string 

        // System.out.println(string.substring(3, 2)); // THROWS EXCEPTION
        // System.out.println(string.substring(3, 8)); // THROWS EXCEPTION 

        System.out.println("\n----------------------------------------\n");

        System.out.println("abcabc".replace('a', 'A')); // AbcAbc - char 
        System.out.println("abcabc".replace("a", "A")); // AbcAbc - CharSequence

        System.out.println("\n----------------------------------------\n");

        System.out.println("abc".strip()); // abc
        System.out.println("\t a b c".strip()); // a b c
        System.out.println("abc".trim()); // abc
        System.out.println("\t a b c".trim()); // a b c

        // _ means whitespace in the examples below

        String text = " abc\t ";
        System.out.println(text.trim().length()); // 3 - abc
        System.out.println(text.strip().length()); // 3 - abc
        System.out.println(text.stripLeading().length()); // 5 - abc\t_ 
        System.out.println(text.stripTrailing().length()); // 4 - _abc

        System.out.println("\n----------------StringBuilder----------------\n");

        StringBuilder sb1 = new StringBuilder("ani").append("mals"); // animals - append() is called directly after the contructor
        String sub = sb1.substring(sb1.indexOf("a"), sb1.indexOf("al"));
        int len = sb1.length();
        char ch = sb1.charAt(6);
        System.out.println(sub + " " + len + " " + ch); // anim 7 s

        System.out.println("\n----------------------------------------\n");

        StringBuilder sb2 = new StringBuilder("animals");
        sb2.insert(7, "-"); // animals-
        sb2.insert(0, "-"); // -animals-
        sb2.insert(4, "-"); // -ani-mals-
        System.out.println(sb2); // Will change the indexes

        System.out.println("\n----------------------------------------\n");

        StringBuilder sb3 = new StringBuilder("abcdef");
        sb3.delete(1, 3); // adef - removes bc (1 and 2) and stops right before d (3) - similar to substring String structure.
        // sb3.deleteCharAt(5); // THROWS EXCEPTION, this index is out of bonds after deleting ab (1 and 2).

        System.out.println(sb3); // adef

        StringBuilder sb4 = new StringBuilder("abcdef");

        // sb4.delete(2, 0); // THROWS EXCEPTION

        sb4.delete(1, 100); // a - will delete everything till the end of the String.
        sb4.delete(0, 0); // Will not delete a (identifies as an empty string like substring).

        System.out.println(sb4); // a
        
        System.out.println("\n----------------------------------------\n");

        // replace is different in StringBuilder - it replaces more than just a specified char (uses indexes).

        StringBuilder builder = new StringBuilder("pigeon dirty");
        builder.replace(3, 6, "sty");
        System.out.println(builder); // pigsty dirty

        builder.replace(3, 100, "");
        System.out.println(builder); // pig

        System.out.println("\n----------------Equality----------------\n");
        
        StringBuilder one = new StringBuilder();
        StringBuilder two = new StringBuilder();
        StringBuilder three = one.append("a");

        System.out.println(one == two); // false
        System.out.println(one == three); // true

        String x = "Hello";
        String z = " Hello   ".trim();

        System.out.println(x.equals(z)); // true - since Strings equals() method checks the values inside rather than the reference.

        String normalString = "a";
        StringBuilder normalBuilder = new StringBuilder("a");

        // System.out.println(normalString == normalBuilder); // DOES NOT COMPILE - different data types 
        System.out.println(normalString.equals(normalBuilder)); // false - stringbuilder is not equal to "a"
        System.out.println(normalString.equals(normalBuilder.toString())); // true
        
        System.out.println("\n----------------String-Pool----------------\n");

        // The String Pool contains `LITERAL VALUES` and `CONSTANTS` that are created in memory at compile-time (or with intern() at runtime)

        String x1 = "Hello World";
        String y1 = "Hello World";
        System.out.println(x1 == y1); // true - because both point to the same location in memory, which is the string pool value "Hello World".

        String x2 = "Hello World";
        String y2 = "Hello World    ".trim();
        System.out.println(x2 == y2); // false - because y2 computes it's value at runtime and creates a new String value.

        String x3 = "Hello World";
        String y3 = "Hello ";
        y3 += "World";
        System.out.println(x3 == y3); // false - Concatenation is just like calling a method and results in a new String.

        String x4 = "Hello World";
        String y4 = new String("Hello World");
        System.out.println(x4 == y4); // false - x4 says to use the string pool normally, the second creates a new String object.

        String x5 = "Hello World";
        String y5 = new String("Hello World").intern();
        System.out.println(x5 == y5); // true - Using the intern() tells Java to use the string pool and both values point to
                                      // the same reference in the String Pool, which is "Hello World".

        String x6 = "Hello World";
        String y6 = "Hello World".trim();
        System.out.println(x6 == y6); // true - both are pointing to the same reference in the Pool, even with the trim() method call

        String first = "rat" + 1; // compile-time literals
        String second = "r" + "a" + "t" + "1"; // compile-time literals
        String third = "r" + "a" + "t" + new String("1"); // runtime because we have a String contructor, creating a new object

        System.out.println(first == second); // true - same reference in the String Pool.
        System.out.println(first == second.intern()); // true - same reference in the String Pool, intern() call is useless but it works.
        System.out.println(first == third); // false - doesn't point to a reference in the String Pool.
        System.out.println(first == third.intern()); // true - intern() looks to the Pool Java notice they point to the same String and prints true.
    }
}