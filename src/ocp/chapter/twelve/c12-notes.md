## Starts the 1Z0-816 and 1Z0-817 Chapters

# Chapter 12 - Java Fundamentals

- Java Fundamentals

  - Create and use final classes
  - Create and use inner, nested and anonymous classes
  - Create and use enumerations

- Java Interfaces

  - Create and use interfaces with default methods
  - Create and use interfaces with private methods

- Functional Interface and Lambda Expressions
  - Define and write functional interfaces
  - Create and use lambda expressions including statement lambdas, local-variable form lambda parameters

## Working with Enums (p.500-505)

- An enumeration (enum) is like a fixed set of constants. In Java, an enum, short for "enumerated type", can be a top-level type like
  a class or interface, as well as a nested type like an inner class.
- Using an enum is much better than using a bunch of constants because it provides type-safe checking. It is impossible to create an
  invalid enum value without introducing a compiler error.
- To create a simple enum, just use the enum keyword instead of the class or interface keyword. Then list all the valid types for him:

      public enum Season {
        WINTER, SPRING, SUMMER, FALL
      }

- Enum declarations don't support optional modifiers such as abstract and final (public abstract/final enum...).
- Enum values are considered constants and are commonly written using snake case, often stylized as snake_case, using underscore to separate
  words, like VANILLA, ROCKY_ROAD, MINT_CHOCOLATE...
- Inner enums are implicitly static.
- Behind the scenes, an enum is a type of class that mainly contains static members. It also includes some helper methods like name().

      Season s = Season.SUMMER;
      System.out.println(Season.SUMMER); // SUMMER
      System.out.println(s == Season.SUMMER); // true

- You can use equals() or == to compare enums, since each enum value is initialized only once in the Java Virtual Machine (JVM).
- An enum provides a values() method to get an array of all of the values. You can use this like any normal array, including in an for-each loop.
- Each enum value has a corresponding int value, in the order in which they are declared (starting from 0). You can get this value with enum.ordinal().
- Even having a corresponding int value, you can't compare an int and enum value directly anyway, since an enum is a type like a Java class.
  `if (Season.SUMMER == 2) { }` // DOES NOT COMPILE
- Its possible to retrieve an enum value from a String using the valueOf() method. The String passed in must match the enum value exactly.
  `Season s = Season.valueOf("SUMMER"); // SUMMER Season s = Season.valueOf("summer"); // Throws an exception at runtime`
- Note that the first line isn't creating an enum value, at least not directly. Each enum value is created once when the enum is first loaded. Once it has
  been loaded, it retrieves the single enum value with the matching name.
- The second example will throw an IllegalArgumentException since there is no enum value with the lowercase name summer.
- Two important rules about working with enums:
  - Enums can't be extended by other enums, classes or interfaces.
  - Only the values in an enum are allowed, because you can't add more by extending an enum.
- Enums can't extends other enums/classes/interfaces. But they can implements interfaces and use their methods, all you have to do is use a enum value as the object
  to call the inherited methods (Season.Summer.interfaceMethod();).

* Using Enums in Switch Statements

  - Remember that enum values can be used in switch statements.
  - The compiler already knows that the only possible matches can be enum values in this case. So Java treats the enum type as implicit. In fact, if you try to type case
    Season.WINTER, literal string or a primitive int, it does not compile. Example:
          Season summer = Season.SUMMER;
          switch(summer) {
            case SUMMER:
              System.out.println("Time for the pool!");
              break;
            case "SPRING":                            // DOES NOT COMPILE
              System.out.println("Spring time!");
              break;
            case Season.WINTER:                       // DOES NOT COMPILE
              System.out.println("Rake some leaves!");
              break;
            case 0:                                   // DOES NOT COMPILE
              System.out.println("Get out the sled!");
            default:
              System.out.println("Is it summer yet?");
          }
  - The first case statement does not compile, because a literal string is used in the case value.
  - The second case statement that does not compile, is because Season is used in the case value, if we changed it to WINTER, then it would compile.
  - The third case statement doesn't compile because you can't compare enums with int values, and you cannot use them in a switch case statement either.
  - So remember for the exam, enums can only be used with enums in switch statements.

* Adding Constructors, Fields and Methods

  - Enums can have more in them than just a list of values.
    - (Coded example on file Season.java)
  - A semicolon (;) is optional when your enum is composed solely of a list of values, but it is required if there is anything in the enum besides the values.
  - Marking instance variables from an enum with 'final' to make sure they are immutable is not required, but its considered a good coding practice to do so.

* Real World Scenario Notes:

  - The immutable objects pattern is an object-oriented design pattern in which an object cannot be modified after it is created. Instead of modifying an
    immutable object, you create a new object that contains any properties from the original object you want copied over.
  - Immutable objects are invaluable in concurrent application since the state of the object cannot change or be corrupted by a rogue thread.
  - String is an example of a Java immutable object.

