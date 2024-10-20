package gui;

import cpuscheduler.PCB;
import cpuscheduler.PlayerThread;
import cpuscheduler.ProcessEvent;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;

public class CPUPlayer extends JFrame {

	private static final long serialVersionUID = 1L;
	private JMenuBar menuBar;
	private JMenu mnQuantum;

	private PlayerThread player;
	private String scheduler = "FCFS";
	private int speed = 1000;
  private int RRQuantum = 3;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				var gui = new CPUPlayer();
				gui.setVisible(true);
				Thread.sleep(1L);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
		});
	}

	public CPUPlayer() {
		super();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new GridBagLayout());

		player = new PlayerThread();
		player.setScheduler(scheduler);
		player.setSpeed(speed);
		player.setRRQuantum(RRQuantum);

		initMenuBar();
		initControls();
		initEventLog();
		initQueues();

		setMinimumSize(new DimensionUIResource(500, 300));
	}

	private void initMenuBar() {
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// File menu
		var mnFile = new JMenu("File");
		menuBar.add(mnFile);

		var mntmNewSecnarioFile = new JMenuItem("Open scenario file");
		mntmNewSecnarioFile.addActionListener((ActionEvent e) -> {
			var filePath = doFileDialog(System.getProperty("user.home"));
			System.out.println("opening file: " + filePath);
			try {
				player = new PlayerThread(filePath, scheduler, speed, RRQuantum);
			} catch (FileNotFoundException ex) {
				System.err.println(ex.getMessage());
			}
			player.setScheduler(scheduler);
		});
		mnFile.add(mntmNewSecnarioFile);

		// settings menu
		var mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		
		// Algorithm subMenu
		var mnSchedule = new JMenu("Scheduler Algorithm");
		mnSettings.add(mnSchedule);

		var scheduleListener = (ActionListener) (ActionEvent e) -> {
			setSchedulerAlgorithm((JRadioButtonMenuItem) e.getSource());
		};

		var rdbtnFCFS = new JRadioButtonMenuItem("First Come First Server");
		rdbtnFCFS.addActionListener(scheduleListener);
		rdbtnFCFS.setName("FCFS");
		rdbtnFCFS.setSelected(true);
		mnSchedule.add(rdbtnFCFS);

		var rdbtnRR = new JRadioButtonMenuItem("Round Robin");
		rdbtnRR.addActionListener(scheduleListener);
		rdbtnRR.setName("RR");
		mnSchedule.add(rdbtnRR);

		var rdbtnPS = new JRadioButtonMenuItem("Priority Scheduling");
		rdbtnPS.addActionListener(scheduleListener);
		rdbtnPS.setName("PS");
		mnSchedule.add(rdbtnPS);

		var scheduleGroup = new ButtonGroup();
		scheduleGroup.add(rdbtnFCFS);
		scheduleGroup.add(rdbtnRR);
		scheduleGroup.add(rdbtnPS);

		// Simulation Time subMenu
		var mnStepTime = new JMenu("Simulation Time(ms)");
		mnSettings.add(mnStepTime);

		var StepListenerCustom = (ActionListener) (ActionEvent e) -> {
			var speedStr = doInputDialog("Step Speed in milliseconds");
			try {
				var speedInt = Integer.parseInt(speedStr);
				setSimulationSpeed(speedInt);
			} catch (NumberFormatException _e) {
				JOptionPane.showMessageDialog(this, "speed must be a number", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (IllegalArgumentException iae) {
				JOptionPane.showMessageDialog(this, iae.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		};

		var mntmStepCustom = new JMenuItem("Custom...");
		mntmStepCustom.addActionListener(StepListenerCustom);
		mnStepTime.add(mntmStepCustom);

		var mntmStep1000 = new JMenuItem("1000 ms");
		mntmStep1000.addActionListener((ActionEvent e) -> {
                    setSimulationSpeed(1000);
                });
		mnStepTime.add(mntmStep1000);

		var mntmStep100 = new JMenuItem("100 ms");
		mntmStep100.addActionListener((ActionEvent e) -> {
                    setSimulationSpeed(100);
                });
		mnStepTime.add(mntmStep100);

		// Quantum Time subMenu
		mnQuantum = new JMenu("Quantum Time(ms)");
		mnSettings.add(mnQuantum);

		var QuantumListenerCustom = (ActionListener) (ActionEvent e) -> {
			var timeStr = doInputDialog("Quantum time in milliseconds\n(Round Robin Only)");
			try {
				var time = Integer.parseInt(timeStr);
				setQuantumTime(time);
			} catch (NumberFormatException _e) {
				JOptionPane.showMessageDialog(this, "Quantum time must be a number", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (IllegalArgumentException iae) {
				JOptionPane.showMessageDialog(this, iae.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		};

		var mntmQuantumCustom = new JMenuItem("Custom...");
		mntmQuantumCustom.addActionListener(QuantumListenerCustom);
		mnQuantum.add(mntmQuantumCustom);
		mnQuantum.setVisible(rdbtnRR.isSelected());
	}

	private void initControls() {
		var controlsListener = (ActionListener) (ActionEvent e) -> {
			controlsAction((JButton) e.getSource());
		};
		var controls = new PlayerControls(controlsListener);
		var constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 4;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = .5;
		constraints.weighty = Double.MIN_VALUE;
		add(controls, constraints);
	}

	private void initEventLog() {
		var eventLog = new PlayerEventLog();
		var constraints = new GridBagConstraints();
		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 2;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 1;
		add(eventLog, constraints);

		eventLog.addEvent(new ProcessEvent(new PCB("James", 69, 3, new int[] {}, 3), ProcessEvent.type.CPUQUEUE));
	}

	private void initQueues() {
		var CpuQueue = new DeviceQueueView("CPU");
		var CpuConstraints = new GridBagConstraints();
		CpuConstraints.gridx = 0;
		CpuConstraints.gridy = 0;
		CpuConstraints.gridwidth = 1;
		CpuConstraints.gridheight = 2;
		CpuConstraints.fill = GridBagConstraints.BOTH;
		CpuConstraints.weightx = 1;
		CpuConstraints.weighty = 1;
		add(CpuQueue, CpuConstraints);
		var IoQueue = new DeviceQueueView("IO");
		var IoConstraints = new GridBagConstraints();
		IoConstraints.gridx = 1;
		IoConstraints.gridy = 0;
		IoConstraints.gridwidth = 1;
		IoConstraints.gridheight = 2;
		IoConstraints.fill = GridBagConstraints.BOTH;
		IoConstraints.weightx = 1;
		IoConstraints.weighty = 1;
		add(IoQueue, IoConstraints);
	}

	protected void setSchedulerAlgorithm(JRadioButtonMenuItem source) {
		scheduler = source.getName();

		switch (scheduler) {
		case "FCFS", "PS" -> mnQuantum.setVisible(false);
		case "RR" -> mnQuantum.setVisible(true);
		}

		player.setScheduler(scheduler);
	}

	protected void setSimulationSpeed(int speed) {
		this.speed = speed;
		player.setSpeed(this.speed);
	}

	protected void setQuantumTime(int time) {
		this.RRQuantum = time;
		player.setRRQuantum(this.RRQuantum);
	}

	protected void controlsAction(JButton button) {
		switch (button.getName()) {
		case "play" -> player.play();
		case "pause" -> player.pause();
		case "step" -> player.step();
		default -> throw new UnsupportedOperationException("Bad Controls input");
		}
	}

	private String doInputDialog(String message) {
		return (String) JOptionPane.showInputDialog(this, message, "");
	}

	private String doFileDialog(String dir) {
		var fd = new FileDialog(this, "Choose a file", FileDialog.LOAD);
		fd.setDirectory(dir);
		fd.setFile("*.txt");
		fd.setVisible(true);
		String filename = fd.getFile();
		if (filename == null) {
			System.out.println("You cancelled the choice");
			return "";
		}

		return fd.getDirectory() + filename;
	}
}
