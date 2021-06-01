package ocp.chapter.eighteen;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

// Thread-safe code example using Atomic classes, discussed about from line 556.
public class AtomicSheepManager {
	private AtomicInteger sheepCount = new AtomicInteger(0);

	private void incrementAndReport() {
		System.out.println(sheepCount.incrementAndGet() + " ");
	}
    
	public static void main(String[] args) {
		ExecutorService service = null;
		try {
			service = Executors.newFixedThreadPool(20);
			AtomicSheepManager manager = new AtomicSheepManager();
			for (int i = 0; i < 10; i++)
				service.submit(() -> manager.incrementAndReport()); 
		} finally {
			if (service != null) service.shutdown();
		} 
	}
}