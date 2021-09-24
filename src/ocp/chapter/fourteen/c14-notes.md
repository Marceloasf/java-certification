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

### Using the *List* Interface

Now we are going to move on to specific classes. A List is used when you need an ordered collection that can contain duplicate values. You can retrieve and insert items at specific positions of the list based on an int index. Unlike an array, many List implementations can change in size after they are declared. 

The List is pretty much like an array, it has a strucutre like where it has an index, starting from 0, and the data it holds.

Lists can be used without caring about the order of elements, but you can sort them out if you need to. The default order of a list is the order the items were added in. There are some implementations of List that can be differente in some aspects, we'll take a look at some of the most common. 

> **Note:** Remember that there are Interfaces and Classes when talking about Collections, for example, List is an interface, while ArrayList is a class that implements List. 

#### Comparing *List* Implementations

 ArrayList is like a resizable array. When new elements are added to it, the ArrayList will automaticcally grow. When you aren't sure which collection to use, use an ArrayList. The main benefit on an ArrayList is that you can look up any element in constant time. Adding and removing an element is slower than accessing an element. This makes ArrayList a good choice when you are reading more (or the same amount as) often than writing. 

 A LinkedList is special because it implements both List and Queue. It has all the methods of a List. It also has additional methods to facilitate adding or removing from the beginning and/or end of the list. The main benefits of a LinkedList are that you can access, add and remove from the beginning and end of the list in constant time. The trade-off is that dealing with an arbitrary index takes linear time, making a LinkedList a good choice when you need a Queue. 

 > **Note:** Both of these classes implements List, but only LinkedList implements Queue.
 
#### Creating a *List* with a Factory

There are some special methods that allow us to create a List without knowing the type, which is different than creating a List from an ArrayList or a LinkedList. These methods are factory methods that allow us to create a List including data in one line. Some of these methods return an immutable object, as you might remember from previous chapters. These are the three factory methods to create a List:

 | Method                    | Description                                                   | Can add elements?  | Can replace element?  | Can delete elements?  |
 | :------------------------ | :-----------------------------------                          | :----------------- | :-------------------- | :-------------------- |
 | Arrays.asList(varargs)    | Returns fixed size list backed by an array                    | No                 | Yes                   | No                    |
 | List.of(varargs)          | Returns immutable list                                        | No                 | No                    | No                    |
 | List.copyOf(collection)   | Returns immutable list with copy of original collection's values  | No             | No                    | No                    |

 Here are some examples on how to use them:

    String[] array = new String[] { "a", "b", "c" };
    List<String> asList = Arrays.asList(array); // [a, b, c]
    List<String> of = List.of(array); // [a, b, c]
    List<String> copyOf = List.copyOf(asList); // [a, b, c]

    array[0] = "z";

    System.out.println(asList); // [z, b, c]
    System.out.println(of); // [a, b, c]
    System.out.println(copyOf); // [a, b, c]

    asList.set(0, "x");
    System.out.println(Arrays.toString(array)); // [x, b, c]

    copy.add("y"); // throws UnsupportedOperationException

Basically, the update on the elements only works when using asList, because the array changes when the asList List changes and vice versa, the other two types of List would throw an exception. All the lists would throw a exception when trying to add or delete elements.

#### Working with *List* Methods

These are methods of the List interface, they work with indexes. These are some that you should be familiar with, in addition to the ones inherited from Collection:

- `boolean add(E element)`: Adds the element to the end (available on all Collection APIs) 
- `void add(int index, E element)`: Adds the element at the index and moves the rest towards the end
- `E get(int index)`: Returns the element at the index
- `E remove(int index)`: Removes the element at the index and moves the rest towards the front
- `void replaceAll(UnaryOperator<E> op)`: Replaces each element in the list with the result of the operator
- `E set(int index, E e)`: Replaces the element at a index and returns the original. Throws **IndexOutOfBoundsException** if the index is larger than the maximum one set

The following examples demonstrate most of these:

    List<String> list = new ArrayList<>();
    list.add("SD");
    list.add(0, "NY");
    System.out.println(list.get(0));   // [NY]
    list.set(1, "FL");
    System.out.println(list);   // [NY, FL]
    list.remove("NY");
    list.remove(0);
    list.set(0, "?");   // IndexOutOfBoundsException

> **Note:** The output would be the same if you tried these examples with a LinkedList. The code would be less efficient, but only noticiable on veryt large lists. 

The `replaceAll()` is a little different than the others, since it takes an UnaryOperator that takes one parameter and returns a value of the same type, for example: 

    List<Integer> numbers = Arrays.asList(1, 2, 3);
    numbers.replaceAll(x -> x * 3);
    System.out.println(numbers); // [3, 6, 9]

This lambda triples the value of each value in the list. So this method calls the lambda on each element of the list and replaces the value at that index.

### Using the *Set* Interface

We use a Set when we don't want to allow duplicate elements on a Collection. Set has some different implementation, but what they all have in common is that they don't allow duplicates.

#### Comparing *Set* Implementations

A HashSet stores its elements in a *hash table*, which means the elements keys are a hash and the values are an Object. It uses the `hashCode()` method of the objects to retrieve them more efficiently. The main benefit is that adding elements and checking whether an element is in the set both have constant time, but in the other hand, we lost the order in which the elements were inserted in the set.

A TreeSet stores its elements in a sorted tree structure. The main benefit is that the set is always sorted, the trade-off being that when you add and check whether an element exists takes longer than with a HashSet, and it takes longer as the tree grows larger.

#### Working with *Set* Methods

Like List, you can create an immutable Set in one line or make a copy of an existing one, for example:

    Set<Character> letters = Set.of('Z', 'o', 'O');
    Set<Character> copy = Set.copyOf(letters);

These are the only extra Set methods that you need to know for the exam, on the other hand, you need to know how sets behave with respect to the traditional Collection methods. Another important thing to know is the difference between the types of Set, first HashSet:

    Set<Integer> set = new HashSet<>();
    boolean b1 = set.add(66); // true
    boolean b2 = set.add(10); // true
    boolean b3 = set.add(66); // false
    boolean b4 = set.add(8); // true
    set.forEach(System.out::println); // this code can print: 66 8 10

