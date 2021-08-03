# Chapter 7 - Methods and Encapsulation

Creating and Using Methods
- Create methods and contructors with arguments and return values
- Create and invoke overloaded methods
- Apply the static keyword to methods and fields

Applying Encapsulation
- Apply access modifiers
- Apply encapsulation principles to a class

## Designing Methods (p.250-251)
A method declaration is composed by, respectively, an access modifier (public), *optional* specifier (final), return type (void), method name (any name that starts with a letter, $ or _), list of parameters (which must be wrapped in parentheses), exceptions(optional) and the method body (must have braces). 

> **Note:** The method name and parameter list are called the method signature.

Parts of a method declaration:
  ____________________   ____________________________________
 | Element             |            Required?                |
 |____________________ | ____________________________________|
 | Access modifier                 No                       |
 | Optional modifier               No                       |
 | Return type                     Yes                      |
 | Method name                     Yes                      |
 | Parameter list                  Yes, but can be empty ( )|
 | Optional exception list         No                       |
 | Method body *                   Yes, but can be empty { }|
 |__________________________________________________________|

  |Element                 | Required?                 |
  |:---------------------- | :------------------------ |
  |Access modifier         | No                        |
  |Optional modifier       | No                        |
  |Return type             | Yes                       |
  |Method name             | Yes                       |
  |Parameter list          | Yes, but can be empty ( ) |
  |Optional exception list | No                        |
  |Method body *           | Yes, but can be empty { } |

> **Table Note:** The full body can be omitted for abstract methods.

## Access Modifiers (p.251-252)

Java offers four choices of access modifiers:
- private: The private modifier means the method can be called only from within the same class.
- Default (package-private) Access: With default access, the method can be called only from classes in the same package. There is no keyword for default access. You simply omit the access modifier.
- protected: The protected modifier means the method can be called only from classes in the same package or subclasses.
- public: The public modifier means the method can be called from any class.

> Obs.: The default keyword exists in Java, but it's not used for access control (Chapter 9 will cover interfaces).

Some tricky method declarations that may appear in the exam:

    `public void walk1() { }` // VALID
    `default void walk2() { }` // DOES NOT COMPILE
    `void public walk3() { }` // DOES NOT COMPILE
    `void walk4() { }` // VALID

## Optional Specifiers (p.252-253)

Different than access modifiers, you can have multiple specifiers in the same method (although not all combinations are legal). Multiple specifiers can be specified in any order. Optional specifiers list:

- static: The static modifier is used for class methods (will cover more later in this Chapter).
- abstract: The abstract modifier is used when a method body is not provided (Chapter 9 will cover it).
- final: The final modifier is used when a method is not allowed to be overridden by a subclass (Chapter 8 will cover it).
- synchronized: The synchronized modifier is used with multithreaded code (1Z0-816 Chapters will cover it).
- native: The native modifier is used when interacting with code written in another language such as C++. It is not on either OCP 11 exam.
- strictfp: The strictfp modifier is used for making floating-point calculations portable. It is not on either OCP 11 exam.

Some tricky method declarations with specifiers that can appear in the exam:

    `public void walk1() { }` // VALID
    `public final void walk2() { }` // VALID
    `public static final void walk3() { }` // VALID
    `public final static void walk4() { }` // VALID
    `public modifier void walk5() { }` // DOES NOT COMPILE
    `public void final walk6() { }` // DOES NOT COMPILE - It's not allowed to declare the optional specifiers after the return type.
    `final public void walk7() { }` // VALID - Java allows the optional specifiers to appear before the access modifier.

## Return Type (p.253-254)

Methods with a return type other than void are required to have a return statement (returning the same data type) inside their body. Methods that have a return type of void are permitted to have a return statement with no value returned or omit the return statement. Some examples of wrong method declaration:

    `public String walk1() { }` // DOES NOT COMPILE - No return statement
    `public walk2() { }` // DOES NOT COMPILE - No return type specified
    `public String int walk3() { }` // DOES NOT COMPILE - Two return types are specified (only one can be specified)
    `String walk4(int a) { if (a == 4) return ""; }` // DOES NOT COMPILE - Missing a default return statement, in case of a !== 4

## Method Name (p.254-255)

Review of some rules:

