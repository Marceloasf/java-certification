# Chapter 18 - Concurrency

Concurrency:

- Create worker threads using Runnable, Callable and use an ExecutorService to concurrently execute tasks.
- Use java.util.concurrent collections and classes including CyclicBarrier and CopyOnWriteArrayList.
- Write thread-safe code.
- Identify threading problems such as deadlocks and livelocks.

Parallel Streams:

- Develop Code that uses parallel streams.
- Implement decomposition and reduction with streams.

## Introducing Threads (p.841-848)

Computers are capable of reading and writing data to external resources, unfortunately, as compared to CPU operations, these disk/network operations tend to be extremely slow, so slow that if your computer's operating system were to stop and wait for every disk or network operation to finish, your computer would appear to freeze or lock up constantly.

Luckily, all modern operating systems support what is known as multithreaded processing. The idea behind multithreaded processing is to allow an application or group of applications to execute multiple tasks at the same time. This allows tasks waiting for other resources to give way to other processing requests.

Since its early days, Java has supported multithreaded programming using the Thread class. More recently, the Concurrency API was introduced. It included numerous classes for performing complex thread-based tasks. The idea was simple: managing complex thread interactions is quite difficult for even the most skilled developers; therefore, a set of reusable features was created. The Concurrency API has grown over the years to include numerous classes and frameworks to assist you in developing complex, multithreaded applications.

> **Note:** Previous Java certifications exams expected you to know details about threads, such as thread life cycles. But now the exam instead covers the basics of threads but focuses more on your knowledge of the Concurrency API.

The start of this chapter is about reviewing common terminology associated with threads:
  - thread: Is the smallest unit of execution that can be scheduled by the operating system
  - process: Is a group of associated threads that execute in the same, shared environment.
  - single-threaded process: Is one that contains exactly one thread.
  - multithreaded process: Is one that contains one or more threads.
  - shared environment: The threads in the same process share the same memory space and can communicate directly with one another.

In this chapter, we will talk a lot about tasks and their relationship to threads. A task is a single unit of work performed by a thread. Throughout this chapter, a *task* will commonly be implemented as a lambda expression. A thread can complete multiple independent tasks, but **only one** task at a time.

When we refer to shared memory, we are generally referring to static variables, as well as instance and local variables passed to a thread. On this chapter, you'll see how static variables can be useful for performing complex multithreaded tasks. For example, if one thread updates the value of a static object, the this information is immediately available for other threads within the process to read.

> **Remember** from Chapter 7, "Methods and Encapsulations" that static methods and variables are defined on a single class object that **all** insatnces share. 

### Distinguishing Thread Types

All the Java applications, including all of the ones that are presented in the book, are all multithreaded. Even a simples Java application that prints `Hello World` to the screen is multithreaded. To understand this, we need to be familiar with concepts of system threads and user-defined threads. 

A *system thread* is created by the JVM and runs in the background of the application. For example, the garbage collection is managed by a system thread that is created by the JVM and runs in the background, helping to free memory that is no longer in used. For the most part, the execution of system-defined threads is invisible to the application developer. When a system-defined thread encounters a problem and cannot recover, such as running out of memory, it generates a Java **Error**, as opposed to an **Exception**.

> **Note:** Even though it's possible to catch an **Error**, it is considered a poor practice to do so, since it is rare that an application can recover from a system-level failure.

A *user-defined thread* is one created by the application dev to accomplish a specific task. With the exception of parallel streams, all of the applications that we created up to this point have been multithreaded, but they contained only one user-defined thread, which calls the `main()` method. Applications that contain only a single user-defined thread are referred to as single-threaded applications.

> **Note:** Although not required knowledge for the exam, a *daemon thread* is one that will not prevent the JVM from exiting when the program finishes, a Java application terminates when the only threads that are running are daemon threads. For example, if garbage collection is the only thread left running, the JVM will automatically shut down. Both system and user defined can be marked as daemon threads.

### Understanding Thread Concurrency

Before we mentioned that multithreaded processing allows the operating system (OS) to execute threads at the same time. The property of executing multiple threads and processes at the same time is referred to as *concurrency*. But with a single-core CPU system, only one task is actually executing at a given time. Even in multicore of multi-CPU systems, there are often far more threads than CPU processors available.

The OS use a thread schedular to determine which threads should be currently executing. For example, a thread schedular may employ a `round-robin schedule` in which each available thread receives an equal number of CPU cycles with which to execute, with threads visited in a circular order (1 -> 2 -> 3 -> 1 -> 2 ...).

