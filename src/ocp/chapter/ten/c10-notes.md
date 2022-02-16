# Chapter 10 - Exceptions

- Handling Exceptions
  - Describe the advantages of Exception handling and differentiate among checked, unchecked execeptions and Errors.
  - Create try-catch blocks and determine how exceptions alter program flow.
  - Create and invoke a method that throws an exception.

## Understanding Exception Types (p.406-407)

- An exception is an event that alters program flow.
- Java has a Throwable superclass for ALL objects that represent these events.
- They don't need to have the word 'exception' in their class name.

* Categories of exception:

  (top) java.lang.Object

  extends

  java.lang.Throwable

  extends

  java.lang.Error | java.lang.Exception <- RuntimeException

- Error means something went so horribly wrong that your program should not attemptto recover from it (like if the program ran out of memory).
- Remember for the exam that Throwable it's the parent class of all exceptions(including the Error class).
- While you can handle Throwable and Error exceptions, it is not recommended you doso in your application code.

## Checked Exceptions (p.407-408)

- A checked exception is an exception that MUST be declared or handled by the application code where it is thrown.
- In Java, checked exceptions all inherit from Exception but not RuntimeException.
- Tend to be more anticipated, for example, trying to read a file that doesn't exist.
- For the exam, you just need to know about checked exceptions extend Exception, but they also include any class that inherits Throwable,
  but not Error or RuntimeException.
- You can't throw a checked exception inside a method without declaring it on the method signature.
- You can't declare a checked exception on a catch block without throwing it on the try block (from a method or directly with the throw statement).

* Checked Exceptions? What are we checking?

  - Java has a rule called the 'handle or declare' rule. Which means that all checked exceptions that could be thrown within a method are either
    wrapped in compatible try and catch blocks or declared in the method signature.
  - IOException is an unchecked exception for example. Is seen in method signatures (... throws IOException { }) and try and catch blocks.

    > **Note:** The 'throw' keyword tells Java that you want to throw an Exception, while the throws keyword simply declare that the method might throw an Exception.

## Unchecked Exceptions (p.408-409)

- An unchecked exception is any exception that does NOT need to be declared or handled by the application code where it is thrown.
- Often referred to as runtime exceptions, although in Java, unchecked exceptions include any class that inherits RuntimeException or Error.
- A runtime exception is defined as the RuntimeException class and its subclasses.
- Runtime exceptions tend to be unexpected but not necessarily fatal, for example, accessing an invalid array index is unexpected but it doesn't stop the program.
- An unchecked exception can often occur on nearly any line of code, as it is not required to be handled or declared.
- Like a nullpointer exception, that can be thrown anytime if a reference is null and the code tries to reach on a property of that null reference.
- The code will compile when you declare an unchecked exception. However it is redundant.

* Runtime vs. at the Time the Program Is Run:
  - A runtime (unchecked) exception is a specific type of exception. All exceptions occur at the time that the program is run (The alternative is compile time,
    which would be a compiler error). People don't refer to them as "run time" exceptions because that would be too easy to confuse with runtime. So when you see
    runtime exception, it means unchecked.

## Throwing an Exceptio (p.409-411)n

- throw vs. throws:
  - throw keyword is used as a statement inside a code block to throw a new exception or rethrow an existing exception.
  - throws keyword is used ONLY at the end of a method declaration to indicate what exceptions it supports and might be thrown.
- An Exception is an Object. This means you can store its value in a variable (`Exception e = new RuntimeException();`).
- Exception has a constructor that takes a message (String).
- Remember that Exceptions are classes, so they need to be have an instance to be thrown (`throw new Exception();`).
- You should not catch Throwable directly in your code (however you can).
- Types of exceptions and errors:
  - Runtime exception -> Subclass of RuntimeException -> It's okay for the program to catch -> The program doesn't need to handle or declare.
  - Checked exception -> Subclass of Exception but not of RuntimeException -> It's okay for the program to catch -> The program need to handle or declare.
  - Error -> Subclass of Error -> Isn't okay for the program to catch -> The program doesn't need to handle or declare.

## Recognizing Exception Classes (p.411-416)