The elements are not ordered, because HashSet is not ordered. Now let's take a look on TreeSet:

    Set<Integer> set = new TreeSet<>();
    boolean b1 = set.add(66); // true
    boolean b2 = set.add(10); // true
    boolean b3 = set.add(66); // false
    boolean b4 = set.add(8); // true
    set.forEach(System.out::println); // this code can print: 8 10 66

The elements are printed in their natural order. Numbers implements the Comparable interface.

The `equals()` method is used to determine equality. On Sets the `hashCode()` method is used to know which *bucket* to look in so that Java doesn't have to look through the whole set to find out wheter an object is there. If all implementations return the same `hashCode()`, then Java needs to call `equals()` on every element of the set. 

### Using the *Queue* Interface

Usually we use a Queue when elements are added and removed in a specific order, for sorting elements too. Unless stated otherwise, a queue is assumed to be FIFO (first-in, first-out), but some queue implementations can change this to use a different type of ordering. Another commonly used format is LIFO (last-in, first-out), also referred to as a *stack*. In Java, both can be implemented with the Queue interface.

In a Queue using FIFO order, the first one to be added is considered to be in the front of the queue (index 0), and the last one to be added to be in the back of the queue (index n).

#### Comparing *Queue* Implementations

LinkedList is not only a list, it is a double-ended queue too. A double-ended queue is different from a regular queue in that you can insert and remove elements from both the front and back of the queue. The main benefit of a LinkedList is that it implements both the List and Queue interfaces, the trade-off is that it isn't as efficient as a common queue. You can use the ArrayDeque class (short for double-ended queue) if you need a more efficient one. 

> **Note:** **ArrayDeque** is not in scope for the exam.

#### Working with *Queue* Methods

The Queue interface contains a lot of methods, but there are only six that you need to be familiar for the exam. These six Queue methods are:

 | Method                    | Description                                                                      | Throws exception on failure  |
 | :------------------------ | :-----------------------------------                                             | :-----------------           |
 | boolean add(E e)          | Adds an element to the back of the queue and returns true or throws an exception | Yes                          |
 | E element()               | Returns next element or throws an exception if the queue is empty                | Yes                          |
 | boolean offer(E e)        | Adds an element to the back of the queue and returns whether it was successful   | No                           |
 | E remove()                | Removes and returns next element or throws an exception if the queue is empty    | Yes                          |
 | E poll()                  | Removes and returns next element or returns null if the queue is empty           | No                           |
 | E peek()                  | Returns next element or returns null if the queue is empty                       | No                           |
    

There are two sets of methods, one that throws an exception when something goes wrong, and another that return a different value when something goes wrong. The `offer()`/`poll()`/`peek()` methods are the most common ones. Let's look at some examples:

    Queue<Integer> queue = new LinkedList<>();
    System.out.println(queue.offer(10)); // true
    System.out.println(queue.offer(4)); // true
    System.out.println(queue.peek()); // 10 
    System.out.println(queue.poll()); // 10 
    System.out.println(queue.poll()); // 4 
    System.out.println(queue.peek()); // null 

> **Note:** Some queues are limited in size, which would cause offering an element to the queue to fail, but this type of scenario is not present in the exam.


### Using the *Map* Interface

Map is used when we need to identify values by a key. For example, a contact list on your phone, you will look up for the contact by its name rather than its phone number. There are some different map implementations, you don't need to know all their names, but you need to know that a TreeMap is sorted. The main thing that all Map classes have in common is that they all have keys and values. Beyond that, they each can offer different functionalities.

Just like *List* and *Set*, there is a helper method to create a Map in one line. But you need to pass the values in **pairs of keys and values**. For example: 

    Map.of("key1", "value1", "key2", "value2", ...);

Unlike the other interfaces, this is not ideal to use, because it can lead to mistakes like miscounting the keys and values and leaving some value out. This kind of code would not compile. But there is a better way to create a Map, which allows you to supply key and value pairs, following is a exemple:

    Map.ofEntries(
        Map.entry("key1", "value1"),
        Map.entry("key2", "value2"));

On this case, if we leave out a parameter, the `entry()` method will not compile. The `Map.copyOf(map)` method works just like the previous interfaces methods.

#### Comparing *Map* Implementations

A HashMap stores the keys in a **hash table**. This means it uses the `hashCode()` method to retrieve the keys more efficiently. The main benefit is that adding elements and retrieving the element by key both have constant time. The trade-off is that you lose the order in which you inserted the elements, but most of the time you aren't concerned with that. If you need that order, you can use LinkedHashMap instead, but this implementation is not in scope for the exam.

A TreeMap stores the keys in a **sorted tree strucuture**. This means that the keys are always in sorted order, but like a TreeSet, the trade-off is that adding and checking whether a key is present takes longer as the tree grows larger.

#### Working with *Map* Methods

Remember that Map does not extend Collection, so there are more specific methods for this interface. Since there are both keys and valuess, we are going to need generic type parameters for both. These generic parameters are K for key and V for value. The methods that you need to know for the exam are listed in the following table. Some of them were simplified to make them easier to understand.

