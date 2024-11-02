package gui;

import java.awt.*;
import java.util.List;
import javax.swing.*;

import cpuscheduler.PCB;

public class ProcessQueue extends JPanel {
	private static final long serialVersionUID = 1934427411074442587L;

	private int processCount = 0;

	//displays a List of PCB objects using ProcessViews, designed to be placed inside a JSCrollPane
	public ProcessQueue() {
		super();
		setPreferredSize(new Dimension(100, 64));
		setLayout(new GridBagLayout());
	}

	public ProcessQueue(List<PCB> processes) {
		this();
		displayList(processes);
	}

	//display a new PPCB list
	public void displayList(List<PCB> processes) {
		if (processes == null)
			return;

		// get rid of all added components
		removeAll();

		// add new components
		int i = 0;
		for (PCB p : processes) {
			var constraints = new GridBagConstraints();
			constraints.gridx = 0;
			constraints.gridy = i++;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.fill = GridBagConstraints.NONE;
			constraints.weightx = 1.0;
			constraints.weighty = 1.0;
			add(new ProcessView(p), constraints);
		}
		processCount = processes.size();

		// revalidate and repaint so the gui is redrawn
		revalidate();
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		// Calculate based on the actual content
		int height = processCount * 64;
		return new Dimension(100, height); // Set width and calculated height
	}
}
