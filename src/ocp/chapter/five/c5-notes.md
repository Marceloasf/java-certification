As mentioned on c1-notes and c2-notes, I am starting this book at this chapter (5) because the OCA-8 book already covers the same subjects of the Chapters 1-4 from this book in its Chapters 1-2, they will be revisited futurely.

# Chapter 5 - Core Java APIs

Working with Java Primitive Data Types and String APIs
- Create and manipulate Strings.
- Manipulate data using the StringBuilder class and its methods.
- String Pool

Working with Java Arrays
- Declare, instantiate, initialize and use one-dimensional and two-dimensional arrays.

Programming Abstractly Through Interfaces
- Declare and use List and ArrayList instances.

Introduction to Wrapper Classes and Java Collections
- Wrapper classes, Sets and Maps.

## String immutability (p.166-167)

Once a String object is created, it is not allowed to change. It cannot be made larger or smaller, and you cannot change one of the characters inside it (Strings are immutable). You can make a reference point to a different String object or replace a char for other. Strings support Method Chaining and each time a method is called, a new String is returned.

> **Note:** Immutable classes in Java are final, which prevents subclasses creation.

## Creating and Manipulating Strings (p.167-172)

Some of the String methods that you should be familiar with:

- `length()` returns the number of characters in the String.
- `charAt(int index)` lets you query the string to find out what character is at a specific index.
- `indexOf()` has multiple signatures and returns an int value, looks at the characters in the string and finds the first index that matches the desired value. indexOf can work with an individual character or a whole String as input. It can also start from a requested postion. A char can be passed to an int parameter type. Signatures are `indexOf(int ch)`, `indexOf(int ch, int fromIndex)`, `indexOf(String str)`, `indexOf(String str, int fromIndex)`. Returns -1 when the value is not found.
- `substring()` has one starting index param and a ending index optional param, if the endingIndex is specified, it will stop right before the endingIndex. `"abcde".substring(1, 4);` // Result is "bcd".
- Strings has two implementations of equals methods to check equality. The `equals()` that checks if the string values are the same and `equalsIgnoreCase()` that does the same thing but ignore the case, because it will converts the chars if needed.
- `startsWith(Strings prefix)` and `endsWith(String suffix)` do exactly what they sound like, they check if a string is the prefix or is the suffix of a String. 
- `replace()` method has two signatures, one that takes a oldChar and a newChar, and another that takes a charSequence as target and charSequence as replacement to replace them left to right.
- `contains()` method looks for matches in the String, using a CharSequence("ABC") - the match can be anywhere in the String.
- `trim()` and `strip()` (strip was added in Java 11) removes whitespaces from the beginning and end of a String. The diference between them is that `strip()` supports unicode.
- `stripLeading()` removes whitespaces only from the beginning of the String.
- `stripTrailing()` removes whitespaces only from the end of the String.
- The `intern()` method returns the value from the string pool if it is there, otherwise, it adds the value to the string pool.

## StringBuilder (p.174-179)
StringBuilder is not immutable (`a.append("x")` concats to the same object). StringBuffer is slower and older than StringBuilder, so just use StringBuilder.

When chaining StringBuilder methods, it will change its own state and returns a reference to itself (`append()` the same object), different than String that returns a new String on chaining. 

`charAt()`, `indexOf()`, `length()` and `substring()` all work the same way as they work with String, but `substring()` returns a String rather than a StringBuilder.

Some StringBuilder methods that you should be familiar with:

- `append()` will convert values (any type) into String, like true to "true".
- `insert()` adds chars to the StringBuilder at the requested index and returns a reference to the current StringBuilder.
- `delete()` deletes chars from a starting index to stop right before the ending index, `deleteCharAt()` deletes only one char at the indicated index.
- `StringBuilder a = new StringBuilder("abc"); - a.delete(1, 100);` // Compiles because Java will just assume you meant the end, `delete()` is more flexible. 
- `replace(int startingIndex, int endingIndex, String newString)` works differently for StringBuilder than it did for String. Java deletes the chars between the startingIndex and stops right before endingIndex, then java inserts the replacement value.
- `reverse()` does just what it sounds like, it reverse the chars in the sequences and returns a ref to the current StringBuilder.
- `toString()` converts a StringBuilder to a String. Methods that expect a String will not accept a StringBuilder instance (use `sb.toString()` to work). 

## Understanding equality (p.179-180)
Logical equality is asserting the values (for strings, the equals() will assert the equality between the value of a and b). Object equality is asserting the references, if they are pointing to the same object (objectA == objectB, same reference is true). Primitives always assert the values when using ==. equals() depends on the implementation of the object it is being called on, the default implementation is using object equality (comparing references). If you call equals() on two Objects that doesn't implements the equals() method, it will check reference equality (StringBuilder for example).

> **Note:** If you try to check Object equality between two different data type Objects, the compiler will throw an error, because he is smart enough to know that two references can't possibly point to the same Object when they are completely different types.

