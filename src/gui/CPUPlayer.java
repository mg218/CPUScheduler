package gui;

import cpuscheduler.PlayerThread;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;

public class CPUPlayer extends JFrame {

	private static final long serialVersionUID = 1L;

	private JMenuBar menuBar;
	private JMenu mnQuantum;
	private PlayerControls controls;
	private PlayerEventLog eventLog;
	private StatusBar statusBar;
	private ProcessTableModel processTableModel;

	private final PlayerThread player;
	private String scheduler = "FCFS";
	private int speed = 1000;
	private int RRQuantum = 3;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				var gui = new CPUPlayer();
				gui.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public CPUPlayer() {
		super("CPU Scheduler");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new GridBagLayout());

		initMenuBar();
		initControls();
		initEventLog();
		initStatusBar();
		initProcessTable();

		player = new PlayerThread(eventLog, statusBar, processTableModel);
		player.setSpeed(speed);
		player.setRRQuantum(RRQuantum);
		player.setScheduler(scheduler);

		initQueues();

		setMinimumSize(new DimensionUIResource(500, 400));
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
			try {
				player.loadProcessesFile(filePath);
			} catch (FileNotFoundException ex) {
				System.err.println(ex.getMessage());
			}
			// reset scheduler with new processes
			player.setScheduler(scheduler);
			eventLog.addEvent("Scenario file loaded: " + filePath);
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
				JOptionPane.showMessageDialog(this, "Quantum time must be a number", "Error",
						JOptionPane.ERROR_MESSAGE);
			} catch (IllegalArgumentException iae) {
				JOptionPane.showMessageDialog(this, iae.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		};

		var mntmQuantumCustom = new JMenuItem("Custom...");
		mntmQuantumCustom.addActionListener(QuantumListenerCustom);
		mnQuantum.add(mntmQuantumCustom);
		mnQuantum.setVisible(rdbtnRR.isSelected());

		// help menu
		var mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		var mntmAbout = new JMenuItem("About");
		var aboutListener = (ActionListener) (ActionEvent e) -> {
			JOptionPane.showMessageDialog(this, "Made By:\nJoshua Miertschin\nMason Goss", "About" , JOptionPane.INFORMATION_MESSAGE);
		};
		mntmAbout.addActionListener(aboutListener);
		mnHelp.add(mntmAbout);
	}

	private void initControls() {
		var controlsListener = (ActionListener) (ActionEvent e) -> {
			controlsAction((JButton) e.getSource());
		};
		controls = new PlayerControls(controlsListener);
		var constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0.0;
		constraints.weighty = Double.MIN_VALUE;
		add(controls, constraints);
	}

	private void initEventLog() {
		eventLog = new PlayerEventLog();
		var constraints = new GridBagConstraints();
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 2;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		add(new JScrollPane(eventLog), constraints);
	}

	private void initQueues() {
		var CpuQueue = new DeviceQueueView(player, 1, 1);
		var CpuConstraints = new GridBagConstraints();
		CpuConstraints.gridx = 0;
		CpuConstraints.gridy = 1;
		CpuConstraints.gridwidth = 2;
		CpuConstraints.gridheight = 2;
		CpuConstraints.fill = GridBagConstraints.BOTH;
		CpuConstraints.weightx = 0.6;
		CpuConstraints.weighty = 1.0;
		add(CpuQueue, CpuConstraints);
	}

	private void initStatusBar() {
		statusBar = new StatusBar();
		var statusConstraints = new GridBagConstraints();
		statusConstraints.gridx = 0;
		statusConstraints.gridy = 0;
		statusConstraints.gridwidth = 3;
		statusConstraints.gridheight = 1;
		statusConstraints.fill = GridBagConstraints.BOTH;
		statusConstraints.weightx = 0.3;
		statusConstraints.weighty = Double.MIN_VALUE;
		add(statusBar, statusConstraints);
	}

	private void initProcessTable() {
		processTableModel = new ProcessTableModel();

		var processTable = new JTable(processTableModel);

		var processTableConstraints = new GridBagConstraints();
		processTableConstraints.gridx = 0;
		processTableConstraints.gridy = 3;
		processTableConstraints.gridwidth = 3;
		processTableConstraints.gridheight = 1;
		processTableConstraints.fill = GridBagConstraints.BOTH;
		processTableConstraints.weightx = 1.0;
		processTableConstraints.weighty = 0.7;
		add(new JScrollPane(processTable), processTableConstraints);
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