* Continuing Enums Constructors, Fields and Methods:

  - All enum constructors are implicitly private, with the modifier being optional. This is reasonable since you can't extend an enum and the constructors
    can be called only within the enum itself. An enum constructor will not compile if it contains a public or protected modifier.
  - To call a method inside an enum you just need to ask for a enum value and call the method, like `Season.SUMMER.printExpectedVisitors();`.
  - Notice how we don't appear to call the constructor. We just ask for the enum value. Because the first time that we ask for any of the enum values, Java
    constructs all of the enum values. After that, Java just returns the already constructed enum values.
  - So remember that the constructor on an enum is called only once. Example:

        public enum OnlyOne {
          ONCE(true);
          private OnlyOne(boolean b) {
            System.out.println("constructing,");
          }
        }
        (...)
        public static void main(String[] args) {
          System.out.println("begin,");
          OnlyOne firstCall = OnlyOne.ONCE; // prints constructing,
          OnlyOne secondCall = OnlyOne.ONCE; // doesn't print anything
          System.out.println("end");
        } // This main() method prints 'begin,constructing,end'

  - If the OnlyOne enum was used earlier, and therefore initialized sooner, then the line that declares the firstCall variable would not print anything.
  - If the constructor on an enum has no parameters, then the values don't need to have parentheses. On the other hand, they are required if there is one
    or more parameters to the list.
  - We can let each enum value manage itself, like if they were a bunch of tiny subclasses.

        public enum Season {
          WINTER {
            public String getHours() { return "9am-3pm"; }
          },
          SPRING {
            public String getHours() { return "9am-5pm"; }
          },
          SUMMER {
            public String getHours() { return "9am-7pm"; }
          };

          public abstract String getHours();
        }

  - On this example, the enum itself has an abstract method, which means that each and every enum value is required to implement this method. If we forget to
    implement the method for one of the values, then we get a compiler error.
  - Another way of implementing this is creating a default implementation and override it only for the special cases.

        public enum Season {
          WINTER {
            public String getHours() { return "9am-3pm"; }
          },
          SPRING,
          SUMMER,
          FALL {
            public String getHours() { return "9am-7pm"; }
          };

          public String getHours() { return "9am-5pm"; }
        }

  - Good practices reminder, try to keep your enums simple. If it has more than one page or two, its too long.
  - The compiler requires that the list of values in enums always be declared first, wheter the enum is simple or contains a ton of members.
  - When overriding abstract methods from implemented interfaces in enums, we can handle them in the same manner, overriding the abstract method for all the values or for
    some/none of the values. For the second case, we need to override the abstract method for the enum like getHours in Season for the code to compile.

## Creating Nested Classes (p.506-514)

- A nested class is a class that is defined within another class.
- Types of nested class:
  - Inner class: A non-static type defined at the member level of a class.
  - Static nested class: A static type defined at the member level of a class.
  - Local class: A class defined within a method body.
  - Anonymous class: A special case of a local class that does not have a name.
- Nested classes can encapsulate helper classes by restricting them to the containing class, can make it easier to create a class that will be used in only one place,
  and can make the code cleaner and easier to read.
- By convention, the term inner or nested class is used to apply to other Java types, including interfaces and enums. Because interfaces and enums can be declared as
  both inner classes and static nested classes, but not as local or anonymous classes.
- Inner enums are implicitly static and inner interfaces are implicitly abstract and static. Both can have all access modifier options. Watch out for conflicting modifiers.
- Inner classes can have inner classes, its not a good coding practice, but it may appear on the exam.

* Declaring an Inner Class:

  - Also known as a member inner class, is a non-static type defined at the member level of a class.
  - Inner classes have the following properties:
    - Can be declared public, protected, package-private (default) or private.
    - Can extend any class and implement interfaces.
    - Can be marked abstract or final.
    - Cannot declare static fields or methods, except for static final fields (constants).
    - Can access members of the outer class, including private members (even constructors).
  - You can instantiate an inner class from the parent class where its located, from a method for example.
  - There is another way to instantiate it, for example:

        public static void main(String[] args) {
          Outer outer = new Outer();
          Inner inner = outer.new Inner(); // create the inner class
          inner.go();
        }

  - Basically, we need an instance of Outer to create Inner, we can't just call new Inner() because Java won't know with which instance of Outer it is associated. Java
    solves this by calling new as if it were a method on the outer variable.
  - When a inner class is compiled, two files are created, one for the outer class and another for the inner class. Like this: Outer.class - Outer$Inner.class
  - Inner classes can have the same variable names as outer classes, making scope a little tricky. There is a special way of calling 'this' to say which variable you want:

        public class A {
          private int x = 10;

          class B {
            private int x = 20;

            class C {
              private int x = 30;

              public void allTheX() {
                System.out.println(x); // 30
                System.out.println(this.x); // 30
                System.out.println(B.this.x); // 20
                System.out.println(A.this.x); // 10
              }
            }
          }
          public static void main(String... args) {
            A a = new A();
            A.B b = a.new B();
            A.B.C c = b.new C();
            c.allTheX();
          }
        }

  - In fact, you are not limited to just one inner class. Please never do this in your code. But yes, you can nest multiple classes and access a variable with the same
    name in each.
  - If we wanted, we could use B directly to instantiate because it is on the member level of the outer class, but C can't be used directly to instantiate, because C is
    too deep for Java to know where to look.
  - Inner classes require an instance. Static methods can't access them, unless you're using an instance explicitly.
  - private inner classes can only be instantiated and accessed from within their outer class.
  - Inner interfaces and enums can contain all their usual members.

