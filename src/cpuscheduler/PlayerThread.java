package cpuscheduler;

import cpuscheduler.ProcessEvent.type;
import gui.PlayerEventLog;
import java.io.*;
import java.util.*;

public class PlayerThread extends Thread {

	private SchedulingAlgorithm sched;
	private int RRQuantum = 3;
	private int speedMS = 1000;
	private boolean paused = true;
	private boolean done = true;

	private final PlayerEventLog log;

	public List<PCB> allProcesses;

	public PlayerThread(PlayerEventLog eventLog) {
		super();

		log = eventLog;
	}

	public void setScheduler(String sa) {
		pause();
		// create copy of processes list to prevent scheduler getting reference to ours
		if (allProcesses != null) {
			var allProcs = new ArrayList<PCB>(allProcesses);
			// turn the string sa into a constructor of the respective scheduler
			switch (sa) {
			case "FCFS" -> sched = new FCFS(allProcs);
			case "PS" -> sched = new PSS(allProcs);
			case "RR" -> sched = new RR(allProcs, RRQuantum);
			default -> throw new IllegalArgumentException("Invalid scheduler");
			}

			// this is our reset so we need to also clear the event log and reset the player
			// if in play
			log.clear();
			done = allProcesses.isEmpty();
		}
	}

	public void setRRQuantum(int t) throws IllegalArgumentException {
		if (t < 0) {
			throw new IllegalArgumentException("quantum time cannot be negative");
		}

		RRQuantum = t;
		setScheduler("RR");
	}

	public void setSpeed(int s) throws IllegalArgumentException {
		if (s < 0) {
			throw new IllegalArgumentException("Speed cannot be negative");
		}

		speedMS = s;
	}

	@Override
	public synchronized void run() {
		while (true) {
			try {
				do {
					if (paused || done) {
						wait();
					} else {
						wait((long) speedMS);
					}
				} while (paused || done);
			} catch (InterruptedException ex) {
				System.err.println("Interrupted");
			}

			logEvents(sched.nextB());
		}
	}

	private synchronized void resume() {
		notify();
	}

	public void play() {
		paused = false;

		switch (this.getState()) {
		case WAITING -> resume();
		case NEW -> {
			if (allProcesses != null) {
				start();
			}
		}
		case TIMED_WAITING, RUNNABLE -> {
		} // do nothing
		default -> throw new IllegalArgumentException("Unexpected value: " + this.getState());
		}

	}

	public void pause() {
		paused = true;
	}

	public void step() {
		if (!done && paused) {
			logEvents(sched.nextB());
		}
	}

	public void loadProcessesFile(String filePath) throws FileNotFoundException {
		allProcesses = new ArrayList<>();

		try (var sc = new Scanner(new File(filePath))) {
			int id = 0;
			String line;
			while (sc.hasNextLine()) {
				line = sc.nextLine();
				String[] arr = line.split(",\\s+");
				String name = arr[0];
				int arrtime = Integer.parseInt(arr[1]);
				int priority = Integer.parseInt(arr[2]);
				int[] cpuBurst = new int[arr.length - 3];// create an array for CPU/IO Bursts which can differ based on
				// length of CPU bursts/IO bursts
				for (int i = 3; i < arr.length; i++) {
					cpuBurst[i - 3] = Integer.parseInt(arr[i]);
				}
				PCB proc = new PCB(name, id++, arrtime, cpuBurst, priority);
				allProcesses.add(proc);
			}
		}
	}

	private void logEvents(List<ProcessEvent> events) {
		log.addEvent("System time: " + sched.systemTime);
		for (var event : events) {

			if (event.event == type.DONE) {
				done = true;
			} else {
				log.addEvent(event);
			}
		}
	}

}
