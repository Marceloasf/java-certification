# Chapter 9 - Advanced Class Design

- Reusing Implementations Through Inheritance

  - Create and extend abstract classes

- Programming Abstractly Through interfaces
  - Create and implement interfaces
  - Distinguish class inheritance from interface inheritance including abstract classes

## Introducing Abstract Classes (p.367)

- An abstract class is a class that cannot be instantiated and may contain abstract methods.
- An abstract method is a method that does not define an implementation when it is declared.
  - Both abstract classes and methods are denoted with the 'abstract' modifier.
- A class that inherits an abstract class, must override the abstract methods of the superclass (all override rules from the last chapter apply).
- An abstract class can be initialized, but only as part of the instantiation of a nonabstract subclass.
- Only at runtime that the methods of an abstract class that have been overridden can be defined.
- Abstract classes can extends nonabstract classes.

## Defining Abstract Methods (p.368-370)

- An abstract class may include all of the same members as a nonabstract class (even constructors).
- It is not required to include any abstract methods in an abstract class.
- An abstract method can only be defined in an abstract class (or an interface).
- Remember that optional modifiers, such as abstract and final, can be placed before or after the access modifier in a class and method declarations.
- It is not possible to define an abstract method that has a body or default implementation.
- Abstract classes constructors work just like nonabstract classes constructors. The only difference is that an abstract class constructor can only
  be called when it is being initialized by a nonabstract class (super()).
- Even with abstract methods, all the rules for overriding methods must be followed.

* Note: Remember that abstract methods can't have a body and must be declared with at least like this:
  - access modifier - abstract modifier (the order between these first two can vary, abstract is REQUIRED) - return type - method name and ();

## Invalid Modifiers (p.371-372)

- abstract and final modifiers are an invalid combination of optional modifiers, since the final modifier won't let a class be extended and a method overridden.
- A method cannot be marked as both abstract and private, since a private method can't be inherited/overridden.
- abstract and static modifiers are an invalid combination of optional modifiers, since a static method is defined as belonging to the class, not an instance of
  the class, so it cannot be overridden (only hidden).
  - The compiler recognizes all this invalid combinations in the parent class and reports an error as soon as the invalid combinations are applied.

## Creating a Concrete Class (p.372-374)

- A concrete class is a nonabstract class.
- The first concrete subclass that extends an abstract class is required to implement all inherited abstract methods.
- If the first concrete subclass extends an abstract class that extends another abstract class too and that overrides an abstract method to being a nonabstract
  method, the first concrete class is not required to override this method, since is not abstract anymore and already has an implementation.

## Reviewing Abstract Class Rule (p.374-375)s

- Abstract Class Definition Rules:
  1. Abstract classes cannot be instantiated.
  2. All top-level types, including abstract classes, cannot be marked protected or private.
  3. Abstract classes cannot be marked final.
  4. Abstract classes may include zero or more abstract and nonabstract methods.
  5. An abstract class that extends another abstract class inherits all of its abstract methods.
  6. The first concrete class that extends an abstract class must provide an implementation for all of the inherited abstract methods.
  7. Abstract class constructors follow the same rules for initialization as regular constructors, except they can be called only as part of the
     initialization of a subclass.

* The following rules for abstract methods apply regardless of whether the abstract method is defined in an abstract class or interface.

- Abstract Method Definition Rules:
  1. Abstract methods can be defined only in abstract classes or interfaces.
  2. Abstract methods cannot be declared private, final or static (in all cases).
  3. Abstract methods must not provide a method body/implementation in the abstract supertype in which they are declared.
  4. Implementing an abstract method in a subclass follows the same rules for overriding a method, including covariant return types, exception
     declarations, etc.

## Implementing Interfaces (p.375-379)

- An interface is an abstract data type that declares a list of abstract methods that any concrete class implementing the interface must provide.
- An interface can also include constant variables.
- An interface can also include private, private static, static and default methods (they have a body).
- A default method is one in which the interface method has a body and is not marked abstract, it doesn't need to be overridden.
  - private, private static, static and default methods in interfaces will be covered in 1Z0-816 topics of the book.