- RuntimeException Classes
  - RuntimeException and its subclasses are unchecked exceptions that don't have to be handled or declared.
  - They can be thrown by the programmer or by the JVM.
  - Common RuntimeException classes include the following:
    - ArithmeticException: Thrown when code attempts to divide by zero.
    - ArrayIndexOutOfBoundsException: Thrown when code uses an illegal index to access an array.
    - ClassCastException: Thrown when an attempt is made to cast an object to a class of which it is not an instance.
    - NullPointerException: Thrown when there is a null reference where an object is required.
    - IllegalArgumentException: _Thrown by the programmer_ to indicate that a method has been passed an illegal or inappropriate argument.
    - NumberFormatException: Subclass of IllegalArgumentException thrown when an attempt is made to convert a string to a numeric type but the string doesn't
      have an appropriate format.
- Checked Exceptions Classes
  - They have an Exception in their hierarchy but not RuntimeException. They must be handled or declared.
  - Common checked exceptions include the following:
    - IOException: Thrown programmatically when there's a problem reading or writing a file.
    - FileNotFoundException: Subclass of IOException thrown programmatically when code tries to reference a file that does not exist.
- Error Classes
  - Errors are unchecked exceptions that extend the Error class.
  - They are thrown by the JVM and should not be handled or declared.
  - Errors prevents Java from continuing.
  - Errors are rare, but you might see these:
    - ExceptionInInitializerError: Thrown when a static initializer throws an exception and doesn't handle it (if a RuntimeException is thrown inside an
      instance initialiazer, Java can't start using the class, so it will throw this ExceptionInInitializerError and will not start the application).
    - StackOverflowError: Thrown when a method calls itself too many times (this is called 'infinite recursion' because the method typically calls itself
      without end).
    - NoClassDefFoundError: Thrown when a class that the code uses is available at compile time but not runtime.

## Using try and catch Statements (p.416-418)

- `try` statement sintax is simple, it has the 'try' keyword, curly braces (required) and the protected code inside of it (code block), after the curly braces
  there is the 'catch' keyword with the type and identifier of the exception object, another set of curly braces (required) and the exception handler code
  inside of the curly braces.

  `try { // Protected code } catch (Exception e) { // Exception handler }`

- If any of the statements running in the try block throws an exception that can be caught by the exception type listed in the catch block, the try block
  stops running and execution goes to the catch statement.
- If none of the statements in the try block throws an exception that can be caught, the catch clause is not run.
- Both terms used for refering to a try or catch block/clause are correct. "Block" is correct because there are braces present and "Clause" is correct
  because they are part of a try statement.
- Remember that the point of a try statement is for something to happen if an exception is thrown. So the code will not compile if you only declare the try block
  without the catch clause.

  `try { // DOES NOT COMPILE // Something }`

## Chaining catch Blocks (p.418-420)

- When chaining catch blocks, it is not possible for all of them to be executed.
- Java looks at them in the order they appear.
- If it is impossible for one of the catch blocks to be executed, a compiler error about unreachable code occurs. For example, this occurs when a superclass catch
  block appears before a subclass catch block.
- Chaining with inherited exceptions example:

      try {
        // COMPILES seeAnimal();
      } catch (NumberFormatException e) {
        // subclass exception System.out.print("NumberFormat");
      } catch (IllegalArgumentException e) {
        // superclass exception System.out.print("IllegalArgument");
      }

- In cases like this, the order of the catch blocks matters, if we reverse the catch blocks shown above, the code would not compile on the line of the subclass
  catch declaration, because the exception its unreachable.

## Applying a Multi-catch Bloc (p.420-423)

- A multi-catch block allows multiple exception types to be caught by the same catch block.

      try {
        fly(Integer.parseInt(array[1]));
      } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
        System.out.print("Missing or invalid input");
      }

- You can declare another catch block in case you want to handle other types of exceptions differently.
- Basically it's like a regular catch clause, except two or more EXCEPTION TYPES are specified separated by a pipe, and there is only one variable name in the
  catch clause. Remember that the pipe (|) is also used as the "or" logical operator.
