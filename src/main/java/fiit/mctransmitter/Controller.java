package fiit.mctransmitter;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Controller {
	
	private Window window;
	private final AtomicBoolean transmitterState = new AtomicBoolean(false);
	private final AtomicReference<PcapMachine> actualPcapMachine = new AtomicReference<>();
	
	public Controller(String titleAddon) {
		window = new Window(titleAddon);
		window.addStartStopListener(e -> {
			try {
				if(transmitterState.get()==false) {
					String multicastAddress = window.getMulticastAddress();	
					String interval = window.getInterval();
					String text = window.getText();							
					actualPcapMachine.set(new PcapMachine(window,multicastAddress,interval,text));
					actualPcapMachine.get().start();
					transmitterState.set(true);
				} else {
					actualPcapMachine.get().stop();
					transmitterState.set(false);
				}
				window.switchButtonText();
			} catch(Exception exception) {
				new ErrorMessage(exception.getMessage());
			}			
		});
	}
	
	public void setVisible(boolean visible) {
		window.setVisible(visible);
	}
	
}
