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

Since its early days, Java has supported multithreaded programming using the Thread class. More recently, the Concurrency API was introduced, it included numerous classes for performing complex thread-based tasks. The idea was simple: managing complex thread interactions is quite difficult for even the most skilled developers; therefore, a set of reusable features was created. The Concurrency API has grown over the years to include numerous classes and frameworks to assist you in developing complex, multithreaded applications.

> **Note:** Previous Java certifications exams expected you to know details about threads, such as thread life cycles. But now the exam instead covers the basics of threads but focuses more on your knowledge of the Concurrency API.

The start of this chapter is about reviewing common terminology associated with threads:
  - thread: Is the smallest unit of execution that can be scheduled by the operating system.
  - process: Is a group of associated threads that execute in the same, shared environment.
  - single-threaded process: Is one that contains exactly one thread.
  - multithreaded process: Is one that contains one or more threads.
  - shared environment: The threads in the same process share the same memory space and can communicate directly with one another.

In this chapter, we will talk a lot about tasks and their relationship to threads. A task is a single unit of work performed by a thread. Throughout this chapter, a *task* will commonly be implemented as a lambda expression. A thread can complete multiple independent tasks, but **only one** task at a time.

When we refer to shared memory, we are generally referring to static variables, as well as instance and local variables passed to a thread. On this chapter, you'll see how static variables can be useful for performing complex multithreaded tasks. For example, if one thread updates the value of a static object, this information is immediately available for other threads within the process to read.

> **Remember** from Chapter 7, "Methods and Encapsulations", that static methods and variables are defined on a single class object that **all** instances share. 

### Distinguishing Thread Types

All the Java applications, including all of the ones that are presented in the book, are all multithreaded. Even a simple Java application that prints `Hello World` to the screen is multithreaded. To understand this, we need to be familiar with concepts of system threads and user-defined threads. 

A *system thread* is created by the JVM and runs in the background of the application. For example, the garbage collection is managed by a system thread that is created by the JVM and runs in the background, helping to free memory that is no longer in use. For the most part, the execution of system-defined threads is invisible to the application developer. When a system-defined thread encounters a problem and cannot recover, such as running out of memory, it generates a Java **Error**, as opposed to an **Exception**.

> **Note:** Even though it's possible to catch an **Error**, it is considered a poor practice to do so, since it is rare that an application can recover from a system-level failure.

A *user-defined thread* is one created by the application dev to accomplish a specific task. With the exception of parallel streams, all of the applications that we created up to this point have been multithreaded, but they contained only one user-defined thread, which calls the `main()` method. Applications that contain only a single user-defined thread are referred to as single-threaded applications.

> **Note:** Although not required knowledge for the exam, a *daemon thread* is one that will not prevent the JVM from shutting down when the program finishes, a Java application terminates when the only threads that are running are daemon threads. For example, if garbage collection is the only thread left running, the JVM will automatically shut down. Both system and user defined threads can be marked as daemon threads.

### Understanding Thread Concurrency

Before we mentioned that multithreaded processing allows the operating system (OS) to execute threads at the same time. The property of executing multiple threads and processes at the same time is referred to as *concurrency*. But with a single-core CPU system, only one task is actually executing at a given time. Even in multicore or multi-CPU systems, there are often far more threads than CPU processors available.

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

In this chapter, you'll se a lot of lambda expressions that implicitly implement the Runnable interface, so just be aware that it's commonly used in class definitions.

### Creating a Thread

The simplest way to execute a thread is by using the **java.lang.Thread** class. Executing with Thread is a two-step process. First, you define the Thread with the corresponding task to be done, then you start the task by using the `Thread.start()` method.

Java does not provide any guarantees about the order in which a thread will be processed once it is started. It may be executed immediately or delayed for a significant amount of time. 

> **Note:** Remember that order of thread execution is not often guaranteed. The exam commonly presents questions in which multiple tasks are started at the same time, and you must determine the result.

Defining the task that a Thread instance will execute can be done two ways in Java:
  - Provide a Runnable object or lambda expression to the Thread constructor.
  - Create a class that extends Thread and overrides the `run()` method.

