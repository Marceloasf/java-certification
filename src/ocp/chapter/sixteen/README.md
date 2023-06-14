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

A try-with-resources statement ensures that any resources declared in the try clause are automatically closed at the conclusion of the try clause. This feature is also known as _automatic resource management_, because Java automatically takes care of closing the resources for you.

### Constructing Try-With-Resources Statements

The first rule when using try-with-resources statements, is that it requires resources that implement the _AutoCloseable_ interface. So for example, you cannot use an try-with-resouces statement with a String variable since the String class doesn't implement the AutoCloseable interface. But what makes this interface so special? It contains a method called close() which the JVM calls it inside a "hidden" finally clause, which we can refer as an implicit finally clause. This AutoCloseable interface method can be overriden by classes that implement it.

The second rule we should be familiar with is that a try-with-resoucers statement can iunclude multiple resources, which are all closed in the **reverse order** in which they are delcared. So resources are separated by a semicolon with the last semicolon being optional. The following would be closed in the reverse order:

        try(var bookRead = new MyFileReader("1");
            var movieRead = new MyFileReader("2");
            var tvRead = new MyFileReader("3");) {
            ...   
         } ...
 
 It will try anything inside the try clause and then it'll close in the following order: 3, 2 and 1. If we added a finally clause (explicit one), it would be executed **after** the close() implicit finally clause.
 
 The last rule we should know is that resources declared within a try-with-resources statement are in scope only within the try clause. So for example those MyFileReader classes that we declared on the try-with-resources above are only available inside the try clause, they can't be used on the catch or finally clauses.
 
### Learning the New Effectively Final Feature

Since Java 9 we can use resources declared prior to the try-with-resources statement, provided they are marked **final** or **effectively final**. The syntax is almost the same as the others, just need to use the resource name in place of the resource declaration, separated by a semicolon (;).

        public void relax() {
                final var bookReader = new MyFileReader("4");
                MyFileReader movieReader = new MyFileReader("5");
                try (bookReader;
                     var tvReader = new MyFileReader("6");
                     movieReader) {
                        ...
                } finally {
                        ...
                }
        }
         
As we can see in the example above, the usage of the final and effectively final variables is different than the common resource declaration one, but it works the same way. About effectively final, just remember that we know that this movieReader variable is effectively final because it gets assigned a value **only once**.

The following example could come across you in the exam and is a non-compilant example:

                var writer = Files.newBufferedWriter(path);
                try (writer) { // DOES NOT COMPILE
                        writer.append("Welcome to the zoo!"); // This is fine
                }
                writer = null; // This is the problem -- Variable is not effectively final because of this assignment
                
This example involves the variable not being effectively final, but there's another trick that could be in the exam and we need to be aware of, this one is when you try to access a variable that was already closed because of the automatic resource management:

                var writer = Files.newBufferedWriter(path);
                writer.append("This write is permitted but a really bad idea!");
                try (writer) {
                        writer.append("Welcome to the zoo!");
                }
                writer.append("This write will fail since the resource was closed at this point"); // IOException
                
Last but not least, take care when using resources declared before try-with-resources statements, even though is permited is not a good idea because these classes don't implement the AutoCloseable interface for no reason. If an exception is thrown during the process of creating a BufferedReader or Writer for a file, that could stop the whole thread from going on. So always use the try-with-resources statements with closeable classes.

### Understanding Supressed Exceptions
 
What happens if the close() method doesn't work? It'll throw an exception and how do we deal with that? Basically we can wrap the class that implements AutoCloseable in a try-with-resources with a catch, that will catch all the exceptions from the try clause and also from the close() method if any are thrown in there.

Now imagine that we handle the one exception from the close() method and another one from the try clause, when this happens Java supress all the exceptions but the first one, so in this case the first one would be one throwed inside the try clause, this would be the first exception and the close() exception would be thrown too but it would be supressed by Java. For example:

        try (JammedTurkeyCage t = new JammedTurkeyCage()) {
                throw new IllegalStateException("Turkey ran off");
        } catch (IllegalStateException e) {
                System.out.println("Caught: ", e.getMessage()); // prints "Turkey ran off"
                for (Throwable t: e.getSuppresed()) 
                        System.out.println("Supressed: ", t.getMessage()); // prints "close() exception message"
        }
        
> **Note:** Java remembers the supressed exception that go with a primary exception even if we don't handle them in the code, for ex if the thrown exception was a RuntimeException instead of the IllegalState one, Java would throw the RuntimeException but it would also print the Supressed exception from the catch.

If more than one resource thrown an exception, the first one to be thrown becomes the primary exception, with the rest being grouped as suppressed exceptions. Since resources are closed in reverse order in which they are declared, the primary exception would be on the last declared resource that throws an exception.

> **Note:** Keep in mind that suppress exceptions are only those thrown in the try clause.

## Declaring Assertions



