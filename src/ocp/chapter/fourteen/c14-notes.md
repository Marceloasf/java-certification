# Chapter 14 - Generics and Collections

Generics and Collections
- Use wrapper classes, autoboxing and autounboxing
- Create and use generic classes, methods with diamond notation and wildcard
- Describe the Collections Framework and use key collection interfaces
- Use Comparator and Comparable interfaces
- Create and use convenience methods for collections

Java Stream API
- Use lambda expressions and method references

## Introducing Generics and Collections (p.600-601)

On this chapter, we'll discuss how to create our own classes and methods that use generics, so that the same class can be used with many types.

> **Note:** Remember, the letters (R, T, and U) are generics that you can pass any type to when using these functional interfaces.

Functional interfaces used in this chapter:


  | Functional interfaces  |  Return type        |  Method name          |  # parameters   |
  | :--------------------  | :-----------------  | :-------------------  |  :------------  |
  | Supplier<*T*>          |  T                  | get()                 |  0              |
  | Consumer<*T*>          |  void               | accept(T)             |  1 (T)          |
  | BiConsumer<*T, U*>     |  void               | accept(T, U)          |  2 (T, U)       |
  | Predicate<*T*>         |  boolean            | test(T)               |  1 (T)          |
  | BiPredicate<*T, U*>    |  boolean            | test(T, U)            |  2 (T, U)       |
  | Function<*T, R*>       |  R                  | apply(T)              |  1 (T)          |
  | BiFunction<*T, U, R*>  |  R                  | apply(T, U)           |  2 (T, U)       |
  | UnaryOperator<*T*>     |  T                  | apply(T)              |  1 (T)          |
 
 ## Using Method References (p.601-605)

**Method references** are another way to make the code easier to read, like lambdas, such as simply mentioning the name of the method. On this section, we'll go through the four types of method references, show the syntax and mix in lambdas with method references. 

Let's suppose we have a Functional Interface called LearnToSpeak with a method `void speak(String sound);`, the following example implements this interface with a lambda expression:

    public class DuckHelper {
        public static void teacher(String name, LearnToSpeak trainer) {
            trainer.speak(name);
        }
    }

    public class Duckling {
        public static void makeSound(String sound) {
            LearnToSpeak learner = s -> System.out.println(s);
            DuckHelper.teacher(sound, learner);
        }
    }

This implementation isn't bad, but there's a bit of redundancy. The lambda declares one parameter (s), however, it does nothing than pass that parameter to another method (teacher(...)). If we use a method reference, we can remove that redundancy, as with the following example:

    LearnToSpeak learner = System.out::println;

A method reference and a lambda behgave the same way at runtime. You can pretend that the compiler turns the method reference into lambdas for you. There are four formats for method references:

- Static methods
- Instance methods on a particular instance (object)
- Instance methods on a parameter to be determined at runtime
- Constructors

> **Note:** Remember that `::` is like a lambda, and it is used for *deferred execution* with a functional interface.

### Calling Static Methods

The Collections class has a static method that can be used for sorting. The Consumer functional interface takes one parameter and does not return anything. Here we will assign a method reference and a lambda to this functional interface:

    Consumer<List<Integer>> methodRef = Collections::sort;
    Consumer<List<Integer>> lambda = x -> Collections.sort(x);

The `sort()` method in this example is being overloaded. How does Java know that we want to call the version with only one parameter? With both lambdas and method references, Java is inferring information from the context. In this case we are declaringa Consumer, which takes only one parameter. Java looks for a method that matches that description. If it can't find it or it finds multiple ones that could match multiple method, then the compiler will report and error. The latter one is sometimes called an `ambiguous type error`.

### Calling Instance Methods on a Particular Object

The String class has a `startsWith()` method that takes one parameter and returns a boolean. Conveniently, a Predicate is a functional interface that takes one parameter and returns a boolean. So the following could be implemented:

    var str = "abc";
    Predicate<String> methodRef = str::startsWith;
    Predicate<String> lambda = s -> str.startsWith(s);

Another good example is using the following method, which uses a Supplier, since a method reference doesn't have to take any parameters we can use it here too:

    var random = new Random();
    Supplier<Integer> methodRef = random::nextInt;
    Supplier<Integer> lambda = () -> random.nextInt();

Since the methods on Random are instance methods, we call the method reference on an instance of the Random class.

### Calling Instance Methods on a Parameter

This time, we are going to call an instance method that doesn't take any parameters. The trick is that we will do so without knowing the instance in advance.

    Predicate<String> methodRef = String::isEmpty;
    Predicate<String> lambda = s -> s.isEmpty();

The method reference example may look like a static method, but it isn't. Instead, Java knows that `isEmpty()` is an instance method that does not take any parameters. Java uses the parameter supplied at runtime as the instance on which the method is called.

