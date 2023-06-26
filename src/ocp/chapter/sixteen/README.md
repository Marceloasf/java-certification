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

_Internationalization_ is the process of designing you program so it can be adapted. This involves placing strings in a properties file and ensuring the proper data formatters are used. _Localization_ means actually supporting multiple locales or geographic regions. Locale can be seen as being like a language and country pairing. Localization includes translating strings to different languages. It also includes outputting dates and numbers in the correct format for that locale.

> **Note:** Initially, your program does not need to support multiple locales. The key is to future-proof your application by using these techniques. This way, when your product becomes successful, you can add support for new languages or regions without rewriting everything.

### Picking a Locale

The Locale class is in the java.util package. The first useful Locale to find is the user's current locale.

        Locale locale = Locale.getDefault();
        System.out.println(locale); // pt_BR

This default output tells us which locale our computer is using.

Locale Formats example:

- en: Only shows lowercase language code.
- en_US: Shows lowercase language code plus uppercase country code.

> **Note:** For the exam we don't need to memorize the langue our country codes. The exam will ley us know about any that are being used.

There are some built-in constants in Java's Locale class, with some common locales:

        System.out.println(Locale.GERMAN); // de
        System.out.println(Locale.GERMANY); // de_DE

The second way of selecting a Locale is to use the constructors to create a new object. For example:

        System.out.println(new Locale("fr")); // fr
        System.out.println(new Locale("hi", "IN")); // hi_IN

Java let's us create a Locale with an invalid language or country, such as xx_XX, but the program will not behavior as expected since there's no match for that.

The third way to create a Locale, which is the more flexible one, involves the **builder design pattern**. This pattern lets us set all of the properties that we care about and then build the object at the end.  

        Locale l1 = new Locale.Builder()
                .setLanguage("en")
                .setRegion("US")
                .build;

        Locale l2 = new Locale.Builder()
                .setRegion("US")
                .setLanguage("en")
                .build;

> **Note:** The builder pattern in Java is often implemented with an instance of a static nested class. Since the builder and the target class tend to be tightly coupled, it makes sense for them to be defined within the same class.

- We can change the default locale of the computer with: Locale.setDefault(Locale locale)
        - We can use this in the exam and in our practice, but in the real world scenarios we rarely write code that changes a user's default locale.

### Localizing Numbers

Formatting or parsing currency and number values can change depending on your locale. For example, in the USA the dollar sign is prepended before the value along with the decimal point for values less tahn one dollar, such as $2.15. In Germany, the euro symbol is appeneded to the value along with a comma for values less than one euro, such as 2,15 €.

Luckily for us, the java.text package includes classes to save the day. The following sections cover how to format numbers, currency and dates based on the locale.

The first step to formatting or parsing data is the same, obtain an instance of a NumberFormat. The following table contains factory methods to get a NumberFormat:

|Description|Using default Locale and a specified Locale|
|:-|:-|
|A general-purpose formatter|NumberFormat.getInstance() and NumberFormat.getInstance(locale)|
|Same as getInstance|NumberFormat.getNumberInstance() and NumberFormat.getNumberInstance(locale)|
|For formatting monetary amounts|NumberFormat.getCurrencyInstance() and NumberFormat.getCurrencyInstance(locale)|
|For formatting percentages|NumberFormat.getPercentInstance() and NumberFormat.getPercentInstance(locale)|
|Rounds decimal values before displaying|NumberFormat.getIntegerInstance() and NumberFormat.getIntegerInstance(locale)|

#### Formatting Numbers

The NumberFormat.format() method formats the given number based on the locale associated with the NumberFormat object.

        int attendeesPerYear = 3_200_000;
        int attendeesPerMonth = attendeesPerYear/12;

        var us = NumberFormat.getInstance(Locale.US);
        println(us.format(attendeesPerMonth));

        var gr = NumberFormat.getInstance(Locale.GERMANY);
        println(gr.format(attendeesPerMonth));

        var ca = NumberFormat.getInstance(Locale.CANADA_FRENCH);
        println(ca.format(attendeesPerMonth));
        
Even though they look similar, the output results are different because of the locale:

- 266,666
- 266.666
- 266 666

Formatting currency works the same way:

        double price = 48;
        var myLocale = NumberFormat.getCurrencyInstance();
        println(myLocale.format(price));

When we run this with a default locale of en_US it outputs $48.00, on the other hand if we run it with the default locale of en_GB for Great Britain, it outputs £48.00.

> **Note:** In the real world, use int or BigDecimal for money and not double. Doing math on amounts with double is dangerous because the values are stored as floating-point numbers. Losing some pennies or fractions of pennies during transactions isn't a good idea.


#### Parsing Numbers

The NumberFormat.parse() method accomplishes the conversion from a String to a structured object or primitive value, taking the locale into consideration.