| Method                    | Description                                                                      |
| :------------------------ | :-----------------------------------                                             |
| void clear()          | Removes all keys and values from the map |
| boolean containsKey(Object key)          | Returns whether key is in map |
| boolean containsValue(Object value)          | Returns whether value is in map |
| Set<*Map.Entry<K,V>*> entrySet()          | Returns a Set of key/value pairs  |
| void forEach(BiConsumer(K key, V value))          | Loop through each key/value pairs  |
| V get(Object key)          | Returns the value mapped by the key or null if none is mapped |
| V getOrDefault(Object key, V defaultValue)          | Returns the value mapped by the key or the default value if none is mapped |
| boolean isEmpty()          | Returns whether the map is empty |
| Set<*K*> keySet()          | Returns set of all keys |
| V merge(K key, V value, BiFunction(<*V, V, V*> func)) | Sets value if the key is not set. Runs the function if the key is set to determine the new value. Removes if null  |
| V put(K key, V value) | Adds or replaces a key/value pair and returns previous value or null |
| V putIfAbsent(K key, V value) | Adds a value if key is not present and returns null. Otherwise, returns the existing value |
| V remove(Object key) | Removes and returns the value mapped to the key or returns null if none |
| V replace(K key, V value) | Replaces the value for a given key if the key is set and returns the original value or null if none |
| void replaceAll(BiFunction<*K, V, V*> func) | Replaces each value with the results of the function |
| int size() | Returns the number of entries (key/value pairs) in the map |
| Collection<V> values() | Returns a Collection of all values |

##### Basic Methods

Here we are going to compare two Map types (HashMap and TreeMap) with the basic methods from the Map interface. First let's use HashMap:

    Map<String, String> map = new HashMap<>();
    map.put("koala", "bamboo");
    map.put("lion", "meat");
    map.put("giraffe", "leaf");
    String food = map.get("koala"); // bamboo
    for (String key: map.keySet()) System.out.print(key + ","); // koala, giraffe, lion

On this example we can see that with `put()` we insert some pairs into the Map, with `get()` we get the value given the key of that pair and finally with `keySet()` we get all the keys. As you might notice, the order of the values printed are not sorted, thats because we are using a HashMap, which uses the `hashCode()` method of the keys to determine the order. Now let's take a look at TreeMap:

    Map<String, String> map = new TreeMap<>();
    map.put("koala", "bamboo");
    map.put("lion", "meat");
    map.put("giraffe", "leaf");
    String food = map.get("koala"); // bamboo
    for (String key: map.keySet()) System.out.print(key + ","); // giraffe, koala, lion

TreeMap **sorts** the keys as we would expect. If we were to have called `values()` instead, the order of the values would correspond to the order of the keys.

With the same map, we can do some boolean checks:

    System.out.println(map.contains("lion")); // DOES NOT COMPILE - Because contains does not exists on the Map interface
    System.out.println(map.containsKey("lion")); // true
    System.out.println(map.containsValue("lion")); // false
    System.out.println(map.size()); // 3
    map.clear();
    System.out.println(map.size()); // 0
    System.out.println(map.isEmpty()); // true

As we can see, most of these methods are simple to use and do literally what their names indicate.
 
##### *forEach()* and *entrySet()*

The Map implementation of the method `forEach()` is a little different than the one seen before, the lambda on this one has two parameters instead of one (its a BiConsumer instead of a Consumer), the key and the value. For example:

    Map<Integer, String> map = new HashMap<>();
    map.put(1, "a");
    map.put(2, "b");
    map.put(3, "c");
    map.forEach((k, v) -> System.out.println(v));

If you dont't need the key, you can call the method `values()` on the map and then use a method reference instead, like the following: 

    map.values().forEach(System.out::println);

Another way of using the `forEach()` method is with the `entrySet()` method, which transforms the Map pairs in a Set. Java has a static interface inside Map called Entry and it provides methods to get the key and value each pair, like the following: 

    map.entrySet().forEach((e) -> System.out.println(e.getKey() + e.getValue()));

##### *getOrDefault()*

The `get()` method returns null if the requested key is not found, but if we need a different value to be return, we can use the `getOrDefault()` method instead. This method allows us to pass a parameter that defines a default value that will be returned if the key is not mapped. For example, the following compare both of them:

    Map<Character, String> map = new HashMap<>();
    map.put('x', "spot");
    System.out.println(map.get('x')); // spot
    System.out.println(map.getOrDefault('x', "")); // spot
    System.out.println(map.get('y')); // null
    System.out.println(map.getOrDefault('y', "")); // ""

##### *replace()* and *replaceAll()*

These are similar to the Collection version of them, except that a key is involved. For example:

    Map<Integer, Integer> map = new HashMap<>();
    map.put(1, 2);
    map.put(2, 4);
    Integer original = map.replace(2, 10); // 4 - returns the original value
    System.out.println(map); // { 1=2, 2=10 }
    map.replaceAll((k, v) -> k + v);
    System.out.println(map); // { 1=3, 2=12 }

##### *putIfAbsent()*

This method sets a value in the map but skips it if the value is already set to a non-null value. For example:

    Map<String, String> favorites = new HashMap<>();
    favorites.put("Jenny", "Bus Tour");
    favorites.put("Tom", null);
    favorites.putIfAbsent("Jenny", "Tram");
    favorites.putIfAbsent("Sam", "Tram");
    favorites.putIfAbsent("Tom", "Tram");
    System.out.println(favorites); // {Tom=Tram, Jenny=Bus Tour, Sam=Tram}

Only the ones with a null value or non mapped were added to the Map.

##### *merge()*

This method allows us to add logic on him of what to choose. For example, if we needed to choose the longest name, we could write a code to express this by passing a mapping function to the `merge()` method:

    BiFunction<String, String, String> mapper = (v1, v2) -> v1.length() > v2.length ? v1 : v2;

    Map<String, String> favorites = new HashMap<>();
    favorites.put("Jenny", "Bus Tour");
    favorites.put("Tom", "Tram");

    String jenny = favorites.merge("Jenny", "Skyride", mapper);
    String tom = favorites.merge("Tom", "Skyride", mapper);

    System.out.println(favorites); // {Tom=Skyride, Jenny=Bus Tour}
    System.out.println(jenny); // Bus Tour
    System.out.println(tom); // Skyride

The `merge()` method can also be used for missing values or null values. But in this case, it doesn't call the BiFunction, because it just uses the new value:

    BiFunction<String, String, String> mapper = (v1, v2) -> v1.length() > v2.length() ? v1 : v2;
    
    Map<String, String> favorites = new HashMap<>();
    favorites.put("Sam", null);
    favorites.merge("Sam", "Skyride", mapper);
    favorites.merge("Tom", "Skyride", mapper);
    System.out.println(favorites); // {Tom=Skyride, Sam=Skyride}

