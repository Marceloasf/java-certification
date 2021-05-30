package ocp.chapter.eighteen;

import java.util.concurrent.*;

// Non thread-safe code example, discussed about on line 518 section.
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
