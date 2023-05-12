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

Now we are going to look at some functional interfaces, that are provided in the java.util.function package. The convention here is to use the generic type T for the type parameter. If a second type parameter is needed, then the letter U is used. If a distinct return type is needed, R for _return_ is used for the generic type.

| Functional interface  | Return type | Method name | # of parameters |
| :-------------------- | :---------- | :---------- | :-------------- |
| Supplier<_T_>         | T           | get()       | 0               |
| Consumer<_T_>         | void        | accept(T)   | 1 (T)           |
| BiConsumer<_T, U_>    | void        | accept(T,U) | 2 (T, U)        |
| Predicate<_T_>        | boolean     | test(T)     | 1 (T)           |
| BiPredicate<_T, U_>   | boolean     | test(T,U)   | 1 (T, U)        |
| Function<_T, R_>      | R           | apply(T)    | 1 (T)           |
| BiFunction<_T, U, R_> | R           | apply(T,U)  | 2 (T, U)        |
| UnaryOperator<_T_>    | T           | apply(T)    | 1 (T)           |
| Supplier<_T_>         | T           | apply(T,T)  | 2 (T, T)        |

There is one functional interface here that was not in the first interfaces table (Collections Framework), which is BinaryOperator. These are not all the functional interfaces available, but these are the most important for this section of the chapter. There are even functional interfaces for handling primitives (we'll see them later in the chapter). It's important to memorize all these functions listed above, because they will be on the exam.

> **Notes:** Many of the functional interfaces are defined in the java.util.function package. On Chapter 18 "Concurrency", there will be two more functional interfaces called Runnable and Callable, they are used for concurrency the majority of time, However, you need to know them for the exam and know that they are both functional interfaces that don't take any parameters, with Runnable returning void and Callable returning a generic type.

### Implementing _Supplier_

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

### Implementing _Consumer_ and _BiConsumer_

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

### Implementing _Predicate_ and _BiPredicate_

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

### Implementing _Function_ and _BiFunction_

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

### Implementing _UnaryOperator_ and _BinaryOperator_

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

| Interface instance | Method return type | Method name | Method parameters |
| :----------------- | :----------------- | :---------- | :---------------- |
| Consumer           | Consumer           | andThen()   | Consumer          |
| Function           | Function           | andThen()   | Function          |
| Function           | Function           | compose()   | Function          |
| Predicate          | Predicate          | and()       | Predicate         |
| Predicate          | Predicate          | negate()    | -                 |
| Predicate          | Predicate          | or()        | Predicate         |

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

## Returning an _Optional_ (p.681-685)

How do we express a "we don't know" or "not applicable" answer in Java? We use the _Optional_ type. An Optional is created using a factory. You can either request an empty Optional or pass a value for the Optional to wrap. You can image an Optional as a box that might have something inside.

### Creating an _Optional_

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

We can check if an Optional contains a value (`isPresent()`) and then `get()` that value if present:

    Optional<Double> opt = average(90,100);
    if (opt.isPresent()) {
        System.out.println(opt.get()); // 95.0
    }

If we don't check before calling `get()` on an Optional, a NoSuchElementException is thrown.

    java.util.NoSuchElementException: No value present

When creating an Optional, we can define it wrapping an value with `of(value)` method or `empty()`. If the value is null, we can use the factory method `ofNullable(value)` to wrap the value and create the Optional.

> **Note:** A variable whose type is Optional should never itself be null. null can be assigned to an Optional, but it's not a good practice, always use one of these factory methods commented above to create an Optional.

These were the static methods that we need to know about Optional for the exam. The following table shows most of the instance methods on Optional that we need to know too. There are a few others that will be covered later in the chapter.

| Method                  | When Optional **is empty**                       | When Optional **contains a value** |
| :---------------------- | :----------------------------------------------- | :--------------------------------- |
| get()                   | Throws an exception                              | Returns value                      |
| ifPresent(Consumer c)   | Does nothing                                     | Calls Consumer with value          |
| isPresent()             | Returns false                                    | Returns true                       |
| orElse(T other)         | Returns other parameter                          | Returns value                      |
| orElseGet(Supplier s)   | Retruns result of calling Supplier               | Returns value                      |
| orElseThrow()           | Throws NoSuchElementException                    | Returns value                      |
| orElseThrow(Supplier s) | Throws exception created by calling the Supplier | Returns value                      |

With these methods we can do code that it's easier to read, that instead of using an if statement, we can do it all in one line, with ifPresent and a Consumer for example.

> **Note:** orElseThrow() was added on Java 10.

### Dealing with an Empty _Optional_

The other methods (orElse...) allow us to specify what to do if a value is not present. So the following are some examples of them all and how they behave:

    Optional<Double> opt = average(); // no value present on opt
    System.out.println(opt.orElse(Double.NaN)); // NaN
    System.out.println(opt.orElseGet(() -> Math.random())); // 0.564594598248
    System.out.println(opt.orElseThrow()); // Throws NoSuchElementException
    System.out.println(opt.orElseThrow(() -> new IllegalStateException())); // Throws IllegalStateException
    System.out.println(opt.orElseGet(() -> new IllegalStateException())); // DOES NOT COMPILE, the opt variable is an Optional<Double>, Supplier must return a Double.

    Optional<Double> optWithValue = average(90, 100);
    System.out.println(optWithValue.orElse(Double.NaN)); // 95.0
    System.out.println(optWithValue.orElseGet(() -> Math.random())); // 95.0
    System.out.println(optWithValue.orElseThrow()); // 95.0
    // orElse is not used in these examples because there is a value present

### Is _Optional_ the same as null?

Before Java 8, the common return was null instead of Optional. But there were some problems with this approach. It wasn't a clear way to express that null might be a special value, because when returning an Optional, is clear that there might not be a value in there. Another advantage of Optional is that we can use functional programming style with the methods rather than using some common statements, like the if statement.

## Using Streams (p.685-706)

A _stream_ in Java is a sequence of data. A _stream pipeline_ consists of the operations that run on a stream to produce a result.

To understand the _pipeline flow_ we need to think of a stream pipeline as an assembly line in a factory, where we have a number of jobs that need different persons to do them, and the steps require to follow a certain order, where the second depends on the first finishing it's assignment. This assembly line is finite. _Finite streams_ have a limit. There are others assembly lines that run forever, where a cycle is treated as _infinite_, since it does not end for an inordinately large period of time. Another important feature of an assembly line is that each person touches each element to do their operation and then that piece of data is gone, it does not come back. They are different than lists and queues as we saw in the other chapter, where elements of a list can be accessed any time and queue elements are limited in which elements we can access, but they are all there. With streams, the data is not generated up front, it is only created when needed. This is an example of _lazy evaluation_, which delays execution until necessary.

Many things can happen along the assembly line stations, in functional programming these are called _stream operations_, operations are ocurr in a pipeline, where someone has to start and end the work. There can be any number of stations in between. There are three parts to a stream pipeline, respectively:

- **Source:** Where the stream comes from.
- **Intermediate operations:** Transforms the stream into another one. There can be as few or as many intermediate operations as we'd like. Since streams use lazy evaluation, the intermediate operations do not run until the terminal operation runs.
- **Terminal operation:** Actually produces a result. Since streams can be used only once, the stream is no longer valid after a terminal operation completes.

The important thing on these 'assembly lines' is what comes in and goes out, what happens in between the intermediate operations is an implementation detail. The following table shows some scenarions to make sure that we are clear about the differences between intermediate operations and terminal operations:

| Scenario                                | Intermediate operation | Terminal operation |
| :-------------------------------------- | :--------------------- | :----------------- |
| Required part of a useful pipeline?     | No                     | Yes                |
| Can exist multiple times in a pipeline? | Yes                    | No                 |
| Return type is a stream type?           | Yes                    | No                 |
| Executed upon method call?              | No                     | Yes                |
| Stream valid after call?                | Yes                    | No                 |

A factory typically has a foreman who oversees the work. Java serves as the 'foreman' when working with stream pipelines. He takes care of everything envolving running a stream pipeline.

### Creating Finite Streams

In Java, the streams we have been talking about are represented by the Stream<_T_> interface.

We'll start with finite streams. There are some ways to create them:

    Stream<String> empty = Stream.empty(); // count = 0
    Stream<Integer> singleElement = Stream.of(1); // count = 1
    Stream<Integer> fromArray = Stream.of(1,2,3); // count = 3

The third example shows how to create a stream from a varargs. The method signature uses varargs, which let us specify an array or individual elements. Java also provides a convenient way of converting a Collection to a stream:

    var list = List.of("a", "b", "c");
    Stream<String> fromList = list.stream();

It's just a simple method call to create a stream from a list.

> **Note:** We can create a parallel stream from a list too, just calling the method `list.parallelStream()`. This stream can be used when concurrency is needed, you can have multiple threads working on your stream, but keep in mind that some tasks cannot be done in parallel, for example a task that needs ordering. More about concurrency is discussed on it's own chapter.

### Creating Infinite Streams

We can't create an infinite list, but we can create infinite streams:

    Stream<Double> randoms = Stream.generate(Math::random);
    Stream<Integer> oddNumbers = Stream.iterate(1, n -> n + 2);

Both of these operations will generate numbers as long as we need them, if we call a `randoms.forEach(System.out::println)` the program will print numbers until we kill it. Later in the chapter we will discuss the `limit()` method which turns the infinite stream into a finite stream.

Note that if we print the stream, it will not print the elements like a List, it will print something like this: java.util.stream.ReferencePipeline$3@4517d9a3.

Imagine that we wanted just odd numbers less than 100. Java 9 introduced an overloaded version of `iterate()` that solves this:

    Stream<Integer> oddNumbersUnder100 = Stream.iterate(
        1,              // seed
        n -> n < 100,   // Predicate to specify when is done
        n -> n + 2      // UnaryOperator to get next value
    );

### Reviewing Stream Creation Methods

To review, make sure you know all the methods on the following table. These are the ways of creating a source for streams, given a Collection instace coll.

| Method                                         | Finite or infinite? | Notes                                                                                                                                                                    |
| :--------------------------------------------- | :------------------ | :----------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Stream.empty()                                 | Finite              | Creates Stream with zero elements                                                                                                                                        |
| Stream.of(varargs)                             | Finite              | Creates Stream with elements listed                                                                                                                                      |
| coll.stream()                                  | Finite              | Creates Stream from a Collection                                                                                                                                         |
| coll.parallelStream()                          | Finite              | Creates Stream from a Collection where the stream can run in parallel                                                                                                    |
| Stream.generate(supplier)                      | Infinite            | Creates Stream by calling the Supplier for each element upon request                                                                                                     |
| Stream.iterate(seed, unaryOperator)            | Infinite            | Creates Stream by using the seed for the first element and then calling the UnaryOperator for each subsequent element upon request                                       |
| Stream.iterate(seed, predicate, unaryOperator) | Finite or Infinite  | Creates Stream by using the seed for the first element and then calling the UnaryOperator for each subsequent element upon request. Stops if the Predicate returns false |

### Using Common Terminal Operations

We can perform a terminal operation _without any intermediate operations_, but not the other way around.

**Reductions** are a special type of terminal operation where all of the contents of the stream are combined into a single primitive or Object (int or String).

The following table summarizes this section:

| Method                                 | What happens for infinite streams | Return value  | Reduction |
| :------------------------------------- | :-------------------------------- | :------------ | :-------- |
| count()                                | Does not terminate                | long          | Yes       |
| min() and max()                        | Does not terminate                | Optional<`T`> | Yes       |
| findAny() and findFirst()              | Terminates                        | Optional<`T`> | No        |
| allMatch(), anyMatch() and noneMatch() | Sometimes terminates              | boolean       | No        |
| forEach()                              | Does not terminate                | void          | No        |
| reduce()                               | Does not terminate                | Varies        | Yes       |
| collect()                              | Does not terminate                | Varies        | Yes       |

- `count()`:

  - Counts the number of elements in a finite stream (in an infinite stream it never terminates).
  - It's a _reduction_ because it looks at each element in the stream and returns a single value.
  - Method signature:

        long count()

  - Example:

        Stream<String> s = Stream.of("monkey", "gorilla", "bonobo"); // finite stream
        System.out.println(s.count()); // 3

- `min() and max()`:

  - Allow us to pass a custom comparator and find the smallets or largest value in a finite stream according to that sort order.
  - These two hang will hang on an infinite stream because they cannot be sure that a smaller or larger value isn't coming later on the stream.
  - Both are reductions, since they return a single value after looking at the entire stream.
  - Method signature:

        Optional<T> min(Comparator<? super T> comparator)
        Optional<T> max(Comparator<? super T> comparator)

  - Example:

        Stream<String> s = Stream.of("monkey", "ape", "bonobo"); // finite stream
        Optional<String> min = s.min((s1, s2) -> s1.length() - s2.length());
        min.ifPresent(System.out::println); // ape

  - Notice that the example above returned an Optional, this allows the method to specify that no minimum or maximum was found. An example of where there isn't a minimum, is as follows:

        Optional<?> minEmpty = Stream.empty().min((s1, s2) -> 0);
        System.out.println(minEmpty.isPresent()); // false

  - Since the stream above is empty, the comparator is never called and no value is present for the Optional (returning false with isPresent()).

  > **Note:** As these methods are terminal to the stream, you can't use them both in the same stream.

- `findAny() and findFirst()`:

  - They return an element of the stream _unless_ the stream is empty. With an empty stream, they'll return an empty Optional.
  - These methods can terminate with an infinite stream.
  - `findAny()` can return any element of the stream. When called on the streams seen up until now, it commonly returns the first element, but this behavior is not guarateed. On the concurrency chapter, when working with parallel streams, the method is more likely to return a random element.
  - These methods are terminal operations, but not reductions, the reason is that they sometimes return without processing **all** of the elements in the stream. Meaning that they return a value based on the stream but do not reduce the entire stream into one value.
  - Method signatures:

        Optional<T> findAny()
        Optional<T> findFirst()

  - Examples:

        Stream<String> s = Stream.of("monkey", "gorilla", "bonobo");
        Stream<String> infinite = Stream.generate(() -> "chimp");

        s.findAny().ifPresent(System.out::println); // monkey (usually)
        infinite.findAny().ifPresent(System.out::println); // chimp

- `allMatch(), anyMatch() and noneMatch()`

  - These methods search a stream and return information about how the stream pertains to the predicate.
  - These may or may not terminate for infinite streams. It depends on the data.
  - Like the find methods, they are not reductions, since they may not look at all the elements in the stream.
  - Method signatures:

        boolean anyMatch(Predicate<? super T> predicate)
        boolean allMatch(Predicate<? super T> predicate)
        boolean noneMatch(Predicate<? super T> predicate)

  - Examples:

        var list = List.of("monkey", "2", "chimp");
        Stream<String> infinite = Stream.generate(() -> "chimp");
        Predicate<String> pred = x -> Character.isLetter(x.charAt(0));

        System.out.println(list.stream().anyMatch(pred)); // true
        System.out.println(list.stream().allMatch(pred)); // false
        System.out.println(list.stream().noneMatch(pred)); // false
        System.out.println(infinite.anyMatch(pred)); // true

  - Notice that we can reuse the same predicate multiple times, but we need a different stream each time. If we used `allMatch()` instead of `anyMatch()` with the infinite stream, the program would run until we killed it, this would happen because with `anyMatch()` it would find one element and terminate the call.

    > **Note:** These methods return a boolean. By contrast, the find methods return an Optional because they return an element of the stream.

- `forEach()`

  - Used to iterate over elements of a stream.
  - On an infinite stream, the loop does not terminate.
  - Since there is no return value, it is not a reduction.
  - Method signature:

        void forEach(Consumer<? super T> action)

  - Notice that this is the only terminal operation with a return type of **void**. If something needs to happen, it must happen in the Consumer. For example:

        Stream<String> s = Stream.of("Monkey", "Gorilla", "Bonobo");
        s.forEach(System.out::print); // MonkeyGorillaBonobo

  > **Note:** It's possible to call `forEach()` directly on a Collection or on a Stream.

  - It's **not** possible to use a traditional `for` loop on a stream.

        Stream<String> s = Stream.of("Monkey", "Gorilla", "Bonobo");
        for (String str : s) { } // DOES NOT COMPILE

  - `forEach()` sounds like a loop, but it is really a terminal operator for streams. Streams **cannot** be used as the source in a for-each loop to run because they don't implement the Iterable interface.

- `reduce()`

  - Combines a stream into a single object.
  - It is a reduction, which means it processes all elements.
  - There are three method signatures:

        T reduce(T identity, BinaryOperator<T> accumulator)

        Optional<T> reduce(BinaryOperator<T> accumulator)

        <U> U reduce(U identity, BinaryOperator<U, ? super T, U> accumulator,   BinaryOperator<U> combiner)

  - First one is the most common way of doing a reduction, by starting with an initial value and keep merging it with the next value.
  - If we needed to concatenate an array of String objects into a single String without functional programming, it might look like this:

        var array = new String[] { "w", "o", "l", "f" };
        var result = "";
        for (var s: array) result = result + s;
        System.out.println(result); // wolf

  - The _identity_ is the initial value of the reduction, in this case would be an empty String.
  - The _accumulator_ combines the current result with the current value in the stream.
  - With lambdas we can do the same thing as we did above, but with a stream and reduction:

        Stream<String> stream = Stream.of("w", "o", "l", "f");
        String word = stream.reduce("", (s, c) -> s + c);
        System.out.println(word); // wolf

  - _identity_ isn't really necessary, we can omit it. When we omit the identity, an Optional is returned because there might be no data. There are three choices for what is in the Optional:

    - If the stream is empty, an empty Optional is returned.
    - If the stream has one element, it is returned.
    - If the stream has multiple elements, the accumulator is applied to combine them.

  - The following example shows these scenarios:

        BinaryOperator<Integer> op = (a, b) -> a * b;
        Stream<Integer> empty = Stream.empty();
        Stream<Integer> oneElement = Stream.of(3);
        Stream<Integer> threeElements = Stream.empty(3, 5, 6);

        empty.reduce(op).ifPresent(System.out::println);         // no output
        oneElement.reduce(op).ifPresent(System.out::println);    // 3
        threeElements.reduce(op).ifPresent(System.out::println); // 90

  - The last method signature is used when we are dealing with different types.
  - It allows Java to create intermediate reductions and then combine them at the end.
  - The following example counts the number of chars in each String:

        Stream<String> stream = Stream.of("w", "o", "l", "f!");
        int length = stream.reduce(0, (i, s) -> i+s.length(), (a, b) -> a+b);
        System.out.println(length); // 5

  - In this example, the first argument is an Integer (i), while the second argument is a String (s) on the accumulator. This one handles mixed data types.
  - The third parameter is called the _combiner_, which combines anyt intermediate totals. In this case, a and b are both Integer values.
  - This signature of reduce is useful when working with parallel streams, because it allows the stream to be decomposed and reassebled by separate threads.

- `collect()`

  - Is a special type of reduction called _mutable reduction_.
  - It is more efficient than a regular reduction because we use the same mutable object while accumulating.
  - Common mutable objects include StringBuilder and ArrayList.
  - The methods signatures are as follows:

        <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner)

        <R,A> R collect(Collector<? super T, A, R> collector)

  - With the first signature, we define how collecting should work. For example:

        Stream<String> stream = Stream.of("w", "o", "l", "f");

        StringBuilder word = stream.collect(
            StringBuilder::new
            StringBuilder::append,
            StringBuilder::append)

        System.out.println(word); // wolf

  - The first parameter is the _supplier_, which creates the object that will store the results as we collect data. Remember that a Supplier doesn't take any parameters and returns a value.
  - The second parameter is the _accumulator_, which is a BiConsumer that takes two parameters and doesn't return anything. It is responsible for adding one more element to the data collection.
  - The third parameter is the _combiner_, which is a BiConsumer. It is responsible for taking two data collections and merging them.
  - Another example with another logic:

        Stream<String> stream = Stream.of("w", "o", "l", "f");

        TreeSet<String> set = stream.collect(
            TreeSet::new,
            TreeSet::add,
            TreeSet::addAll);
        System.out.println(set); // [f, l, o, w]

  - The collector has three parts as before. The combiner adds all of the elements of one TreeSet to another in case the operations were done in parallel and need to be merged.
  - Java provides a class with common collectors named Collectors. This approach makes the code easier to read because it is more expressive than using a custom made collector.
  - We can rewrite the previous example with Collectors:

        Stream<String> stream = Stream.of("w", "o", "l", "f");
        TreeSet<String> set = stream.collect(Collectors.toCollection(TreeSet::new));
        System.out.println(set); // [f, l, o, w]

  - If we didn't need the set to be sorted, we could make the code even shorter:

        Stream<String> stream = Stream.of("w", "o", "l", "f");
        Set<String> set = stream.collect(Collectors.toSet());
        System.out.println(set); // [f, w, l, o]

  - Using `toSet()` might get us a different output every time. Another thing is that it doens't guarantee which implementation of Set you'll get. It is likely to be a HashSet, but don't count on that.
  - The exam, expects that us know about common predefined collections in addition to being able to write our own by passing a supplier, accumulator and combiner.