Notice that the function is not called, because if it were, we would have a NullPointerException thrown. The mapping funcion is used only when there are two actual values to decide between them.

The last thing you'll need to know about the `merge()` method, is that when the mapping function is called and returns a null, the key is removed from the map:

    BiFunction<String, String, String> mapper = (v1, v2) -> null;

    Map<String, String> favorites = new HashMap<>();
    favorites.put("Tom", "Bus Tour");
    favorites.put("Sam", "Bus Tour");

    favorites.merge("Tom", "Skyride", mapper);
    favorites.merge("Jenny", "Skyride", mapper);

    System.out.println(favorites); // { Tom=Bus Tour, Jenny=Skyride }

Notice that the value that existed on the map was removed, but the one that didn't was added.

The following table shows these scenarios of the `merge()` method:

 | If the request key " "    | And mapping function returns " "     | Then:              |
 | :------------------------ | :----------------------------------- | :----------------- |
 | Has a null value in the map | N/A (mapping function not called)  | Update key's value in map with value parameter |
 | Has a non-null value in the map | null                           | Remove key from map |
 | Has a non-null value in the map | A non-null value               | Set key to mapping function result |
 | Is not in the map  | N/A (mapping function not called)           | Add key with value parameter to map directly without calling mapping function |

 ### Comparing Collection Types

 We conclude this section reviewing all these collection classes, the following table shows some of the Collections Framework types:

 | Type    | Can contain duplicate elements?      | Elements always ordered?            | Has keys and values? | Must add/remove in specific order? | 
 | :------ | :----------------------------------- | :---------------------------------- | :------------------- | :--------------------------------- |
 | List    | Yes                                  | Yes (by index)                      | No                   | No                                 |
 | Map     | Yes (for values)                     | No                                  | Yes                  | No                                 |
 | Queue   | Yes                                  | Yes (retrieved in defined order)    | No                   | Yes                                |
 | Set     | No                                   | No                                  | No                   | No                                 |
    
Additionally, collection atributes:

 | Type    | Java Collections Framework interface | Sorted?    | Calls `hashCode`?    | Calls `compareTo`? |
 | :------ | :----------------------------------- | :--------- | :------------------- | :------------------|
 | ArrayList   | List                             | No         | No                   | No                 |
 | HashMap     | Map                              | No         | Yes                  | No                 |
 | HashSet     | Set                              | No         | Yes                  | No                 |
 | LinkedList  | List, Queue                      | No         | No                   | No                 |
 | TreeMap     | Map                              | Yes        | No                   | Yes                |
 | TreeSet     | Set                              | Yes        | No                   | Yes                |

 > **Note:** Remember that the data structures that involve sorting do not allow null values. 

 ### Some Older Java Collections

 All the following three were early Java data strucutres that you could use with threads, but nowadays there are better modern alternatives if you need a concurrent collection. The following are the collections:

- Vector: Implements List. If you don't need concurrency, use ArrayList instead.
- Hashtable: Implements Map. If you don't need concurrency, use HashMap instead.
- Stack: Implements Queue. If you don't need concurrency, use LinkedList instead.


## Sorting Data (p.631-641)

We discussed earlier about 'order' for TreeSet and TreeMap. For numbers the order its numerical as always, but for String objects, order is defined according to the Unicode char mapping, but for the exam, this means that numbers sort before letters, and uppercase letters sort before lowercase letters.

On many of the following examples, `Collections.sort()` will be used, this method returns void because the method parameter that gets sorted. We can also sort objects that we create. Java provides an interface called *Comparable*, if the created class implements it, it can be used in these data strucutures that require comparison. There is also a class called *Comparator*, this one can be used to specify that we want to use a different order than the object itself provides. Don't be confused by both of them, remember that Comparable and Comparator are different objects.

### Creating a *Comparable* Class

The Comparable interface has only one method, the following code snippet is the entire interface:

    public interface Comparable<T> {
        int compareTo(T o);
    }

The generic T on Comparable lets us implement this method and specify the type of the object. With this T, we avoid a cast when implementing compareTo(). Any object can be Comparable. For example:

    import java.util.*;
    public class Duck implements Comparable<Duck> {
        private String name;
        public Duck(String name) {
            this.name = name;
        }
    }
    public String toString() {
        return name;
    }
    public int compareTo(Duck d) {
        return name.compareTo(d.name); // sorts asc by name
    }
    public static void main(String[] args) {
        var ducks = new ArrayList<Duck>();
        ducks.add(new Duck("Quack"));
        ducks.add(new Duck("Puddles"));
        Collections.sort(ducks); // sorts by name
        System.out.println(ducks); // [Puddles, Quack]
    }

Without implementing the interface, all we would have is a method named `compareTo()`, it wouldn't be a Comparable object.

> **Note:** Since Duck is comparing objects of type String and the String class already has a `compareTo()` method, **it can just delegate**. 

We need to know three rules to understand what the `compareTo()` method returns, these are:

1. The number 0 is returned when the current object is equivalent to the argument to `compareTo()`.
2. A negative number (less than 0) is returned when the current object is smaller than the argument to `compareTo()`.
3. A positive number (greater than 0) is returned when the current object is larger than the the argument to `compareTo()`.

For example:

    public class Animal implements Comparable<Animal> {
        private int id;
        public int compareTo(Animal a) {
            return id - a.id; // sorts asc by id
        }
        public static void main(String[] args) {
            var a1 = new Animal();
            var a2 = new Animal();
            a1.id = 5;
            a2.id = 7;
            System.out.println(a1.compareTo(a2)); // -2
            System.out.println(a1.compareTo(a1)); // 0
            System.out.println(a2.compareTo(a1)); // 2
        }
    }

> **Note:** Remember that `id - a.id` sorts in ascending order, and `a.id - id` sorts in descending order.

As we can see the outputs, they reflect the rules that were presented before.

#### Casting the *compareTo()* Argument