The following are examples of these techniques:

    public class PrintData implements Runnable {
    	@Override public void run() { // Overrides method in Runnable
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
		(new ReadInventoryThread()).start();
		(new Thread(new PrintData())).start();
		(new ReadInventoryThread()).start();
		System.out.println("end");
	}

The answer is that is unknown until runtime. A possible output is:

	begin
	Printing zoo inventory
	Printing record: 0
	end
	Printing zoo inventory
	Printing record: 1
	Printing record: 2

This example uses a total of four threads, the `main()` user thread and three additional threads created on the block. Each thread created on these lines is executed as an asynchronous task. By asynchronous, it's meant that the thread executing the `main()` method does not wait for the results of each newly created thread before continuing. The opposite of this behavior is a synchronous task in which the program waits (or blocks) for the thread to finish executing before moving on to the next line. The vast majority of method calls used in this book have been synchronous up until now.

While the order of thread execution once the threads have been started is indeterminate, the order within a single thread is still linear. In particular, the for loop in PrintData is still ordered. Also, `begin` appears before `end` in the main() method.

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
		private static int counter = 0;
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
		private static int counter = 0;
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
	
Just by delaying on the end of the loop, we have now prevented a possibly infinite loop from executing and locking up our program. Notice that we also changed the signature of the main() method, since `Thread.sleep()` throws the **checked exception InterruptedException**. Alternatively, we could have wrapped the call to `Thread.sleep()` method in a try/catch block.

But now that we changed the implementation, how many times does the while() loop execute in this class? Still unknown! While polling does prevent the CPU from being overwhelmed with a potentially infinite loop, it does not guarantee when the loop will terminate. For example, the separate thread could be losing CPU time to a higher-priority process, resulting in multiple executions of the while() loop before it finishes.

Another issue is the shared counter variable. What if one thread is reading the counter variable while another thread is writing it? The thread reading the shared variable may end up with an invalid or incorrect value. We will discuss these issues in detail in the upcoming section on writing thread-safe code.

## Creating Threads with the Concurrency API (p.849-861)

Java includes the Concurrency API to handle the complicated work of managing threads for you. This API includes the ExecutorService interface, which defines services that create and manage threads for you. It is recommended that you use this framework anytime you need to create and execute a separate task, even if you need only a single thread.

### Introducing the Single-Thread Executor

To get an instance of the interface ExecutorService, you'll use the Concurrency API, which includes the Executors factory class and that can be used to create instances of the ExecutorService object. 

> **Note:** As you may remember from previous chapters, the factory pattern is a creational pattern in which the underlying implementation details of the object creation are hidden from us. 

This is a simple example using the `newSingleThreadExecutor()` method to obtain an ExecutorService instance and the `execute()` method to perform asynchronous tasks.

	import java.util.concurrent.*;
	public class ZooInfo {
		public static void main(String[] args) {
			ExecutorService service = null;

			Runnable task1 = () -> System.out.println("Printing zoo inventory");
			Runnable task2 = () -> { for(int i = 0; i < 3; i++)
				 		System.out.println("Printing record: "+i);};
		
			try {
				System.out.println("begin");
				service = Executors.newSingleThreadExecutor(); // Calls factory class to get an instance of ExecutorService (single-thread)
				service.execute(task1);
				service.execute(task2);
				service.execute(task1);
				System.out.println("end");
			} finally {
				if (service != null) service.shutdown();
			}
		}
	}

> **Note:** This code snippet is a rewrite of our earlier PrintData and ReadInventoryThread classes to use lambda expressions and an ExecutorService instance.

In this example, we use the `Executors.newSingleThreadExecutor()` method to create the service. Unlike our earlier example, in which we had three extra threads for newly created tasks, this example uses only one, which means that the threads will order their results. The following is a possible output for this code snippet:

	begin
	Printing zoo inventory
	Printing record: 0
	Printing record: 1
	end
	Printing record: 2
	Printing zoo inventory

With a single-thread executor, results are **guaranteed** to be executed sequentially. Notice that the end text is output while our thread executor tasks are still running. This is because the main() method is still an independent thread from the ExecutorService.

### Shutting Down a Thread Executor

Once you have finished using a thread executor, remember to call the `shutdown()` method, because a thread executor creates a non-daemon thread on the first task that is executed, so not calling the `shutdown()` method will result in your application never terminating! 

The shutdown process for a thread executor involves first rejecting any new tasks submitted to the thread executor while continuing to execute any previously submitted tasks. During this time, calling `isShutdown()` will return true, while `isTerminated()` will return false. If a new task is submitted to the thread executor while it is shutting down, a RejectedExecutionException will be thrown. Once all active tasks have been completed, `isShutdown()` and `isTerminated()` will both return true. 
  - Active: Accepts new tasks, executes tasks and `isShutdown()` and `isTerminated()` are both false.
  - Shutting Down: After calling the `shutdown()` method, it'll reject new tasks, still execute tasks, `isShutdown()` return true and `isTerminated()` return false.
  - Shutdown: Reject new tasks, no tasks running and `isShutdown()` and `isTerminated()` are both true.

For the exam, you should be aware that `shutdown()` does not actually stop any tasks that have already been submitted to the thread executor. 

If you want to cancel all running and upcoming tasks, the ExecutorService provides a method called `shutdownNow()`, which *attempts* to stop all running tasks and discards any that have not been started yet. The `shutdownNow()` method returns a List<Runnable> of tasks that were submitted to the thread executor but that were never started. 

> **Note:** It is possible to create a thread that will never terminate, so any attempt to interrupt it may be ignored.

As you learned in previous chapters, resources such as thread executors should be properly closed to prevent memory leaks. The ExecutorService interface does not extend the AutoCloseable/Closeable interface, so you cannot use a try-with-resources statement, but you can still use a finally block. While not required, it's considered a good practice to do so.

### Submitting Tasks

You can submit tasks to an ExecutorService instance many ways. The first that was presented, using the `execute()` method, is inherited from the Executor interface, which ExecutorService interface extends. The `execute()` method takes a Runnable lambda expression or instance and completes the task asynchronously. Because the return type of the method is void, it does not tell us anything about the result of the task.

> **Note:** The `execute()` method is considered a "fire-and-forget" method, as once it's submitted, the results are not directly available to the calling thread.

But fortunately, we have the `submit()` method on the ExecutorService interface too, which like `execute()`, can be used to complete tasks asynchronously. Unlike `execute()`, `submit()` returns a **Future** instance that can be used to determine whether the task is complete. It can also be used to return a generic result object after the task has been completed.

> **Note:** Don't worry if you haven't seen Future or Callable before, we will discuss them shortly.

<h5>ExecutorService methods:</h5>

| Method name    				  		 |    Description   									|
| :------------------------------------ | :--------------------------------------------------	|
| void execute(Runnable command)  		 | Executes a Runnable Task at some point in the future |
| Future<?> submit(Runnable task) 		 | Executes a Runnable task at some point in the future and returns a Future representing the task |
| <*T*> Future<*T*> submit(Callable<*T*> task) | Executes a Callable task at some point in the future and returns a Future representing the pending results of the task |
| <*T*> List<Future<*T*>> invokeAll(Collection<? extends Callable<*T*>> tasks) throws InterruptedException | Executes the given tasks and waits for all tasks to complete. Returns a List of Future instances, in the same order they were in the original collection |
| <*T*> T invokeAny(Collection<? extends Callable<*T*>> tasks) throws InterruptedException, ExecutionException  | Executes the given tasks and waits for at least one to complete. Returns a Future instance for a complete task and cancels any unfinished tasks |

The `execute()` and `submit()` methods are nearly identical when applied to Runnable expressions. The obvious advantage of `submit()` is that he does the same thing `execute()` does, but with a return object that can be used to track the result. Because of this advantage and the fact that `execute()` does not support Callable expressions, we tend to prefer `submit()` over execute, even if you don't store the Future reference.

For the exam, you need to be familiar with both `execute()` and `submit()`, but in your own code, it's recommended that you use `submit()` over `execute()` whenever possible.

### Waiting for Results

The java.util.concurrent.Future<*V*> instance returned by the `submit()` method can be used to know when a task submitted to an ExecutorService is complete.

	Future<?> future = service.submit(() -> System.out.println("Hello!"));

The Future type is actually an interface! For the exam, you don't need to know any of the classes that implement Future, just that a Future instance is returned by various API methods. The following table includes useful methods for determining the state of a task.

| Method name    				  		 		|    Description   									  |
| :-------------------------------------------- | :-------------------------------------------------- |
| boolean isDone() 								| Returns true if the task was completed, threw an exception, or was cancelled |
| boolean isCancelled() 						| Returns true if the task was cancelled before it completed normally |
| boolean cancel(boolean mayInterruptIfRunning) | Attempts to cancel execution of the task and returns true if it was successfully cancelled, or false if it could not be cancelled or is complete |
| V get() 										| Retrieves the result of a task, waiting endlessly if it is not yet available |
| V get(long timeout, TimeUnit unit) 			| Retrieves the result of a task, waiting the specified amount of time. If the result is not ready bu the time the timeout is reached, a checked TimeoutException will be thrown |

When the return type of tasks use Future<*V*> and Runnable methods, the type V is determined by the return type of the Runnable method. Since Runnable.run() is void, the get() always returns null when working with Runnable expressions.

The Future.get() method can take an optional value and enum type from java.util.concurrent.TimeUnit. Numerous methods in the Concurrency API use the TimeUnit enum. The following is a table including all of its values.

| Enum Name    				  		 		    |    Description   									  |
| :-------------------------------------------- | :-------------------------------------------------- |
| TimeUnit.NANOSECONDS							| Time in one-billionth of a second (1/1,000,000,000) |
| TimeUnit.MICROSECONDS							| Time in one-millionth of a second (1/1,000,000) 	  |
| TimeUnit.MILLISECONDS							| Time in one-thousandth of a second (1/1,000)		  |
| TimeUnit.SECONDS								| Time in seconds 									  |
| TimeUnit.MINUTES								| Time in minutes 									  |
| TimeUnit.HOURS								| Time in hours 									  |
| TimeUnit.DAYS									| Time in days 									  	  |

### Introducing *Callable*

The java.util.concurrent.Callable functional interface is similar to Runnable, except that its `call()` method returns a value and can throw a checked exception. The following is the definition of it:

	@FunctionalInterface public interface Callable<V> {
		V call() throws Exception;
	}

The Callable interface is often preferable over Runnable, since it allows more details to be retrieved easily from the task after it is completed. They are interchangeable in situations where the lambda does not throw an exception and there is no return type.

The ExecutorService includes an overloaded version of the `submit()` method that takes a Callable object and returns a generic Future<*T*> instance.

Unlike Runnable, in which the `get()` methods always return null, the `get()` methods on a Future instance return the matching generic type (which could also be a null value). Example using Callable:

	import java.util.concurrent.*;
	public class AddData {
		public static void main(String[] args) throws Exception {
			ExecutorService service = null;

			try {
				service = Executors.newSingleThreadExecutor();
				Future<Integer> result = service.submit(() -> 30 + 11);
				System.out.println(result.get()); // 41
			} finally {
				if (service != null) service.shutdown();
			}
		}
	}

This solution could have also been obtained using Runnable and some shared, possibly static, object. Although, this soulution that relies on Callable is a lot simpler and easier to follow.

### Waiting for All Tasks to Finish

After submitting a set of tasks to a thread executor, it is common to wait for the results. But if we don't need the results of the tasks and are finished using our thread executor, there is a simpler approach.

First, we `shutdown()` the thread executor. Then we use the `awaitTermination()` method (it is available for all thread executors). The method waits the specified time untill all tasks are finished, returning sooner if all tasks finish or an InterruptedException is detected. Following a code snippet using it:

	ExecutorService service = null;
	try {
		service = Executors.newSingleThreadExecutor();
		// Add tasks to the thread executor
	} finnaly {
		if (service != null) service.shutdown();
	}

	if (service != null) {
		service.awaitTermination(1, TimeUnit.MINUTES);

		// Check whether all tasks are finished
		if (service.isTerminated()) System.out.println("Finished!");  // We can call the isTerminated() after the awaitTermination() method finishes to confirm the status
		else System.out.println("At least one task is still running")"; 
	}

In this example, we submit a number of tasks to the thread executor, then shut down him and wait up to one minute for the results. 

> **Note:** If `awaitTermination()` is called before `shutdown()` within the same thread, then that thread will need to wait until the full timeout value sent with awaitTermination().

### Submiting Task Collections

The last two ExecutorService methods that you should know for the exam are `invokeAll()` and `invokeAny()`. Both of these methods execute **synchronously** and take a Collection of tasks. Remember that by synchronous, we mean that unlike the other methods used to submit tasks to a thread executor, these methods will wait until the results are available before returning control to the enclosing program.

The `invokeAll()` method executes all tasks in a provided collection and returns a List of ordered Future instances, with one Future instance corresponding to each submitted task, in the order they were in the original collection.

	Executor service = ...
	System.out.println("begin");
	Callable<String> task = () -> "result";
	List<Future<String>> list = service.invokeAll(List.of(task, task, task));
	for (Future<String> future : list) {
		System.out.println(future.get());
	}
	System.out.println("end");

In this example, the JVM waits the `invokeAll()` tasks to finish before moving on to the next line. Unlike our earlier examples, in this one the 'end' will be printed last. Another thing is that even with `future.isDone()` returning true for each element of the returned list, a task could have completed normally or thrown an exception.

The `invokeAny()` method executes a collection of tasks and returns the result of one of the tasks that successfully completes execution, **cancelling all unfinished tasks**. While the first task to finish is often returned, this behavior is not guaranteed, as any completed task can be returned by this method.

	Executor service = ...
	System.out.println("begin");
	Callable<String> task = () -> "result";
	String data = service.invokeAny(List.of(task, task, task));
	System.out.println(data);
	System.out.println("end");

Like in `invokeAll()`, the JVM waits on the `invokeAny()` for a completed task before moving on to the next line. Remember that all the other tasks that did not completed after a task is completed are cancelled.

For the exam, you'll need to remember that the `invokeAll()` will wait indefinitely until **all tasks are complete**, while the `invokeAny()` will wait indefinitely until **at least one task completes**.

The ExecutorService interface also includes overloaded versions of `invokeAll()` and `invokeAny()` that take a timeout value and TimeUnit parameter.

### Scheduling Tasks
 
If we need to schedule a task to happen at some future time, even if we need to schedule the task to happen repeatedly, at some set interval, we can use the `ScheduledExecutorService`, which is a subinterface of ExecutorService.

Like ExecutorService, we obtain an instance of ScheduledExecutorService using a factory method in the Executors class, as shown in the following code snippet:

	ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

We could store an instance of ScheduledExecutorService in an ExecutorService variable, but doing so would mean that we'd have to cast the object to call any scheduled methods. The following table is a summary of ScheduledExecutorService methods:

| Method Name    				  		 						|    Description   									  |
| :------------------------------------------------------------ | :-------------------------------------------------- |
| schedule(Callable<*V*> callable, long delay, TimeUnit unit)								| Creates and executes a Callable task after the given delay |
| schedule(Runnable command, long delay, TimeUnit unit)										| Creates and executes a Runnable task after the given delay |
| scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)		| Creates and executes a Runnable task after the given initial delay, creating a new task every period value that passes |
| scheduleWithFixedDelay(Runnable command, long initialDelay, long period, TimeUnit unit) 	| Creates and executes a Runnable task after the given initial delay and subsequently with the given delay between the termination of one execution and the commencement of the next |

