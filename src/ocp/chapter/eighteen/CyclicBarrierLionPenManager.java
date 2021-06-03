package ocp.chapter.eighteen;
	
import java.util.concurrent.*;

// Thread-safe example, it contains CyclicBarrier, discussed about from line 804.
public class CyclicBarrierLionPenManager {
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
			e.printStackTrace();
		}
	}
	public static void main(String... args) {
		ExecutorService service = null;
		try {
			service = Executors.newFixedThreadPool(4);
			var manager = new CyclicBarrierLionPenManager();
			var cb1 = new CyclicBarrier(4);
			var cb2 = new CyclicBarrier(4, () -> System.out.println("*** Pen Cleaned!"));
			for (int i = 0; i < 4; i++) 
				service.submit(() -> manager.performTask(cb1, cb2));
		} finally {
			if (service != null) service.shutdown();
		}
	}
}