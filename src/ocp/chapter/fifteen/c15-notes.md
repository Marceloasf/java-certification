# Chapter 15 - Functional Programming

Java Stream API
- Describe the Stream interface and pipelines
- Use lambda expressions and method references

Built-in Functional Interfaces
- Use interfaces from the java.util.function package
- Use core functional interfaces including Predicate, Consumer, Function and Supplier
- Use primitive and binary variations of base interfaces of java.util.function package
  
Lambda Operations on Streams
- Extract stream data using map, peek and flatMap methods
- Search stream data using search findFirst, findAny, anyMatch, allMatch and noneMatch methods
- Use the Optional class
- Perform calculations using count, max, min, average and sum stream operations
- Sort a ccollection using lambda expressions
- Use Collectors with streams, including the groupingBy and partitioningBy operations

## Introduction

This chapter will be focusing on the Streams API. Note that Streams API in this chapter is used for functional programming. Many more functional classes and Optional classes will be introduced. Then the Stream pipeline to end it tieing it all together. This is a long chapter but the many objectives of it will cover similar topics.

## Working with Built-in Functional Interfaces (p.670-681)

The last chapter presented some basic functional interfaces that are used with the Collections Framework. Now, we will learn them in more detail and more thoroughly. Remember that a functional inteface has exactly one abstract method. 

Now we are going to look at some functional interfaces, that are provided in the java.util.function package. The convention here is to use the generic type T for the type parameter. If a second type parameter is needed, then the letter U is used. If a distinct return type is needed, R for *return* is used for the generic type.


 | Functional interface     | Return type       | Method name   | # of parameters |
 | :------                  | :-------------    | :------------ |  :------------  |
 | Supplier<*T*>            | T                 | get()         | 0               |
 | Consumer<*T*>            | void              | accept(T)     | 1 (T)           |
 | BiConsumer<*T, U*>       | void              | accept(T,U)   | 2 (T, U)        |
 | Predicate<*T*>           | boolean           | test(T)       | 1 (T)           |
 | BiPredicate<*T, U*>      | boolean           | test(T,U)     | 1 (T, U)        |
 | Function<*T, R*>         | R                 | apply(T)      | 1 (T)           |
 | BiFunction<*T, U, R*>    | R                 | apply(T,U)    | 2 (T, U)        |
 | UnaryOperator<*T*>       | T                 | apply(T)      | 1 (T)           |
 | Supplier<*T*>            | T                 | apply(T,T)    | 2 (T, T)        |