These methods in practice are among the most convenient in the Concurrency API, as they perform relatively complex tasks with a single line of code. The delay and period parameters rely on the TimeUnit argument (ENUM) to determine the format of the value, such as seconds or milliseconds.

The `ScheduleFuture` interface is identical to the Future interface, except that it includes a `getDelay()` method that returns the remaining delay. The following are examples using the `schedule()` method with Callable and Runnable:

	ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	
	Runnable task1 = () -> System.out.println("Hello Zoo");
	Callable<String> task2 = () -> "Monkey";
	ScheduledFuture<?> r1 = service.schedule(task1, 10, TimeUnit.SECONDS);
	ScheduledFuture<?> r2 = service.schedule(task2, 8, TimeUnit.MINUTES);

Remember that this methods are about schedule, so the first task is scheduled 10 seconds in the future and the second is 8 minutes in the future. 

> **Note:** While these tasks are schedules in the future, the actual execution may be delayed. For example, there may be no threads available to perform the task, at which point they will just wait in the queue. Also, if the ScheduledExecutorService is shut down by the time the scheduled task execution time is reached, then these tasks will be discarded.

The `scheduleAtFixedRate()` method creates a new task and submits it to the executor every period, regardless of whether the previous task finished. The following example executes a Runnable task every minute, folowwing an initial five-minute delay:

	service.scheduleAtFixedRate(command, 5, 1, TimeUnit.MINUTES);

This method is useful for tasks that need to be run at specific intervals, such as checking an application's health.

> **Tip:** If each task consistently takes longer to run than the execution interval, bad things can happen with scheduleAtFixedRate(). Given enough time, the program can submit more tasks to the executor service than could fit in memory, causing the program to crash.

The `scheduleWithFixedDelay()` method creates a new task only after the previous task has finished. For example, if a task runs at 12:00 and takes five minuytes to finish, with a period between executions of two minutes, then the next task will start at 12:07. For example:

	service.scheduleWithFixedDelay(command, 0, 2, TimeUnite.MINUTES);

This method is useful for processes that you want to happen repeatedly but whose specific time is unimportant. 

### Increasing Concurrency with Pools 

All the content with the Concurrency API were implemented with single-thread executors, which, weren't particularly useful. After all, the chapter is about concurrency, and you can't do a lot of that with a single-thread executor.

In this section, it's presented three additional factory methods in the Executors class that act on a pool of threads, rather than on a single thread. A *thread pool* is a group of pre-instantiated reusable threads that are available to perform a set of arbitrary tasks. The next table, includes the two previous single-thread executor methods, along with the new ones that you should be familiar with for the exam:

| Method    				  		 		    				|    Description   									  |
| :------------------------------------------------------------ | :-------------------------------------------------- |
| ExecutorService newSingleThreadExecutor() 					| Creates a single-threaded executor that uses a single worker thread operating off an unbounded queue, the results are processed sequentially in the order in which they are submitted |
| ScheduledExecutorService newSingleThreadScheduledExecutor() 	| Creates a single-threaded executor that can schedule commands to run after a given delay or to execute periodically |
| ExecutorService newCachedThreadPool() 						| Creates a thread pool that creates new threads as needed, but will reuse previously consctructed threads when they are available |
| ExecutorService newFixedThreadPool(int) 						| Creates a thread pool that reuses a fixed number of threads operating off a shared unbounded queue |
| ScheduledExecutorService newScheduledThreadPool(int) 			| Creates a thread pool that can schedule commands to run after a given delay or to execute periodically |