## String Pool (p.181-182)
The string pool contains literal values and constants ("ABC") that appear in your program. Java realizes that many strings repeat in the program and solves this issue by reusing common ones, the string pool is a location in the JVM that collects all these strings. How the String Pool can impact on reference equality:

	String x = "Hello";
	String y = "Hello";
	x == y // is true, because the JVM created only one literal in memory. x and y both point to the the same location in memory;
			  therefore, the statement outputs true.

	String x = "Hello";
	String y = "  Hello".trim();
	x == y // is false, because we don't have the same String literal. Although both happen to evaluate to the same string,
			  one is computed at runtime, since it isn't the same at compile-time, a new String object is created.
		
- If we call the `intern()` after that `trim()`, Java will add it to the string pool and the result will be true.

> **Note:** Never use `intern()` or == to compare String objects in your code. Just know how they work for the exam.

## Understanding Java arrays (p.184-186)

An array is a fixed-sized area of memory on the heap, that has space for primitives or pointers to objects. Default declaration: int[] numbers = new numbers[3] -> arrays are reference (object) variables. Anonymous array is a declaration of an array that doesn't need you to specify the type and size, for example: `int[] numbers2 = {42, 45}`, since you are specifying the type of the array on the left side of the equal sign, Java already knows the type and since you are specifying the initial values, it already knows the size too. The array does not allocate space for the Objects, instead, it allocates space for a reference to where the objects are really stored. 

For the exam is good to know that all these statements do the exact thing (declaring arrays):

	int[] numAnimals;
	int [] numAnimals2;
	int []numAnimals3;
	int numAnimals4[];
	int numAnimals5 [];
	int[] ids, types; // Declare both as int[].
	int ids[], types; // Declare only ids as int[] and types is declared as int.

We can call `equals()` because an array is an object (any type of array), but it uses reference equality because it doesn't overrides Object implementation. ** The `equals()` method on arrays, does not look at the elements of the array. 

> **Note:** Since Java 5, Java has provided a method that prints an array nicely: `String [] bugs = { "cricket", "beetle", "ladybug" };`, `java.util.Arrays.toString(bugs)` would print [cricket, beetle, ladybug] (inside of a System.out.println...).

## Using an Array (p.187-194)

length does not consider what is in the array, it only considers how many slots have been allocated, and length is not a method. `String[] numbers = new String[6];` // numbers.length results in 6, even though all six elements are null. There is no direct way to remove and add elements from an array in Java.

### Sorting

Arrays.sort requires an import -> `java.util.*` or `java.util.Arrays`. Numbers sort before letters, and uppercase sorts before lowercase (with Strings).

### Searching