* Creating a static Nested Class:

  - A static nested class is a static type defined at the member level.
  - Unlike an inner class, a static nested class can be instantiated without an instance of the enclosing class.
  - The trade-off is it can't access instance variables or methods in the outer class directly. It can be done, but requires an explicit reference to an outer class var.
  - In other words, it is like a top-level class, except for the following:
    - The nesting creates a namespace because the enclosing class name must be used to refer to it.
    - It can be made private or use one of the other access modifiers to ecanpsulate it.
    - The enclosing class can refer to the fields and methods of the static nested class.
  - Example of a static nested class:

        public class Enclosing {
          static class Nested {
            private int price = 6;
          }

          public static void main(String[] args) {
            Nested nested = new Nested();
            System.out.println(nested.price); // You don't need an instance of Enclosing to use the Nested class, since     the class is static.
          }
        }

  - You are allowed to access private instance variables from the static nested class.
  - Importing a static nested class has two ways of doing it, first you can import it using a regular import, and the other way is using a static import:

    - `import bird.Toucan.Beak;`
    - `import static bird.Toucan.Beak;`

      - Toucan is the enclosing class and Beak is the static inner class.

  - Remember that Java treats the enclosing class as if it were a namespace.

* Writing a Local Class:

  - A local class is a nested class defined within a METHOD, CONSTRUCTOR or INITIALIZER. Like local variables, a local class declaration does not exist until the block
    is invoked, and it goes out of scope when the block ends.
  - You can create an instance only from within the method, those instances can still be returned from the method.
  - Remember that they are not limited to being declared only inside methods. They can be declared inside constructors and initializers too.
  - They can be marked as final or abstract.
  - Local classes have the folowing properties:
    - They do not have an access modifier.
    - They cannot be declared static and cannot declare static fields or methods, except for static final fields (constants).
    - They have access to all fields and methods of the enclosing class (when defined in an instance method).
    - They can access local variables if the variables are final or effectively final.
      - As we saw earlier in lambdas, effectively final refers to a local variable whose value does not change after it is set (even if not marked final).
  - Why local variables need to be final or effectively final? Because the compiler is generating a .class file from your local class. A separate class has no way to
    refer to local variables, so if the local variable is final, Java can handle it by passing it to the constructor of the local class or by storing it in the .class
    file, if it weren't effectively final, these tricks wouldn't work because the value could change after the copy was made. An example of this:

          public void processData() {
            final int length = 5;
            int width = 10;
            int height = 2;

            class VolumeCalculator {
              public int multiply() {
                return length * width * height; // DOES NOT COMPILE, because width value changes after is set.
              }
            } // Semicolon is optional for Local classes...

            width = 2;
          }