These methods return the same instance types, ExecutorService and ScheduledExecutorService. In other words, all of our previous examples are compatible with these new pooled-thread executors.

The difference between a single-thread and a pooled-thread executor is what happens when a task is already running. While a single-thread executor will wait for a thread to become available before running the next task, a pooled-thread executor can execute the next task concurrently. If the pool runs out of available threads, the task will be queued by the thread executor and wait to be completed.

The `newCachedThreadPool()` method will create a thread pool of unbounded size, which allocates a new thread anytime one is required or all existing threads are busy. This is commonly used for pools that require executing many short-lived asynchronous tasks, but for long-lived processes, the usage of this executor is strongly discourage, as it could grow to encompass a large number of threads over the application life cycle.

The `newFixedThreadPool(int)` method takes a number of threads and allocates them all upon creation. As long as our number of tasks is less or equal than our number of threads, all tasks will be executed concurrently, if at any point the number of tasks exceeds the number of threads in the fixed pool, they will wait in the queue in a simmilar manner as we saw in a single-thread executor. Actually, calling `newFixedThreadPool(int)` with a value of **1** is equivalent to calling `newSingleThreadExecutor()`.

The `newScheduledThreadPool(int)` method is identical to the `newFixedThreadPool(int)` method, except that it returns an instance of ScheduledExecutorService, therefore is compatible with scheduling tasks. 

> **Real World Scenario:** When choosing an appropriate pool size, you want at least a handful more threads than you think you will ever possibly need, on the other hamd, you don't want to choose so many threads that your application uses up too many resources or to much CPU processing power. It is a common practice to allocate threads based on the number of CPUs. Oftentimes, the command used to determine the thread pool size with the number of CPUs available is this one: `Runtime.getRuntime.availableProcessors()`

## Writing Thread-Safe Code (p.861-876)

*Thread-safety* is the property of an object that guarantees safe execution by multiple threads at the same time. Since threads run in a shared environment and memory space, we must organize access to data to prevent two ore more threads interfering with each other, which if not handled can end up in invalid or unexpected results.

In this section of the chapter, we are going to discuss a variety of techniques to protect data, including: atomic classes, synchronized blocks, the Lock framework and cyclic barriers.

### Understanding Thread-safety

Imagine that we have a program in our zoo that counts sheep. Each zoo worker runs out to a field, adds a new sheep to the flock, counts the total number of sheep and runs back to us to report the results. The following code snippet is the conceptual representation of this example, choosing a thread pool size so that all tasks can be run concurrently: 

	import java.util.concurrent.*;
	public class SheepManager {
		private int sheepCount = 0;

		private void incrementAndReport() {
			System.out.println((++sheepCount)+" ");
		}

		public static void main(String[] args) {
			ExecutorService service = null;

			try {
				service = Executors.newFixedThreadPool(20);
				SheepManager manager = new SheepManager();

				for (int i = 0; i < 10; i++)
					service.submit(() -> manager.incrementAndReport()); 
			} finally {
				if (service != null) service.shutdown();
			} 
		}
	}

What does this program output? Reading it as a synchronous code, we think that it will output numbers from 1 to 10 in order, but that is far from guaranteed in this case. It may output in a different order, it may print duplicate numbers and even worse, not print some numbers at all! The following are some possible outputs of this program:

	1 2 3 4 5 6 7 8 9 10
	1 9 8 7 3 6 6 2 4 5
	1 8 7 3 2 6 5 4 2 8
	6 8 5 3 2 1 9 7 4 10

In this example we used the pre-increment (++) operator to update the sheepCount variable. A problem occurs when two threads both execute the right side of the expression, reading the "old" value before either thread writes the "new" value of the variable. In the end, the two assignments become redundant; they both assign the same new value, with two threads, assuming that sheepCount has a starting value of 1. Both threads read and write the same value, causing one of the two ++sheepCount operations to be lost. Therefore, the increment operator ++ is not thread-safe.

Later in the chapter, will se that the unexpected result of two tasks executing at the same time is referred to as *race condition*. The ideia is that some threads may be faster on their way to do the task but are slower on their way finishing it, on the other hand, others may be slower on their way to do the task, but somehow be the first ones finishing the task.

### Protecting Data with Atomic Classes

One way to improve our sheep counting example is to use the java.util.concurrent.atomic package. As with many of the classes in the Concurrency API, these classes exist to make our lifes easier.

As demonstrated in the previous section, the increment operator ++ is not thread-safe, but the reason that is not thread-safe is that the operation is not atomic, carrying out two tasks, read and write, that can be interrupted by other thread.

*Atomic* is the property of an operation to be carried out as a single unit of execution without any interference by another thread. A thread-safe atomic version of the increment operator would be one that performed the read and write of the variable as a single operation, not allowing any other threads to access the variable during the operation.

Implementing this concept in the sheepCount variable, any thread trying to access the sheepCount variable while an atomic operation is in process would need to wait until the atomic operation on the variable is complete. Conceptually, this is like setting a rule for our workers that there can be only one employee in the field at a time, although they may not each report their result in order.

The Concurrency API includes numerous useful classes that are conceptually the same as our primitive classes but that support atomic operations. The next table lists the atomic classes with which you should be familiar for the exam:

| Class Name    				  		 		| Description   									  |
| :-------------------------------------------- | :-------------------------------------------------- |
| AtomicBoolean									| A boolean value that may be updated atomically     |
| AtomicInteger									| An int value that may be updated atomically         |
| AtomicLong									| A long value that may be updated atomically         |

Each atomic class includes numerous methods that are equivalent to many of the primitive built-in operators that we use on primitives, such as the assignment operator (=) and the increment operators (++). In the following example, we update our SheepManager class with an AtomicInteger:

	private AtomicInteger sheepCount = new AtomicInteger(0);
	private void incrementAndReport() {
		System.out.print(sheepCount.incrementAndGet()+" ");
	}

With this implementation, we get some different outputs, the numbers 1 through 10 will always be printed, without duplicates or missing numbers, but the order is still not guaranteed. This last issue will be adressed shortly. The key in this section is that using the atomic classes ensures that the data is consistent between threads and that no values are lost due to concurrent modifications.

The following is a table that lists some common atomic methods:

| Method Name    				  		 		| Description   									  			             |
| :-------------------------------------------- | :------------------------------------------------------------------------- |
| get()											| Retrieves the current value						  			             |
| set()											| Sets the given value, equivalent to the assignment operator (=)            |
| getAndSet()									| Atomically sets the new value and returns the old value  			         |
| incrementAndGet()								| For numeric classes, atomic pre-increment operation equivalent to ++value  |
| getAndIncrement()								| For numeric classes, atomic post-increment operation equivalent to value++ |
| decrementAndGet()								| For numeric classes, atomic pre-decrement operation equivalent to --value  |
| getAndDecrement()								| For numeric classes, atomic post-decrement operation equivalent to value-- |

### Improving Access with Synchronized Blocks

Atomic classes are great at protecting single variables, but not if you need to execute a series of commands or call a method. For that, you may use a monitor, also called a *lock*, which is commonly used to synchronize access. A *monitor* is a structure that supports *mutual exclusion*, which is the property that at most one thread is executing a particular segment of code at a given time. In Java, any **Object** can be used as a monitor, along with the `synchronized` keyword, as shown in the following example:

	SheepManager manager = new SheepManager();
	synchronized(manager) {
		// Work to be completed by one thread at a time
	}

