package fiit.mctransmitter;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class Window extends JFrame {

	private static final String TITLE = "Multicast Transmitter";
	private static final String DEFAULT_ADDRESS = "233.10.47.10";
	private static final String DEFAULT_INTERVAL = "5000";
	private static final String DEFAULT_TEXT = "FIIT STU";
	private static final int HEIGHT = 600;
	private static final int WIDTH = 900;	
	
	// Labels
	private static final String[] LABELS = {"Multicast address:","Interval [msec]:","Text:","Debugging window:"};
	private final JLabel lblMutlicastAddress = new JLabel(LABELS[0]);
	private final JLabel lblInterval = new JLabel(LABELS[1]);
	private final JLabel lblText = new JLabel(LABELS[2]);
	private final JLabel lblDebuggingWindow = new JLabel(LABELS[3]);
	
	// Text fields
	private final JTextField txtMulticastAddress = new JTextField(DEFAULT_ADDRESS);
	private final JTextField txtInterval = new JTextField(DEFAULT_INTERVAL);
	private final JTextField txtText = new JTextField(DEFAULT_TEXT);
	
	// Button
	private static final String[] BUTTONS = {"Start","Stop"};
	private final JButton btnStartStop = new JButton(BUTTONS[0]);
	
	// Separator
	private final JSeparator textSeparator = new JSeparator(SwingConstants.HORIZONTAL);
	
	// Text area
	private final JTextArea areaDebugging = new JTextArea();
	
	public Window(String titleAddon) {
		
		// Configuration
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setResizable(true);
		setTitle(TITLE + " " + titleAddon);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, 
				dim.height / 2 - this.getSize().height / 2);
		
		// Layout
		GridBagLayout gridLayout = new GridBagLayout();
		gridLayout.columnWidths = new int[] {20, 80, 200, 80, 80, 20 };
		gridLayout.columnWeights = new double[] {3.0, 1.0, 1.0, 1.0, 1.0, 3.0 };
		setLayout(gridLayout);
		
		// Labels
		lblMutlicastAddress.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblInterval.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblText.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblDebuggingWindow.setFont(new Font("Times New Roman", Font.BOLD, 16));

		add(lblMutlicastAddress, new GridBagTemplate(GridBagConstraints.WEST, 0, 1, 10, 10, 10, 10, 1));
		add(lblInterval, new GridBagTemplate(GridBagConstraints.WEST, 0, 3, 10, 10, 10, 10, 1));
		add(lblText, new GridBagTemplate(GridBagConstraints.WEST, 1, 1, 10, 10, 10, 10, 1));
		add(lblDebuggingWindow, new GridBagTemplate(GridBagConstraints.WEST, 3, 1, 10, 10, 10, 10, 4));
		
		// Text fields
		txtMulticastAddress.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		txtInterval.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		txtText.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		
		txtMulticastAddress.setHorizontalAlignment(SwingConstants.CENTER);
		txtInterval.setHorizontalAlignment(SwingConstants.CENTER);
		txtText.setHorizontalAlignment(SwingConstants.CENTER);
		
		add(txtMulticastAddress, new GridBagTemplate(GridBagConstraints.WEST, 0, 2, 10, 10, 10, 10, 1));
		add(txtInterval, new GridBagTemplate(GridBagConstraints.WEST, 0, 4, 10, 10, 10, 10, 1));
		add(txtText, new GridBagTemplate(GridBagConstraints.WEST, 1, 2, 10, 10, 10, 10, 1));
		
		// Buttons
		btnStartStop.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		add(btnStartStop, new GridBagTemplate(GridBagConstraints.WEST, 1, 3, 10, 10, 10, 10, 2));
		
		// Separator
		add(textSeparator, new GridBagTemplate(GridBagConstraints.WEST, 2, 1, 10, 10, 10, 10, 4));
		
		// Text area
		areaDebugging.setFont(new Font("Courier New", Font.PLAIN, 14));
		areaDebugging.setEditable(true);
		JScrollPane scrollDebuggingWindow = new JScrollPane(areaDebugging);
		add(scrollDebuggingWindow, new GridBagTemplate(GridBagConstraints.WEST, 4, 1, 10, 10, 20, 10, 4, 1.0));
		
	}
	
	public void addStartStopListener(ActionListener listener) {
		btnStartStop.addActionListener(listener);
	}
	
	public String getMulticastAddress() {
		return txtMulticastAddress.getText().trim();
	}
	
	public String getInterval() {
		return txtInterval.getText().trim();
	}
	
	public String getText() {
		return txtText.getText();
	}
	
	public void addLineToArea(String text) {
		SwingUtilities.invokeLater(() -> {
			areaDebugging.append(text);
			areaDebugging.append("\n");
		});		
	}
	
	public void switchButtonText() {
		SwingUtilities.invokeLater(() -> {
			if(btnStartStop.getText().equals(BUTTONS[0])) {
				btnStartStop.setText(BUTTONS[1]);
			} else {
				btnStartStop.setText(BUTTONS[0]);
			}
		});
	}
	
}