* Defining an Anonymous Class:

  - An anonymous class is a specialized form of a local class that does not have a name.
  - It is declared and instantiated all in one statement using the new keyword, a type name with parentheses and a set of braces {}.
  - Are required to extend an existing class or implement an existing interface.
  - They are useful when you have a short implementation that will not be used anywhere else.
  - Example of an anonymous class extending a class:

        public class ZooGiftShop {
          abstract class SaleTodayOnly {
            abstract int dollarsOff();
          }

          public int admission(int basePrice) {
            SaleTodayOnly sale = new SaleTodayOnly() {
              int dollarsOff() { return 3; }
            }; // Semicolon is required!

            return basePrice - sale.dollarsOff();
          }
        }

  - Even SaleTodayOnly being abstract, we can instantiate her, because we are declaring the anonymous class body that extends her inside the braces.
  - Example of an anonymous class implementing an interface:

        public class ZooGiftShop {
          interface SaleTodayOnly {
            int dollarsOff();
          }

          public int admission(int basePrice) {
            SaleTodayOnly sale = new SaleTodayOnly() {
              public int dollarsOff() { return 3; }
            }; // Semicolon is required!

            return basePrice - sale.dollarsOff();
          }
        }

  - You can notice how little it changed, the main difference is that the method dollarsOff() is required to be declared public on the anonymous
    class implementation. Since interface abstract methods require public instead of default access.
  - Just remember that `SaleTodayOnly sale` that has been created is always an instance of a class instead of an interface.
  - You can't implement both an interface and extend a class with anonymous classes.
  - So remember that an anonymous class is just an unnamed local class, you can write a local class and give it a name if you want to extend a class and implement
    many interfaces.
  - You can define them right where they are needed, even if that is an argument to another method. For example:

        public class ZooGiftShop {
          interface SaleTodayOnly {
            int dollarsOff();
          }

          public int pay() {
            return admission(5, new SaleTodayOnly() {
              public int dollarsOff() { return 3; }
            });
          }

          public int admission(int basePrice, SaleTodayOnly sale) {
            return basePrice - sale.dollarsOff();
          }
        }

  - You can even define anonymous classes outside a method body.
    `public class Gorilla { interface Climb {} Climb climbing = new Climb() { }; // Remember, we are not creating a reference to an interface instance, the { } indicates that we are creating an anonymous // class that implements the Climb interface. }`
  - Prior to Java 8, anonymous classes were frequently used for asynchronous tasks and event handlers. For example an anonymous class being used as an event handler
    in a JavaFX application:
          Button redButton = new Button();
          redButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
              System.out.println("Red button pressed!");
            }
          });
  - Since Java 8, lambda expressions are a much more concise way of expressing the same thing:

        Button redButton = new Button();
        redButton.setOnAction(e -> System.out.println("Red button pressed!"));

  - The only restrictions when using anonymous classes and lambda expressions, is that the variable type must be a functional interface.

## Reviewing Nested Classes (p.515-516)

- For the exam, you'll need to know about which syntax and access rules are permitted in Java on nested classes.
- Modifiers in nested classes:

  | Permitted Modifiers | Inner class | static nested class | Local class | Anonymous class |
  | :------------------ | :---------- | :------------------ | :---------- | :-------------- |
  | Access modifiers    | All         | All                 | None        | None            |
  | abstract            | Yes         | Yes                 | Yes         | No              |
  | final               | Yes         | Yes                 | Yes         | No              |
  |                     |             |                     |             |                 |

- Members in nested classes:

  | Permitted Members  | Inner class    | static nested class | Local class    | Anonymous class |
  | :----------------- | :------------- | :------------------ | :------------- | :-------------- |
  | Instance methods   | Yes            | Yes                 | Yes            | Yes             |
  | Instance variables | Yes            | Yes                 | Yes            | Yes             |
  | static methods     | No             | Yes                 | No             | No              |
  | static variables   | Yes (if final) | Yes                 | Yes (if final) | Yes (if final)  |
  |                    |                |                     |                |                 |

- Nested class access rules:
  | Permitted Modifiers               | Inner class | static nested class | Local class            | Anonymous class                         |
  | :-------------------------------- | :---------- | :------------------ | :--------------------- | :-------------------------------------- |
  | Can extend any class or implement | Yes         | Yes                 | Yes                    | No, must have exactly one superclass    |
  | any number of interfaces          |             |                     |                        | or one interface                        |
  |                                   |             |                     |                        |                                         |
  | Can access instance members of    | Yes         | No                  | Yes (if declared in an | Yes (if declared in an instance method) |
  | enclosing class without a ref     |             |                     | instance method)       |                                         |
  |                                   |             |                     |                        |                                         |
  | Can access local variables of     | N/A         | N/A                 | Yes (if final or       | Yes (if final or effectively final)     |
  | enclosing method                  |             |                     | effectively final)     |                                         |
  |                                   |             |                     |                        |                                         |

### Understanding Interface Members (p.516-526)

- When Java first release, there were only two types of members an interface declaration could include: abstract methods and constant (static final) variables.
  Since Java 8 and 9, four new method types have been added that will be covered in this section.

  |                       | Since Java version | Membership type | Required modifiers | Implicit modifiers  | Has value or body? |
  | :-------------------- | :----------------- | :-------------- | :----------------- | :------------------ | :----------------- |
  | Constant variable     | 1.0                | Class           | -                  | public static final | Yes                |
  | Abstract method       | 1.0                | Instance        | -                  | public abstract     | No                 |
  | Default method        | 8                  | Instance        | default            | public              | Yes                |
  | Static method         | 8                  | Class           | static             | public              | Yes                |
  | Private method        | 9                  | Instance        | private            | -                   | Yes                |
  | Private static method | 9                  | Class           | private static     | -                   | Yes                |
  |                       |                    |                 |                    |                     |                    |

- This section will cover the newer interface member types, because the constant variables and abstract methods were covered before.

### Relying on a default Interface Method