This example is referred to as a *synchronized block*. Each thread that arrives will first check if any threads are in the block. A thread "acquires the lock" for the monitor, if the lock is available, one thread will enter the block and acquire the lock, preventing all other threads from entering. While the thread is executing the block, the other threads that arrive will attempt to acquire the same lock and wait for the first thread to finish. Once a thread finishes executing the block, it will release the lock, allowing one of the waiting threads to proceed.

> **Note:** To synchronize access across multiple threads, each thread must have access to the same object. For example, synchronizing on different objects wouldn't actually order the results.

We can revisit our SheepManager example and try to improve the results. What if we replaced the our for() loop with the following:

	for(int i = 0; i < 10; i++) {
		synchronized(manager) {
			service.submit(() -> manager.incrementAndReport()); 
		}
	}

Does this solution fix the problem? No, it doesn't. We've *synchronized* the *creation* of the threads, but not the *execution* of the threads. They would be created one at a time, but they may all still execute and perform their work at the same time, resulting in the same outputs presented earlier. Threading problems are often the most difficult to diagnose and resolve in any programming language.

The following code is the corrected version of the SheepManager class, which does order the workers correctly:

	import java.util.concurrent.*;

	public class SyncSheepManager {
		private int sheepCount = 0;

		public void incrementAndReport() {
			synchronized(this) {
				System.out.println((++sheepCount)+" ");
			}
		}
	
		public static void main(String[] args) {
			ExecutorService service = null;
			try {
				service = Executors.newFixedThreadPool(20);
				SyncSheepManager manager = new SyncSheepManager();
				for (int i = 0; i < 10; i++)
					service.submit(() -> manager.incrementAndReport()); 
			} finally {
				if (service != null) service.shutdown();
			} 
		}
	}

This code will consistently output from 1 to 10 in order. Although all threads are still created and executed at the same time, they each wait at the synchronized block for the worker to increment and report the result before entering. While it's random which thread will enter the block next, it is guaranteed that there will be at most one on the block and that the results will be reported in order. 

We could have synchronized on any object, so long as it was the same object. For example, this would work too:

	private final Object herd = new Object();
	private void incrementAndReport() {
		synchronized(herd) {
			System.out.println((++sheepCount)+" ");
		}
	}

We didn't need to make the herd variable **final**, doing so ensures that it is not reassinged after threads start using it.

> **Note:** We could have used an atomic variable along with the synchronized block in this example, although it is unnecessary. Since synchronized blocks allow only one thread to enter at a time, we are not gaining any improvement by using an atomic variable if the only time that we access the variable is within a synchronized block.

### Synchronizing on Methods

On our previous example, we established our monitor using synchronized(this) around the method's body. Java actually provides a more convenient compiler enhancement for doing so. We can add the synchronized modifier to any instance method to synchronize automatically on the object itself. For example, the following two method definitions are equivalent: 

	private void incrementAndReport() {
		synchronized(this) {
			System.out.println((++sheepCount)+" ");
		}
	}

	private synchronized void incrementAndReport() {
		System.out.println((++sheepCount)+" ");
	}
	
The first uses a *synchronized block* and the second uses the *synchronized method modifier*. Which one to use is completely up to you. 

We can also apply the synchronized modifier to static methods. When using it on static methods, the object used as the monitor is the class object. For example, the following two methods are equivalent for static synchronization inside our SheepManager class:

	public static void printDaysWork() {
		synchronized(SheepManager.class) { // USES THE CLASS OBJECT AS THE MONITOR.
			System.out.print("Finished work!");
		}
	}

	public static synchronized void printDaysWork() {
		System.out.print("Finished work!");
	}

As before, the first uses a synchronized block, with the second example using the synchronized modifier. You can use static synchronization if you need to order thread access across all instances, rather than a single instance.

#### Avoid Synchronization Whenever Possible

Correctly using the synchronized keyword can be quite challenging, especially if the data you are trying to protect is available to dozens of methods. Even when the data is protected, though, the performance cost for using it can be high.

There are many classes within the Concurrency API that are a lot easier to use and more performant than synchronization. Some were already presented, like the atomic classes, and other will be covered shortly, including the Lock framework, concurrent collections and cyclic barriers.

You may not be familiar with all of the classes in the Concurrency API, you should study them carefully if you are writing a lot of multithreaded applications. They contain a wealth of methods that manage complex processes for you in a thread-safe and performant manner.

### Understanding the *Lock* Framework

The Concurrency API includes the Lock interface that is conceptually similar to using the synchronized keyword. Instead of synchronizing on any Object, we can "lock" only on an object that implements the Lock interface.

#### Applying a *ReentrantLock* Interface

When you need to protect a piece of code from multithreaded processing, create an instance of Lock that all threads have access to. Each thread then calls lock() before it enters the protected code and calls unlock() before it exits the protected code. The following example shows two implementations, one with a synchronized block and the other with a Lock instance:

	Object object = new Object();
	synchronized(object) {
		// Protected code
	}

	Lock lock = new ReentrantLock();
	try {
		lock.lock(); 
	} finnaly {
		lock.unlock();
	}

> **Note:** Altough similar, the Lock solution has a number of features not available to the synchronized block. While certainly not required, it is a good practice to use a try/finnaly block with Lock instances. This ensures any acquired locks are properly released.

These two implementations are conceptually equivalent. The ReentrantLock class is a simple monitor that implements the Lock interface and supports mutual exclusion. In other words, at most one thread is allowed to hold a lock at any given time.

The ReentrantLock class ensures that once a thread has called lock() and obtained the lock, all the other threads that call lock() will wait until the first thread calls unlock(). Besides making sure to release a lock, you also need to make sure that you only release a lock that it's actually locked. If you attempt to release a lock that you do not have, an exception will be thrown at runtime:

	Lock lock = new ReentrantLock();
	lock.unlock(); // throws IllegalMonitorStateException at runtime

> **Note:** The ReentrantLock class contains a constructor that can be used to send a boolean "fairness" parameter. If set to true, then the lock will usually be granted to each thread in the order it was requested. It is false by default when using the no-argument constructor. In practice, you should enable fairness only when ordering is absolutely required, as it could lead to a significant slowdown.

The Lock interface includes four methods that you should know for the exam:

| Method	    				  		 		| Description   									  			             |
| :-------------------------------------------- | :------------------------------------------------------------------------- |
| void lock() 									| Requests a lock and blocks until lock is acquired 						 |
| void unlock() 								| Releases a lock  |
| boolean tryLock() 							| Requests a lock and and returns immediately a boolean indicating whether the lock was successfully acquired  |
| boolean tryLock(long, TimeUnit) 				| Requests a lock and blocks up to the specified time until lock is required, it returns a boolean indicating whether the lock was successfully acquired  | 

#### Attempting to Acquire a Lock

While the ReentrantLock class allows you to wait for a lock, it so far suffers from the same problem as synchronized blocks, a thread could end up waiting forever to obtain a lock. But as shown in the table above, there are two additional methods that make the Lock interface a lot safer than a synchronized block.

The `tryLock()` method will try to obtain a lock and immediatly return a boolean result indicating whether the lock was obtained. Unlike the `lock()` method, it does not wait if a thread already holds the lock. It returns immediatly, regardless of whether or not a lock is available. The following is an example of the `tryLock()` method implementation:

	Lock lock = new ReentrantLock();
	new Thread(() -> printMessage(lock)).start();
	if (lock.tryLock()) {
		try {
			System.out.println("Locked"); // Lock obtained
		} finally {
			lock.unlock(); // Unlock for other threads
		}
	} else {
		System.out.println("Something else"); // Unable to acquired lock
	}