When dealing with legacy code or code that don't use generics, the compareTo() method requires a cast since it is passed an Object. We can do that as the following example:

    public class LegacyDuck implements Comparable { // note that there is no generics declared
        private String name;
        public int compareTo(Object obj) {
            LegacyDuck d = (LegacyDuck) obj; // casting because there is no generics
            return name.compareTo(d.name);
        }
    }

Since we don't specify a generic type for Comparable, Java assumes that we want an Object, which means that we have to cast to the desired Object before accessing instance variables on it.

#### Checking for *null*

When working with Comparable and Comparator we tend to assume the data has values, but this isn't always the reality. When writing our own methods, we should check the data before comparing it if is not validated ahead of time. For example:

    public class MissingDuck implements Comparable<MissingDuck> {
        private String name;
        public int compareTo(MissingDuck quack) {
            if (quack == null) throw new IllegalArgumentException("Poorly formed");
            if (this.name == null && quack.name == null) 
                return 0;
            else if (this.name == null) return -1;
            else if (quack.name == null) return 1;
            else return name.compareTo(quack.name);
        }  
    }


#### Keeping *compareTo()* and *equals()* Consistent

If a class implements Comparable, new business logic for determining equality is introduced. The `compareTo()` method returns 0 when the two objects are equal, while the `equals()` method returns true when two objects are equal. A *natural ordering* that uses `compareTo()` is said to be *consistent with equals* if, and only if, `x.equals(y)` is true whenever `x.compareTo(y)` equals 0, and vice versa. It's strongly encouraged to make our own Comparable classes consistent with equals because not all collection classes behave predictably if the `compareTo()` and `equals()` methods are not consistent. For example, a class that defines a `compareTo()` that is not consistent with `equals()`:

    public class Product implements Comparable<Product> {
        private int id;
        private String name;

        public int hashCode() { return id; }
        public boolean equals(Object obj) {
            if (!(obj instanceof Product)) return false;
            var other = (Product) obj;
            return this.id == other.id;
        }
        public int compareTo(Product obj) {
            return this.name.compareTo(obj.name);
        } 
    }

We might sort this by name, but names is not unique. Therefore, the return value of `compareTo()` might not be 0 when comparing two equal Product objects, so this method is not consistent with equals. One way to fix this would be to use a Comparator to define the sort elsewhere.

### Comparing Data with a *Comparator*

If we want to sort an object that did not implement Comparable, or sort objects in different ways at different times, we can use Comparator, for example:

    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.Comparator;

    public class Duck implements Comparable<Duck> {
        private String name;
        private int weight;

        // assume that there are getters/setters/constructors provided
        public String toString() {
            return name;
        }
        public int compareTo(Duck d) {
            return name.compareTo(d.name);
        }
        public static void main(String[] args) {
            Comparator<Duck> byWeight = new Comparator<Duck>() {
                public int compare(Duck d1, Duck d2) {
                    return d1.getWeight()-d2.getWeight();
                }
            };
            var ducks = new ArrayList<Duck>();
            ducks.add(new Duck("Quack", 7));
            ducks.add(new Duck("Puddles", 10));
            Collections.sort(ducks); // sorts by name
            System.out.println(ducks); // [Puddles, Quack]
            Collections.sort(ducks, byWeight); // sorts by weight with Comparator
            System.out.println(ducks); // [Quack, Puddles]
        }
    }

> **Note:** Comparator is in java.util package, while Comparable is in java.lang (it does not have to be imported).

Comparator is a functional interface, since there is only one abstract method to implement. We could rewrite it with a lambda.

    Comparator<Duck> byWeight = (d1, d2) -> d1.getWeight()-d2.getWeight();

Alternativaly, we can use a method reference and a helper method to specify we want to sort by weight.

    Comparator<Duck> byWeight = Comparator.comparing(Duck::getWeight);

In this example, `Comparator.comparing()` is a static interface method that creats a Comparator given a lambda expression or a method reference.

> **Is *Comparable* a Functional Interface?** Comparable is a functional interface, since it has only one abstract method, but it would be silly to use it with a lambda, because the point of Comparable is to implement it inside the object being compared.

### Comparing *Comparable* and *Comparator*

There are some differences between Comparable and Comparator, they are listed on the following table:

 | Difference                                           | Comparable    | Comparator    |
 | :------                                              | :-------------| :------------ |
 | Package name                                         | java.lang     | java.util     |
 | Interface must be implemented by class comparing?    | Yes           | No            |
 | Method name in interface                             | compareTo()   | compare()     |
 | Number of parameters                                 | 1             | 2             |
 | Common to declare using a lambda                     | No            | Yes           |

 Memorize this table for the exam. For example, the following code does not compile because the method is wrong. A Comparator must implement a method named `compare()`. Pay special attention to these details.

    var byWeight = new Comparator<Duck>() { // DOES NOT COMPILE
        public int compareTo(Duck d1, Duck d2) {
            return d1.getWeight() - d2.getWeight();
        }
    }

### Comparing Multiple Fields

When writing a Comparator that compares multiple instance vars, the code can turn a lot more complex than it really need to be. We can use method references and build a comparator. For example, the following code can be reduced:

    public class MultiFieldComparator implements Comparator<Squirrel> {
        public int compare(Squirrel s1, Squirrel s2) {
            int result = s1.getSpecies().compareTo(s2.getSpecies);
            if (result != 0) return result;
            return s1.getWeight()-s2.getWeight();
        } 
    }

This code presented above, represents the same logic as the following: 

    Comparator<Squirrel> c = Comparator.comparing(Squirrel::getSpecies).thenComparingInt(Squirrel::getWeight);

Here we chain the method, first creating a comparator on species asc and then sort it by weight if there is a tie. We can also sort in desc order. This method `thenComparingInt()` is one of the many default method on Comparator. 

> **Note:** To sort in descending order, we can use Comparator method `reversed()`.