### Using Common Intermediate Operations

- An intermediate operation produces a stream as its result.
- Can also deal with an infinite stream simply by returning another infinite stream, since elements are produced only as needed.
- It focus on the current element rather than the other elements of a stream.

- `filter()`

  - Returns a Stream with elements that match a given expression.
  - Method signature is as follows:

        Stream<T> filter(Predicate<? super T> predicate)

  - We can pass any Predicate to it. For example:

    Stream<String> s = Stream.of("monkey", "gorilla", "bonobo");
    s.filter(x -> x.startsWith("m")).forEach(System.out::print); // monkey

- `distinct()`

  - Returns a stream with duplicate values removed.
  - The duplicates do not need to be adjacent to be removed.
  - As you might imagine, Java calls `equals()` to determine whether the objects are the same.
  - The method signature is as follows:

        Stream<T> distinct()

  - Here's an example:

        Stream<String> s = Stream.of("duck", "duck", "duck", "goose");
        s.distinct().forEach(System.out::print); // duckgoose

- `limit() and skip()`

  - Can make a Stream smaller or they could make a finite stream out of an infinite stream.
  - Method signatures are the following:

        Stream<T> limit(long maxSize)
        Stream<T> skip(long n)

  - The following example creates an infinite stream of numbers counting from 1:

        Stream<Integer> s = Stream.iterate(1, n -> n + 1);
        s.skip(5).limit(2).forEach(System.out::print); /// 67

  - The `skip()` operation returns an infinite stream staring with the numbers counting from 6, since it skips the first five elements.
  - The `limit()` call takes the first two of those.
  - Then we have a finite stream with two elements, which we can then print with the `forEach()` method.