This code could produce either outputs, depending on the order of execution. Like `lock()`, the `tryLock()` method should be used with a try/finally block. Fortunately, with `tryLock()` you need to release the lock only if it was successfully acquired.

The `tryLock(long, TimeUnit)` method is an overloaded version of the `tryLock()` method, it acts like an hybrid of `lock()` and `tryLock()`. Like the other two methods, if a lock is available, then it will immediately return with it. If a lock is unavailable, it will wait up to the specified time limit for the lock. The following example uses the overloaded version of `tryLock(long, TimeUnit)`:

	Lock lock = new ReentrantLock();
	new Thread(() -> printMessage(lock)).start();
	if (lock.tryLock(10, TimeUnit.SECONDS)) {
		try {
			System.out.println("Lock obtained"); // Lock obtained
		} finally {
			lock.unlock();
		}
	} else {
		System.out.println("Something else"); // Unable to acquired lock
	}

The code is the same as before, except this time on of the threads waits up to 10 seconds to acquire the lock.

#### Duplicate Lock Requests

The ReentrantLock class maintains a counter of the number of times a lock has been given to a thread. To release the lock for other threads to use, `unlock()` must be called the same number of times the lock was granted. The following code snippet contains an error, because of duplicate lock requests: 

		Lock lock = new ReentrantLock();
		if (lock.tryLock()) {
			try {
				lock.lock();
			} finally {
				lock.unlock();
			}
		}

The thread obtains the lock twice but releases it only once. You can verify this by spawning a new thread after this code runs that attempts to obtain a lock. The following prints false:

	new Thread(() -> System.out.println(lock.tryLock())).start();

It's critical that you release a lock the same number of times it is acquired. For calls with `tryLock()`, you need to call `unlock()` only if the method returned true.

#### Reviewing the Lock Framework

To review, the ReentrantLock class supports the same features as a synchronized block, while adding a number of improvements.

- Ability to request a lock without blocking
- Ability to request a lock while blocking for a specified amount of time
- A lock can be created with a fairness property, in which the lock is granted to threads in the order it was requested.

The Concurrency API includes other lock-based classes, although ReentrantLock is the only one you need to know for the exam.

> **Tip:** While not on the exam, ReentrantReadWriteLock is a really useful class, it includes separate locks for reading and writing data and is useful on data structures where reads are far more common than writes. For example, if you have a thousand threads reading data but only one thread writing data, this class can help you maximize concurrent access.

### Orchestrating Tasks with a *CyclicBarrier*

We complete our discussion on thread-safety by discussing how to orchestrate complex tasks across many things.

We have some tasks that need to be done and those need to be done concurrently, we can use the **CyclicBarrier** class to coordinate these tasks. For now, let's start with a code sample without a CyclicBarrier:

	import java.util.concurrent.*;
	public class LionPenManager {
		private void removeLions() { System.out.println("Removing lions"); }
		private void cleanPen() { System.out.println("Cleaning the pen"); }
		private void addLions() { System.out.println("Adding lions"); }
		public void performTask() {
			removeLions();
			cleanPen();
			addLions();
		}
		public static void main(String... args) {
			ExecutorService service = null;
			try {
				service = Executors.newFixedThreadPool(4);
				var manager = new LionPenManager();
				for (int i = 0; i < 4; i++) 
					service.submit(() -> manager.performTask());
			} finally {
				if (service != null) service.shutdown();
			}
		}
	}

The following is sample output based on this implementation:

	Removing lions
	Removing lions
	Cleaning the pen
	Removing lions
	Cleaning the pen
	Adding lions
	Removing lions
	Cleaning the pen
	Adding lions
	Adding lions
	Cleaning the pen
	Adding lions

Although within a single thread the results are ordered, among multiple works the output is entirely random. We can improve these results by using the CyclicBarrier class. This class takes in its constructors a limit value, indicating the number of threads to wait for. As each thread finishes, it calls the `await()` method on the cyclic barrier. Once the specified number of threads have each called `await()`, the barrier is released and all threads can continue. The following is a reimplementation of our LionPenManager class that uses CyclicBarrier objects to coordinate access:

	import java.util.concurrent.*;
	public class LionPenManager {
		private void removeLions() { System.out.println("Removing lions"); }
		private void cleanPen() { System.out.println("Cleaning the pen"); }
		private void addLions() { System.out.println("Adding lions"); }
		public void performTask(CyclicBarrier cb1, CyclicBarrier cb2) {
			try {
			removeLions();
			cb1.await();
			cleanPen();
			cb2.await();
			addLions();
			} catch (InterruptedException | BrokenBarrierException e) {
				// Handle checked exceptions here
			}
		}
		public static void main(String... args) {
			ExecutorService service = null;
			try {
				service = Executors.newFixedThreadPool(4);
				var manager = new LionPenManager();
				var cb1 = new CyclicBarrier(4);
				var cb2 = new CyclicBarrier(4, () -> System.out.println("*** Pen Cleaned!"));
				for (int i = 0; i < 4; i++) 
					service.submit(() -> manager.performTask(c1, c2));
			} finally {
				if (service != null) service.shutdown();
			}
		}
	}

In this example, the updated `performTask()` method uses CyclicBarrier objects. Like synchronizing on the same object, coordinating a task with a CyclicBarrier requires the object to be static or passed to the thread performing the task. We also add a try/catch block in the `performTask()` method, as the `await()` method throws multiple checked exceptions. The following is sample output based on this revised implementation:

	Removing lions
	Removing lions
	Removing lions
	Removing lions
	Cleaning the pen
	Cleaning the pen
	Cleaning the pen
	Cleaning the pen
	*** Pen Cleaned!
	Adding lions
	Adding lions
	Adding lions
	Adding lions

As you can see, all of the results are now organized. In this example, two different constructors for our CyclicBarrier objects were used, the latter of which called a Runnable method upon completion.

The CyclicBarrier class allows us to perform complex, multithreaded tasks, while all threads stop and wait at logical barriers. This solution is superior to a single-threaded solution, as the individual tasks such as removing the lion, can be completed in parallel by all threads. There is a slight loss in performance to be expected from using a CyclicBarrier. For example, one worker may be incredibly slow at removing lions, resulting in the other three workers waiting for him to finish, since we can't start cleaning the pen while it is full of lions, though, this solution is about as concurrent as we can make it. 

About thread pool size and the cyclic barrier limit, make sure that you set the number of available threads to be at least as large as your CyclicBarrier limit value. For example, if we changed the code in the previous exaxmple to allocate only two threads (Executors.newFixedThreadPool(2)), the code would hang indefinitely. The barrier would never be reached as the only threads available in the pool are stuck waiting for the barrier to be complete. This would result in a *deadlock*.

We may reuse our CyclicBarrier. After a CyclicBarrier is broken, all threads are released and the number of threads waiting on the CyclicBarrier goes back to zero. At this point, the CyclicBarrier may be used again for a new set of waiting threads. For example, if our CyclicBarrier limit is 5 and we have 15 threads that call `await()`, then the CyclicBarrier will be activated a total of three times.

## Using Concurrent Collections (p.876-883)

The Concurrency API also includes interfaces and classes that help you coordinate access to collections shared by multiple tasks. By collections, we are of course referring to the Java Collections Framework. This section demonstrates many of the concurrent classes available to you when using the Concurrency API.

### Understanding Memory Consistency Errors