When a thread's allotted time is complete but the thread has not finished processing, a context switch occurs. A *context switch* is the process of storing a thread's current state and later restoring the state of the thread to continue execution. Be aware that there is often a cost associated with a context switch by way of lost time saving and reloading a thread's state. Intelligent thread schedules do their best to minimize the number of context switches.

A thread can interrupt or supersede another thread if it has a higher thread priority than the other thread. A *thread priority* is a numeric value associated with a thread that is taken into consideration by the thread scheduler when determining which threads should currently be executing. In Java, thread priorities are specified as integer values.

> **The Importance of Thread Scheduling:** Even though multicore CPUs are common these days, single-core CPUs were the standard in personal computing for many decades. During this time, operating systems developed complex thread-scheduling and context-switching algorithms that allowed users to execute dozens or even hundred of threads on a single-core CPU system. These algorithms allowed users to experience the illusion that multiple tasks were being performed at the same time within a single-core CPU system. For example, a user could listen to music and write a paper at the same time. Since the number of threads requested often far outweighs the number of processors available even in multicore systems, these algorithms are still employed in operating systems today.

### Defining a Task with Runnable

As mentioned before, java.lang.Runnable is a functional interface that takes no arguments and returns no data. The following is the definition of the interface:

    @FunctionalInterface public interface Runnable {
    
      void run();
    
    }

The Runnable interface is commonly used to define the task or work a thread will execute, separate from the main application thread.

The Following Lambda expressions each implement the Runnable interface:

    Runnable sloth = () -> System.out.println("Hello World");
    Runnable sloth = () -> {int i=10; i++;};
    Runnable sloth = () -> {return;};
    Runnable sloth = () -> {};

Notice that all of these lambda expressions start with a set of empty parentheses. Also, none of the lambda expressions returns a value. The following lambdas, while valid for other functional interfaces, are not compatible with Runnable because they return a value.

    Runnable capybara = () -> ""; // DOES NOT COMPILE
    Runnable Hippo = () -> 5; // DOES NOT COMPILE
    Runnable emu = () -> {return new Object();}; // DOES NOT COMPILE

#### Creating Runnable Classes

Even though Runnable is a functional interface, many classes implement it directly, as shown in the following code:

    public class CalculateAverage implements Runnable {
      public void run() {
        // Define work here
      }
    }

It is also useful if you need to pass information to your Runnable object to be used by the run() method, such as in the following constructor:

    public class CalculateAverages implements Runnable {
      private double[] scores;

      public CalculateAverages(double[] scores) {
        this.scores = scores;
      }

      public void run() {
        // Define work here that uses the scores object
      }
    }

In this chapter, you'll se alot of lambda expressions that implicitly implement the Runnable interface, so just be aware that it's commonly used in class definitions.

### Creating a Thread

The simplest way to execute a thread is by using the **java.lang.Thread** class. Executing with Thread is a two-step process. First, you define the Thread with the corresponding task to be done, then you start the task by using the `Thread.start()` method.

Java does not provide any guarantees about the order in which a thread will be processed once it is started. It may be executed immediately or delayed for a significant amount of time. 

> **Note:** Remember that order of thread execution is not often guaranteed. The exam commonly presents questions in which multiple tasks are started at the same time, and you must determine the result.

Defining the task that a Thread instasnce will execute can be done two ways in Java:
  - Provide a Runnable object or lambda expression to the Thread constructor.
  - Create a class that extends Thread and overrides the `run()` method.

The following are examples of these techniques:

    public class PrintData implements Runnable {
    	@Override public void run() { Overrides method in Runnable
          for(int i = 0; i < 3; i++) 
		  	  System.out.println("Printing record: " + i);
        }
    	public static void main(String[] args) {
			(new Thread(new PrintData())).start();
		}
	}

	public class ReadInventoryThread extends Thread {
		@Override public void run() { // Overrides method in Thread
			System.out.println("Printing zoo inventory");
		}
		public static void main(String... args) {
			(new ReadInventoryThread()).start();
		}
	}

The first example creates a Thread using a Runnable instance, while the second example uses the less common practice of extending the Thread class and overriding the `run()` method. In general, you should extend the Thread class only under specific circumstances, such as when you are creating your own priority-based thread. But in most situations you should implement the Runnable interface.

Anytime you create a Thread instance, make sure to remember to start the task with the `Thread.start()` method. This starts the task in a separate OS thread. Let's try this, what is the output of the following code snippet:

	public static void main(String[] args) {
		System.out.println("begin");
		(new ReadInventoryThread()).start;
		(new Thread(new PrintData())).start;
		(new ReadInventoryThread()).start;
		System.out.println("end");
	}

