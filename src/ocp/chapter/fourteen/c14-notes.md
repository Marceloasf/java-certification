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
