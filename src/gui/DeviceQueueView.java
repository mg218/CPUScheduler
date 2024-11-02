package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import cpuscheduler.PlayerThread;

public class DeviceQueueView extends JPanel implements ActionListener {

	private static final long serialVersionUID = 8068052131837157540L;

	private static final int REFRESH_MS = 25;
	private PlayerThread player;
	private ProcessView[] devices;
	private ProcessQueue[] queues;

	public DeviceQueueView(PlayerThread p, int CPUs, int IOs) {
		super();
		player = p;

		setLayout(new GridBagLayout());

		setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		devices = new ProcessView[CPUs + IOs];
		queues = new ProcessQueue[CPUs + IOs];

		setupDeviceQueue("CPU", 0, 0);
		setupDeviceQueue("IO", 0, 1);

		setPreferredSize(new Dimension((CPUs + IOs) * 150, 400));

		var timer = new Timer(REFRESH_MS, this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		devices[0].setProcess(player.getCurProcess("CPU"));
		queues[0].displayList(player.getCurQueue("CPU"));

		devices[1].setProcess(player.getCurProcess("IO"));
		queues[1].displayList(player.getCurQueue("IO"));
	}

	private void setupDeviceQueue(String name, int number, int xOffset) {
		var title = new JLabel(name + Integer.toString(number));
		title.setFont(this.getFont());
		title.setHorizontalAlignment(SwingConstants.CENTER);
		var titleConstraints = new GridBagConstraints();
		titleConstraints.gridx = number + xOffset;
		titleConstraints.gridy = 0;
		titleConstraints.gridwidth = 1;
		titleConstraints.gridheight = 1;
		titleConstraints.fill = GridBagConstraints.NONE;
		titleConstraints.weightx = 1.0;
		titleConstraints.weighty = 0.1;
		add(title, titleConstraints);

		devices[number + xOffset] = new ProcessView();
		var deviceConstraints = new GridBagConstraints();
		deviceConstraints.gridx = number + xOffset;
		deviceConstraints.gridy = 1;
		deviceConstraints.gridwidth = 1;
		deviceConstraints.gridheight = 1;
		deviceConstraints.fill = GridBagConstraints.NONE;
		deviceConstraints.weightx = 1.0;
		deviceConstraints.weighty = 0.3;
		add(devices[number + xOffset], deviceConstraints);

		queues[number + xOffset] = new ProcessQueue();
		var queueConstraints = new GridBagConstraints();
		queueConstraints.gridx = number + xOffset;
		queueConstraints.gridy = 2;
		queueConstraints.gridwidth = 1;
		queueConstraints.gridheight = 3;
		queueConstraints.fill = GridBagConstraints.BOTH;
		queueConstraints.weightx = 1.0;
		queueConstraints.weighty = 1.0;

		var scrollPane = new JScrollPane(queues[number + xOffset], JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(200, 192));

		add(scrollPane, queueConstraints);
	}
}
