package gui;

import javax.swing.JPanel;

public class DeviceQueueView extends JPanel {

	private static final long serialVersionUID = 8068052131837157540L;
	String name;

	public DeviceQueueView(String name) {
		super();
		this.name = name;

		var queue = new ProcessQueue();
		add(queue);
	}
}