The following table shows the helper methods we need to know for building a Comparator. The parameters are omitted to keep the focus on the methods. They use many functional interfaces that we'll be going to talk about in the next chapter.

 | Method                     | Description     | 
 | :------                    | :-------------  | 
 | comparing(function)        | Compare by the results of a function that returns any Object (or object autoboxed into an Object) | 
 | comparingDouble(function)  | Compare by the results of a function that returns a double | 
 | comparingInt(function)     | Compare by the results of a function that returns an int | 
 | comparingLong(function)    | Compare by the results of a function that returns a long | 
 | naturalOrder()             | Sort using the order specified by the Comparable implementation on the object itself | 
 | reverseOrder()             | Sort using the reverse of the order specified by the Comparable implementation on the object itself | 

The following table shows the methods that we can chain to a Comparator to further specify its behavior.

 | Method                           | Description     | 
 | :------                          | :-------------  | 
 | reversed()                       | Reverse the order of the chained Comparator | 
 | thenComparing(function)          | If the previous Comparator returns a 0, use this comparator that returns an Object or can be autoboxed into one | 
 | thenComparingDouble(function)    | If the previous Comparator returns a 0, use this comparator that returns a double, otherwise, return the value from the previous Comparator  | 
 | thenComparingInt(function)       | If the previous Comparator returns a 0, use this comparator that returns an int, otherwise return the value from the previous Comparator | 
 | thenComparingLong(function)      | If the previous Comparator returns a 0, use this comparator that returns a long, otherwise, return the value from the previous Comparator | 

### Sorting and Searching

For sorting we can use `Collections.sort()` method uses the `compareTo()` method to sort. It expects teh objects to be sorted to be Comparable.

    public class SortRabbits {
        static class Rabbit{ int id; }
        public static void main(String[] args) {
            List<Rabbit> rabbits = new ArrayList<>();
            rabbits.add(new Rabbit());
            Collections.sort(rabbits); // does not compile
        }
    }

 It does not compile because Java knows that the Rabbit class is not Comparable, knowing that the sorting will fail, it doesn't even let the coded compile. You can fix this by passing a Comparator to `sort()`. Remember that a Comparator is useful when you to specify sort order without using a `compareTo()` method.

    public class SortRabbits {
        static class Rabbit{ int id; }
        public static void main(String[] args) {
            List<Rabbit> rabbits = new ArrayList<>();
            rabbits.add(new Rabbit());
            Comparator<Rabbit> c = (r1, r2) -> r1.id - r2.id;
            Collections.sort(rabbits, c);
        }
    }

The `sort()` and `binarySearch()` methods allow you to pass in a Comparator object when you don't want to use the natural order.

#### **Reviewing** *binarySearch()*

The `binarySearch()` method requires a sorted List. 

    List<Integer> list = Arrays.asList(6,9,1,8);
    Collections.sort(list);
    System.out.println(Collections.binarySearch(list, 6)); // 1
    System.out.println(Collections.binarySearch(list, 3)); // -2

It sorts and then it searchs, so it can call binary search properly. The first print, prints the index at which a match is found. The other one prints a negated index of where the requested value would need to be inserted.

    var names = Arrays.asList("Fluffy", "Hoppy");
    Comparator<String> c = Comparator.reverseOrder();
    var index = Collections.binarySearch(names, "Hoppy", c);
    System.out.println(index);

The correct answer -1. Before you panic, you don't need to know that the answer is not defined, you do need to know that the answer is not defined. This list is sorted in ascending order. The comparator reverses the order to the natural order. The line that requests the `binarySearch()`, requests it in descending order. Since the list is ordered in ascending order, the precondition for doing the search is not met. 

We talked about collections that require classes to implement Comparable. Unlike sorting, they don't check that you have actually implemented Comparable at compile time.

If we try to add that Rabbit that does not implement to a TreeSet, an exception is thrown. When TreeSet tries to sort the rabbits after adding one of them, Java discovers the fact that the class does not implement Comparable. Java throws an exception that looks like this:

    Exception in thread "main" java.lang.ClassCastException:
        class Duck cannot be cast to class java.lang.Comparable

Java throws the exception even when the first object is added to the set, it works this way for consistency. If we tell collections that require sorting that we want to use a specific Comparator, Java knows that we want that and all is well. For example:

    Set<Rabbit> rabbits = new TreeSet<>((r1,r2) -> r1.id-r2.id);
    rabbits.add(new Rabbit());

    // Instead of
    Set<Rabbit> rabbits = new TreeSet<>();
    rabbits.add(new Rabbit()); // ClassCastException

## Working with Generics (p.641-655)

Now we conclude the Chapter with one of the most useful (at times most confusing) feature in the Java language: generics. Generics allow we to write and use parameterized types. We can specify that, for example, we want an ArrayList of String objects. With this, the compiler has enough info to prevent we from causing a casting problem for example. The following code shows a "good" compiler error, because it warns you of a mistake:

    List<String> names = new ArrayList<String>();
    names.add(new StringBuilder("Webby")); // DOES NOT COMPILE

### Generic Classes

You can introduce generics into your own classes. The syntax for introducing a genericis to declare a *formal type parameter* in angle brackets. For example, the following class has a generic type var declared after the name of the class:

    public class Crate<T> {
        private T contents;
        public T emptyCrate() {
            return contents;
        }
        public void packCrate(T contents) {
            this.contents = contents;
        }
    }

The generic type T is available anywhere within the Crate class. When you instantiate the class, you tell the compiler what T should be for that particular instance.

There are naming conventions for generics, while you can name a type anything you want, the convention is to use single uppercase letters to make it obvious that they aren't real class names. The following are the most common letters to use:

- E for an element
- K for a map key
- V for map value
- N for a number
- T for a generic data type
- S, U, V and so forth for multiple generic types

The Crate class presented before can be used with any type of class:

    Elephant elephant = new Elephant();
    Crate<Elephant> crateForElephant = new Crate<>();
    crateForElephant.packCrate(elephant);

    Zebra zebra = new Zebra();
    Crate<Zebra> crateForZebra = new Crate<>();
    crateForZebra.packCrate(zebra);
    
    Robot robot = new Robot();
    Crate<Robot> crateForRobot = new Crate<>();
    crateForRobot.packCrate(robot);