- A default method is a method defined in an interface with the default keyword and includes a method body.
- May be overridden by a class implementing the interface. The class has the option of overriding the default method, but if it does not, then the default
  implementation will be used.
- The modifier name 'default' comes from the concept that it is viewed as an abstract interface method with a default implementation (has no relation with both the switch
  default and default (package-private) access modifier).
- Must be overridden using the public access modifier (is implicitly added on the interface), a different access modifier is not allowed.
- Default method implementation example:

      public interface IsWarmBlooded {
        boolean hasScales(); // abstract method - concrete class must implement
        default double getTemperature() { // default method - concrete class may implement
          return 10.0;
        }
      }

- Default Interface Method Definition Rules:

  1. A default method may be declared only within an interface.
  2. A default method must be marked with the default keyword and include a method body.
  3. A default method is assumed to be public.
  4. A default method cannot be marked abstract, final or static.
  5. A default method may be overridden by a class that implements the interface.
  6. If a class inherits two or more default methods with the same method signature, then the class must override the method.

* Rule six discussion (duplicate inherited default methods):

  - Rule six will make the compiler report an error on the class declaration line, unless the method has been overridden.
  - This rule holds true even for abstract classes because the duplicate method could be called within a concrete method within the abstract class.
  - These rules also apply to methods with the same signature but different return types or declared exceptions.
  - By overriding the conflicting method, the ambiguity about which version of the method to call is removed.
  - If a default method is overridden in the concrete class, then it must use a declaration that is compatible, following the rules for overriding methods.

* Calling a Hidden Default Method:

  - super.method() can't be used with interface methods, since an interface is not part of the class hierarchy.
  - When you have two default methods on two distinct interfaces and that a class implements both, this is where a default method exhibits properites of both a static
    and a instance method. For example:

          public class Cat implements Walk, Run {
            public int getSpeed() {
              return 1;
            }

            public int getWalkSpeed() {
              return Walk.super.getSpeed(); // This is how you call one of the default implementations, not with super.   getSpeed() or Walk.getSpeed().
            }

            public static void main(String[] args) {
              System.out.print(new Cat().getWalkSpeed());
            }
          }

  - First we use the interface name, followed by the super keyword, followed by the default method we want to call.
  - super call to the default method is placed inside an instance method, as super is not accessible in the main method.
  - Remember for the exam, default methods may only be declared within interfaces.

### Using static Interface Methods

- These methods are defined EXPLICITLY with the static keyword and for the most part behave just like static methods defined in classes.
- Static Interface Method Definition Rules:

  1. A static method must be marked with the static keyword and include a method body.
  2. A static method is assumed to be public.
  3. A static method cannot be marked abstract or final.
  4. A static method is not inherited and cannot be accessed in a class implementing the interface without a reference to the interface name.

- Static interface method example:

      public interface Hop {
        static int getJumpHeight() {
          return 8;
        }
      }

- Works just like a static method as defined in a class. So it can be accessed without an instance of a class using the Hop.getJumpHeight() syntax.
- Remember that since the method is defined without an access modifier, the compiler will automatically insert the public access modifier.
- Without an explicit reference to the name of the interface (getJumpHeight()), the code will not compile, even if you to call it from a class that implements Hop.
- The static methods are not inherited by a class implementing the interface! As they would if the method were defined in a parent class (extends).
- Compiling code example:
  `public class Bunny implements Hop { public void printDetails() { return System.out.println(Hop.getJumpHeight()); } }`
- Java "solved" the multiple inheritance problem of static interface methods by not allowing them to be inherited.
- This applies to both subinterfaces and classes that implement that interface! No one inherits the static methods. For example, a class that implements two
  interfaces containing static methods with the same signature will still compile.

* New Java 9 interface methods bellow (p.522-524)

### Introducing private Interface Methods

- They cannot be used outside the interface definition.
- They also cannot be used in static interface methods without a static method modifier.
- They were added on Java 9 to be used to reduce code duplication. For example, if we had a logger piece of code that needed to be inside a bunch of default methods,
  prior to 9 we would need to duplicate the code in the methods, but since 9 we can have private methods with this kind of specific code that can be reused.
- Example of working code:

      public interface Schedule {
        default void wakeUp() { checkTime(7); }
        default void haveBreakfast() { checkTime(9); }
        default void haveLunch() { checkTime(12); }
        default void workOut() { checkTime(18); }
        private void checkTime(int hour) {
          if (hour > 17) {
            System.out.println("Late!");
          } else {
            System.out.println("You have " + (17-hour) + " hours left to make the appointment.");
          }
        }
      }

- Private Interface Method Definition Rules:

  1. A private interface method must be marked with the private modifier and include a method body.
  2. A private interface method may be called only by default and private (all non-static) methods within the interface definition.