binarySearch only works if the array is already sorted. Binary search (Arrays.binarySearch) rules: 
- Target element found in sorted array: Index of match.
- Target element not found in sorted array: Negative value showing one smaller than the negative of the index where a match needs to be inserted to preserve the sorted order.
- Unsorted array: A surprise (this result isn't predictable).

### Comparing
`compare()` returns -1, 0 and +1 (negative number, zero and positive number on the exam).
- -1: First array is smaller than the second.
- 0: Arrays are equal.
- +1: First array is larger than the second.

`compare()` will not compare only the length of the arrays. Rules of comparation:

- If both arrays are the same length and have the same values in each spot in the same order, return 0.
- If all the elements are the same but the second array has extra elements at the end, returns -1 (it returns a positive number if the first array is 'bigger').
- If the first element that differs is smaller in the first array, return a negative number (it returns a positive number if the first element to differ in the first array is'larger').
- The following rules apply to `compare()` and to `compareTo()`:
- null is smaller than any other value.
- For numbers, normal numeric order applies.
- For strings, one is smaller if it is a prefix of another.
- For strings/chars, numbers are smaller than letters.
- For strings/chars, uppercase is smaller than lowercase.

Arrays.`compare()` examples applying this rules:

	new int[] {1, 2} - new int[] {1} // Positive number; 
	new int[] {1, 2} - new int[] {1, 2} // Zero; 
	new String[] {"a"} - new String[] {"aa"} // Negative number; 
	new String[] {"a"} - new String[] {"A"} // Positive number; 
	new String[] {"a"} - new String[] {null} // Positive number; 
	
> **Note:** When comparing two arrays, they must be the same array type, otherwise, the code will not compile.

`Arrays.mismatch()` (Java 9): Returns -1 if the arrays are equal, otherwise, it returns the first index where they differ.

### Multidimensional Arrays

	int[] a[]; // 2D array.
	int[][] b; // 2D array.
	int c [][]; // 2D array.
	int[] d[], e[][]; // 2D array and a 3D array.
	String [][] f = new String[3][2];

Assymetric Arrays:

	int[][] g = {{1, 4}, {3}, {9, 8, 7}};
	int[][] args = new int[4][]; -> args[0] = new int[5]; -> args[1] = new int[3];

## ArrayList (p.196-197)
Default contructor slots size value is 10 (new ArrayList<>()). Implements an interface called List, so you can store an ArrayList in a List reference variable, but not vice versa, because List is an interface and interfaces can't be instantiated. If you don't specify a type for the ArrayList, it will accept any Object subclasses (not primitives),
but if a type is specified, only that type can be added (add()) into the ArrayList. If a type is not specified with the diamond operator < and >, Object will be the default type for ArrayList and List. Some methods of ArrayList:

- add() method can be used in two different ways, add(index, value) and add(value), one adding the value at the specified index and the other adding to the end of the ArrayList.
- remove() removes the first matching value in the ArrayList (remove(Object object)) or remove the element at a specified index (remove(int index)). 
- set(int index, E newElement) changes one of the elements of the ArrayList without changing the size (updates a value).
- isEmpty() and size() methods look at how many of the slots are in use.
- contains(Object object) checks whether a certain value is in the ArrayList.
- equals(Object object) compare two lists to see whether they contain the same elements in the same order.

> **Note:** E appears in this chapter on some method signatures. E is used by convention in generics to mean "any class that this array can hold". If you didn't specify a type when creating the ArrayList, E means Object. 

Sorting ArrayList: Is similar to sorting an array, the difference is the helper class, which is `Collections`.sort(array) for an ArrayList.

## Wrapper Classes (p.201-203)
Each primitive type has a wrapper class, which is an object type that corresponds to the primitive. Each wrapper class also has a contructor. It works the same way as valueOf(), but isn't recommended for new code, because the valueOf() allows object caching. The wrapper classes are immutable and take advantage of some caching as well. Only the Character class doesn't has parse/valueOf methods. The valueOf() can receive a String or the primitive value that corresponds to that wrapper class data type. String's valueOf() is different, because it can receive char, int, long, float, double and Objects as params. All the parse methods (parseInt(s), parseFloat(s)...) on the wrapper classes receive a String as the param.

### Autoboxing and Unboxing

Since Java 5, you can just type the primitive value and Java will convert it to the relevant wrapper class for you (data types matter). The reverse conversion of wrapper class to primitive value is called unboxing. For example:

	Integer a = 50; // Autoboxing 50 (literal) to Integer.
	exArray.add(a);
	double first = exArray.get(0); // Unboxing exArray.get(0) (Integer) to double (50.0).

> **Note:** In some cases the unboxing may produce a NullPointerException, since calling any method on null gives a NullPointerException.

## Converting Between array and List (p.204-205)

	List<String> list = new ArrayList<>();
	Object[] objectArray = list.toArray(); // Default return is an array of class Object.
	String[] objectArray = list.toArray(new String[0]); // Specified the type of array (corresponding to the List type).

Arrays.asList(array) is an option to create a list from an array, but be careful using this method, because it generates a fixed-size list and is also known as a backed List, because the array changes with it. You can 'update' the values from the List, but you can't change the size of the List (for example using a list.remove(0)).

List.of(array) is another option to create a list from an array, but this option returns an immutable List, that means you cannot change the values or the size of the List. You can change the original array, but will not be reflected in the immutable List like Arrays.asList().

None of the options allows you to change the number of elements.

> **Note:** For the typed declaration, specifying a size of 0 for the parameter is an advantage, because Java will create a new array of the proper size for the return value, but you can suggest a larger array to be used instead, if the ArrayList fits in, it will be returned. Otherwise, a new one will be created (a dimension must be specified in this type of declaration).

Arrays.asList and List.of can take varargs (`Arrays.asList("ABC", "DEF")`), which let you pass in an array or just type out the String values.

## Introducing Sets and Maps (p.206-208)

#### Sets

A Set is a collection of objects that cannot contain duplicates, the API will not fulfill a request that add a duplicate. The most common implementation of Set is HashSet. All the methods for ArrayList apply to a Set, with the exception of those taking an index as a param, because Set isn't ordered like a List, so `remove(int index)` or `set(int index, value)` will not work (obs.: `remove(Object value)` works). Methods like `add()` return a boolean because of Set. When trying to add a duplicate value, the method returns false and does not add the value.

#### Maps

A Map uses a key to identify values (key -> value, Map<K,V>). The most common implementation of Map is HashMap. Some of the methods are the same as those in ArrayList, like `clear()`, `isEmpty()` and `size()`. There are also methods specific to dealing with key and value pairs:

- V get(Object key) - Returns the value mapped by key or null if none is mapped.
- V getOrDefault(Object key, V other) - Returns the value mapped by key or other if none is mapped.
- V put(K key, V value) - Adds or replaces key/value pair. Returns previous value or null.
- V remove(Object key) - Removes and returns value mapped to key. Returns null if none.
- boolean containsKey(Object key) - Returns whether key is in Map.
- boolean containsValue(Object value) - Returns whether value is in Map.
- Set<K> keySet() - Returns set of all keys.
- Collection<V> values() - Returns Collection of all values.
    

## Calculating with Math APIs (p.208-210)

- `min(a, b)` and `max(a, b)` compare two values and return one of them (overloaded with int, long, float and double return types). `min()` returns the smaller of A and B and`max()` returns the bigger of A and B.
- `round(double/float num)` method gets rid of the decimal portion of the value, choosing the next higher number if appropriate (.5 or higher will round up).
- `pow(double num, double exponent)` handles exponents (fractional too).
- `random()` generates a value greater or equal to 0 and less than 1.0 (returns a double).