- `map()`

  - Creates a one-to-one mapping from the elements in the stream to the elements of the next step in the stream.
  - Method signature is as follows:

        <R> Stream<R> map(Function<? super T, ? extends R> mapper)

  - It uses the lambda expression to figure out the type passed to that function and the one returned. The return types is the stream that gets returned.
  - On streams is for transforming data. Don't confuse it with the Map interface, which maps keys to values.
  - The following example converts a list of String objects to a list of Integer objects representing their lengths:

        Stream<String> s = Stream.of("monkey", "gorilla", "bonobo");
        s.map(String::length).forEach(System.out::porint); // 676

  - String::length is shorthand for the lambda `x -> x.length()`, which clearly shows it is a function that turns a String into a Integer.

- `flatMap()`

  - Takes each element in the stream and makes any elements it contains top-level elements in a **single stream**.
  - Helpful when you want to remove empty elements from a stream or you want to combine a stream of lists.
  - Method signature:

        <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper )

  - Basically this signature means that it returns a Stream of the type that the function contains at a lower level.
  - The following example gets all the elements into the same level along with getting rid of the empty list:

        List<String> zero = List.of();
        var one = List.of("Bonobo");
        var two = List.of("Mama Gorilla", "Baby Gorilla");
        Stream<List<String>> animals = Stream.of(zero, one, two);

        animals.flatMap(m -> m.stream()).forEach(System.out::println); // prints Bonobo (/n) Mama Gorilla (/n) Baby Gorilla

  - Notice that it removed the empty list and changed al elements of each list to be at top level of the stream.

