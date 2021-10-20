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

Here are some examples of the Consumer interface:

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

A BiConsumer can have doesn't need the two parameters to be the same type, they can be too. As we can see, we can use method references in most of the cases with Consumer and BiConsumer.

