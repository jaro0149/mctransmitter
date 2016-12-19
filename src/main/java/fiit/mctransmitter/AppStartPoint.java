package fiit.mctransmitter;

import java.awt.EventQueue;

public class AppStartPoint {
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			if(args.length==0) {
				Controller mainWindow = new Controller("");
				mainWindow.setVisible(true);
			} else {
				Controller mainWindow = new Controller(args[0]);
				mainWindow.setVisible(true);
			}
		});
	}
}