The answer is that is unkown until runtime. A possible output is:

	begin
	Printing zoo inventory
	Printing record: 0
	end
	Printing zoo inventory
	Printing record: 1
	Printing record: 2

This example uses a total of four threads, them `main()` user thread and three additional threads created on the block. Each thread created on these lines is executed as an asynchronous task. By asynchronous, it's meant that the thread executing the `main()` method does not wait for the results of each newly created thread before continuing. The opposite of this behavior is a synchronous task in which the program waits (or blocks) for the thread to finish executing before moving on to the next line. The vast majority of method calls used in this book have been synchronous up until now.

While the order of thread execution once the threads have benen started is indeterminate, the order within a single thread is still linear. In particular, the for loop in PrintData is still ordered. Also, `begin` appears before `end` in the main() method.

You can call `run()` instead of `start()`, but be careful using `run()`. Calling `run()` on a Thread or a Runnable does not actually start a new thread. While the following code snippets will compile, none will actually execute a task on a separate thread:

	public static void main(String[] args) {
		System.out.println("begin");
		(new ReadInventoryThread()).run();
		(new Thread(new PrintData())).run();
		(new ReadInventoryThread()).run();
		System.out.println("end");
	}

Unlike the previous example, each line of this code will wait until the run() method is complete before moving on to the next line. Also unlike the previous program, the output for this code sample will be the same each time it is executed.

Now we conclude our discussion of the Thread class. While previous versions of the exam were quite focused on understanding the difference between extending Thread and implementing Runnable, the exam now encourages devs to use the Concurrency API. Also, you don't need to know about other thread-related methods, such as Object.wait(), Object.notify(), Thread.join(), etc. In fact, you should **avoid** them in general and use the Concurrency API as much as possible. It takes a large amount of skill (and some luck) to use these methods correctly. 

> **Real World Scenario:** Despite that the exam no longer focuses on creating threads by extending the Thread class and implementing the Runnable interface, it is extremely common when interviewing for a Java development position to be asked to explain the difference between extending the Thread class and implementing Runnable. If asked this question, you should answer it accurately. You should also mention that you can now create and manage threads indirectly using an ExecutorService (which we will discuss in the next section).

### Polling with Sleep

We know that multithreaded programming allows us to execute multiple tasks at the same time, but one thread often needs to wait for the results of another thread to proceed. One solution is to use polling. Polling is the process of intermittently checking data at some fixed interval. For example, imagine you have a thread that modifies a shared static counter value and your main() thread is waiting for the thread to increase the value to be greater than 100, as shown in the following class:

	public class CheckResults {
		private static int count = 0;
		public static void main(String[] args) {
			new Thread(() -> {
				for(int i = 0; i < 500; i++) CheckResults.counter++;
			}).start();

			while(CheckResults.counter < 100) {
				System.out.println("Not reached yet");
			}
			
			System.out.println("Reached!");
		}
	}

This program can output "Not reached yet" zero, ten or even a million times! If our thread schedular is particularly poor, it could operate infinitely. Using a while() loop to check for data without some kind of delay is considered a bad coding practice as it ties up CPU resources for no reason.

We can improve this by using the `Thread.sleep()` method to implement polling. This method requests the current thread of execution rest for a specified number of milliseconds. When used inside the body of the main() method, the thread associated with the main() method will pause, while the separate thread will continue to run. The following example uses the `Thread.sleep()` method:

	public class CheckResults {
		private static int count = 0;
		public static void main(String[] args) throws InterruptedException { // CHANGED
			new Thread(() -> {
				for(int i = 0; i < 500; i++) CheckResults.counter++;
			}).start();

			while(CheckResults.counter < 100) {
				System.out.println("Not reached yet");
				Thread.sleep(1000); // 1 SECOND
			}
			
			System.out.println("Reached!");
		}
	}
	
Just by delaying on the end of the loop, we have now prevented a possibly infinite loop from executing and locking up our program. Notice that we also changed the signature of the main() method, since Thread.sleep() throws the **checked exception InterruptedException**. Alternatively, we could have wrapped the call to Thread.sleep() method in a try/catch block.

But know that we changed the implementation, how many times does the while() loop execute in this class? Still unknown! While polling does prevent the CPU from being overwhelmed with a potentially infinite loop, it does not guarantee when the loop will terminate. For example, the separate thread could be losing CPU time to a higher-priority process, resulting in multiple executions of the while() loop before it finishes.

Another issue is the shared counter variable. What if one thread is reading the counter variable while another thread is writing it? The thread reading the shared variable may end up with an invalid or incorrect value. We will discuss these issues in detail in the upcoming section on writing thread-safe code.

## Creating Threads with the Concurrency API (p.849-860)