> **Note:** As in other parse() methods implemented in Java, this one throws a ParseException too, so remember to handle it.

        String s = "40.45";

        var us = NumberFormat.getInstance(Locale.US);
        println(us.parse(s)); // 40.45

        var fr = NumberFormat.getInstance(Locale.FRENCH);
        println(fr.parse(s)); // 40

In the US, a dot (.) is part of a number and the number is parsed how you might expected. But in France a decimal point is not used to separate numbers, so Java pareses it as a formatting character and it stops looking at the rest of the number. So always make sure you pare the values using the right locale.

#### Writing a Custom Number Formatter

The patterns when creating custom formatters can get complex, but we need to know only about two formatting characters:

- #: Omit the position if no digit exists for it.
- 0: Put a 0 in the position if no digit exists for it.

These are some examples of how we can use these symbols:

        double d = 1234567.467;
        
        NumberFormat f1 = new DecimalFormat("###,###,###.0");
        println(f1.format(d)); // 1,234,567.5
        
        NumberFormat f2 = new DecimalFormat("000,000,000.00000");
        println(f2.format(d)); // 001,234,567.46700
        
        NumberFormat f3 = new DecimalFormat("$#,###,###.##");
        println(f3.format(d)); // $1,234,567.47

### Localizing Dates

Like numbers, date formats can vary by locale. The following table shows methods used to retrieve an instance of a DateTimeFormatter using the default locale.

|Description|Using default Locale|
|:-|:-|
|For formatting dates|DateTimeFormatter.ofLocalizedDate(dateStyle)|
|For formatting times|DateTimeFormatter.ofLocalizedTime(dateStyle)|
|For formatting dates and times|DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle) and DateTimeFormatter.ofLocalizedDateTime(dateTimeStyle)|

Each method in the table takes a FormatStyle parameter, with posible values SHORT, MEDIUM, LONG and FULL. For the exam we don't need to know the format of each of these styles. If we need to specify a locale all we gotta do is append withLocale(Locale locale) to the method call.

Examples of usage:

        Locale.setDefault(new Locale("en", "US"));
        var italy = new Locale("it", "IT");
        var dt = LocalDateTime.of(2020, Month.OCTOBER, 20, 15, 12, 34);

        print(DateTimeFormatter.ofLocalizedDate(SHORT), dt, italy);
        // 10/20/20, 20/10/20

        print(DateTimeFormatter.ofLocalizedTime(SHORT), dt, italy);
        // 3:12 PM, 15:12

        print(DateTimeFormatter.ofLocalizedDateTime(SHORT), dt, italy);
        // 10/20/20, 3:12 PM, 20/10/20, 15:12

As we can see on the above examples, we are outputing each value with both locales declared on the top. Applying a locale has a big impact on the built-in date and time formatters.

### Specifying a Locale Category

When we set a default locale, several display and formatting option are internally selected. If we need a finer-grained control of the default locale, Java actually subdivides the underlying formatting options into distinct categories, with the Locale.Category enum. This enum is a nested element in Locale, which supports distinct locales for displaying and foramtting data. For the exam we should be familiar with the two enum values:

- **DISPLAY**: Category used for displaying data about the locale.
- **FORMAT**: Category used for formatting dates, numbers or currencies.

We can see some examples of these on the following code snippet:

        public static void printCurrency(Locale locale, double money) {
                println(NumberFormat.getCurrencyInstance().format(money) + ", " + locale.getDisplayLanguage());
        }

        ...

        var spain = new Locale("es", "ES");
        var money = 1.23;
        
        Locale.setDefault(new Locale("en", "US"));
        printCurrency(spain, money); // $1.23, Spanish

        Locale.setDefault(Category.DISPLAY, spain);
        printCurrency(spain, money); // $1.23, español

        Locale.setDefault(Category.FORMAT, spain);
        printCurrency(spain, money); // 1,23 €, español

For the exam we just need to know that we can set parts of the locale independently. Also know that calling Locale.setDefault(localeOptions) after the previous code snippet will change both locale categories to the inputted one.

## Loading Properties with Resource Bundles

Up until now, we've kept all of the text strings displayed to our users as part of the program inside the classes that use them. Localization requires eternalizing them to elsewhere.

A _resource bundle_ contains the locale-specific bojects to be used by a program. It is like a map with keys and values. The resource bundle is commonly stored in a properties file. A _properties file_ is a text file in a specific format with key/value pairs.

> **Note:** For the exam we only need to know about resource bundles that are created from properties files. That said, we can also create a resource bundle from a class by extending ResourceBundle class. One advantage of this apprach is that it allows you to specify values using a method or in format other than String, such as other numeric primtives, objects, or lists.

_Resource bundles_ can be quite helpful when we need to internatiolize our programs. They let us easily translate our application to multiple locales or even support multiple locales at once. It's also easy to add more locales later if needed. 