> **Note:** Generic classes become useful when the classes used as the type parameter can have absolutely nothing to do with each other.

Before generics, we would need to use Object as the type on Crate and cast the objects returned. When using generics, the generic class don't need to know about the objects that go into it and vice versa. We do not require the objects implement an interface or anything like that. A class can be put in the generic class without any changes at all.

Generics aren't limited to having a single type parameter, for example, the following class has two generic type parameters:

    public class SizeLimitedCrate<T, U> {
        private T contents;
        private U sizeLimit;
        public SizeLimitedCrate(T contents, U sizeLimit) {
            this.contents = contents;
            this.sizeLimit = sizeLimit;
        }
    }

On this example, T is the type of the crate and U represents the unit that we are using to measure the maximum size for the crate. We can use this class like this:

    Elephant elephant = new Elephant();
    Integer numPounds = 15_000; // Remember that numeric literals can have underscores
    SizeLimitedCrate<Elephant, Integer> c1 = new SizeLimitedCrate<>(elephant, numPounds);

Here the type is Elephant and the unit is Integer. 

### What Is Type Erasure?

Specifying a generic type allows the compiler to enforce proper use of the generic type. For example, specifying the type of Crate as Test is like replacing T in Crate with Test. But this is just for compile time. The compiler replaces all references to T in Crate with Object, making Crate look like the following at runtime:

    public class Crate<Object> {
        private Object contents;
        public Object emptyCrate() {
            return contents;
        }
        public void packCrate(Object contents) {
            this.contents = contents;
        }
    }

This means there is only **one** .class file. There are no copies for different parameterized types. (Some other languages work that way.)

This process of removing the generics syntax from the code is referred to as *type erasure*. Type erasure allows your code to be compatible with older versions of Java that do not contain generics. 

The compiler adds the relevant casts for your code to work with this type of erased class. For example, if we code the following:

    Robot r = crate.emptyCrate();

The compiler turns it into the following:

    Robot r = (Robot) crate.emptyCrate();
    
### Generic Interfaces

Just like a class, an interface can declare a formal type parameter. For example:

    public interface Shippable<T> {
        void ship(T t); 
    }

There are three ways a class can approach implementing this interface, the first is to specify the generic type in the class. The following concrete class says that it deals only with robots. 

    class ShippableRobotCrate implements Shippable<Robot> {
        public void ship(Robot t) { }
    }

The next way is to create a generic class. The following concrete class allows the **caller** to specify the type of the generic:

    class ShippableAbstractCrate<U> implements Shippable<U> {
        public void ship(U t) { }
    }

> **Note:** Remember that the type parameter can be named anything, including T or U.

### Raw Types

The final way is not use generics at all! This is the old way of writing code. It generates a compiler warning about the Class being a *raw type*, but it does compile. On this case, the `ship()` method has an Object parameter since the generic type is not defined:

    class ShippableCrate implements Shippable {
        public void ship(Object t) { }
    }

> **Note:** Basically this is the equivalent of we coding what the compiler would do to a generic code behind the scenes.

### What You Can't Do with Generic Types (Real World Scenario)

There are some limitations on what you can do with a generic type. These aren't on the exam, but they are helpful for real world scenarios. 

Most of the limitations are due to type erasure. Oracle refers to types whose information is fully available at runtime as *reifiable*. Reifiable types can do anything that Java allows, while nonreifiable types have some limitations.

Here are the things that you can't do with generics:

- Calling a constructor: Writing `new T()` is not allowed because at runtime it would be `new Object()`.
- Creating an array of that generic type: This one is the most annoying, but it makes sense because you'd be creating an array of Object values.
- Calling instanceof: This is not allowed because at runtime `List<Integer>` and `List<String>` look the same to Java thanks to type erasure.
- Using a primitive type as a generic type parameter: This isn't a big deal because you can use the wrapper class instead. If you want a type of int, just use Integer.
- Creating a static variable as a generic type parameter: This is not allowed because the type is linked to the instance of the class.

### Generic Methods

We can declare formal type parameters on method level too. It's often used for static methods since they aren't part of an instance that can declare the desired type. However, they are also allowed on non-static methods. The following is an example of methods using generic parameters:

    public class Handler {
        public static <T> void prepare(T t) {
            System.out.println("Preparing" + t);
        }
        
        public static <T> Crate<T> ship(T t) {
            System.out.println("Shipping" + t);
            return new Crate<T>();
        }
    }

The method parameter is the generic type T. Before the return type, we need to declare the `formal type parameter of <T>`. In the ship method, we do show how to use the generic parameter in the return type, `Crate<T>`, for the method.

Unless a method is obtaining the generic formal type parameter from the class/interface, it is specified immediately before the return type of the method. The following are some examples of this:

    public class More {
        public static <T> void sink(T t) { }
        public static <T> T identity(T t) { return t; }
        public static void sink(T t) { } // DOES NOT COMPILE, because it omits the formal parameter type
        public static T noGood(T t) { return t; } // DOES NOT COMPILE, because it omits the formal parameter type
    }

We can call a generic method normally and the compiler will try to figure out which one we want. But there is an optional syntax for invoking a generic method, which is specifying the type explicitly, making it obvious what the type is:

    Box.<String>ship("package");
    Box.<String[]>ship(args);

When a method declare a generic parameter type, it is independent of the class generics. For example, the following class declares a generic T on both levels:

    public class Crate<T> {
        public <T> T tricky(T t) {
            return t;
        }
    }

When we call the code as follows:

    public static String createName() {
        Crate<Robot> crate = new Crate<>();
        return create.tricky("bot");
    }

T on the Crate class declaration is Robot, because is what gets referenced when constructing a Crate, on the other hand, the T on the method tricky is String, because that is what is passed to the method. So keep an eye out for these kind of code on the exam. 

### Bounding Generic Types

