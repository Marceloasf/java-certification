package ocp.chapter.eight;

class Penguin {
  public static int getHeight() { return 3; }
  
  public void printInfo() {
    System.out.println(this.getHeight()); // Prints 3 - since the getHeight method isn't overridden, it's hidden. We can remove the this keyword in this case 
                                          // since getHeight is static.
  }
}

public class HidingExample extends Penguin {
  public static int getHeight() { return 8; }

  public static void main(String... fish) {
    new HidingExample().printInfo();
  }
}