- They look a lot like instance methods within a class. Like private methods in a class, they cannot be declared abstract since they are not inherited.

### Introducing private static Interface Methods

- Has the same purpose as private methods, to reduce duplication. But this time is meant to reduce duplication inside static methods within the interface declaration.
- But remember, private static interface methods can also be accessed by default and private methods that are non-static.
- Example of working code:

      public interface Swim {
        private static void breathe(String type) {
          System.out.println("Inhale");
          System.out.println("Performing stroke: " + type);
          System.out.println("Exhale");
        }
        static void butterfly() { breathe("butterfly"); }
        public static void freestyle() { breathe("freestyle"); }
        default void backstroke() { breathe("backstroke"); }
        private void breaststroke() { breathe("breaststroke"); }
      }

- Private Static Interface Method Definition Rules:
  1. A private static interface method must be marked with the private and static modifiers and include a method body.
  2. A private static interface method may be called only by other methods within the interface definition.
- They can be called by any methods from within the interface definition. On the other hand, private static methods cannot call default or private methods, this would
  be like trying to access an instance method from a static method in a class.
- Why mark these methods with private?
  - The answer is to improve encapsulation and reduce duplication, as we might not want these methods exposed outside the interface declaration and duplicated all around her.
  - Encapsulation and security work best when the outside caller knows as little as possible about the internal implementation of a class or an interface.
  - Maintaining code is much more effective and efficient when you need to fix a problem only on one place.

### Reviewing Interface Members

- Interface Member Access:

|                       | Accessible from default and private methods within the interface definition? | Accessible from static methods within the interface definition? | Accessible from instance methods implementing or extending the interface? | Accessible outside the interface without an instance of interface? |
| :-------------------- | :--------------------------------------------------------------------------- | :-------------------------------------------------------------- | :------------------------------------------------------------------------ | :----------------------------------------------------------------- |
| Constant variable     | Yes                                                                          | Yes                                                             | Yes                                                                       | Yes                                                                |
|                       |                                                                              |                                                                 |                                                                           |                                                                    |
| abstract method       | Yes                                                                          | No                                                              | Yes                                                                       | No                                                                 |
|                       |                                                                              |                                                                 |                                                                           |                                                                    |
| default method        | Yes                                                                          | No                                                              | Yes                                                                       | No                                                                 |
|                       |                                                                              |                                                                 |                                                                           |                                                                    |
| private method        | Yes                                                                          | No                                                              | No                                                                        | No                                                                 |
|                       |                                                                              |                                                                 |                                                                           |                                                                    |
| static method         | Yes                                                                          | Yes                                                             | Yes                                                                       | Yes                                                                |
|                       |                                                                              |                                                                 |                                                                           |                                                                    |
| private static method | Yes                                                                          | Yes                                                             | No                                                                        | No                                                                 |

- The first two columns are about accessing from within the interface definition, the last two are about accessing from outside the interface.
- Recall what you know about class access modifiers and private members, instance methods can access static members within the class, but static members cannot access
  instance methods without a reference to the instance. Also, private and static interface members are never inherited, so they are never accessible directly by a class
  implementing the interface or a interface extending the interface.
- When an interface X extends another interface Y that contains an abstract method, X can override this method to be a default method.

### Abstract classes vs. Interfaces

- A key distinction is that interfaces do not implement constructors and are not part of the class hierarchy. While a class can implement multiple interfaces, it can
  only directly extend a single class, and an interface can extends multiple interfaces.

## Introducing Functional Programming (p.526-537)

- Functional interfaces are used as the basis for lambda expressions in functional programming.
- A functional interface is an interface that contains a single abstract method (SAM).
- A lambda expression is a block of code that gets passed around, sort of like an anonymous class that defines one method. There is a variety of short and long forms to it.

### Defining a Functional Interface

- Example of a functional interface and a class that implements it:

      @FunctionalInterface // This annotation is optional, just states that this interface is intended to be a functional interface.
      public interface Sprint {
        public void sprint(int speed);
      }

      public interface Dash extends Sprint { }

      public class Tiger implements Dash {
        public void sprint(int speed) {
          System.out.println(speed);
        }
      }

- Sprint and Dash are functional interfaces because they contain exactly one abstract method and the Tiger class is a valid class that implements the interface Dash.
- Interfaces that have only one abstract method, even if the method is inherited, are functional interfaces. Functional interfaces can define a lot of other methods and
  still be considered a functional interface, since there is only one abstract method defined.
- If a interface has already one abstract method and extends another interface that has one or more abstract methods, then the interface is not a functional interface.
- Even though default methods on interfaces function like abstract methods, in that they can be overridden by a concrete class implementing the interface, they are
  insufficient for satisfying the single abstract method requirement.

### Declaring a Functional Interface with Object Methods (Object.java)