- An identifier may only contain letters, numbers, $ or _.
- The first character is not allowed to be a number.
- An identifier is not allowed to contain special characters (%,#,&,@...).
- Reserved words are not allowed (public), but remember that Java is camelcase, for example public !== Public. 
- Single underscore (_) character is not allowed as an identifier.
- By convention, methods begin with a lowercase letter, but are not required to.

** p.256-257
** Working with Varargs:
  - A varargs parameter must be the last element in a method's parameter list.
  - Only one varargs parameter is allowed per method:
    `public void walk1(int... nums) { }` // COMPILES
    `public void walk2(int start, int... nums) { }` // COMPILES
    `public void walk3(int... nums, int start) { }` // DOES NOT COMPILES - The varargs parameter isn't the last parameter on the list.
    `public void walk4(int... start, int... nums) { }` // DOES NOT COMPILES - There are two varargs parameters on the list (only one allowed).
  - You can call a method with varargs parameter in more than a way:
    * Pass in an array with the values.
    * List the elements of the array and let Java create if for you.
    * Omit the varargs values in the method call, Java will create an array of length zero (empty array).
    * It is possible to pass null explicitly, but you can get a nullpointer exception in the usage of this null varargs array.

* Note.: Remember that a member of a class, is an instance variable or instance method.

** p.258-266
** Applying Access Modifiers:
  - Access Modifiers in order from most restrictive to least restrictive:
    * private: Only code in the same class can call private methods or access private fields.
      `private String name;` or `private String getName() { ... }`
      
    * Default (package-private) access: Only classes in the same package may call these methods or access these fields.
      `String name;` or `String getName() { ... }`

    * protected: 
      - Allows everything that package-private allows, plus adds the ability to access members of a parent class (subclasses).
      - Not allowed to refer to protected members of a parent class if they are not in the same package and the reference type used is an object 
      that is not a subclass of the parent class.
      -You can only access the protected members with the reference of the subclass from inside the subclass.
        `protected String name;` or `protected String getName() { ... }`

    * public: Anyone can access the member from anywhere.
      `public String name;` or `public String getName() { ... }`

  Obs.: Extending means creating a subclass that has access to any protected and public members of the parent class. Modules redefine
  public access, making possible to restrict access to public code.

  * Access modifiers rules:
   ________________________________________________________________________________________________________________________________
  | A method in X can access a Y member             private           Default (package-private)         protected           public |
  |                                                                                                                                |
  | in the same class                               Yes               Yes                               Yes                 Yes    |
  | in another class in the same package            No                Yes                               Yes                 Yes    |
  | in a subclass in a different package            No                No                                Yes                 Yes    |
  | in an unrelated class in a different package    No                No                                No                  Yes    |
  |________________________________________________________________________________________________________________________________|
 
** p.266-274
** Applying the static Keyword
  - When the static keyword is applied to a variable, method or class, it applies to the class rather than a specific instance of the class.
* Designing static Methods and Fields:
  - static methods don't require an instance of the class.
  - They are shared among all users of the class.
  - Is a member of the single class object that exists independently of any instances of that class.
  - main(String... args) method is a static method, that can be called like any other static method. `TestClass.main(new String[0]);`
  - static methods have two main purposes:
    * For utility or helper methods that don't require any object state. Since there is no need to access instance variables, having static 
    methods eliminates the need for the caller to instantiate an object just to call the method.
    * For state that is shared by all instances of a class, like a counter. All instances must share the same state. Methods that merely 
    use that should be static as well.

* Accessing a static Variable or Method:
  - You don't need an instance of the object to call a static member. `System.out.println(Koala.count);`
  - If the static member is declared inside the same class you're calling, you just call him directly. `System.out.println(count);` - inside Koala class. 
  - You can use an instance of the object to call a static member. The compiler checks for the type of the REFERENCE and uses that instead of the OBJECT.
    `Koala k = new Koala();`
    `System.out.println(k.count);` // k is a Koala, so print 0
    `k = null`
    `System.out.println(k.count);` // k is still a Koala, so print 0 too
    Obs.: This works since we're looking for a static member.

* Static vs. Instance:
  - A static member cannot call an instance member without referencing an instance of the class.
  - A static or instance member can call a static member because static doesn't require an object to use.
  - Only an instance member can call another instance member on the same class without using a reference variable (instance of the object), because 
  instance objects do require an object.
  - Method params can be used inside static methods to access nonstatic members of a class.
  - Each object has a copy of the instance variables.
  - There is only one copy of the code for the instance methods.
  - Parameters and local variables go on the stack.

* static Variables:
  - Constants are variables that are meant to never change during the program. It uses the final modifier to ensure the variable never changes.
  - Constants use the modifier static final and a different naming convention than other variables. They use all upercase letters with underscores
  - Constants must be set exactly once, in the same line as the declaration or inside a static initializer block.
  between "words". `private static final int NUM_BUCKETS = 45;` - If you try to change this value the code will note compile.
  - The compiler can only check if we don't try to reassign the final value to point to a different object. So adding a element to a final
  ArrayList will compile. 

* Static Initialization:
  - They are like instance initializers, just code inside braces, but they add the static keyword to specify they should be run when the class is first loaded.
  - All static initializers run when the class is first used in the order they are defined.
  - You can initialize a constant variable (static final) inside an static initializer, without initializing it before the block, if you initialize in 
  the same line as the declaration and inside the static initializer, the code will not compile.
  - Try to avoid static and instance initializers. One case that a static initializer is usefull, is when the code needed to initialize a constant variable 
  required more than one line of code.

* Static Imports:
  - This imports are only for static members, like "import static java.util.Arrays.asList" // will import only the static method asList,
  and it can be used directly without Arrays.
  - If we create an asList method in our coding class. Java would give it preference over the imported one, and the method we coded would be used.
  - Some exam cases of static import usage:
    `import static java.util.Arrays;` // Does not compile
    `import static java.util.Arrays.asList;` // Compile
    `static import java.util.Arrays.*;` // Does not compile
    `import static java.util.Arrays.*;` // Compile
    Obs.: If we try to use Arrays.asList() it will not compile, since we didn't imported the class, we imported only the static member asList().

  Note: Regular imports are for classes and Static imports are for static members of a class.

** p.274-277
** Passing Data among Methods:
  - Java is a "pass-by-value" language. This means that a copy of the variable is made and the method receives that copy. Assignments made in the
  method do not affect the caller.
  - If the change made on the other method changes a value of the object, like StringBuilder append() method, the change will be
  available to both references, but in a case of trying to assign a new value to the copy, it will not mirror the change.
  - "pass-by-reference" is another approach, which Java don't utilize, that the values can be affected if they are passed as params and modified.
  - To review: Assigning a new primitive or reference to a parameter doesn't change the caller. Calling methods on a reference to an object can affect
  the caller.

** p.277-283
** Overloading Methods:
  - Method overloading occurs when methods have the same name but different method signatures, which means they differ by method parameters.
  - Everything other than the method name can vary for overloading methods.
  - You cannot have methods where the only difference is that one is an instance method and the other is a static method.
  - It's not allowed to overload methods with the same parameter list. They must differ in at least one of the parameters.
  - Java will always pick the most specific version of a method that it can. So it will not autobox if we have two methods with the corresponding param type,
  one being a Wrapper and the other a primitive. It would call the primitive method in this case: `a(1)` -> `a(int a)` instead of `a(Integer a)`.
  - Remember that Java only accept wider types, it will not automatically convert to a narrower type. If you want so,
  you'll need to explicitly cast the wider to a narrower type.
  - Java has a concept called "type erasure", where generics are used only at compile time. So you can't overload with two Lists of different generics.
   * `List<String> strings` and `List<Integers> integers` look like this in compiled code `List strings` and `List integers`, so they are considered equal.
  - Normal arrays will work just fine with overload (`int[] ints` and `Integers[] integers`), they don't participate in type erasure.
  
  Note: Java will always do only one conversion. Java can convert a 4 (int) to a long or autobox it to an Integer 4, but it will not convert a 4 (int) 
  to a long and then autobox it to a Long 4.

  * The order that Java uses to choose the right overloaded method:
     _______________________________________________________________________________
    | Rule                            Example of what will be chosen for glide(1, 2)|
    |                                                                               |
    | Exact match by type             String glide(int i, int j)                    |
    | Larger primitive type           String glide(long i, long j)                  |
    | Autoboxed type                  String glide(Integer i, Integer j)            |
    | Varargs                         String glide(int... nums)                     |
    |_______________________________________________________________________________|

** p.283-285
** Encapsulation:
  - Means that only methods in the class with the same variables can refer to the instance variables. Callers are required to use these methods. 
  - getter and setter are also known as, respectively, 'accessor method' and 'mutator method'.
  - Conventions for getters and setters:
    * Getter methods most frequently begin with 'is' if the property is a boolean.
    * Getter methods most frequently begin with 'get' if the property is not a boolean.
    * Setter methods begin with 'set'.
  
** Notes after the review exam: Some exam questions try tricking you with the type 'var' in method declaration.
   Remember that 'var' can only be used with local variables.   
