package fiit.mctransmitter;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class ErrorMessage extends JOptionPane {

	public ErrorMessage(String text) {
		showMessageDialog(getRootPane(), text, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
}