- `sorted()`

  - It sorts the elements within a stream.
  - Just like sorting arrays, Java uses the natural ordering unless we specify a comparator.
  - Method signatures:

        Stream<T> sorted()
        Stream<T> sorted(Comparator<? super T> comparator)

  - If we call the first signature, it will use the default sort order:

        Stream<String> s = Stream.of("brown", "bear-");
        s.sorted().forEach(System.out::print); // bear-brown-

  - We can optionally use a Comparator implementation via a method or a lambda. In this example, we are using a method:

        Stream<String> s = Stream.of("brown bear-", "grizzly-");
        s.sorted(Comparator.reverseOrder()).forEach(System.out::print); // grizzly-brown bear

- `peek()`

  - This is the final _intermediate operation_. It is used mainly for debugging because it allows us to perform a stream operation without actually changing the stream.
  - Method signature:

        Stream<T> peek(Consumer<? super T> action)

  - Notice that it takes the same argument as the terminal `forEach()` operation. Think of `peek()` as an intermediate version of `forEach()` that returns the original stream back to you.
  - The most common use for `peek()` is to output the contents of the stream as it goes by.
  - Remember that `peek()` is intended to perform an operation without changing the result. But be careful, Java does not prevent us from writing bad code, for example:

        var numbers = new ArrayList();
        var letters = new ArrayList();
        numbers.add(1);
        letters.add('a');
        Stream<List<?>> bad = Stream.of(numbers, letters);
        bad.peek(x -> x.remove(0)).map(List::size).forEach(System.out::print); // 00

  - Notice why this is a bad code example? Because `peek()` is modifying the data structure that is used in the stream.

