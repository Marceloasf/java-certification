package ocp.chapter.eighteen;

import java.util.concurrent.*;

// Thread-safe code example using the synchronized block, discussed on line 595 further.
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