- All classes inherit certain methods from Object. For the exam, you should be familiar with the following Object method declarations:
  - String toString()
  - boolean equals(Object)
  - int hashCode()
- This methods are brought here now because there is one exception to the SAM rule that you should be familiar with. The exception is that if a functional interface
  includes an abstract method with the same signature as a public method found in Object, then those methods do not count toward the SAM test. The motivation behind
  this rule is that any class that implements the interface will extend Object, as all classes do, and therefore always implements these methods.
- Since Java assumes all classes extend from Object, you also cannot declare an interface method that is incompatible with Object. For example, declaring an abstract
  method `int toString()` in an interface would not compile since Object's version of the method returns a String.
- Example of this rule:

      public interface Soar { // This is not considered a functional interface, since toString is a public method of    Object.
        abstract String toString(); // Remember, if we change this return type the code will not compile.
      }

- On the other hand, this example is considered a functional interface:

      public interface Dive {
        String toString(); // Doesn't count toward the SAM test.
        public boolean equals(Object o); // Doesn't count toward the SAM test.
        public abstract int hashCode(); // Doesn't count toward the SAM test.
        public void dive(); // It counts toward the SAM test as the single abstract method of this interface.
      }

- Changing only the signature of those methods listed before will make them count on the SAM test, since they don't match the signature of Object's public methods.
  This can be looked at as redeclaring those methods, making them individual methods that have no relation with Object's methods. For example:

  `String toString(Boolean b);`

  `int toString(Boolean b);`

  `Integer hashCode(String hash);`

        public interface Hibernate { // Not considered a functional interface.
          String toString(); // Doesn't count toward the SAM test.
          public boolean equals(Hibernate h); // It counts toward the SAM test.
          public abstract int hashCode(); // Doesn't count toward the SAM test.
          public void rest(); // It counts toward the SAM test.
        }

- It's not an override of the methods, so the signatures must be the same, any change on the signature will have the effect shown before. If the return type is different,
  then the code will not compile. You can't override the methods from Object in default interface methods.

### Notes on Overriding toString(), equals(Object) and hashCode() (p.529)

- While knowing how to properly override this methods was part of Java certification exams prior to Java 11, this requirement was removed on all of the Java 11 exams.
- As a professional Java developer, it is important for you to know at least the basic rules for overriding each of these methods:
  - toString(): Is called when you try to print an object or concatenate the object with a String. It is commonly overridden with a version that prints a unique
    description of the instance using its instance fields.
  - equals(Object): Is used to compare objects, with the default implementation just using the == operator. You should override the equals(Object) method anytime
    you want to conveniently compare elements for equality, especially if this requires checking numerous fields.
  - hashCode(): Any time you override equals(Object), you must override hashCode() to be consistent. This means that for any two objects, if a.equals(b) is true, then
    a.hashCode() == b.hashCode() must also be true. If they are not consistent, then this could lead to invalid data and side effects in hash-based collections such as
    HashMap and HashSet.
- All of these methods provide a default implementation in Object, but to make intelligent use out of them, you should override them.

### Implementing Functional Interfaces with Lambdas

- Remember that <T> (generics) allows the interface to take an object of a specified type.
- The relationship between functional interfaces and lambda expressions is as follows:

  - `Any functional interface can be implemented as a lambda expression`.

- Even older Java interfaces that pass the SAM test are functional interfaces, which can be implemented with lambda expressions.
- Code example on TraditionalSearch.java, this code has the goal to print out all animals in a list according to some criteria.
- Lambda expressions rely on the notion of deferred execution.
- Deferred Execution means that the code is specified now but runs later. In the case of TraditionalSearch, later is when the print() method calls it. Even though the
  execution is deferred, the compiler will still validate that the code syntax is correct.

### Writing Lambda Expressions

- The syntax of lambda expressions is tricky because many parts are optional.
- The left side of the lambda expression lists the variables, it must be compatible with the type and number of input parameters of the functional interface's SAM.
- The right side of the lambda expression represents the body of the expression, it must be compatible with the return type of the functional interface's SAM. For
  example, if the abstract method returns int, then the lambda expression MUST return an int, a value that can be implicitly cast to an int or throw an exception.
- The short form of our interface (Predicate) has three parts omitting optional parts:

  `a -> a.canHop()`

  - From left to right:
    1. A single parameter specified with the name a.
    2. The arrow operator to separate the parameter and body.
    3. A body that calls a SINGLE method and returns the result of that method.

- Now let's look at a more verbose version of this lambda expression, it also contain three parts including optional parts:

  `(Animal a) -> { return a.canHop(); }`

      1. A single parameter specified with the name a and stating the type is Animal (parentheses in this case are required).
      2. The arrow operator to separate the parameter and body.
      3. A body that has one or more lines of code inside braces, that includes a semicolon and a return statement.