The purpose of concurrent collection classes is to solve common memory consistency errors. A *memory consistency error* occurs when two threads have inconsistent views of what should be the same data. They were created to avoid common issues in which multiple threads are adding and removing objects from the same collections. 

When two threads try to modify the same nonconcurrent collection, the JVM may throw a **ConcurrentModificationException** at runtime. In fact, it can happen with a single thread. For example, take a look at the following code snippet:

	var foodData = new HashMap<String, Integer>();
	foodData.put("penguin", 1);
	foodData.put("flamingo", 2);
	for (String key: foodData.keySet()) 
		foodData.remove(key);

After the first iteration of the loop, this code will throw a **ConcurrentModificationException**, since the iterator on `keySet()` is not properly updated after the first element is removed. Changing the first line to a **ConcurrentHashMap** will prevent from throwing the exception at runtime:

	var foodData = new ConcurrentHashMap<String, Integer>();
	foodData.put("penguin", 1);
	foodData.put("flamingo", 2);
	for (String key: foodData.keySet()) 
		foodData.remove(key);

In this example the ConcurrentHashMap is ordering read and write access, such that all access to the class is consistent. The iterator created by `keySet()` is updated as soon as an object is removed from the Map. At any given instance, all threads should have the same consistent view of the structure of the collection.

### Working with Concurrent Classes

You should use a concurrent collection class anytime you are going to have multiple threads modify a collections object outside a synchronized block or method, even if you don't expect a concurrency problem. On the other hand, immutable or read-only objects can be accessed by any number of threads without a concurrent collection.

> **Note:** Immutable objects can be accessed by any number of threads and do not require synchronization. By definition, they don't change, so there is no chance of a memory consistency error.

As we do when instantiating an ArrayList object but passing a List reference, it's considered a good practice to instantiate a concurrent collection but pass it around using a nonconcurrent interface whenever possible. In some cases the callers need to know that is a concurrent collection, but in the majority of circumstances, that distinction is not necessary.

The following table lists the common concurrent classes with which you should be familiar with for the exam:

| Class name	| Java Collections Framework interfaces   | Elements Ordered?   | Sorted?   | Blocking?  |
| :------------ | :-------------------------------------- | :------------ 		| :-------- | :--------- | 
| ConcurrentHashMap		| ConcurrentMap							 | No			| No		| No		 | 
| ConcurrentLinkedQueue	| Queue									 | Yes			| No		| No		 |
| ConcurrentSkipListMap	| ConcurrentMap, SortedMap, NavigableMap | Yes			| Yes		| No		 |
| ConcurrentSkipListSet	| SortedSet, NavigableSet				 | Yes			| Yes		| No		 |
| CopyOnWriteArrayList	| List									 | Yes			| No		| No		 |
| CopyOnWriteArraySet	| Set									 | No			| No		| No		 |
| LinkedBlockingQueue	| BlockingQueue							 | Yes			| No		| Yes		 |

Some of the most common classes listed above are ConcurrentHashMap and ConcurrentLinkedQueue, we often use an interface reference for the variable type of the newly created object and use it the same way as we would a nonconcurrent object. The main difference is that these objects are safe to pass to multiple threads. For example:

	Map<String, Integer> map = new ConcurrentHashMap<>();
	map.put("zebra", 52);
	map.put("elephant", 10);
	System.out.println(map.get("elephant")); // 10

	Queue<Integer> queue = new ConcurrentLinkedQueue<>();
	queue.offer(31);
	System.out.println(queue.peek()); // 31
	System.out.println(queue.poll()); // 31

All of these classes implement multiple interfaces, for example, ConcurrentHashMap implements Map and ConcurrentMap. So it's up to us to decide which is the appropriate method parameter type. 

#### Understanding *SkipList* Collections

The SkipList classes, `ConcurrentSkipListMap` and `ConcurrentSkipListSet`, are concurrent versions of their sorted counterparts, `TreeMap` and `TreeSet`. They maintain their elements or keys in the natural ordering of their elements, in this manner, working with them is the same as working with their counterparts:

	Set<String> gardenAnimals = new ConcurrentSkipListSet<>();
	gardenAnimals.add("rabbit");
	gardenAnimals.add("gopher");
	System.out.println(gardenAnimals.stream().collect(Collectors.joining(","))); // gopher, rabbit

	Map<String, String> rainForestAnimalDiet = new ConcurrentSkipListMap<>();
	rainForestAnimalDiet.put("koala", "bamboo")
	rainForestAnimalDiet.entrySet().stream().forEach(e -> System.out.println(e.getKey() + "-" + e.getValue())); // koala-bamboo

When you see any of these two on the exam, just remember that they are "sorted" concurrent collections.

#### Understanding *CopyOnWrite* Collections

The CopyOnWrite classes, `CopyOnWriteArrayList` and `CopyOnWriteArraySet`, are a little different than the other examples that we've seen. These classes copy all of their elements to a new underlying structure anytime an element is added, modified or removed from the collection. By *modified* element, it's meant that the reference in the collection is changed, so changing the contents of objects within the collection will not cause a new structure to be allocated.

Although the data is copied to a new underlying structure, our reference to te Collection object doesn't change. Any iterator established prior to a modification will not see the changes, but instead it will iterate over the original elements prior to the modification.

> **Note:** The CopyOnWrite classes are similar to the immutable object pattern, as a new underlyin structure is created every time the collection is modified. Unlike a true immutable object, the reference to the object stays the same even while the underlying data is changed.

To demonstrate a CopyOnWriteArrayList usage, we have the following code snippet:

	List<Integer> favNumbers = new CopyOnWriteArrayList<>(List.of(4, 3, 42));
	for(var n: favNumbers) {
		System.out.print(n + " ");
		favNumbers.add(9);
	}
	System.out.println();
	System.out.println("Size: " + favNumbers.size()); 
	// This code snippet outputs: 
	// 4 3 42
	// Size: 6
	
Despite adding elements to the array while iterating over it, the for loop only iterated on the ones created when the loop started. If we had used a regular ArrayList object, then a ConcurrentModificationException would have been thrown at runtime.

The CopyOnWriteArraySet is used just like a HashSet and has similar properties as the CopyOnWriteArrayList class. For example:

	Set<Character> favLetters = new CopyOnWriteArraySet<>(List.of('a', 't'));
	for(char c: favLetters) {
		System.out.print(c + " ");
		favLetters.add('s');
	}
	System.out.println();
	System.out.println("Size: " + favNumbers.size()); 
	// This code snippet outputs: 
	// a t
	// Size: 3

The same ideia as with the CopyOnWriteArrayList, except that we don't have duplicates of an element.

The CopyOnWrite classes can use a lot of memory, since a new collection structure needs to be allocated anytime the collection is modified. They are most used in multi-threaded environment situtions where reads are far more common than writes.

##### Revisiting Deleting While Looping

As shown before, when we try to delete from an ArrayList while iterating over it, a ConcurrentModificationException is thrown. Here is presented a version that works using CopyOnWriteArrayList:

	List<String> birds = new CopyOnWriteArrayList<>();
	birds.add("hawk");
	birds.add("hawk");
	birds.add("hawk");
	for (String bird: birds) birds.remove(bird);
	System.out.print(birds.size()); // 0

As mentioned, CopyOnWrite classes can use a lot of memory. Another approach would be to use the ArrayList class with an iterator, as shown next:

	var iterator = birds.iterator();
	while(iterator.hasNext()) {
		iterator.next();
		iterator.remove();
	}
	System.out.print(birds.size()); // 0

#### Understanding Blocking Queues

