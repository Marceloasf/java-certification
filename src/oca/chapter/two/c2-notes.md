# Chapter 2 - Operators and Statements

- Using Operators and Decision Constructs (decision-making controls structures)

  - Use Java operators; including parentheses to override operator precedence;
  - Create if, if/else and ternary constructs;
  - Use a switch statement;

- Using Loop Constructs (repetion control structures)
  - Create and use while loops;
  - Create and use for loops, including the enhanced for loop;
  - Create and use do/while loops;
  - Compare loop constructs;
  - Use break and continue;

Ps.: This chapter covers the same subjects as the Chapters 3 and 4 of the OCP-11 book, but it covers them in Java 8.

## Operators (p.52-55)

- Three types of operators are available in Java: unary (++, --), binary(+, -, \*, /, %) and ternary(a ? b : c);
- Unless overridden with parentheses, Java operators follow order of operation;
- If two operators have the same level of precedence, then Java guarantees left-to-right evaluation;
- All of the arithmetic operators may be applied to any Java primitives, except boolean;
- To String values, only the addition operators + and += may be applied, which results in String concatenation;
- Modulus operation (%) may also be applied to negative integers and floating-point integers;

* Order of operator precedence (decreasing order of precedence):

  - Post-unary operators - expression++, expression--
  - Pre-unary operators - ++expression, --expression
  - Other unary operators - ~, + (positive), - (negative/negates a expression), ! (inverts a boolean's logical value)
  - Multiplication/Division/Modulus - \*, /, %
  - Addition/Subtraction - +,-
  - Shift operators - <<, >>, >>>
  - Relational operators - <, >, <=, >=, instanceof
  - Equal to/not equal to - ==, !=
  - Logical operators - &, ^, | - (Evaluates all conditions)
  - Short-circuit logical operators - &&, || (Short circuit on the first true)
  - Ternary operators - boolean expression ? expression1 : expression2
  - Assignment operators - =, +=, -=, \*=, /=, %=, &=, ^=, <<=, >>=, >>>=

## Numeric promotion rules (p.55-56)

- If two values have different data types, Java will automatically promote one of the values
  to the larger of the two data types (ex.: short -> int or long);
- If one of the values is integral and other is floating-point, Java will automatically promote
  the integral value to the floating-point value's data type;
- Smaller data types, namely byte, short and char, are first promoted to int any time they're
  used with a Java binary arithmetic operator, even if neither of the operands is int;
- After all promotion has occurred and the operands have the same data type, the resulting value
  will have the same data type as its promoted operands;

## Increment and Decrement Operators (p.59)

There are two orders that can be applied when using these operators:
PRE (++x or --x) and POST (x++ or x--);
Examples of the distinction (code snippet):

    int counter = 0;
    System.out.print(counter); // Outputs 0
    System.out.print(++counter); // Outputs 1
    System.out.print(counter); // Outputs 1
    System.out.print(counter--); // Outputs 1
    System.out.print(counter); // Outputs 0

    int x = 3;
    int y = ++x * 5 / x-- + --x;
    System.out.println("x is " + x);
    System.out.println("y is " + y);

    Simplify this:

    int y = 4 * 5 / x-- + --x; // x assigned value of 4
    then
    int y = 4 * 5 / 4 + --x; // x assigned value of 3
    then
    int y = 4 * 5 / 4 + 2; // x assigned value of 2 then the result is `x is 2` and `y  is 7`

## Overflow and Underflow (p.61)

Overflow is when a number is so large that it will no longer fit within the data type, so the system "wraps around" to the next lowest value and counts up from there. There is a analogous Underflow, when the number is too low to fit in the data type. For example:

- `System.out.print(2147483647+1); // -2147483648`
  Since 2147483647 is the maximum int value, adding any strictly positive value to it will cause it to wrap to the next negative number.

## Relational Operators (p.63)

The instanceof operator is the fifth relational operator, and it's applied to object references and classes or interfaces.

- `a instanceof b` - True if the reference that 'a' points to is an instance of a class, subclass or class that implements a particular interface, as named in 'b'.

## Equality Operators (p.65)

The equality operators (== and !=) are used in one of three scenarios:

1. Comparing two numeric primitive types. If the numeric values are of different data types, the values are automatically promoted as previously described. For example, 5 == 5.00 returns true since the left side is promoted to a double.
2. Comparing two boolean values.
3. Comparing two objects, including null and String values.

- For object comparison, the equality operator is applied to the references to the objects, not the objects they point to;
- Two references are equal if and only they point to the same object or both point to null.

## The if-then Statement (p.67-68)

- For readability, it is considered good coding practice to put blocks around the execution component of if-then
  statements, as well as many other control flow statements, although it is not required:
- Single line statement:
  - `if (boolean condition) expression //branch if true;`.

## The if-then-else Statement (p.69-70)

- The else operator takes a statement or block of statements, in the same manner
  as the if statement does (`if (b) {} else {}` or `if (b) {} else if (b2) {}`);

- Using a if-then-else statement in a single line works, but remember to guarantee that all the semicolons (;) are written:

      if (x != 5) System.out.println("if-then-else single line true branch") // DOES NOT COMPILE
          else System.out.println("if-then-else single line false branch");

## Ternary Operator (p.71)

- It takes 3 operands, a boolean expression (condition) and two expressions of any kind that returns a value;
- Note that it is often helpful for readability to add parentheses around the expressions in ternary operators, although it is certainly not required;
- In the exam, pay extra attention to any cases which a variable can be modified by the right-hand side expressions of a ternary;

## The switch Statement (p.72-75)

- Supported data types are byte, short, char, int, enum values, String, Byte, Short, Character and Integer.
- A switch statement may contain 0 or more case branches, break inside the case is optional, and the default is optional and it may appear anywhere within the switch statement.
- The values in each case statement must be compile-time constant values of the same data type as the switch value, this means that you can use only literals, enum constants or final constant variables of the same data type. A final constant is a variable that is marked with the final modifier and initialized with a literal value in the same expression in which it is declared.
- If there isn't a break statement on the default or case block, flow will continue to the next proceeding case or default block automatically until it finds a break statement or finishes the structure.

## The while Statement (p.77)

- Curly braces are required for block of multiple statements and optional for single statement.
- The loop may terminate after its first evaluation of the boolean expression (returning a false), in this manner, the statement block may never be executed (while evaluates before continuing execution).

## The do-while Statement (p.78)

- Curly braces are required for block of multiple statements and optional for single statement.
- Unlike a while loop, a do-while loop guarantees that the statement or block will be executed at least once.

## The Basic for Statement (p.80-83)

- Curly braces are required for block of multiple statements and optional for single statement.
- Its composed of 3 sections, an initialization block, boolean expression and an update statement (`for (initialization; booleanExpression; updateStatement)).

* Basic for steps are:

  1. Initialization statement executes.
  2. If booleanExpression is true continue, else exit loop.
  3. Body executes.
  4. Execute updateStatement.
  5. Return to Step 2.

  - The initialization and update sections may contain multiple statements, separated by commas (,).

* Some examples of exam basic for variations and edge cases:

  1. Creating an Infinite Loop

     - `for( ; ; ) { ... }`
       - This example will compile, it is an infinite loop that will run the body repeatedly.
     - `for( ; )` or `for( )` will not compile.

  2. Adding Multiple Terms to the for Statement

     - `int x = 0; for(long y = 0, z = 4; x < 5 && y < 10; x++, y++) { ... } // COMPILES`
       - This example will compile, since you can add multiple terms into basic for sections.

  3. Redeclaring a Variable in the Initialization Block

     - `int x = 0; for(long y = 0, x = 4; x < 5 && y < 10; x++, y++) { ... } // DOES NOT COMPILE`

       - Does not compile because x is repeated in the initialization block after being already being declared before the loop, resulting the compiler stopping because of a duplicate variable declaration, we can fix this removing the declaration on the for and only assigning the values to x and a pre declared y.

     - `int x = 0; long y = 10; for(y = 0, x = 4; x < 5 && y < 10; x++, y++) { ... } // COMPILES`

  4. Using Incompatible Data Types in the Initialization Block

     - `for(long y = 0, int x = 4; x < 5 && y < 10; x++, y++) { ... } // DOES NOT COMPILES`
       - The variables in the initialization block must all be of the same type.

  5. Using Loop Variables Outside the Loop
     - `for(long y = 0, x = 4; x < 5 && y < 10; x++, y++) { ... } System.out.println(x); // DOES NOT COMPILE`

## The for-each Statement (enhanced for) (p.83)

- Curly braces are required for block of multiple statements and optional for single statement.
- The for-each loop declaration is composed of an initialization section and an object to be iterated over.
- The right-hand side of the loop (iterable collection of objects) must be a built-in Java array or an object whose class implements java.lang.Iterable (includes most of the Java Collections framework).
- The left-hand side of the loop (collection member) must include a declaration for an instance of a variable, whose type matches the type of a member of the array or collection in the right-hand side of the statement.

## Optional Labels, break Statement and continue Statement (p.87-91)

- if-then statements, switch statements and loops can all have optional labels.
- A label is an optional pointer to the head of a statement that allows the application flow to jump to it or break from it.
- It is rarely considered good coding practice to use this labels to control and block structures,
  they are mostly used in loop structures.
  Ex.: `OUTER_LOOP: for(long y = 0, z = 4; x < 5 && y < 10; x++, y++) { break OUTER_LOOP; } // COMPILES`
- Not using a label on a 'break' statement (`break` instead of `break LOOP:`) will make the break statement terminate the nearest inner loop it is currently on the process of executing.
- 'continue' statement syntax and usage mirrors the break statement, but has different results.
- Difference between 'break' and 'continue' statements: While the break statement transfers control to the enclosing statement, terminating the execution of a loop, the continue statement transfers control to the boolean expression that determines if the loop should continue. In other words, it ends the current iteration of the loop.

  > Obs.: The if-then statement does not allow the usage of unlabeled break statement. The if-then and switch statements, both don't allow the continue statement usage.