Up until this point, we saw generics being treated as an Object and therefore don't have many methods available. Bounded wildcards solve this by restricting what types can be used in a wildcard. A *bounded parameter type* is a generic type that specifies a bound for the generic. 

A *wildcard generic type* is an unknown generic type represented with a question mark (?). We can use generic wildcards in three ways, the following table shows each one of these three wildcard types:

 | Type of Bound                   | Syntax         | Example    |
 | :------                         | :------------- | :------------ |
 | Unbounded wildcard              | ?              | `List<?> a = new ArrayList<String>();`     |
 | Wildcard with an upper bound    | ? extends type | `List<? extends Exception> a = new ArrayList<RuntimeException>();`     |
 | Wildcard with an lower bound    | ? super type   | `List<? super Exception> a = new ArrayList<Object>();`     |

The following three sections will explain these three types.

### Unbounded Wildcards

An unbounded wildcard represents any type. When we use ?, we specify that any type is ok. For example, we could have a method that looks through a list of any type:

    public static void printList(List<?> list) {
        for (Object x: list) System.out.println(x);
    }
    public static void main(String[] args) {
        List<String> keywords = new ArrayList<>();
        keywords.add("java");
        printList(keywords);
    }

The `printList()` method takes any type of list as a parameter, in this case String happens to be 'anything'.

We must know that when using `var`, the following two are not equivalent:

    List<?> x1 = new ArrayList<>();
    var x2 = new ArrayList<>();

There are two differences here, first one is that x1 is of type List, while x2 is of type ArrayList, and that we can only assign x2 to a `List<Object>`. These two variables have one thing in common. Both return type is Object when calling the `get()` method.

There are some things that we must pay attention on the exam. The following is the same method showed above but declaring `List<Object>` instead of `List<?>`:

    public static void printList(List<Object> list) {
        for (Object x: list) System.out.println(x);
    }
    public static void main(String[] args) {
        List<String> keywords = new ArrayList<>();
        keywords.add("java");
        printList(keywords); // DOES NOT COMPILE
    }

It does not compile because `List<String>` cannot be assigned to `List<Object>`. Even with String being a subclass os Object. This happens because Java is trying to keep us from writing code like this:

    List<Integer> numbers = new ArrayList<>();
    numbers.add(new Integer(42));
    List<Object> objects = numbers; // DOES NOT COMPILE
    objects.add("forty two");

Thhe compiler promises us that only Integer objects will appear in numbers. If we could assign numbers to objects, then the next line would break that promise by adding a String in the List, since both variables (numbers and objects) are references to the same object. Good thing that the compiler prevents this.

### Upper-Bounded Wildcards

We can't write code that uses a superclass as the reference and the instance as the subclass. For example: 

    ArrayList<Number> list = new ArrayList<Integer>(); // DOES NOT COMPILE

Instead, we can use a upper bounded wildcard:

    List<? extends Number> list = new ArrayList<Integer>();

This wildcard says that any class that extends Number or Number itself can be used as the formal parameter type.

Remember that type erasure makes Java thing that a generic type is an Object, so for example, this method:

    public static long total(List<? extends Number> list) {
        long count = 0;
        for (Number number: list) count += number.longValue();
        return count; 
    }

Is equivalent to this when Java converts it:

    public static long total(List list) {
        long count = 0;
        for (Object obj: list) {
            Number number = (Number) number;
            count += number.longValue();
        }
        return count; 
    }

Something interesting happens when we work with **upper bounds or unbounded wildcards**. The list becomes logially immutable and therefore cannot be modified. Technically, you can remove elements from the list (the exam won't ask about this).

    static class Sparrow extends Bird { }
    static class Bird { }

    public static void main(String[] args) {
        List<? extends Bird> birds = new ArrayList<Bird>();
        birds.add(new Sparrow()); // Does not compile
        birds.add(new Bird()); // Does not compile
    }

The problem here is that Java does not know what type `List<? extends Bird>` really is. It could be `List<Bird>` or `List<Sparrow>` or some other generic type. The first compiler error is that we can't add a Sparrow to a `List<? extends Bird>`, the second compiler error happens because we can't add a Bird to a `List<Sparrow>`. To Java both of these are possible scenarios, so neither is allowed.

We can also use upper bounds with interfaces, for example:

    interface Flyer { void fly(); }
    private void groupOfFlyers(List<? extends Flyer> flyer) { }

Note that we used the keyword extends instead of implements. Upper bounds are like anonymous classes in that they use extends regardless of whether we are working with a class or an interface. The method `groupOfFlyers()` accepts everyone that implements Flyer.

### Lower-Bounded Wildcards

If we try to write a method adding strings with two lists: 

    List<String> strings = new ArrayList<String>();
    strings.add("tweet");

    List<Object> objects = new ArrayList<Object>(strings);

    addSound(strings);
    addSound(objects);

There is a problem with these implementation. It is that we want to pass a `List<String>` and a `List<Object>` to the same method. Before we understand why this is a problem and how we can fix it, let's look at the following table, which explains why we need a lower bound:


 | `public static void addSound(_____ list) {list.add("quack");}`     | Method compiles   | Can pass a `List<String>` | Can pass a `List<Object>`    |
 | :------                         | :-------------                             | :------------                              |  :------------ |
 | List<?>                         | No (unbounded generics are immutable)      | Yes                                        |  Yes  |
 | List<? extends Object>          | No (upper-bounded generics are immutable)  | Yes                                        |  Yes  |
 | List<*Object*>                  | Yes                                        | No (with generics, must pass exact match)  |  Yes  |
 | List<? super String>            | Yes                                        | Yes                                        |  Yes  |


To solve this problem, we need to use a lower bound:

    public static void addSound(List<? super String> list) {
        list.add("quack");
    }

With a lower bound, we are telling Java that the list will be a *list of String objects or a list of some objects that are a superclass of String*. Either way, it's safer to add a String to that list. Just like generic classes, you probably won't use this in your code unless you are writing code for others to reuse. But for the exam you need to understand it.
