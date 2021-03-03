package chapter.one;

public class Chick {

    private void printName() {
        System.out.println("Field 'name' has the value: " + name);
    }

    private String name = "Fluffy";
    private long number = 2_147_483_648l;

    { System.out.println("Setting field 'name' with value: " + name); }

    public Chick() {
        name = "Tiny";
        System.out.println("Setting Contructor...");
    }

    public static void main(String[] args) {
        Chick chick = new Chick();

        System.out.println(chick.name);
        System.out.println(chick.number);

        chick.printName();
    }
}