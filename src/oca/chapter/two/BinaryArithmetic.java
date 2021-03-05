package oca.chapter.two;

public class BinaryArithmetic {

    public static void main(String[] args) {

        // Numeric promotion rules examples, this rules applies to any binary arithmetic operations (+, -, /, %).
        
        short a = 1;
        short b = 2;

        /* Smaller data types (byte, short and char) are promoted to int,
        /  so the result of a + b is a int value. 
        */
        int c = a + b;

        System.out.println(c);

        short x = 14;
        float y = 14;
        double z = 4;

        /* Promotes x to int, then promotes x to float and multiplies x and y,
        /  then promotes x and y result (float type) to double, so it can finnaly
        /  be divided with z, resulting in a double value.
        */ 
        double r = x * y / z;

        System.out.println(r);

        /* Obs.: Applying a unary operator (ex.: ++ or --) to a short value results in a short value,
        /  unary operators are excluded from the third rule.
        */
        short u = 1;

        System.out.println(++u); 
    }
}
