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

## Using Lists, Sets, Maps and Queues (p.608-630)

First let's start defining these collections. A *collection* is a group of objects contained in a single object. The Java Collections Framework is a set of classes in java.util for storing collections. The four main interfaces on this framework are:

- List: A *list* is an ordered collection of elements that allows duplicate values. The elements in a list can be accessed by an index (int).
- Set: A *set* is a collection that does not allow duplicate entries.
- Queue: A *queue* is a collection that orders its elements in a specific order for processing. The typical order of a queue is in a `first-in, first-out` (FIFO) order, but other orderings are possible.
- Map: A *map* is a collection that maps keys to values, with no duplicate keys allowed. The elements in a map are key/value pairs.

These collections have some classes that implement them. Some of them you need to be familiar for the exam:

- List: ArrayList and LinkedList
- Set: HashSet and TreeSet
- Queue: LinkedList
- Map: HashMap and TreeMap

From all these interfaces shown above, the Map interface is the only one which does not implements the Collection interface. It is considered part of the framework, even though isn't technically considered a Collection. But it is a collection (note the lowercase), since it contains a group of objects. Maps are treated differently because they need different methods due to being key/value pairs.

### Common Collections Methods

The Collections interface contains useful methods for working with lists, sets and queues. Many of these methods are *convenience methods*, meaning that they could be implemented by you, but using these methods makes your code easier to read and write.

> **Note:** On this section, the classes used are ArrayList and HashSet, but these methods can apply to any class that inherits the Collections interface.

#### *add()*

The `add()` method inserts a new value into the Collection and reutrns whether it was successful. The method signature is as follows:

    boolean add(E element)

Remember that the Collections Framework uses a lot of generics, so expect to see E frequently. The E means the generic type that was used to create the collection. Some Collection types will always return true when using `add()`, for other types, there is logic to define the return. Here are some examples of `add()`:

    Collection<String> list = new ArrayList<>();
    System.out.println(list.add("Sparrow")); // true
    System.out.println(list.add("Sparrow")); // true

    Set<String> set = new HashSet<>();
    System.out.println(set.add("Sparrow")); // true
    System.out.println(set.add("Sparrow")); // false

As you can see above, List allows duplicates, making the return true each time, on the other hand, Set does not allow duplicates, so it returns false when trying to add a duplicate value.

#### *remove()*

The `remove()` method removes a single matching value in the collection and returns whether it was successful. The method signature is as follows:

    boolean remove(Object object)

The following are some examples on how to use it:

    Collection<String> birds = new ArrayList<>();
    birds.add("hawk"); // [hawk]
    birds.add("hawk"); // [hawk, hawk]
    System.out.println(birds.remove("cardinal")); // false
    System.out.println(birds.remove("hawk")); // true
    System.out.println(birds); // [hawk]

Note that when the element is not found/removed, it just returns false. It only removes one match.

It's possible to call `remove()` on a List with an int using the index, remember that using the index can result in a IndexOutOfBoundsException, in case that a index that does not exists is passed. So just remember that `remove()` has overloaded implementations.

Java does not allow removing elements from a list while using the enhanced for loop. For example:

    Collection<String> birds = new ArrayList<>();
    birds.add("hawk");
    birds.add("hawk");

    for (String bird : birds)
        birds.remove(bird); // Throws ConcurrentModificationException

Yes, it's possible to get a ConcurrentModificationException without threads. This is they way Java complains that you are trying to modify the list while looping through it. There are some ways to fix this and they will be shown in Chapter 18 (Concurrency).

#### *isEmpty()* and *size()*

This are some simple methods, basically they look at how many elements are in the Collection. These are their signatures:

    boolean isEmpty()
    int size()

The main difference between them is that `isEmpty()` checks if the Collection is empty returning a boolean, and `size()` return the size of the Collection. Some examples:

    Collection<String> birds = new ArrayList<>();
    System.out.println(birds.isEmpty()); // true
    System.out.println(birds.size()); // 0
    birds.add("hawk");
    birds.add("hawk");
    System.out.println(birds.isEmpty()); // false
    System.out.println(birds.size()); // 2

#### *clear()*

The `clear()` method provides an easy way to discard all elements of the Collection. The signature is as follows:

    void clear()

The following are some examples of how to use it:

    Collection<String> birds = new ArrayList<>();
    birds.add("hawk");
    birds.add("hawk");
    System.out.println(birds.isEmpty()); // false
    System.out.println(birds.size()); // 2
    birds.clear();
    System.out.println(birds.isEmpty()); // true
    System.out.println(birds.size()); // 0

After calling `clear()`, the Collection turns back to being an empty Collection.

#### *contains()*

The `contains()` method checks whether a certain value is in the Collection. The signature is as follows:

    boolean contains(Object object)

The following are some examples of how to use it:

    Collection<String> birds = new ArrayList<>();
    birds.add("hawk");
    System.out.println(birds.contains("hawks")); // true
    System.out.println(birds.contains("robin")); // false

The `contains()` method calls `equals()` on elements of the Collection to check if there are any matches.

#### *removeIf()*

The `removeIf()` method removes all elements that match a condition, only if they match the condition. It's possible to specify what should be deleted using a block of code or even a method reference. The signature is as follows (the ? super will be explained further):

    boolean removeIf(Predicate<? super E> filter)

It uses a Predicate, so it'll take one parameter and return a boolean. Some examples:

    Collection<String> list = new ArrayList<>();
    list.add("Magician");
    list.add("Assistant");
    list.removeIf(s -> s.startsWith("A"));
    System.out.println(list); // [Magician]
    
That condition will remove all of the String values that start with the letter A. Another example, but this time using method reference:

    Collection<String> set = new HashSet<>();
    set.add("Wand");
    set.add("");
    set.removeIf(String::isEmpty);
    System.out.println(set); // [Wand]

#### *forEach()*

The `forEach()` method allows you to loop through a Collection. It uses a Consumer that takes a single parameter and does not return anything. The signature is as follows: 

    void forEach(Consumer<? super T> action)

The following are some examples of how to use it:

    Collection<String> cats = Arrays.asList("Annie", "Ripley");
    cats.forEach(System.out::println);
    cats.forEach(c -> System.out.println(c));

### Using the List Interface

Now we are going to move on to specific classes. A List is used when you need an ordered collection that can contain duplicate values. You can retrieve and insert items at specific positions of the list based on an int index. Unlike an array, many List implementations can change in size after they are declared. 

The List is pretty much like an array, it has a strucutre like where it has an index, starting from 0, and the data it holds.

Lists can be used without caring about the order of elements, but you can sort them out if you need to. The default order of a list is the order the items were added in. There are some implementations of List that can be differente in some aspects, we'll take a look at some of the most common. 

> **Note:** Remember that there are Interfaces and Classes when talking about Collections, for example, List is an interface, while ArrayList is a class that implements List. 

#### Comparing List Implementations

 ArrayList is like a resizable array. When new elements are added to it, the ArrayList will automaticcally grow. When you aren't sure which collection to use, use an ArrayList. The main benefit on an ArrayList is that you can look up any element in constant time. Adding and removing an element is slower than accessing an element. This makes ArrayList a good choice when you are reading more (or the same amount as) often than writing. 

 A LinkedList is special because it implements both List and Queue. It has all the methods of a List. It also has additional methods to facilitate adding or removing from the beginning and/or end of the list. The main benefits of a LinkedList are that you can access, add and remove from the beginning and end of the list in constant time. The trade-off is that dealing with an arbitrary index takes linear time, making a LinkedList a good choice when you need a Queue. 

 > **Note:** Both of these classes implements List, but only LinkedList implements Queue.
 