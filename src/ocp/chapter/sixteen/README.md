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

An assertion is a boolean expression that you place at a point in your code where you expect something to be true. An assert statement contains this statement along with an optional message. 

> **Note:** We can turn on assertions for testing and debugging while leaving them off when the program is running.

### Validating Data with the assert Statement

The syntaxx for an assert statement has two forms:

        assert test_value; // boolean expression
        assert test_value: message; // optional message

When the assertions are enabled and the boolean expression evaluates to false, then an AssertionError wil lbe thrown at runtime. Remember that programs aren't supposed to catch Errors, so this means that assertion failures are **fatal** and end the program.

These are some examples of usage:

        assert 1 == age;
        assert (2 == height);
        assert 100.0 == length : "Problem with length";
        assert ("Cecelia".equals(name)): "Failed to verify user data";

The three possible outcomes of an assert statement are as follows:

- If assertions are disabled, Java skips the assertion and goes to on in the code.
- If assertions are enabled and the boolean expression is true, then our assertion has been validated and nothing happens. The program continues to execute in its normal manner.
- If assertions are enabled and the boolean expression is false, then our assertion is invalid and an AssertionError is thrown (stops the program).

### Enabling Assertions

By default, assert statements are ignored by the JVM at runtime, to enable them use the -enableassertions or -ea flags on the command line. To run a java single-file source-code we can execute the following:

        java -ea Filename.java
        java -enableassertions Filename.java

Using these flags without any arguments enables assertions in all classes. We can also enable assertions for a specific class or package, for example:

        java -ea:com.demos... my.programs.Main // only for a package
        java -ea:com.demos.TestColors my.programs.Main // only for a class

### Disabling Assertions

If we wanted to enable assertions for the entire class but disable it in some specific packages or classes, we could do that using -disableassertions or -da flags on the command line:
        
        java -ea:com.demos... -da:com.demos... my.programs.Main // only for a package
        java -ea:com.demos... -da:com.demos.TestColors my.programs.Main // only for a class

### Assertions Applications

The following is a list that will not be on the exam, but is just here to show you some ideas of how they can be used.

| Usage | Description | 
|: ----- | : ------ |
| Internal invariants | Assert that a value is within a certain constraint, such as assert x < 0. |
| Class invariants | Assert the validity of an object's state. Class invariants are typically private methods within the class that return a boolean. |
| Control flow invariants | Assert that a line of code you assume is unreachable is never reached. |
| Pre-conditions | Assert that certain conditions are met before a method is invoked. |
| Post-conditions | Assert that certain conditions are met after a method executes successfully. |

## Working with Dates and Times

In this exam you'll need to know how to work with Date and Time API, but knowing many of the varios date/time classes and their various methods, how to specify amounts of time with the Period and Duration classes, and even how to resolve values across time zones with daylight savings.

### Understanding Date and Time Types

Java includes numerous classes to model the examples in the previous paragraph. These are listed in the following table:

|Class|Description|Example|
|:-|:-|:-|
|java.time.LocalDate|Date with day, month, year|Birth date|
|java.time.LocalTime|Time of a day|Midnight|
|java.time.LocalDateTime|Day and time with no time zone|10 a.m. next Monday|
|java.time.ZonedDateTime|Date and time with a specific time zone|9 a.m. EST on 2/20/2021|

Each of these types contains a static method called now() that allows you to get the current value.

        System.out.println(LocalDate.now());
        System.out.println(LocalTime.now());
        System.out.println(LocalDateTime.now());
        System.out.println(ZonedDateTime.now());

The output is as it follows:

        2020-10-14
        12:45:20.854
        2020-10-14T12:45:20.854
        2020-10-14T12:45:20.854-04:00[America/New_York]

### Using the of() Methods

We can create some date and time values using the of() methods in each class.

        LocalDate date1 = LocalDate.of(2020, Month.OCTOBER, 20);
        LocalDate date2 = LocalDate.of(2020, 10, 20);

Both pass in the year, month and date. Although it's good to use the Month constants to make the code easier to read, we can also pass the int number of the month directly.

