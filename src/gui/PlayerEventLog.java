package gui;

import cpuscheduler.ProcessEvent;
import javax.swing.JTextArea;

public class PlayerEventLog extends JTextArea {
	private static final long serialVersionUID = -5464549856362389332L;

	public PlayerEventLog() {
		super();
		setEditable(false);
		setLineWrap(true);
		setWrapStyleWord(true);
	}

	public void addEvent(ProcessEvent event) {
		this.addEvent(event.toString());
	}

	public void addEvent(String event) {
		this.append(event + "\n");
		this.setCaretPosition(this.getDocument().getLength());
	}

	public void clear() {
		super.setText("");
	}
}