- Both abstract methods and constant variables included within an INTERFACE are always implicitly assumed to be public.
- Java allows a class to implement any number of interfaces.
  Interface declaration in order of appearance:
  - Access modifier: public or default (package-private).
  - `Implicit` modifier: abstract.
  - Interface keyword: interface.
  - Interface name and brackets.
    Interface members:
    - For methods:
      - `Implicit` modifiers: public abstract
      - Return type: Integer or int for example.
      - Method name.
      - Parentheses, parameter list (optional) and semicolon. // (); or (int a);
    - For constant variables:
      - `Implicit` modifiers: public static final
      - Variable type: Integer or int for example.
      - Variable name.
      - Variable constant value assignment.
      * Notes on interface constants:
        - Since they are all public and static, they can be used outside the interface declaration without requiring
          an instance of the interface.
        - An instance of the interface, means the instance of a class that implements the interface.
        - Remember that interface constants must be initialized inside the interface.
- Interfaces are not required to define any methods.
- The abstract modifier is optional, because the compiler will insert it implicitly in the declaration if there is no explicit declaration of it.
- Interfaces can't be final for the same reason as abstract classes, since they are always abstract too.
- The concrete class that implements an interface, must declare the overridden methods as public, since they are all public (the ones without a body).
- Interfaces can extends other interfaces. Unlike a class, an interface can extend multiple interfaces.
- Interfaces are not part of instance initialization.
- Many of the rules for class declarations also apply to interfaces, including the following:
  1. A Java file may have at most one public top-level class or interface (top-level types), and it must match the name of the file.
  2. A top-level class or interface can only be declared with public or package-private access.

## Implicit Modifiers (p.379)

- The compiler will automatically insert these modifiers on the interface and interface methods declarations.
- You can choose to insert this implicit modifiers yourself or let the compiler insert them for you.
- List of implicit modifiers for the exam (1Z0-815):
  - Interfaces are assumed to be abstract.
  - Interface variables are assumed to be public, static and final.
  - Interface methods without a body are assumed to be abstract and public. // They must be overridden with an explicit public access modifier.
  * Note: Only interfaces have these implicit modifiers.

## Conflicting Modifiers (p.380-381)

- Examples of conflicts:

      private final interface Crawl {
        String distance;
        private int MAXIMUM = 1000;
        protected abstract boolean UNDERWATER = false;
        private void dig(int depth);
        protected abstract double depth();
        public final void surface();
      } // Every single line does not compile, since some modifiers aconflicting with the implicit modifiers and interface rules...

* Note for difference between interfaces and classes (p.381): Remember that ONLY interfaces make use of implicit modifiers.

## Inheriting an Interface (p.382-383)

- An interface can be inherited in one of three ways:
  - An interface can extends another interface(s).
  - A class can implements an interface(s).
  - A class can extend another class whose ancestor implements an interface.
- Just like abstract classes, the first concrete class (nonabstract) that inherits the interface, must implement all of the inherited ABSTRACT methods.

## Mixing Class and Interface Keywords (p.383)

- The following, are the only valid syntaxes for relating classes and interfaces in their declaration:
  - class1 extends class2
  - interface1 extends interface2, interface3, ...
  - class1 implements interface1, interface2, ...
  - class1 extends class 2 implements interface1, interface2 ...

## Duplicate Interface Method Declarations (p.384-386)

- When two methods have identical declaration on two (or more) implemented interfaces, they are considered compatible. By that, we mean that the compiler
  can resolve the differences between the two (or more) declarations without finding any conflicts.
- If two (or more) interface methods have identical behaviors (the same method declaration), you just need to be able to create a single method that
  overrides both inherited abstract methods at the same time.
- If two (or more) interface methods have the same name but different signatures, it is considered a method overload and there is no conflict.
  When overload happens, all the methods must be implemented by the concrete class, since they are considered separate methods.
- If the return types are different in interface methods, they must be covariant, or else the class that implements them will not compile. The compiler
  would also throw an exception if you define an abstract class or interface that inherits from two conflicting abstract types.

## Casting Interfaces (p.387)

