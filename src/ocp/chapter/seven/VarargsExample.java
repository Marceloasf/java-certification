package ocp.chapter.seven;

public class VarargsExample {

    public static void walk(int start, int... nums) {
     
        System.out.println(nums.length);
    }

    public static void run(int... nums) {
     
        System.out.println(nums[1]); // Accessing a varargs parameter is just like accessing an array.
    }

    public static void main(String... args) {

        walk(1); // 0
        walk(1, 2); // 1
        walk(1, 2, 3); // 2
        walk(1, new int [] {4, 5}); // 2

        run(11, 22); // 22
    }
}