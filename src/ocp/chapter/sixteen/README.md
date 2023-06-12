# Chapter 16 - Exceptions, Assertions and Localization 

Exception Handling and Assertions

- Use the try-with-resources construct
- Create and use custom exception classes
- Test invariants by using assertions

Localization

- Use the Locale class
- Use resource bundles
- Format messages, dates, and numbers with Java

## Creating Custom Exceptions

Java provides many exceptions classes out of the box for us, but sometimes we need to have a custom exception for a more specialized purpose.

### Declaring Exception Classes

When creating a custom exception class we need to decide whether we want a checked or unchecked exception. We can extend any exception class to create a custom exception class, but the most common is to extend Exception for checked exceptions and RuntimeException for unchecked exceptions. Here are some examples of custom checked and unchecked exceptions:

        class CannotSwimException extends Exception {}
        class DangerInTheWater extends RuntimeException {}
        class SharkInTheWaterException extends DangerInTheWater {}
        class Dolphin {
            public void swim() throws CannotSwimException {
                // logic
            }
        }
        
### Adding Custom Constructors

These one liners messages exceptions are pretty useful but we can pass more parameters to them with custom constructors. The following are some examples of the most common constructors defined by the Exception class:

        public class CannotSwimException extends Exception {
            public CannotSwimException() {
                super(); // Optional, compiler will insert it automatically
            }
            public CannotSwimException(Exception e) {
                super(e);
            }
            public CannotSwimException(String message) {
                super(message);
            }
        }
        
> **Note:** The default no-argument constructor is provided automatically if you don't write any constructors of your own (already seen on Chapter about Class Design)

These are some great additions to other ways of calling the constructor of an exception, but we can also create some custom ones like the following:


        public class CannotSwimException extends Exception {
            public CannotSwimException(Exception e) {
                super("Cannot swim because: " + e.toString());
            }
        }
        
This is just one example of the many things that we can do inside constructors of custom exceptions.

### Printing Stack Traces

A stack trace shows the exception along with the method calls it took to get there. The JVM automatically prints a stack trace when an exception is thrown and not handled by the program. We can also print the stack trace if we want to when we handle the exception, as the following:

        try {
          throw new CannotSwimException();
        } catch (CannotSwimException e) {
          e.printStackTrace();
        }
        
## Automating Resource Management

A try-with-resources statement ensures that any resources declared in the try clause are automatically closed at the conclusion of the try block. This feature is also known as _automatic resource management_, because Java automatically takes care of closing the resources for you.

### Constructing Try-With-Resources Statements

The first rule when using try-with-resources statements, is that it requires resources that implement the _AutoCloseable_ interface. So for example, you cannot use an try-with-resouces statement with a String variable since the String class doesn't implement the AutoCloseable interface. But what makes this interface so special? It contains a method called close() which the JVM calls it inside a "hidden" finally block, which we can refer as an implicit finally block. This AutoCloseable interface method can be overriden by classes that implement it.

The second rule we should be familiar with is that a try-with-resoucers statement can iunclude multiple resources, which are all closed in the **reverse order** in which they are delcared. So resources are separated by a semicolon with the last semicolon being optional. The following would be closed in the reverse order:

        try(var bookRead = new MyFileReader("1");
            var movieRead = new MyFileReader("2");
            var tvRead = new MyFileReader("3");) {
            ...   
         } ...
 
 It will try anything inside the try block and then it'll close in the following order: 3, 2 and 1. If we added a finally block (explicit one), it would be executed **after** the close() implicit finally block.
 
 The last rule we should know is that resources declared within a try-with-resources statement are in scope only within the try block. So for example those MyFileReader classes that we declared on the try-with-resources above are only available inside the try block, they can't be used on the catch or finally blocks.
 
 ### Learning the New Effectively Final Feature
  
 