There is one functional interface here that was not in the first interfaces table (Collections Framework), which is BinaryOperator. These are not all the functional interfaces available, but these are the most important for this section of the chapter. There are even functional interfaces for handling primitives (we'll see them later in the chapter). It's important to memorize all these functions listed above, because they will be on the exam.   

> **Notes:** Many of the functional interfaces are defined in the java.util.function package. On Chapter 18 "Concurrency", there will be two more functional interfaces called Runnable and Callable, they are used for concurrency the majority of time, However, you need to know them for the exam and know that they are both functional interfaces that don't take any parameters, with Runnable returning void and Callable returning a generic type.

### Implementing *Supplier*

A Supplier is used when you want to generate or supply values without taking any input. The interface is defined like this:

    @FunctionalInterface
    public interface Supplier<T> {
        T get();
    }

You can create a LocalDate using the factory method `now()`. This example shows how to use a Supplier to call it:

    Supplier<LocalDate> s1 = LocalDate::now;
    Supplier<LocalDate> s2 = () -> LocalDate.now();

    LocalDate d1 = s1.get();

    System.out.println(d1);
    System.out.println(s2.get()); // Prints the same date as the println above

Both implementations create Suppliers. This is a good example of using static methods with lambda and method reference. A Supplier is often used when constructing new objects. For example, we can print two empty StringBuilder objects.

    Supplier<StringBuilder> s1 = StringBuilder::new;
    Supplier<StringBuilder> s2 = () -> StringBuilder();

    System.out.println(s1.get());
    System.out.println(s2.get());

On this example we used a constructor reference to create the object (first line) and we've been using generics to define what type of Supplier we are using. The last example is a little bit different but is simple, just look at it carefully:

    Supplier<ArrayList<String>> s3 = ArrayList<String>::new;
    ArrayList<String> a1 = s3.get();
    System.out.println(a1);

When we call `s3.get()`, we get a new instance of `ArrayList<String>`, which is the generic type of the Supplier, in other words, a generic that contains another generic.

### Implementing *Consumer* and *BiConsumer*

You use a Consumer when you want to do something with a parameter but not return anything. BiConsumer does the same thing, except that it takes two parameters. They are defined as follows:

    @FunctionalInterface
    public interface Consumer<T> {
        void accept(T t);
        // omitted default method
    }

    @FunctionalInterface
    public interface BiConsumer<T, U> {
        void accept(T t, U u);
        // omitted default method
    }

Here are some examples of the Consumer and BiConsumer interface:

    Consumer<String> c1 = System.out::println;
    Consumer<String> c2 = x -> System.out.println(x);

    c1.accept("Annie");
    c2.accept("Annie"); // Both print Annie

    var map = new HashMap<String, Integer>();
    BiConsumer<String, Integer> b1 = map::put;
    BiConsumer<String, Integer> b2 = (k, v) -> map.put(k, v);

    b1.accept("chicken", 7);
    b2.accept("chick", 1);

    System.out.println(map); // {chicken=7, chick=1}

    var map2 = new HashMap<String, String>();
    BiConsumer<String, String> b3 = map::put;
    BiConsumer<String, String> b4 = (k, v) -> map.put(k, v);

    b3.accept("chicken", "Cluck");
    b4.accept("chick", "Tweep");

    System.out.println(map2); // {chicken=Cluck, chick=Tweep}

A BiConsumer can have doesn't need the two parameters to be the same type, they can be too. As we can see, we can use method references in most of the cases with Consumer and BiConsumer.

### Implementing *Predicate* and *BiPredicate*

A Predicate is often used when filtering or matching, both are common operations. A BiPredicate is just like a Predicate except that it takes two parameters. The following are the interfaces definitions:

    @FunctionalInterface 
    public interface Predicate<T> {
        boolean test(T t);
        // omitted default and static methods 
    }

    @FunctionalInterface 
    public interface BiPredicate<T, U> {
        boolean test(T t, U u);
        // omitted default methods
    }

Here are some examples of Predicate and BiPredicate usage:

    Predicate<String> p1 = String::isEmpty;
    Predicate<String> p2 = b -> b.isEmpty();

    System.out.println(p1.test("")); // true
    System.out.println(p2.test("")); // true

    BiPredicate<String> b1 = String::startsWith;
    BiPredicate<String> b2 = (string, prefix) -> string.startsWith(prefix);

    System.out.println(b1.test("chicken", "chick")); // true
    System.out.println(b2.test("chicken", "chick")); // true

We can see that method references save a good bit of typing, but the downside is that they are less explicit.

### Implementing *Function* and *BiFunction*

A Function is responsible for turning one parameter into a value of a potentially different type and returning it. A BiFunction is responsible for turning two parameters into a value and returning it. The interfaces are defined as follows:

    @FunctionalInterface
    public interface Function<T, R> {
        R apply(T t);
        // omitted default and static methods 
    }

    @FunctionalInterface
    public interface BiFunction<T, U, R> {
        R apply(T t, U u);
        // omitted default method
    }

The following example converts a String to the length of the String:

    Function<String, Integer> f1 = String::Length;
    Function<String, Integer> f2 = x -> x.length();

    System.out.println(f1.apply("cluck")); // 5
    System.out.println(f2.apply("cluck")); // 5

This function turns a String into an Integer. Technically it turns the String into an int, which is autoboxed into an Integer. The types don't have to be different. The following combines two String objects and produces another String:

    BiFunction<String, String, String> b1 = String::concat;
    BiFunction<String, String, String> b2 = (string, toAdd) -> string.concat(toAdd);

    System.out.println(b1.apply("baby", "chick")); // baby chick
    System.out.println(b2.apply("baby", "chick")); // baby chick

The first two types in the BiFunction are the input types, the third is the return type. For the method reference on the example above, the first parameter is the instance that `concat()` is called on and the second is passed to `concat()` as the parameter.

### Implementing *UnaryOperator* and *BinaryOperator*

UnaryOperator and BinaryOperator are a special case of a Function (UnaryOperator extends Function and BinaryOperator extends BiFunction). They **require** all type parameters to be the same type. A UnaryOperator transform its value into one of the same type. For example, incrementing by one is a unary operation. A BinaryOperator merges two values into one of the same type. Adding two numbers is a binary operation. The following are the interfaces definitions:

    @FunctionalInterface
    public interface UnaryOperator<T> extends Function<T, T> { }

    @FunctionalInterface
    public interface BinaryOperator<T> extends BiFunction<T, T, T> {
        // omitted static methods
     }

This means that method signatures look like this: 

    T apply(T t); // UnaryOperator

    T apply(T t1, T t2); // BinaryOperator

These methods are the actually inherited from the Function and BiFunction superclasses. But the generic declarations on the subclasses are what force the types to be the same. For example:

    UnaryOperator<String> u1 = String::toUpperCase;
    UnaryOperator<String> u2 = x -> x.toUpperCase();

    System.out.println(u1.apply("chirp")); // CHIRP
    System.out.println(u2.apply("chirp")); // CHIRP

We don't need to specify the return type in the generics, since UnaryOperator requires it to be the same as the parameter. And here's the binary example:

    BinaryOperator<String> b1 = String::concat;
    BinaryOperator<String> b2 = (string, toAdd) -> string.concat(toAdd);

    System.out.print(b1.apply("baby ", "chick")); // baby chick
    System.out.print(b2.apply("baby ", "chick")); // baby chick

The code here is way easier to understand, since we don't need to declare more than one generic type.

#### Creating Your Own Functional Interfaces

Java provides a built-in interface for functions with one or two parameters, what if we need more than that? We can create a funtional interface with more than that, like this:

    @FunctionalInterface
    interface TriFunctional<T,U,V,R> {
        R apply(T t, U u, V v);
    }
    
    @FunctionalInterface
    interface QuadFunctional<T,U,V,W,R> {
        R apply(T t, U u, V v, W w);
    }

On both these examples, the last type parameter is the return type, just like the Function and BiFunction interfaces. This Java built-in interfaces are meant to facilitate the most common functional interfaces that you'll need. Remember that we can add any functional interfaces that we want to, Java matches them when we use lambdas or method references.

### Convenience Methods on Functional Interfaces

By definition, a functional interface can only contain a single abstract method, but this doesn't mean that it can't have other methods, like default methods. It's common for interfaces to have several helpful default methods. All of these methods facilitate using functional interfaces. The following table shows only the main methods methods on the built-in interfaces, that you'll need to know for the exam:

> **Note:** BiConsumer, BiFunction and BiPredicate interfaces have similar methods **available**.

 | Interface instance     | Method return type       | Method name   | Method parameters |
 | :------                | :-------------           | :------------ |  :------------    |
 | Consumer               | Consumer                 | andThen()     | Consumer          |
 | Function               | Function                 | andThen()     | Function          |
 | Function               | Function                 | compose()     | Function          |
 | Predicate              | Predicate                | and()         | Predicate         |
 | Predicate              | Predicate                | negate()      | -                 |
 | Predicate              | Predicate                | or()          | Predicate         |

Let's start with these two simple examples:

    Predicate<String> egg = s -> s.contains("egg");
    Predicate<String> brown = s -> s.contains("brown");

Now we need a predicate for brown eggs and another for all other eggs:

    Predicate<String> brownEggs = s -> s.contains("egg") && s.contains("brown");
    Predicate<String> otherEggs = s -> s.contains("egg") && ! s.contains("brown");

This works, but it can be better. A better way to deal with this case is to use two default methods on Predicate:

    Predicate<String> brownEggs = egg.and(brown);
    Predicate<String> brownEggs = egg.and(brown.negate); // These are the first two Predicate example variables

Now these two are way shorter, cleaner and even easier to understand what is going on. 

Moving on to the Consumer interface now. We can use the `andThen()` method, which runs two functional interfaces in sequence:

    Consumer<String> c1 = x -> System.out.print("1: " + x);
    Consumer<String> c2 = x -> System.out.print(",2: " + x);

    Consumer<String> combined = c1.andThen(c2);
    combined.accept("Annie"); // 1: Annie,2: Annie

Notice that the same parameter is passed to both c1 and c2 in this case. The Consumer instances are run in sequence and are independent of each other. 

Now we can have a look at the Function interface `compose()` method, that chains functional interfaces. But instead it passes along the output of one to the input of another: 

    Functional<Integer, Integer> before = x -> x + 1;
    Functional<Integer, Integer> after = x -> x * 2;

    Function<Integer, Integer> combined = after.compose(before);
    System.out.println(combined.apply(3)); // 8

This time **before** runs first, turning 3 into a 4, then **after** runs, doubling the 4 to 8. 

## Returning an *Optional*

How do we express a "we don't know" or "not applicable" answer in Java? We use the *Optional* type. An Optional is created using a factory. You can either request an empty Optional or pass a value for the Optional to wrap. You can image an Optional as a box that might have something inside.

### Creating an *Optional*

Here's an example of an average method that uses Optional and creates it both ways commented above:

    public static Optional<Double> average(int... scores) {
        if (scores.length == 0) return Optional.empty();
        int sum = 0;
        for (int score: scores) sum += score;
        return Optional.of((double) sum / scores.length);
    }

If we call the method above we can see what is inside our two boxes:

    System.out.println(average(90, 100)); // Optional[95.0]
    System.out.println(average()); // Optional.empty