It's possible to combine the last two types of instance method references shown above. We are going to use a functional interface called a BiPredicate, which takes two parameters and returns a boolean:

    BiPredicate<String, String> methodRef = String::startsWith;
    BiPredicate<String, String> lambda = (s, p) -> s.startsWith(p);

Since the functional interface takes two parameters, Java has to figure out what they represent. The first one will always be the instance of the object for instance methods. Any others are to be method parameters.

> **Note:** Remember, this may look like a static method, but it is really a method reference delcaring that the instance of the object will be specified later.

### Calling Constructors

A *constructor reference* is a special type of method reference that uses `new` instead of a method, and it instantiates an object. It is common for a constructor reference to use a Supplier as shown here:

    Supplier<List<String>> methodRef = ArrayList::new;
    Supplier<List<String>> lambda = () -> new ArrayList();

It expands like the method references you have seen so far. Like the previous example, the lambda doesn't have any parameters.

Method references can be tricky. In the next example, it'll use the Function functional interface, which takes one parameter and returns a result. Notice that the methodRef line has the same implementation as the one before:

    Function<Integer, List<String>> methodRef = ArrayList::new;
    Function<Integer, List<String>> lambda = x -> new ArrayList(x);

This means you can't always determine which method can be called by looking at the method reference. Instead, you have to look at the context to seewhat parameters are used and if there is a return type. In this example, Java sees that we are passing an Integer parameter and calls the constructor of ArrayList that takes a parameter.

### Reviewing Method References

Reading method references is helpful in understanding the code. The following table, shows the four types of method references. If this table doesn't make sense, reread the previous section.

  | Type                                    |  Before colon          |  After colon  |  Example           |
  | :--------------------                   | :-----------------     | :------------ |  :------------     |
  | Static Methods                          | Class name             | Method name   |  Collections::sort |
  | Instance methods on a particular object | Instance variable name | Method name   |  str::startsWith   |
  | Instance methods on a parameter         | Class name             | Method name   |  String::isEmpty   |
  | Constructor                             | Class name             | new           |  ArrayList::new    |
 
> **Note:** The number of parameters in a method reference is irrelevant, since we can even use it with a method that accepts a varargs parameter.

## Using Wrapper Classes and the Diamond Operator (p.605-608)

Remember that each Java primitive has a corresponding class. With `autoboxing`, the compiler will automatically convert the primitive value to the corresponding wrapper. Same for `unboxing`, which will do the reverse.

Wrapper Classes:

  | Primitive type        |  Wrapper class      |  Example of initializing   |
  | :-------------------- | :------------------ | :------------------------  |
  | boolean               | Boolean             | Boolean.valueOf(true)      |
  | byte                  | Byte                | Byte.valueOf((byte) 1)     |
  | short                 | Short               | Short.valueOf((short) 1)   |
  | int                   | Integer             | Integer.valueOf(1)         |
  | long                  | Long                | Long.valueOf(1)            |
  | float                 | Float               | Float.valueOf((float) 1.0) |
  | double                | Double              | Double.valueOf(1.0)        |
  | char                  | Character           | Character.valueOf('c')     |

The following are examples of autoboxing and unboxing:

    Integer pounds = 120; // Autoboxing an primitive int to an Integer on the declaration
    Character letter = "robot".charAt(0); // Autoboxing can involve methods
    char r = letter; // Character 'letter' is unboxed to a char type var

> **Note:** Remember that one advantage of a wrapper class over a primitive is that it can hold a null value. 

Be careful with tricks, autobox can work on methods, but remember that if a method has an overloaded signature of the primitive value, Java will will match this signature instead of the Object one.

### Diamond Operator (<>)

In the past we would write generics with the following sintax: 

    List<Integer> list = new ArrayList<Integer>();
    List<String,Integer> map = new HashMap<String,Integer>();
    List<String,List<Integer>> mapLists = new HashMap<String,List<Integer>>();

There are some expressions where generic types might not be the same on both sides, but often they are identical. The diamond operator was added to solve this duplication problem. He is a shorthand notation that allows you to omit the generic type from the **right** side of a statement when the type can be inferred. The following are the equivalent to the previous example, but using the diamond operator:

    List<Integer> list = new ArrayList<>();
    List<String,Integer> map = new HashMap<>();
    List<String,List<Integer>> mapLists = new HashMap<>();

Basically, the first is the variable declaration and fully specifies the generic type. The second is an expression that infers the type from the assignment operator (using the <>).

> **Note:** The diamond operator cannot be used as the type in a variable declaration! It can be used only on the right side of an assignment operation. 

Another way to declare it, it's using **var** with the diamond operator, for example:

    var list = new ArrayList<Integer>(); // Declares a new ArrayList<Integer>
    var list2 = new ArrayList<>(); // Declares a new ArrayList<Object>

Remember that Java infers the type on this kind of expression, so using only the <> without a type on the left, Java will assume you wanted Object.