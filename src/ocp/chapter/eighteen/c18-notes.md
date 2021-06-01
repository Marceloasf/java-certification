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

> **Note:** Although not required knowledge for the exam, a *daemon thread* is one that will not prevent the JVM from exiting when the program finishes, a Java application terminates when the only threads that are running are daemon threads. For example, if garbage collection is the only thread left running, the JVM will automatically shut down. Both system and user defined threads can be marked as daemon threads.

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
	
Just by delaying on the end of the loop, we have now prevented a possibly infinite loop from executing and locking up our program. Notice that we also changed the signature of the main() method, since `Thread.sleep()` throws the **checked exception InterruptedException**. Alternatively, we could have wrapped the call to `Thread.sleep()` method in a try/catch block.

But now that we changed the implementation, how many times does the while() loop execute in this class? Still unknown! While polling does prevent the CPU from being overwhelmed with a potentially infinite loop, it does not guarantee when the loop will terminate. For example, the separate thread could be losing CPU time to a higher-priority process, resulting in multiple executions of the while() loop before it finishes.

Another issue is the shared counter variable. What if one thread is reading the counter variable while another thread is writing it? The thread reading the shared variable may end up with an invalid or incorrect value. We will discuss these issues in detail in the upcoming section on writing thread-safe code.

## Creating Threads with the Concurrency API (p.849-861)

Java includes the Concurrency API to handle the complicated work of managing threads for you. This API includes the ExecutorService interface, which defines services that create and manage threads for you. It is recommended that you use this framework anytime you need to create and execute a separate task, even if you need only a single thread.

### Introducing the Single-Thread Executor

To get an instance of the interface ExecutorService, you'll use the Concurrency API, which includes the Executors factory class and that can be used to create instances of the ExecutorService object. 

> **Note:** As you may rember from previous chaptes, the factory pattern is a creational pattern in which the underlying implementation details of the object creation are hidden from us. 

This is a simple example using the `newSingleThreadExecutor()` method to obtain an ExecutorService instance and the `execute()` method to perform asynchronous tasks.

	import java.util.concurrent.*;
	public class ZooInfo {
		public static void main(String[] args) {
			ExecutorService service = null;

			Runnable task1 = () -> System.out.println("Printing zoo inventory");
			Runnable task2 = () -> { for(int i = 0; i < 3; i++)
				 		System.out.println("Printing record: "+i);};
		
			try {
				service = Executors.newSingleThreadExecutor(); // Calls factory class to get an instance of ExecutorService (single-thread)
				service.execute("task1");
				service.execute("task2");
				service.execute("task1");
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

You can submit tasks to an ExecutorService instance many ways. The first that was presented, using the `execute()` method, is inherited from the Executor interface, which ExecutorService interface extends. The `execute()` method takes a Runnable lambnda expression or instance and completes the task asynchronously. Because the return type of the method is void, it does not tell us anything about the result of the task. 

> **Note:** The `execute()` method is considered a "fire-and-forget" method, as once it's submitted, the results are not directly available to the calling thread.

But fortunately, we have the `submit()` methods on the ExecutorService interface too, which like `execute()`, can be used to complete tasks asynchronously. Unlike `execute()`, `submit()` returns a **Future** instance that can be used to determine whether the task is complete. It can also be used to return a generic result object after the task has been completed.

> **Note:** Don't worry if you haven't seen Future or Callable before, we will discuss them shortly.

<h5>ExecutorService methods:</h5>

| Method name    				  		 |    Description   									|
| :------------------------------------ | :--------------------------------------------------	|
| void execute(Runnable command)  		 | Executes a Runnable Task at some point in the future |
| Future<?> submit(Runnable task) 		 | Executes a Runnable task at some point in the future and returns a Future representing the task |
| <*T*> Future<*T*> submit(Callable<*T*> task) | Executes a Callable task at some point in the future and returns a Future representing the pending results of the task |
| <*T*> List<Future<*T*>> invokeAll(Collection<? extends Callable<*T*>> tasks) throws InterruptedException | Executes the given tasks and waits for all tasks to complete. Returns a List of Future instances, in the same order they were in the original collection |
| <*T*> T invokeAll(Collection<? extends Callable<*T*>> tasks) throws InterruptedException, ExecutionException  | Executes the given tasks and waits for at least one to complete. Returns a Future instance for a complete task and cancels any unfinished tasks |

The `execute()` and `submit()` methods are nearly identical when applied to Runnable expressions. The obvious advantage of `submit()` is that he does the same thing `execute()` does, but with a return object that can be used to track the result. Because of this advantage and the fact that `execute()` does not support Callable expressions, we tend to prefer `submit()` over execute, even if you don't store the Future reference.

For the exam, you need to be familiar with both `execute()` and `submit()`, but in your own code, its recommended that you use `execute()` over `submit()` whenever possible.

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

The `invokeAny()` method executes a collection of tasks and returns the result of one of the tasks that successfully completes execution, **cancelling all unfinished tasks**. While the first task to finish is often retunred, this behavior is not guaranteed, as any completed task can be returned by this method.

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

We could store an instance of ScheduledExecutorService in an ExecutorService variable, but doing so would mean that we'd have to cast tyhe object to call any scheduled methods. The following table is a summary of ScheduledExecutorService methods:

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

All the content with the Concurrency API were implemented with signle-thread executors, which, weren't particularly useful. After all, the chapter is about concurrency, and you can't do a lot of that with a single-thread executor.

In this section, its presented three additional factory methods in the Executors class that act on a pool of threads, rather than on a single thread. A *thread pool* is a group of pre-instantiated reusable threads that are available to perform a set of arbitrary tasks. The next table, includes the two previous single-thread executor methods, along with the new ones that you should be familiar with for the exam:

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

As demonstrated in the previous section, the increment operator ++ is not thread-safe, but the resason that is not thread-safe is that the operation is not atomic, carrying out two tasks, read and write, that can be interrupted by other thread.

*Atomic* is the property of an operation to be carried out as a single unit of execution without any interference by another thread. A thread-safe atomic version of the increment operator would be one that performed the read and write of the variable as a single operation, not allowing any other threads to access the variable during the operation.

Implementing this concept in the sheepCount variable, any thread trying to access the sheepCount variable while an atomic operation is in process would need to wait until the atomic operation on the variable is complete. Conceptually, this is like setting a rule for our workers that there can be only one employee in the field at a time, although they may not each report their result in order.

The Concurrency API inclides numerous useful classes that are conceptually the same as our primitive classes but that support atomic operations. The next table lists the atomic classes with which you should be familiar for the exam:

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

Atomic classes are great at protecting single variables, but not if you need to execute a series of commands or call a method. For that, you may use a monitor, also called a *lock*, which is commonly used to synchronize access. A *monitor* is a structure that supports *mutual exclusion*, which is the property that at most one thread is executing a particular segment of code at a given time. In Java, any **Object** can be used as a monitor, along with the `synchronized` keyword, as hown in the following example:

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

Correctly using the synchronized keyword can be quite challenging, especially if the data you are trying to protect is available to dozens of methods. Even when the data is protected, though, the perforance cost for using it can be high.

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