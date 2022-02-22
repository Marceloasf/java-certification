# Chapter 8 - Class Design

- Creating and Using Methods

  - Create methods and contructors with arguments and return values

- Reusing Implementations Through Inheritance

  - Create and use subclasses and superclasses
  - Enable polymorphism by overriding methods
  - Utilize polymorphism to cast and call methods, differentiating object type versus reference type
  - Distinguish overloading, overriding and hiding

## Understanding Inheritance (p.298-299):

- Is the process by which a subclass automatically includes any public or protected members of the class, including primitives,
  objects or methods, that are defined in the parent class.
- Package-private members are only available if the child class is in the same package as the parent class.
- private members are restricted to the class they are defined in and are never available via inheritance. This means that the
  direct access to them is restricted, but other members with more lenient access modifiers can modify and access them.

## Single vs. Multiple Inheritance (p.299-300):

- Java allows a class Z to inherit from a class X and if that class X inherit from a class Y, then class Z will inherit the members of
  X and Y.
- But it doesn't support multiple inheritance like this: `public class C extends A, B {}`. Because it can lead to complex often
  difficult-to-maintain data models.
- Java does allow one exception to the single inheritance rule: A class may implement multiple interfaces.
- The final modifier on a class, prevents the class from being extended. // Code will not compile if you try to extend a final class

## Inheriting Object (p.300):

- All classes inherit from java.lang.Object.
- Object is the only class that doesn't have a parent class.
- The compiler automatically inserts code into any class you write that doesn't extend a specific class, adds the syntax `extends Object`.

## Creating and Extending a Class (p.301-302):

- The declaration of a class has the following members:
  - Access modifier: public or default (package-private).
  - Optional modifier: abstract or final keyword (optional).
  - Class keyword: class (required).
  - Class name: AnyNameFollowingTheRules (required).
  - Extends parent class: extends Class (optional).
  - Implements interface(s): implements A, B... (optional).
  - Brackets (required) and body inside (optional).
    Obs.: Remember that final means a class cannot be extended.
- If the classes are in the same package they don't require an import statement to access each other.

## Applying Class Access Modifiers (p.303):

- In Java, a "top-level" class is a class that is not defined inside another class.
- Classes can only have public or package-private access.
  Note: An inner class is a class defined inside of another class and is the opposite of a top-level class. Inner classes can have all
  access modifiers.
- Remember, a Java file can have many top-level classes, but at most one public top-level class.

## Accessing the `this` Reference (p.304):

- The this reference refers to the current instance of the class and can be used to access any members of the class, including inherited ones.
- It can be used in any instance method, contructor and instance initializer block of the class.
- It cannot be used when there is no implicit instance of the class, such as in a static method or static initializer block.
- this reference is optional.
- If Java encounters a variable or method it cannot find, it will check the class hierarchy to see if it is available.

> **Note**: You can't use this inside static members, because this refers to an instance of the class and in static contexts you will
> not have access to a reference. For static variables it's better to use static reference (not using the 'this' reference).

## Calling the super Reference (p.305-306):

- The super reference is similar to the this reference, except that it excludes any members found in the current class.
- You can use super to call members that have the same name in a class A that extends a class B.

      return super.type + ":" + this.type;

  - Java uses the narrowest scope it can, so if you don't use the super.type, it will pick the type variable from the class that its the caller.
    Obs.: If you try to call a member that doesn't exist on the superclass with `super`, the code will not compile, since super only looks at the
    parent's scope.

## Declaring Constructors (p.307):

- Contructors are special methods that matches the name of the class and has no return type.
- It is called only when a new instance of the class is created.
- It is the last block to execute in the instantiation of a class.
- Contructor parameters are similar to method parameters (Remember that they may not include var).
- A class can have multiple contructors, so long as each contructor has a unique signature (parameters must be distinct). This practice is also
  known as 'contructor overloading'.
- Contructors can only be called by writing new before the name of the class or this()/super() inside a constructor (first line of it).

## Default Contructor (p.308-309):

- Every class in Java has a contructor whether you code one or not. If you don't include one, Java will create one for you implicitly without
  parameters. It is created during the compile step.
- This Java-created contructor is called 'default contructor', also referred as the 'default no-argument contructor'.
- It is declared as a `public` contructor without parameters and an empty body ({}).
- Remember, private contructors prevents other classes from instantiating the class and static methods may access private members,
  including private contructors.

## Calling Overloaded Contructors with this() (p.310-311):

- When this() is used with parentheses, Java calls another constructor on the same instance of the class. Searching for a signature with the same
  parameters specified inside the parentheses.