- The exceptions can be listed in any order within the catch clause.
- Some exam examples:

  `catch (Exception1 e | Exception2 e | Exception3 e) {} // DOES NOT COMPILE`

  `catch (Exception1 e1 | Exception2 e2 | Exception3 e3) {} // DOES NOT COMPILE`

  `catch (Exception1 | Exception2 | Exception3 e) {} // COMPILES`

- Java intends multi-catch to be used for exceptions that aren't related, and it prevents you from specifying redundant types in a multi-catch.

  `try { throw new IOException(); } catch (FileNotFoundException | IOException e) { } // DOES NOT COMPILE, redundant since FileNotFoundException is already caught by the alternative IOException.`

- The same rules listed before are applied to multi-catch blocks, if there are other catch blocks in the try statement for example.
- The main difference between chaining and multi-catch is that the order they appear does not matter on multi-catch blocks within a single catch expression.

* Reviewing some 'rules':
  - There can be only one exception variable per catch block.
  - You can't list the same exception type more than once in the same try statement.
  - The more general superclasses must be caught after their subclasses.

## Adding a finally Block (p.423-426)

- The try statement also lets you run code at the END with a finally clause, regardless of whether an exception is thrown.
- The block always executes, whether or not an exception occurs.
- The catch block is optional when finally is used.

      try {
        // Protected code
      } catch (Exception e) {
        // OPTIONAL ** // Exception handler
      } finally {
        // Finally block
      }

- The finally is always executed, and is always the last clause to be executed.
- A finally block with a 'return' statement will replace other returns on the try and catch block, since is always executed last.
- System.exit(int status) is the only exception to 'the finally block is always executed' rule:
  - Java defines this method that you call as System.exit(int status), it takes an integer parameter that represents the error code that gets returned.
  - When System.exit(int status) is used in the try or catch block, the finally block does not run.

## Finally Closing Resources (p.426-428)

- If you don't close a resource when you are one with it, a lot of bad things could happen. If you are connecting to a database, you could use up all available
  connections, meaning no one can talk to the database until you release your connections. Although you commonly hear about memory leaks as causing programs to
  fail, a resource leak is just as bad and occurs when a program fails to release its connections to a resource and then the resource becoming inaccessible.
- To treat this without needing lots of try and catch blocks, Java includes the 'try-with-resources' statement to automatically close all resources opened in a
  try clause.