A _properties file_ is a text file that contains a list of key/value pairs. It is conceptually similar to a Map<_String, String_>, with each line representing a different key/value pair. The key and value are separated by and equal sign (=) or colon (:). 

### Creating a Resource Bundle

For now we're going to create only two properties files for our resource bundle Zoo, one for English and the other French.

        // File: Zoo_en.properties
        hello=Hello
        open=The zoo is open

        // File: Zoo_fr.properties
        hello=Bonjour
        open=Le zoo est ouvert

The filenames match the name of our resource bundle, Zoo. They are then followed by and underscore (_), target locale, and .properties file extension. We can use that resource bundle on our code like this:

        public static void printWelcomeMessage(Locale locale) {
                var rb = ResourceBundle.getBundle("Zoo", locale);
                System.out.println(rb.getString("hello") + ", " + rb.getString("open"));
        }
        public static void main(String[] args) {
                var us = new Locale("en", "US");
                var fr = new Locale("fr", "FR");
                printWelcomeMessage(us); // Hello, The zoo is open
                printWelcomeMessage(fr); // Bonjour, Le zoo est ouvert
        }

Since a resource bundle contains key/values pairs, we can even loop through them to list all of the pairs. The ResourceBundle class provides a keySet() method to get a set of all keys.

        ResourceBundle rb = ResourceBundle.getBundle("Zoo", us);
        rb.keySet().stream
                .map(k -> k + ": " + rb.getString(k))
                .forEach(System.out::println);

This example goes through all of the keys. It maps each key to a String with borh the key and the value before priting everything.

### Picking a Resource Bundle

There are two methods to pick a resource bundle:

- ResourceBundle.getBundle("name");
- ResourceBundle.getBundle("name", locale);

The first one uses the default locale, the second approach uses the locale you passed to it. Java handles the logic of picking the best available resource bundle for a given key. It tries to find the most specific value.

The following table shows how Java handles picking a resource bundle for French/France (new Locale("fr","FR")) with a default locale English/US (Locale("en", "US")):

|Step|Looks for file|Reason|
|:-|:-|:-|
|1|Zoo_fr_FR.properties|The requested locale|
|2|Zoo_fr.properties|The language we requested with no country|
|3|Zoo_en_US.properties|The default locale|
|4|Zoo_en.properties|The default locale's language with no country|
|5|Zoo.properties|No locale at all--the default bundle|
|6|If still not found, throw MissingResourceException|No locale or default bundle available|

As another way of remembering the order of this table, learn these steps:

1. Look for the resource bundle for the requested locale, followed by the one for the default locale.
2. For each locale, check language/country, followed by just the language.
3. Use the default resource bundle if no matching locale can be found.

> **Note:** As mentioned earlier, Java supports resource bundles from Java lasses and properties alike. When Java is searching for a matching resource bundle, it will **first** check for a resource bundle file with the matching class name. For the exam, we just need to know how to work with properties files.

### Selecing Resource Bundle Values

The steps we've discussed so far are for finding the matching resource bundle to use as a base. Java isn't required to get all of the keys from the same resource bundle. It can get them from any parent of the matching resource bundle. A parent resource bundle in the hierarchy just removes components from of the name until it gets to the top, as shown in the following examples:

- Matching resource bundle: **Zoo_fr_FR**
        - Properties files keys can come from: **Zoo_fr_FR.properties, Zoo_fr.properties or Zoo.properties**

Once a resource bundle has been selected, only properties along a single hierarchy will be used. But what does this mean exactly? Assume the requested locale is fr_FR and the default is en_US. The JVM will provide data from an en_US _only if there is no matching fr_FR or fr resource bundles_. If it finds a fr_FR or fr resource bundle, then only those bundles, along with the default bundle, will be used. For example the following bundle files:

        Zoo.properties
        name=Vancouver Zoo

        Zoo_en.properties
        hello=Hello
        open=is open

        Zoo_en_us.properties
        name=The Zoo

        Zoo_en_CA.properties
        visitors=Canada Visitors

Suppose that we have a visitor from Quebec (which has a default locale of French/Canada) who has asked the program to provide information in English:

        Locale.setDefault(new Locale("en", "US"));
        Locale locale = new Locale("en", "CA");
        ResourceBundle rb = ResourceBundle.getBundle("Zoo", locale);
        print(rb.getString("hello");
        print(". ");
        print(rb,getString("name");
        print(" ");
        print(rb.getString("open"));
        print(" ");
        print(rb.getString("visitors"));

The program prints the following:

        Hello. Vancouver Zoo is open Canada visitors

What if a property is not found in any resource bundle? Then, an exception is thrown.

### Formatting Messages

Sometimes we want to format the text data from a resource bundle. The convetion is to use a number inside braces such as {0}, {1}, etc. The number indicates the order in which the parameters will be passed. Although resource bundles don't support this directly, the MessageFormat class does.

