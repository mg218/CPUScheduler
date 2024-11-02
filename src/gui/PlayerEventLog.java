package gui;

import cpuscheduler.ProcessEvent;

import java.awt.Font;
import java.io.*;
import javax.swing.JTextArea;

public class PlayerEventLog extends JTextArea {
	private static final long serialVersionUID = -5464549856362389332L;

	// simple JTextField with support for printing ProcessEvent Objects
	// and saving the whole log to a filepath
	public PlayerEventLog() {
		super();
		setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
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

	public void saveLog(String filePath) throws IOException {
		// open the file and write this object's text to it
		FileWriter writer = new FileWriter(filePath);
		writer.write(this.getText());
		writer.close();
	}
}