- When casting two classes that implement the same interface but don't have any kind of relation between them, Java will not throw a compiler error, but
  will throw a ClassCastException after. This is one of the rules of casting discussed in Chapter 8, that the compiler does not allow casts to unrelated
  types, like String casting to Long, but with interfaces there are limitations to what the compiler can validate.

  - For ex.:

        interface Canine {}
        class Dog implements Canine {}
        class Wolf implements Canine {}

        ... method() {
          Canine canine = new Wolf();
          Canine badDog = (Dog)canine;
          }  // Compiles, but will throw a ClassCastException at runtime, because both classes don't have any kind of relation.

- But the compiler can enforce one rule around interface casting. The compiler does not allow a cast from an interface reference to an object reference if
  the object type does not implement the interface.
  - For ex.: `Object badDog = (String)canine; // Since String does not implements Canine, the compiler recognizes that this cast is not possible.`

## Interfaces and the instanceof Operator (p.388)

- With interfaces, the compiler has limited ability to enforce the rule of unrelated classes when using the intanceOf operator too, because even though a
  reference type may not implement an interface, one of its subclasses could.
- But the compiler can check for unrelated interfaces if the reference is a class that is marked final, like Integer.

## Reviewing Interface Rule (p.388-389)

- Interface Definition Rules:

  1. Interfaces cannot be instantiated.
  2. All top-level types, including interfaces, cannot be marked protected or private.
  3. Interfaces are assumed to be abstract and cannot be marked final.
  4. Interfaces may include zero or more abstract methods.

  - Note that this four rules are similar to the abstract class declaration rules that were seen before.

  5. An interface can extend any number of interfaces.
  6. An interface reference may be cast to any reference that inherits the interface, although this may produce an exception at runtime if the classes aren't
     related (ClassCastException).
  7. The compiler will only report an unrelated type error for an instanceof operation with an interface on the RIGHT side if the reference on the LEFT side
     is a final class that does not inherit the interface. Remember that the right side is composed by a supertype and the left side is a reference.
  8. An interface method with a body must be marked default, private, static or private static (covered on 1Z0-816).

- Abstract Interface Method Rules:

  1. Abstract methods can be defined only in abstract classes and interfaces.
  2. Abstract methods cannot be declared private, static or final.
  3. Abstract methods must not provide a method body/implementation in the abstract class/interface in which it's declared.
  4. Implementing an abstract method in a subclass follows the same rules for overriding a method, including covariant return types, exception declarations, etc.

     - Note that this four rules are similar to the abstract methods rules seen before.

  5. Interface methods without a body are assumed to be abstract and public.

- Interface Variable Rules:

  1. Interface variables are assumed to be public, static and final (public constants).
  2. Because interface variables are marked final, they must be initialized with a value when they are declared.

- Note: Remember that the primary differences between interfaces and abstract classes are that only interfaces include implicit modifiers, do not contain constructors,
  do not participate in the instance initialization process, and support multiple inheritance (classes only support single inheritance), remember that this isn't a true
  multiple inheritance, since Java enables only limited multiple inheritance for interfaces and classes can't extend multiple classes directly.

## Introducing Inner Classes (p.390-391)

- A member inner class is a class defined at the member level of a class (same level as methods, instance variables, etc). It can be declared only inside
  another class.
- For the 1Z0-816 exam, there are four types of nested classes you will need to know about (they will be covered further) member inner classes, local classes,
  anonymous classes and static nested classes.
- A class can have many inner classes and interfaces.
- While top-level classes and interfaces can only be set with public or package-private access, member inner classes do not have the same restriction. A member
  inner class, can be declared with all of the same access modifiers as a class member (even private abstract is allowed for the inner class declaration).
- A member inner class can contain many of the same methods and variables as a top-level class, but it cannot contain some members, such as static members.

- Notes after taking the review exam:
  - Remember that an abstract class may include all of the same members as a nonabstract class (even constructors).
  - The main() method can be included inside an abstract class.
  - Note that the methods can be overloaded inside instead of overridden, so there are some tricky questions that do this in the exam and the concrete will not compile,
    since the concrete class doesn't override all the abstract inherited methods. Abstract methods can be overloaded on abstract classes too, causing the same problem if
    the concrete class doesn't override the abstract method.
