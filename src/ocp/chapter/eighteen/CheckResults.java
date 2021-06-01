package ocp.chapter.eighteen;

import java.util.concurrent.*;

// Using ExecutorService methods (submit()) to do polling on a class.
// Thus example is the essence of the Concurrency API: to do complex things with threads without having to manage them directly.
// Non thread-safe example, but its using a single-thread executor.
public class CheckResults {

    private static int counter = 0;

    public static void main(String[] unused) throws Exception {
        ExecutorService service = null;

        try {
            service = Executors.newSingleThreadExecutor();
            Future<?> result = service.submit(() -> {
                for(int i = 0; i < 500; i++) {
                    CheckResults.counter++;
                    System.out.println(CheckResults.counter);
                };
            });
            result.get(10, TimeUnit.SECONDS); // This result is async, so if it takes more than 10 seconds, it'll throw the exception, but as we
                                              // are catching her, the submit task will continue until 500 and it will never print "Reached!".
            System.out.println("Reached!");
        } catch (TimeoutException e) {
            System.out.println("Not reached in time");
        } finally {
            if(service != null) service.shutdown();
        }
    }
}