### Putting Together the Pipeline
           
- Streams allow you to use chaining and express what you want to accomplish rather than how to do so.
            
This is an example of code that can be refactored with streams:

        var list = List.of("Toby", "Anna", "Leroy", "Alex");
        List<String> filtered = new ArrayList();
        for(String name: list) 
            if (name.length() == 4) filtered.add(name);
        
        Collections.sort(filtered);
        var iter = filtered.iterator();
        if (iter.hasNext()) System.out.println(iter.next());
        if (iter.hasNext()) System.out.println(iter.next());
            
This code piece works fine, but with streams is shorter, briefer and clearer to read:
        
        var list = List.of("Toby", "Anna", "Leroy", "Alex");
        list.stream()
            .filter(n -> n.length() == 4)
            .sorted()
            .limit(2)
            .forEach(System.out::println);
            
As we can see, the code is way simpler to understand. This stream call followed by multiple intermediate operations and a terminal operation is called a **stream pipeline**.  
> **Notes:** The following explanations will use the stream above to explain the pipeline behavior.

The pipeline on this code works this way:
1. The stream() is created with the list values starting the stream pipeline, then first thing it does its calling the filter() intermediate operation for each stream value.
2. The filter() operation will filter out the values based on its condition and will hold the values until all of them pass through it, once all values are filtered it'll call the next operation on the pipeline, which is the sorted() intermediate operation.
3. The sorted() operation sorts the values in the stream and keeps sending them down to the limit() operation by the sorted order until the limit is reached.
4. As limit() is set to two, so while it receives each one of the values at a time from sort() it'll send each one of them to the forEach() terminal operation to print them until the set limit is reached. Once the limit is reached it stops to process other values.
            
