# Chapter 1 - Java Building Blocks

## Java Basics

- Define the scope of variables;
- Define the structure of a Java class;
- Create executable Java applications with a main method; run a Java program from the command line; including console output;
- Import other Java packages to make them accessible in your code;
- Compare and contrast the features and components of Java such as platform independence, object orientation, encapsulation and etc;

## Working with Java Data Types

- Declare and initialize variables (including casting or primitive types);
- Differentiate between object reference variables and primitive variables;
- Know how to read or write to object fiels;
- Explain an Object's Lifecycle (creation, "dereference by reassignment" and garbage collection);

Obs.: This chapter covers the same subjects as the Chapters 1 and 2 of the OCP-11 book, but it covers them in Java 8.

## Java comment types:

    	//comment until end of line - single line comment

    	/* Multiple
    	*  line comment
    	*/

    	/**
    	* Javadoc multiple-line comment
    	* @author Marcelo
    	*/

## Compile commands:

- javac Zoo.java -> Generates bytecode file (.class)
- WITHOUT PACKAGE - java Zoo -> Executes generated file (Zoo.class)
- p.14-15 - WITH PACKAGE - java chapter.one.Zoo from the \src (because the package is underneath him) -> Executes generated file (Zoo.class)
- Obs.: Filename needs to match the name of a public class in the .java file to compile

\*\* Rules for what a java code file content (p.6-10)

- Each file can contain only one class;
- The filename must match the class name, including case, and have a .java extension;
- String[] args, String args[] or String...args - Compiler accepts any of these declarations of the main() parameters
- Java only looks for class names in the packages
- `import java.util.*;` -> \* is a 'wildcard'
- When wildcards are used, java imports only the classes within a package
- A wildcard only matches class names - ex: GOOD - `java.nio.file.*;` / NO GOOD - `java.nio.*;`
- If there is a 'package' (folder) without any .java files inside declaring it as a package, Java wont compile any .java classes that import something from this non declared package.
- `java.lang` package is automatically imported by Java.
  - Tip -> Always question yourself when reading a code in the exam, the question is `What imports would work to get this code to compile?`

## Naming conflicts (imports) - (p.12-19)

- java.util and java.sql have a class with same name, which is Date

      import java.util.*;
      import java.sql.*; // DOES NOT COMPILE

- When classes with the same name are found in multiple packages and imported inside one, Java gives you a compiler error: The type date is ambiguous
- But if we explicitly import a class name it will take precedence over any wildcards present, for example, if we have a `import java.util.Date;` with a `import.java.sql.*;` in the same file, the `util.Date` will take precendence over the `sql.*`.
- Another way to use the class, is importing one class as usual and the other one you can write down like this inside a method or class: `java.sql.Date sqlDate;`

- Make sure that the exam question gives you the imports if there are no lines or they start at 1,
  if they start at something bigger than 1, you can assume that the imports are OK.
- If there is no main() method on the code, assume that the main() method,
  class definition and all the necessary imports are present

## Order of Initialization Rules (p.19)

- Fields and instance initializer blocks are run in the order in which they appear in the file;
- The constructor runs after all fields and instance initializer blocks have run;

## Primitive Types Key Points (p.21-23)

- A byte can hold a value from -128 to 127.

Java allows you to specify digits in several other formats:

- octal (digits 0-7), which uses the number 0 as a prefix - for ex., 017;
- hexadecimal (digits 0-9 and letters A-F), which uses the number 0 followed by x or X as a prefix - for ex., 0xFF;
- binary (digits 0-1), which uses the number 0 followed by b or B as a prefix - for ex., 0b101. You can add underscores anywhere except at the beginning of a literal, the end of a literal, right before a decimal point or right after a decimal point.

## Reference Types (p.24-34)

- Reference types can be assigned null, which means they do not currently refer to an object.
- Primitive types will give you a compiler error if you attempt to assign them null. Example:

      int value = null; // DOES NOT COMPILE - String s = null; COMPILE

- You can declare and initialize as many variables you want in a single line (same statement), as long as they share the same type declaration and not repeat it.
- Local variables must be initialized, if not, you'll get a compiler error when you try to read its value;
- Instance variables are those that can be declared inside a class, without being static.
- A class variable has the static keyword in its declaration;
- Instance and class variables do not require you to initialize them. As soon as they are declared, they are given a default value.
  - Default values:
  - boolean = false;
  - byte, short, int, long = 0;
  - float, double = 0.0;
  - char = '\u0000' (NUL);
  - All object references (everything else) = null.

## Variables Scope Rules (p.34-35)

- Local variables: in scope from declaration to end of block;
- Instance variables: in scope from declaration until object garbage collected;
- Class variables: in scope from declaration until the program ends.
- Multiple classes can be defined in the same file, but only one of them is allowed to be public.

## Garbage Collection (p.36-38)

The heap, also known as free store, represents a giant pool of unused memory allocated to your Java application;

- System.gc() suggests that now might be a good time for Java to kick off a garbage collection run, but is not guaranteed that Java will run it; An object is no longer reachable when one of two situations occurs:
  - The object no longer has any references pointing to it;
  - All references to the object have gone out of scope.
    The method finalize() might not be called and it definitely won't be called twice to collect the object from the heap.

    > Obs.: Objects are always on the heap.