- The rules for calling this() are:

  - It must be the first statement in the constructor.
  - There can be only one call to this() in any contructor.
  - A constructor can't call itself infinitely.
    Obs.: The compiler is capable of detecting this last one, since the code can never terminate, the compiler stops and reports this as an error.

        public class Gopher { public Gopher() { this(5); // Does not compile }
        public Gopher(int dugHoles) { this(); // Does not compile } }

> **Note**: Despite using the same keyword, this and this() are very different. this, refers to an instance of the class, while this(), refers to a
> constructor call within the class.

## Calling Parent Contructors with super() (p.312-314):

- super() is very similar to this(), but instead it refers to any and only parent constructor.
- It must be the first statement and there can be only one call to super() in any contructor, just like this().
- Any valid parent contructor is acceptable to be called inside the child contructors, as long as the appropriate input parameters to the parent
  constructor are provided.

> **Note**: Like this and this(), super and super() are unrelated in Java.

## Understanding Compiler Enhancements (p.314-315):

- The first line of every constructor is a call to either this() or super(), even if you don't code it.
- Java compiler automatically inserts a call to the no-argument constructor super() if you do not explicitly call this() or super() at the first
  line of a constructor.
- For example, this three constructor definitions are equivalent, because the compiler will automatically convert them all to the last example:

      public class Donkey {}
      public class Donkey { public Donkey() {} }
      public class Donkey { public Donkey() { super() } }

\*\> **Notes** about classes with only private constructors:

- Classes with only private constructors are not considered final. Because they can be extended, but only an inner class defined in the class
  itself can extend it.
- An inner class is the only one that would have access to a private constructor and would be able to call super().
- Other top-level classes cannot extend such a class.

## Missing a Default No-Argument Constructor (p.315-316):

- If a class extends a class that has no default no-argument constructor, having at least one constructor already, if this child class doesn't have one
  constructor, Java will insert a default no-argument constructor on the child and call super() by default, but the code will not compile since the parent
  class has already a constructor declared and it's not a no-argument constructor (because it has parameters).
  For example:

      public class Mammal { public Mammal (int a) {} }
      public class Elephant extends Mammal {} // DOES NOT COMPILE

  - It doesn't because there is no superclass no-argument constructor, so the implicit super() call on Elephant will fail.
  - Remember that Java will automatically create a no-argument constructor for Elephant with a call to super() at the first line.
  - To fix this, we can create a constructor in Elephant with a explicit call of super(int a).

> **Note**: super() always refers to the most direct parent.

## Constructors and final Fields (p.316-318):

- final instance variables can be initialized inside constructors, instance initializers or in the same line as they are declared.
- static final variables (class variables) can't be initialized inside constructors, only in the same line as the declaration or inside a static initializer.
- By the time the constructor completes (last block to be executed), all final instance variables must be assigned a value. \*\*
- Unlike local final variables, which are not required to have a value unless they are actually used, final instance variables must be assigned.
- Default values are not used for these variables. They aren't for static final variables too.
- A final instance variable can be assigned a value only once, like the static final variables. Initializing them in more than one place, will result
  in a compiler error.
- Each constructor is considered independently in terms of assignment. The constructor must initialize all final variables if they aren't before him,
  even if they are initialized in another constructor.
- Failure to assign a value is considered a compiler error in the CONSTRUCTOR.
- We can assign a null value to final instance variables, so long as they are explicitly set.

## Order of Initialization (p.318-324):

- Class Initialization:

  - This is often referred to as 'loading the class', the JVM controls when the class is initialized, although you can assume the class is loaded before
    it is used, the reality is that the class may be initialized when the program first starts, when a static member of the class is referenced, or
    shortly before an instance of the class is created.
  - First, all static members in the class hierarchy are invoked, starting with the highest superclass and working downward.
  - The most important rule with class initialization is that it happens at most once for each class.
  - A class may also never be loaded if it is not used in the program.
  - Summarized order of initialization of a class X:
    1. If there is a superclass Y of X, then initialize Y first.
    2. Process all static variable declarations in the order they appear in the class.
    3. Process all static initializers in the order they appear in the class.
    - Following the order they appear.

> **Note**: The class containing the program entry point, aka the main() method, is loaded before the main() method is executed.

- Instance Initialization:
  - An instance is initialized anytime the new keyword is used.
  - Summarized order of initialization of an instance of X:
    1. If there is a superclass Y of X, then initialize the instance of Y first.
    2. Process all instance variable declarations in the order they appear in the class.
    3. Process all instance initializers in the order they appear in the class.
    4. Initialize the constructor including any overloaded constructors referenced with this().

> **Note**: super() call on the child constructor is called before the initialization of the child class. It works like this because the parent class
> is instantiated before the child class.

> **Note for Class and Instance Initialization**: The order of initialization of the variables and initializer blocks will follow the declaring order of the class. You can't access or modify a variable inside a initializer block before she has been declared.

- Reviewing Contructor Rules:

  1. The first statement of every constructor is a call to an overloaded constructor via this(), or a direct parent constructor via super().
  2. If the first statement of a constructor is not a call to this() or super(), then the compiler will insert a no-argument super() as the first statement.
  3. Calling this() and super() after the first statement of a contructor results in a compiler error.
  4. If the parent class doesn't have a no-argument constructor, then every constructor in the child class must start with an explicit this() or super() call.
  5. If the parent class doens't have a no-argument constructor and the child doesn't define any constructors, then the child class will not compile.
  6. If a class only defines private constructors, then it cannot be extended, nor be instantiated by a top-level class.
  7. All final instance variables must be assigned a value exactly once, if they are not initialized before the contructor call, they must be by the end of
     the constructor. Any final instance variables not assigned a value will be reported as a compiler error on the line the constructor is declared.

- Exam Note: Before even attempting to answer the question on the exam, check if the constructors are properly defined using the previous set of rules.
  Also verify if the classes include valid access modifiers for members.

## Overriding a Method in Inheritance (p.326-332):

- Remember that you can't have two methods with the same signature inside a class.
- Occurs when a subclass declares a new implementation for an inherited method with the same signature and compatible return type.
- You may reference the parent version of the method using the super keyword.
- this and super keywords allow you to select between the current and parent versions of a method, respectively.
- The compiler performs the following checks when you override a method:
  1. The method in the child class must have the same signature as the method in the parent class.
  2. The method in the child class must be at least as accessible as the method in the parent class (same or broader access modifer only).
     - The subclass can't reduce the visibility of the parent method.
  3. The method in the child class may not declare a checked exception that is new or broader than the class of any exception
     declared in the parent class method (this rule only applies for CHECKED exceptions).
     - Basically, the overridden method can't be more restrictive than the inherit method.
  4. If the method returns a value, it must be the same or a subtype of the method return type in the parent class, known as 'covariant return types'.
     - If the inherited method return type is void, then the overridden must be void too, as nothing is covariant with void except itself.
     - To check if two types are covariant: Given an inherited return type A and an overridden return type B, can you assign an instance of B to a
       reference of A without a cast? If so, then they are covariant (like String and CharSequence, all Strings are CharSequence since String
       implements CharSequence).

* Defining Subtype and Supertype:

  - A subtype is the relationship between two types where one type inherits the other.
  - If we define X to be a subtype of Y, then one of the following is true:
    - X and Y are classes, and X is a subclass of Y.
    - X and Y are interfaces, and X is a subinterface of Y.
    - X is a class and Y is an interface, and X implements Y (either directly or through an inherited class).
  - Likewise, a supertype is the reciprocal relationship between two types where one type is the ancestor of the other.
  - Remember, a subclass is a subtype, but not all subtypes are subclasses (subinterfaces are subtypes too).

* Overloading vs. Overriding:

  - If two methods have the same name but different signatures, the methods are overloaded and not overridden.
  - Overloaded methods are considered independent and do not share the same polymorphic properties as overridden methods.
  - Both involve redefining a method using the same name.
  - On overloaded methods the return type and parameter list can change.
  - On overridden methods the return type must be compatible with the parent's type and the parameter list can't change.

* Access Modifiers on Overridden Methods:
  - Java avoids ambiguity caused by access modifiers being different in the parent and child methods, by limiting overriding a method to access modifiers
    that are as accessible or more accessible than the version in the inherited method.
    - If the parent method has public access, then the overridden method must be public too, other options will not compile.

## Overriding a Generic Method (p.332-334):

- Remember that you can't overload methods with generic parameters, because of type erasure. Generics are used only at compile time.
  - So you can't overload with two Lists of different generics for example, because they will result in the same after (`List X` and `List Y`).
- But you can override a method with generic parameters, since the signatures and the generic types match exactly.

## Redeclaring private Methods (p.334-335):

- You can't override private methods, since they are not inherited.
- Java permits you to redeclare a new method in the child class with the same or modified signature as the method in the parent class.
  This method in the child class is a separate and independent method, unrelated to the parent version's method, so none of the rules of
  overriding methods is invoked. Child classes may override only methods that are public, protected and package-private (if they are in
  the same package).

## Hiding Static Methods (p.335-337):

- A hidden method occurs when a child class defined a static method with the same name and signature as an inherited static method defined in a parent class.
- Method hiding is similar but not exactly the same as method overriding.
- The previous four rules for overriding a method must be followed when a method is hidden. In addition, a new rule is added: 5. The method defined in the child class must be marked as static if it is marked as static in a parent class.

## Creating final Methods (p.337-338):

- final methods cannot be replaced. By marking a method final you forbid a child class from replacing this method.
- This rule is in place both when you override a method and when you hide a method.
- Remember that this rule only applies to inherited methods. So if you have a inherited method that is not final, you can use the final
  on the overridden method.
- Using final on methods will guarantee certain behavior of a method in the parent class, regardless of which child is invoking the method.

> **Note**: All the rules listed above apply on this situations.

## Hiding Variables (p.338):

- Remember that Java doesn't allow variables to be overridden. Variables can be hidden, though.
- A hidden variable occurs when a child class defines a variable with the same name as an inherited variable defined in the parent class.
  This creates two distinct copies of the variable within an instance of the child class:
  - One instance defined in the parent class.
  - One instance defined in the child class.

> **Note** on Hiding Inherited Members: Hiding a method or variable replaces the member only if a child REFERENCE type is used. Java uses the REFERENCE type to
> use the member (p.339).

## Understanding Polymorphism (p.339-340):

- The property of an object can take on many different forms.
- A Java object may be accessed using a reference with the same type as the object, a reference that is a superclass of the object, or a reference that
  defines an interface the object implements, either directly or through a superclass.
- Once the object has been assigned to a new reference type using polymorphism, only the methods and variables available to that reference type are
  callable on the object without an explicit cast.

## Object vs. Reference (p.341):

- The object in memory will never change depending on the reference type you assign him. What changes is our ability to access members within the object,
  those being only the members of the reference type. We can have access back to the object members only if we do a direct cast back to the object type.
- Summarizing this principle with the following two rules:
  1. The type of the object determines which properties exist within the object in memory.
  2. The type of the reference to the object determines which methods and variables are accessible to the Java program.

## Casting Objects (p.342-343):

- Is similar to casting primitives. You do not need a cast operator if the current reference is a subtype of the target type (implicit cast or type conversion).
- Alternatively, if the current reference is not a subtype of the target type, then you need to perform an explicit cast with a compatible type.
- If the underlying object is not compatible with the type, then a ClassCastException will be thrown at runtime. \*
- Summarizing these concepts into a set of rules:
  1. Casting a reference from a subtype to a supertype doesn't require an explicit cast.
  2. Casting a reference from a supertype to a subtype requires an explicit cast.
  3. The compiler disallows casts to an unrelated class.
  4. At runtime, an invalid cast of a reference to an unrelated type results in a ClassCastException being thrown.
     > **Note**: The third rule on casting applies to class types only, not interfaces.
- The fourth rule will apply in a situation that either the reference type and object type are the same as the supertype, when trying to cast this object to
  an subtype reference, the ClassCastException will be thrown. Keep in mind that the object created with the parent's type does not inherit the child class.
- When reviewing a question on the exam that involves this topics, be sure to remember what the instance of the object actually is. Then, focus on whether
  the compiler will allow the object to be referenced with or without explicit casts.

## The instanceOf Operator (p.343-344):

- Can be used to check whether an object belongs to a particular class or interface and to prevent ClassCastException at runtime (inside a if for example).
- The compiler does not allow instanceOf to be used with unrelated types, just like casting.

## Polymorphism and Method Overriding (p.344-345):

- When a method is overridden in the subclass and called by a object of its type, all calls to it are replaced at runtime, meaning that the calls will be
  associated with the precise object in memory, not the current reference type where is called. Even using the this reference, it does not call the parent
  version because the method has been replaced. // Coded example on file PolymorphismOverridingExample.java
- The facet of polymorphism that replaces methods via overriding is one of the most important properties of Java. It allows to create complex inheritance
  models, with subclasses that have their own custom implementation of overridden methods.

## Overriding vs. Hiding Members (p.346-347):

- While method overriding replaces the method everywhere it is called, method (static) and variable hiding does not.
- Hiding members is not a form of polymorphism since the methods and variables maintain their individual properties.
- Hiding members is very sensitive to the reference type and location where the member is used.
- Contrast this with overriding a method, where it returns the same value for an object regardless of which class it is called in.

* Real World Scenario Note: Hiding variables and methods is allowed by Java, but it is considered an extremely poor coding practice.

## Summary (p.348):

- All instance methods, contructors and instance initializers have access to this and super.
- this() and super() can only be used in constructors, following the rules of constructors.
- A method is overloaded if it has the same name but a different signature as another accessible method.
- A method is overridden if it has the same signature as an inherited method and has access modifiers, exceptions and a return type that are compatible.
- Remember that 'covariant types' only apply to return values of overridden methods, not method parameters.
- A static method is hidden if it has the same signature as an inherited static method. Remember that the four rules plus a fifth rule are applied here.
- A method is redeclared if it has the same name and possibly the same signature as an uninherited method (superclass private methods).
- You can access private methods (instance methods and constructors) from the main() method, if he is declared inside the same class.