> **Note:** Keep an eye out for Stream.generate() since it can create infinite streams and result in executions that never stop until the thread gets killed or the program runs out of memory.

#### Real World Scenario - Peeking Behind the Scenes

You can use the peek() method to see how a stream pipeline works behind the scenes. Remember that the methods run agains **each** element one at a time until processing is done. This is an example of the peek() usage to understand the pipeline:
            
        var infinite = Stream.iterate(1, x -> x + 1);
        infinite.limit(5)
            .peek(System.out::println)
            .filter(x -> x % 2 == 1)
            .forEach(System.out::println);            

This example prints the following: 11233455. First it prints the peek value and then it prints the filtered value in the forEach, but as you can see it'll process each element at a time.
            
### Working with Primitive Streams
            
Until now we've been working with Streams created with generic types, but Java actually includes stream classes besides Stream that can be used to work with select primitives like int, double and long. Supposing that we want to calculate the sum of numbers in a finite stream:
            
        Stream<Integer> stream = Stream.of(1, 2, 3);
        System.out.println(stream.reduce(0, (s, n) -> s + n)); // prints 6

This is not bad, but it could be way easier to implement and read using the primitive Stream classes:
            
        Stream<Integer> stream = Stream.of(1, 2, 3);
        System.out.println(stream.mapToInt(x -> x).sum()); // prints 6
            
This time we used what's called an **IntStream** and asked for this class to calculate the sum for us instead of making a reduction. An IntStream contains many of the same intermediate and terminal methods as a Stream but includes specialized methods for working with numeric data. This looks like more a convenience than an important thing, but if you need for example how to compute an average, it would take a lot of work compared to using the methods that are in an IntStream:
            
        IntStream intStream = IntStream.of(1, 2, 3);
        OptionalDouble avg = intStream.average();
        System.out.println(avg.getAsDouble()); // 2.0            
            
So this enables you to calculate an average in a much simpler way than hard coding it.
            
#### Creating Primitive Streams
            
These are three types of primitive streams:

- IntStream: Used for primitive types int, short, byte and char;
- LongStream: Used for the primitive type long;
- DoubleStream: Used for the primitive types double and float.