> **Note:** While programmers often count from zero, working with dates is one of the few times where it is expected to count from 1, just like in the real world.

When creating a time we can choose how detailed you want to be. We can specify the just the hour and minute, or we can include the number of seconds. We can even include nanoseconds if we want to be very precise (a nanosecond is a billionth of a second). 

        LocalTime time1 = LocalTime.of(6, 15); // hour and minute
        LocalTime time2 = LocalTime.of(6, 15, 30); // + seconds
        LocalTime time3 = LocalTime.of(6, 15, 30, 200); // + nanoseconds

We can combine dates and times in multiple ways.

        var dateTime1 = LocalDateTime.of(2020, Month.OCTOBER, 20, 6, 15, 30);

        LocalDate date = LocalDate.of(2020, Month.OCTOBER, 20);
        LocalTime time = LocalTime.of(6, 15);
        var dateTime2 = LocalDateTime.of(date, time);

> **Note:** Did you noticed that we didn't used a single constructor in all these examples? This is an example of the usage of the **Factory Pattern**, which rather than use a constructor, the creation of objects is delegated to a _static_ factory method.

### Formatting Dates and Times

Thee date and times classes support many methods to get data out of them.

        LocalDate date = LocalDate.of(2020, Month.OCTOBER, 20);
        System.out.println(date.getDayOfWeek()); // TUESDAY
        System.out.println(date.getMonth()); // OCTOBER
        System.out.println(date.getYear()); // 2020
        System.out.println(date.getDayOfYear()); // 294

Java also provides a class called **DateTimeFormatter** to display standard formats:

        LocalDate date = LocalDate.of(2020, Month.OCTOBER, 20);
        LocalTime time = LocalTime.of(11, 12, 34);
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        System.out.println(date.format(DateTimeFormatter.ISO_LOCAL_DATE)); // 2020-10-20
        System.out.println(time.format(DateTimeFormatter.ISO_LOCAL_TIME)); // 11:12:34
        System.out.println(dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)); // 2020-10-20T11:12:34

The DateTimeFormatter will throw an exception if it encounters an incompatible type. For example the usage of DateTimeFormatter.ISO_LOCAL_TIME with a LocalDate type object.

If we want to we can create a custom format with the DateTimeFormatter class:

        var customFormat = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm");
        System.out.println(dt.format(customFormat)); // October 20, 2020 at 11:12

Breaking this down we see that we have the usage of M, d, y and the symbols or letters go on. Java assigns each symbol or letter a specific date/time part. For example, M is used for month, while y is used for year. The case here matters! Using m instead of M means that you want minutes instead of months. 

> **Note:** If we want to include a custom text value in the pattern we can do that using the single quote as you saw in the example from above. This is called escaping characters.

#### Learning the Standard Date/Time Symbols

For the exam we need to be familiar enough with the various symbols that can be used in a date/time String. The following table shows the most common of them:

|Symbol|Meaning|Examples|
|:-|:-|:-|
|y|Year|20,2020|
|M|Month|1,01,Jan,January|
|d|Day|5, 05|
|h|Hour|9,09|
|m|Minute|45|
|s|Second|52|
|a|a.m./p.m.|AM,PM|
|z|Time Zone Name|Eastern Standard Time, EST|
|Z|Time Zone Offset|-0400|

As you can see we hve some variations on their usage, for example when using Month depending on how we use the letter M, MM, MMM or MMMM makes a difference in the final result.

We need to make sure the format String is compatible with the underlying date/time type. We can't use Day, Month and Year with a LocalTime object. So the following shows which symbols we can use with each of the date/time objects:

|Symbol|LocalDate|LocalTime|LocalDateTime|ZonedDateTime|
|:-|:-|:-|:-|:-|
|y|Yes|No|Yes|Yes|
|M|Yes|No|Yes|Yes|
|d|Yes|No|Yes|Yes|
|h|No|Yes|Yes|Yes|
|m|No|Yes|Yes|Yes|
|s|No|Yes|Yes|Yes|
|a|No|Yes|Yes|Yes|
|z|No|No|No|Yes|
|Z|No|No|No|Yes|

## Supporting Internationalization and Localization