- The parentheses can be omitted only if there is a single parameter and its type is not explicitly stated.
- The braces can be omitted when we have only a single statement. When no braces are used, Java doesn't require you to type return or use a semicolon.
- Remember this for the exam, `s -> {}` is a valid lambda. If the return type of the functional interface is void, then you don't need the semicolon or return statement.
- Some examples of valid lambda expressions, assuming that there are functional interfaces that can consume them:

  `() -> new Duck()`

  `d -> {return d.quack();}`

  `(Duck d) -> d.quack()`

  `(Animal a, Duck d) -> d.quack()`

- Now some examples of invalid lambda expressions and the explanation why they aren't valid:

  `a, b -> a.startsWith("test")`

  - This lambda require the parentheses around the parameter list.

  `Duck d -> d.canQuack();`

  - This lambda require the parentheses around the parameter list. Since the parentheses are only optional when there is one parameter and no type declared.

  `a -> { a.startsWith("test"); }`

  - Is missing the return keyword, which is required since we said the lambda must return a boolean.

  `a -> { return a.startsWith("test") }`

  - Is missing the semicolon inside of the braces ({ }).

  `(Swan s, t) -> s.compareTo(t) != 0`

  - Is missing the parameter type for t. If the parameter type is specified for one of the parameters, then it MUST be specified for all of them.

### Working with Lambda variables

- Variables can appear in three places with respect to lambdas:
  - The parameter list.
  - Local variables declared inside the lambda body.
  - Variables referenced from the lambda body.

* Parameter List

  - Since specifying the type of parameters is optional, var can be used in a lambda parameter list. All three of these statements are interchangeable:

    `Predicate<String> p = x -> true;`
    `Predicate<String> p = (var x) -> true;`
    `Predicate<String> p = (String x) -> true;`

  - If a functional interface var, that takes a generic type, is declared without a diamond operator (<T>), then the default type is always Object.
  - A lambda infers the types from the surrounding context. That means that you need to do the same on the exam. In this case, the lambda is being assigned to a Predicate
    that takes a String. Another place to look for the type is in a method signature, for example:

          public void whatAmI() {
            test((var x) -> x > 2, 123); // the lambda `var x` inferred type is Integer, since the test method signature    requires a Predicate<Integer>.
          }

          public void test(Predicate<Integer> c, int num) {
            c.test(num);
          }

  - There are some cases where you don't even need the method signature to determine the type:

        public void counts(List<Integer> list) {
          list.sort((var x, var y) -> x.compareTo(y)); // The answer is Integer, since we are sorting a list, we can use the type of the list to determine the lambda params types.
        }

* Restrictions on Using var in the Parameter List

  - While you can use var inside a lambda parameter list, there is one rule you need to be aware of.
  - If var is used for one of the types in the parameter list, then it must be used for all parameters in the list.
  - Examples of lambda expressions that compiles and doesn't compile:

    `(var num) -> 1` // COMPILES

    `(var a, var b) -> "Hello"` // COMPILES

    `(var b, var k, var m) -> 3.14159` // COMPILES

    `var w -> 99` // DOES NOT COMPILE, because the parentheses are missing.

    `(var x, y) -> "goodbye"` // DOES NOT COMPILE, because even when using var for all the parameter types, each parameter type must be written out.

    `(var a, Integer B) -> true` // DOES NOT COMPILE, because the parameter types include a mix of var and type names.

    `(String x, var y, Integer z) -> true` // DOES NOT COMPILE, because of the same reason of the last example. All the params must be var or another types.

### Local Variables Inside the Lambda Body

- While it is most common for a lambda body to be a single expression, it is legal to define a block. That block can have anything that is valid in a normal Java block,
  including local variable declarations.

- The following code does just that:

  `(a, b) -> { int c = 0; return 5; }`

  - Note: When writing your own code, a lambda block with a local variable is a good hint that you should extract that code into a method.

- Java doesn't allow you to create a local variable with the same name as one already declared in that scope, this applies to lambdas too, for example:

  `(a, b) -> { int a = 0; return 5; }` // DOES NOT COMPILE, a is already declared in the parameter list.

- Remember when declaring a lambda variable inside a method for example, that the semicolon on the end of the statement is required:

  `Predicate<Integer> p1 = a => {return true;}` // DOES NOT COMPILE
  `Predicate<Integer> p1 = a => {return true;};` // COMPILES

### Variables Referenced from the Lambda Body

- Lambda bodies are allowed to use static variables, instance variables and local variables if they are FINAL or EFFECTIVELY FINAL. If not, the code does not compiles on the
  line of the lambda expression that uses the variables. Lambdas follow the same rules for vars access as local and anonymous classes! This is not a coincidence, as behind the
  scenes, anonymous classes are used for lambda expressions.
