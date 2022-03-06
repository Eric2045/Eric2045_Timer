import static org.junit.Assert.*;
import org.junit.Test;
import application.Main;
import application.timerFx;
import javafx.application.Application;

/**
 * Unittest Voor de timerFx en Main classes, voor beide heb ik een wrapper Thread gebruikt, er wordt 10 sec gewacht zodat de applicatie gedurende 10 secondes 
 * geopend en getest wordt. Met thread.interrupt(), wordt de wrapper thread stopt gezet en de applicatie stopt. 
 *
 * 
 * @author Eric
 */
public class javafxTimerTest {
	public boolean succes = false;
	
	@Test
	public void testSceneMain() throws InterruptedException {
		timerFx tfx = new timerFx();
		Thread trd = new Thread() {
			@Override
			public void run() {
				try {
					Application.launch(timerFx.class);
					succes = true;				
				} catch (Throwable e) {
					if (e.getCause() != null && e.getCause().getClass().equals(InterruptedException.class)) {
			 				succes = true;
							return;
					}									
				}
			}
		};		
		trd.start();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException ex) {}
		 trd.interrupt();	
		 trd.join(1);	
		 assertTrue(succes);	
	}
	
	
	
	/*@Test 
	public void testMain() throws InterruptedException {
		Main mainn = new Main();
		Thread trd = new Thread() {
			@Override
			public void run() {
				try {
					mainn.main(null);
					succes = true;
				} catch (Throwable e) {
					if (e.getCause() != null && e.getCause().getClass().equals(InterruptedException.class)) {
		 				succes = true;
						return;
					}									
				}
			}
		};
		trd.start();
		try {
			Thread.sleep(9000);
		} catch (InterruptedException ex) {}
		trd.interrupt();
		trd.join(1);
		assertTrue(succes);
	}*/
		
	
}