The last collection class that was listed on the table that you should know for the exam is the `LinkedBlockingQueue`, which implements the BlockingQueue interface. The BlockingQueue is just like a regular Queue, except that it includes methods that will wait a specific amount of time to complete an operation.

The new methods included in BlockingQueue, apart from those inherited from Queue, are:

- offer(E e, long timeout, TimeUnit unit): Adds an item to the queue, waiting the specified time and returning false if the time elapses before space is available.
- poll(long timeout, TimeUnit unit): Retrieves and removes an item from the queue, waiting the specified time and returning null if the time elapses before the item is available.

The implementation class LinkedBlockingQueue, maintains a linked list between elements. The following example is using a LinkedBlockingQueue to wait for the results of some of the operations, the methods presented before (from BlockingQueue) can each throw a checked InterruptedException, as they can be interrupted before they finish waiting for a result (they must be properly caught).

	try {
		var blockingQueue = new LinkedBlockingQueue<Integer>();
		blockingQueue.offer(39);
		blockingQueue.offer(3, 4, TimeUnit.SECONDS);
		System.out.println(blockingQueue.poll()); // 39
		System.out.println(blockingQueue.poll(10, TimeUnit.MILLISECONDS)); // 3
	} catch (InterruptedException e) {
		// Handle interruption
	}

As shown in this example, since LinkedBlockingQueue implements both Queue and BlockingQueue, we can use methods available to both.

#### Obtaining Synchronized Collections

Besides the concurrent collections covered in this chapter, the Concurrent API also includes methods for obtaining synchronized versions of existing nonconcurrent collection objects. These synchronized methods are defined in the Collections class. They operate on the inputted and return a reference that is the same type as the underlying collection. The methods are the following:

- synchronizedCollection(Collection<*T*> c)
- synchronizedList(List<*T*> list)
- synchronizedMap(Map<*K,V*> m)
- synchronizedNavigableMap(NavigableMap<*K,V*> m)
- synchronizedNavigableSet(NavigableSet<*T*> s)
- synchronizedSet(Set<*T*> s)
- synchronizedSortedMap(SortedMap<*K,V*> m)
- synchronizedSortedSet(SortedSet<*T*> s)

If you are given an existing collection that is not a concurrent class and need to access it among multiple threads, then you could wrapt it using the methods listed above. Otherwise, if you know at the time of creation that your object requires synchronization, then you should use one of the concurrent collection classes listed before.

Unlike the concurrent collections, the synchronized collections also throw an exception if they are modified within an iterator by a single thread. For example:

	var foodData = new HashMap<String, Object>();
	foodData.put("penguin", 1);
	foodData.put("flamingo", 2);
	var syncFoodData = Collections.synchronizedMap(foodData);
	for(String key: synFoodData.keySet()) 
		synFoodData.remove(key);

This loop throws a ConcurrentModificationException, whereas our example that used ConcurrentHashMap did not. Other than iterating over the collection, the synchronized methods listed above return objects that are safe from memory consistency errors and can be used among multiple threads.  

## Identifying Threading Problems (p.883-888)

A threading problem can occur when two or more threads interact in an unexpected and undesirable way. For example, two threads may block each other from accessing a particular segment of code or access a variable in a concurrent manner.

The Concurrency API was created to help eliminate potential threading issues common to all devs. As we saw earlier, the Concurrency API creates and manages threads for you. But even with the usage of the Concurrency API, which reduces potencial threading issues, we can still have some issues.

### Understanding Liveness

As we saw earlier in the chapter, many thread operations can be performed independently, but some require coordination. For example, synchronizing on a method requires all the threads that call the method to wait for other threads to finish before continuing, or using a CyclicBarrier, where each thread inside of it must wait for the barrier limit to be reached before continuing.

In many of the cases, the waiting of the threads is so quick that the user has very little idea that any delay has ocurred, but there are some cases where the waiting can be extremely long and sometimes even infinite.

**Liveness** is the ability of an application to be able to execute in a timely manner. Liveness problems are those in which the application becomes unresponsive or in some cases in some kind of stuck state. For the exam, there are three types of liveness issues that you should be familiar with: deadlock, starvation and livelock.

### Deadlock

**Deadlock** happens when two or more threads threads are blocked forever, each waiting on the other.

An ilustration of deadlock is the following one for example. Imagine that our zoo has two foxes: Foxy and Tails. Foxy likes to eat first and then drink water, while Tails like to drink water first and then eat. Furthermore, neither animal likes to share, and they will finish their meal only if they have exclusive access to both food and water. Then, the zookeeper places the food on one side of the environment and the water on the other side. Although our foxes are fast, it still takes them 100 milliseconds to run from one side of the environment to the other. If Foxy obtains the food and then moves to the other side of the environment to obtain the water, but Tails has already drank the water and is waiting the food to become available, the result would be them hanging indefinitely (coded example `Fox.java`). Basically this deadlock is caused because both participants are permanently blocked, waiting on resources that will never become available.

How do you fix a deadlock? The answer is that you can't in most situations, what you can do is prevent them from happening. There are numerous strategies to help prevent deadlocks from happening. One common strategy to avoid them is for all threads to order their resource requests. For example, if both foxes have a rule that they need to obtain food before water, then the previous deadlock scenario will not happen again, because once one of the foxes obatined food, the second fox would wait, leaving the water resource available.

### Starvation

**Starvation** occurs when a single thread is perpetually denied access to a shared resource or lock. The thread is still active, but it is unable to complete its work as a result of other threads constantly taking the resource that they are trying to access.

Using the fox example again, imagine that we have a pack of very hungry and very competitive foxes in our environment. Every time Foxy stands up to go get food, one of the other foxes sees her and rushes to eat before her. Foxy is free to roam around the enclosure and do other things, but is never able to obtain access to the food. In this example, Foxy literally and figuratively experiences starvation.

### Livelock

**Livelock** occurs when two or more threads are conceptually blocked forever, although they are each still active and trying to complete their task. Livelock is a special case of resource starvation in which two or more threads actively try to acquire a set of locks, are unable to do so, and restart part of the process. Is often a result of two threads trying to resolve a deadlock.

Returning to our example, imagine that Foxy and Tails are both holding their food and water resources, respectively. They each realize that they cannot finish their meal in this state, so they both let go of their food and water, run to opposite side of the environment and pick up the other resource. Now Foxy has the water, Tails has the food, and neither is able to finish their meal. If they continue this process forever, it is referred to as livelock. Both are active, trying to restart the process, but neither is able to finnish their task. They are executing a form of failed deadlock recovery, where each one notices that they are potentially entering a deadlock state and responds by releasing all of its locked resources. Unfortunately, the lock and unlock process is cyclical, and the two foxes are conceptually deadlocked.

In practive, livelock is often a difficult issue to detect. Threads in this state appear to be active and able to respond to requests, even when they are in fact stuck in an endless cycle.

### Managing Race Conditions

A **race condition** is an undesirable result that occurs when two tasks, which should be completed sequentially, are completed at the same time. Earlier when we introduced synchronization, we discussed examples of race conditions.

For example, if two users try to create an account at the same time with the same username, we can have three outcomes for this race condition:

- Both users are able to create accounts with the same username.
- Both users are unable to create accounts with the same username, returning an error message to both users.
- One user is able to create the account with username, while the other user receives an error message.

Looking at all the three possible outcomes, the first one can be considered the worst of them all, because when one of the users log in to the site, the application will return an error because it cannot tell them apart (duplicate users). The other two are far more acceptable than the first, because those two are not leading to invalid data being persisted.

> **Note:** For the third scenario, it often doesn't matter which user gain access to the account, but is a common practice to choose whichever thread made the request first, whenever possible.