- This feature is also known as 'automatic resource management', because Java takes care of the closing.

      // Instead of using this:
      FileInputStream is = null;
      try {
        is = new FileInputStream("myfile.txt");
        // Read file data
        } catch (IOException e) {
        e.printStackTrace();
        } finally {
        if (is != null) {
          try {
            is.close();
          } catch (IOException e2) {
            e2.printStackTrace();
          }
        }
      }

      // You can use try-with-resources:
      public void readFile(String file) {
        try (FileInputStream is = new FileInputStream("myfile.txt")) {
          // Read file data
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

- Using a try-with-resources statement, guarantees that as soon as a connection passes out of scope, Java will attempt to close it within the same method.
- try-with-resources has implicit finally blocks. Behind the scenes, the compiler replaces a try-with-resources block with a try and finally block:
  - We refer to this "hidden" finally block as an implicit finally block since it is created and used by the compiler automatically.
  - You can still create a programmer-defined finally block when using a try-with-resources statement; just be aware that the implicit one will be created and called first.

## Try-with-Resources (p.428-432)

- One or more resources can be opened in the try clause.
- When there are multiple resources opened, they are closed in the REVERSE order from which they were created.
- Notice that parentheses are used to list those resources, and semicolons are used to separate the declarations. It works just like declaring multiple
  indexes in a for loop.
- The syntax of a basic try-with-resources:

      try (FileInputStream in = new FileInputStream("data.txt"); // Required semicolon between resource declarations
      FileOutputStream out = FileOutputStream("output.txt");) {
        // Last semicolon is optional and is usually omitted // Protected code
      } // Resources are closed at this point

- the catch and finally block are optional with ONLY a try-with-resources statement.
- try statement must have one or more catch blocks or a finally block, this is still true with this case, but the finally clause exists implicitly as said before.
- Is still allowed to have catch and/or finally blocks declared anyway. In fact, if the code within the try block throws a checked exception not declared by the
  method in which it is defined or handled by another try/catch blocks, then it will need to be handled by the catch block.
- The catch and finally blocks are run in addition to the implicit one that closes the resources. Remember that the implicit finally block always runs before any
  programmer-coded ones.
- The syntax of try-with-resources including catch/finally:

      try (FileInputStream in = new FileInputStream("data.txt"); FileOutputStream out = FileOutputStream("output.txt");) {
        // Protected code
      } // Resources are closed at this point (implicit finally)
      catch (IOException e) {
        // Exception handler
      } finally { // finally block }

- Legal vs. illegal configurations with a traditional try statement:

---

| 0 finally blocks 1 finally block 2 or more finally blocks |
|0 catch blocks Not legal Legal Not legal |
|1 or more catch blocks Legal Legal Not legal |
|\***\*\*\*\*\***\*\*\***\*\*\*\*\***\*\*\***\*\*\*\*\***\*\*\***\*\*\*\*\***\_\***\*\*\*\*\***\*\*\***\*\*\*\*\***\*\*\***\*\*\*\*\***\*\*\***\*\*\*\*\***|

- Legal vs. illegal configurations with a try-with-resources statement:

---

| 0 finally blocks 1 finally block 2 or more finally blocks |
|0 catch blocks Legal Legal Not legal |
|1 or more catch blocks Legal Legal Not legal |
|\***\*\*\*\*\***\*\*\***\*\*\*\*\***\*\*\***\*\*\*\*\***\*\*\***\*\*\*\*\***\_\***\*\*\*\*\***\*\*\***\*\*\*\*\***\*\*\***\*\*\*\*\***\*\*\***\*\*\*\*\***|

- You can see that for both of these try statements, two or more programmer-defined finally blocks are not allowed. The implicit one is not count here.

* AutoCloseable and Closeable:

  - You can't just put any random class in a try-with-resources statement.
  - Java requires classes used in a try-with-resources implement the AutoCloseable or Closeable interfaces, which includes a void close() method.

* Declaring Resources:

  - try-with-resources statement does not support multiple variable declarations, so each variable must be declared in a separate statement. For example
    the following do not compile.

        try (FileInputStream in = new FileInputStream("data.txt"), out = ("output.txt")) {
           // DOES NOT COMPILE // Protected code
        }

        try (FileInputStream in = new FileInputStream("data.txt"), FileInputStream out = FileInputStream("output.txt")) {
           // DOES NOT COMPILE // Protected code
        }

  - Each resource must include the data type and be separated by a semicolon (;).
  - Declaring resources is a common situation where using var is quite helpful, as it shortens the already long line of code.

    try (var in = new BufferedInputStream(new FileInputStream("data.txt"));) { // COMPILES // Protected code }

* Scope of Try-with-Resources:

  - The resources created in the try clause are in scope only within the try block. Because the implicit finally runs before any catch/finally blocks that
    you code yourself.

        try (Scanner s = new Scanner(System.in)) {
          s.nextLine();
        } catch (Exception e) {
          s.nextInt(); // DOES NOT COMPILE
        } finally {
          s.nextInt(); // DOES NOT COMPILE
        }

* Following Order of Operation:
  - Remember these two rules:
    1. Resources are closed after the try clause ends and before any catch/finally clauses.
    2. Resources are closed in the reverse order from which they were created.
  - The try-with-resources only guarantees that the close() method of the AutoCloseable subclass will be called.

## Throwing Additional Exceptions (p.432-434)

- A catch or finally block can have any valid Java code in it, including another try statement.
- The exception will always be handled in the catch statement if its properlly handled.
- If an exceptions is thrown inside a catch/finally block, and not handled, then the exception thrown in the last executed block will be the one thrown.

## Calling Methods That Throw exceptions (p.434-436)

- When you have a method with a checked exception on the signature (declared), even if the exception is not thrown on the code, you must declare or
  handle that exception where you use the method.
- Example where the code do not compile:

      class NoMoreCarrotsException extends Exception { }
      public class Bunny {
        public static void main(String[] args) {
          eatCarrot(); // DOES NOT COMPILE - Because NoMoreCarrotsException is a checked    exception
        }

        private static void eatCarrot() throws NoMoreCarrotsException { }
      }

- There are some ways to fix this and make this code work:

      public static void main(String[] args) throws NoMoreCarrotsException { // declare exception eatCarrot(); }

      public static void main(String[] args) { try { eatCarrot(); } catch     (NoMoreCarrotsException e) { // handle exception ... } }

- The compiler is always on the lookout for unreachable code, and declaring an unused exception isn't considered unreachable code.

      private static void bad() {
        try {
          eatCarrot();
        } catch (NoMoreCarrotsException e) { // DOES NOT COMPILE, because Java knows that this checked exception or a subclass of the checked exception will not be thrown on eatCarrot() implementation (unreachable code).
          ...
        }
      }

      public void good() throws NoMoreCarrotsException { // COMPILES, since this    exception on good() is unused (not unreachable).
        eatCarrot();
      }

      private void eatCarrot() { } // NO EXCEPTION DECLARED

- You can handle a checked exception with runtime exceptions together in a multi-catch or chaining catch blocks, but you need to handle the checked exception.
- This rule does not extend to unchecked exceptions or exceptions declared in a method signature. So a method such as eatCarrot() can be called inside a
  try block, but the exception on the catch block can't be a checked one, because its unreachable.

> **Note:** Remember that you can't throw a checked exception inside a method without declaring it on the method signature. If eatCarrot() needed to throw NoMoreCarrotsException, then you'll need to declare it on the eatCarrot() signature.

## Declaring and Overriding Methods with Exceptions (p.436-437)

- Remember that an overridden method, it's not allowed to add new CHECKED exceptions to the method signature. For example, this is not allowed:

      class CanNotHopException extends Exception { }
      class Hopper {
        public void hop() { }
      }

      public class Bunny extends Hopper {
        public void hop() throws CanNotHopException { } // DOES NOT COMPILE
      }

- An overridden method in a subclass is allowed to declare fewer exceptions than the superclass or interface. This is legal because callers are already
  handling them. For example, this is allowed:

      class Hopper {
        public void hop() throws CanNotHopException { }
      }

      public class Bunny extends Hopper {
        public void hop() { } // COMPILES
      }

- Similarly, a class is allowed to declare a subclass of an exception type. The superclass or interface has already taken care of a broader type. Example:

      class Hopper {
        public void hop() throws Exception { }
      }

      public class Bunny extends Hopper {
        public void hop() throws CanNotHopException { } // COMPILES - Bunny could declare that it throws Exception directly, or a more specific type of Exception or even that it throws nothing at all.
      }

> **Note:** Remember that this rule applies only to checked exceptions. So a subclass overridden method can throws unchecked exceptions. Because this is relevant, since a unchecked exception doesn't need to be handled or declared, because methods are free to throw any unchecked exceptions they want.

## Printing an Exception (p.437-438)

- There are three ways to print an exception. You can let Java print it out, print just the message or print where the stack trace comes from.
- Example of the three approaches:

      public static void main(String[] args) {
        try {
          hop();
        } catch (Exception e) {
          System.out.println(e);
          System.out.println(e.getMessage());
          e.printStackTrace();
        }
      }

      private static void hop() {
        throw new RuntimeException("Cannot hop");
      }

- The code results are:
  1. java.lang.RuntimeException: Cannot hop // Shows that Java prints out by default: the exception type and message
  2. Cannot hop // Shows just the message
  3. java.lang.RuntimeException: Cannot hop // Shows a stack trace
     at Handling.hop(Handling.java:15)
     at Handling.main(Handling.java:7)
- The stack trace is usually the most helpful one because it is a picture in time the moment the exception is thrown. It shows the hierarchy of method calls
  that were made to reach the line that threw the exception.
- The stack trace shows all the methods on the stack. Every time you call a method, Java adds it to the stack until it completes. When an exception is thrown,
  it goes through the stack until it finds a method that can handle it or it runs out of stack.
- A method stack:
  (top) new RuntimeException -> hop() -> main()
