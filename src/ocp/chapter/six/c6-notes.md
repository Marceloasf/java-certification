# Chapter 6 - Lambdas and Functional Interfaces

Programming Abstractly Through Interfaces

- Declare and use List and ArrayList instances
- Understanding Lambda Expressions

## Lambda Syntax (p.226-229)

    print(animals, a -> a.canSwim());

This concept is called deferred execution. Deferred execution means that code is specified now but will run later. Later is when the `print()` method call it.

Lambdas work with interfaces that have only one abstract method. Java relies on context when figuring out what lambda expressions mean. Looking at interfaces that are implemented for ex. Lambda syntax is tricky because many parts are optional. These two lines do the exact same thing:

    a -> a.canHop()     // "a" is the parameter name, "->" is the arrow operator that separates param and body, and the a.canHop()
                        // is the body, which calls one single method and returns the result of that method.

    (Animal a) -> { return a.canHop(); }    // This one is similar to the less verbose form of a lambda, but it shows explicitly
                                            // the parameter type and the, which body has a semicolon with a return statement.

The optional and required parts are explained like this:

- The parentheses can be omitted only if there is a single parameter and its types is not explicitly stated.
- We can omit braces when we have only a single statement.
- When you omit the braces, Java doesn't require you to type return or use a semicolon (doesn't work when we have 2 or more statements).

Some valid lambdas:

    s -> {} // If there is no code on the right side of the expression, you don't need the semicolon or return statement.
    () -> true // 0 params
    a -> a.startsWith("test") // 1 params
    (String a) -> a.startsWith("test") // 1 params
    (a, b) -> a.startsWith("test") // 2 params - there is no rule that defines you must use all defined params.
    (String a, String b) -> a.startsWith("test") // 2 params

Some invalid lambdas:

    a, b -> a.startsWith("test") // two params missing parentheses.
    a -> { a.startsWith("test"); } // braces declared but is missing the return statement.
    a -> { return a.startsWith("test") } // braces and return statement declared but is missing the semicolon after the return.

> **Note:** Remember that the parentheses are optional ONLY when there is one parameter and it doesn't have a type declaration.

## Introducing Functional Interfaces (p.229-232)

Lambdas work with interfaces that have only one abstract method. These are called 'functional interfaces' (simplified definition for the 1Z0-815 exam). This is named Single Abstract Method (SAM) rule. @FunctionalInterface is an annotation that means the authors of the interface promise it will be safe to use in a lambda in the future. However, just because there isn't this annotation in an interface, it doesn't mean it's not a functional interface, having exactly one abstract method is what makes it a functional interface (not the annotation).

Predicate, Consumer, Supplier and Comparator are functional interfaces. These functional interfaces can be used without specifying the data type (no diamond operator < and >).

| Functional Interface | # parameters | Return type       |
| :------------------- | :----------- | :---------------- |
| Comparator           | Two          | int               |
| Consumer             | One          | void              |
| Predicate            | One          | boolean           |
| Supplier             | None         | One (type varies) |

## Working with Variables in Lambdas (p.233-236)

Variables can appear in three places with respect to lambdas:

- The parameter list.
- Local variables declared inside the lambda body.
- Variables referenced from the lambda body.

### Parameter List

Specifying the type of parameters is optional. Additionally, var can be used in place of the specific type.

These three statements are interchangeable:

    Predicate<String> p = x -> true;
    Predicate<String> p = (var x) -> true;
    Predicate<String> p = (String x) -> true;

The type of the lambda parameter is String in all of these, because a lambda infers the type from the surrounding context.

    Predicate<String> p = (String x, y) -> true; // Do not compile, because type declaration for the params is optional,
                                                 // but if the params type is declared, it needs to be consistent.
    Predicate<String> p = (String x, String y) -> true; // Compiles

### Local Variables inside the Lambda Body:

It is legal to define a block in the lambda's body. That block can have anything that is valid in a normal Java block, including local variable declarations.

    (a, b) -> { int c = 0; return 5; }

Beware of local variables that are declared with the same name of another that is already declared in the scope.

    (a, b) -> { int a = 0; return 5;} // a is already declared as a parameter of the lambda.

Note.: When writing your own code, a lambda block with a local variable is a good hint that you should extract that code into a method.

### Variables Referenced from the Lambdas Body:

Lambdas can access a method parameter or local variable under certain conditions. Instance variables and Class variables are always allowed. Method parameters and local variables are allowed to be referenced if they are effectively final. This means that the value of a variable doesn't change after it's set, regardless of whether it is explicitly marked as final.

Rules for accessing a variable from a lambda body inside a method:

| Variable type           | Rule                         |
| :---------------------- | :--------------------------- |
| Instance variable       | Allowed                      |
| Static variable (class) | Allowed                      |
| Local variable          | Allowed if effectively final |
| Method parameter        | Allowed if effectively final |
| Lambda parameter        | Allowed                      |

## Calling APIs with Lambdas (1ZO-815 - p.236-238):

- `removeIf()` List and Set declare this method, that takes a Predicate.
- `sort()` can be used directly on the list object, instead of calling `Collections.sort(list)`. There is no `sort()` for Set or Map, because neither of those types has indexing.
- `forEach()` takes a Consumer and calls that lambda for each element encountered.

## Summary (p.238-239)

- Lambdas are passed to method expecting an instance of a funtional interfaces.
- Predicate is a common interface that returns a boolean and takes any type.
- Consumer takes any type and doesn't return a value.
- Supplier returns a value and does not take any parameters.
- Comparator takes two parameters and returns an int.
