package ocp.chapter.eight;

class Penguin {
  public int getHeight() { return 3; }
  
  public void printInfo() {
    System.out.println(this.getHeight());
  }
}

public class PolymorphismOverridingExample extends Penguin {
  public int getHeight() { return 8; }

  public void printThis() {
      super.printInfo(); // Prints 8 - because the object is a PolymorphismExample and the method getHeight has been overridden to return 8
      this.printInfo(); // Prints 3 - because printInfo has been overriden on PolymorphismExample and it calls super.getHeight (3) and not the this.getHeight (8)
  }

  public void printInfo() {
    System.out.println(super.getHeight());
  }

  public static void main(String []fish) {
    new PolymorphismExample().printThis();
  }
}