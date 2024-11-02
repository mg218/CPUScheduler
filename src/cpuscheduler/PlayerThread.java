package cpuscheduler;

import cpuscheduler.ProcessEvent.type;
import gui.*;

import java.io.*;
import java.util.*;

public class PlayerThread extends Thread {

	private SchedulingAlgorithm sched;
	private int RRQuantum = 3;
	private int speedMS = 1000;
	private boolean paused = true;
	private boolean done = true;
	private boolean newData = true;

	private final PlayerEventLog log;
	private final StatusBar status;
	private final ProcessTableModel tableModel;

	public List<PCB> allProcesses;
	public List<PCB> allProcesses_copy;

	public PlayerThread(PlayerEventLog eventLog, StatusBar statusbar, ProcessTableModel processTableModel) {
		super();

		allProcesses = new ArrayList<>();
		allProcesses_copy = new ArrayList<>();

		log = eventLog;
		status = statusbar;
		tableModel = processTableModel;
	}

	public void setScheduler(String sa) {
		pause();
		//refresh AllProcesses with the copy
		copyProcessList();
		// turn the string sa into a constructor of the respective scheduler
		switch (sa) {
		case "FCFS" -> sched = new FCFS(allProcesses);
		case "PS" -> sched = new PSS(allProcesses);
		case "RR" -> sched = new RR(allProcesses, RRQuantum);
		case "SJF" -> sched = new SJF(allProcesses);
		default -> throw new IllegalArgumentException("Invalid scheduler");
		}

		// this is our reset so we need to also clear the event log and reset the player
		// if in play
		log.clear();
		status.setTime(0);
		status.setScheduler(sa);
		done = allProcesses.isEmpty();
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

	public boolean isNewData() {
		var temp = newData;
		newData = false;
		return temp;
	}

	public PCB getCurProcess(String type) {
		if(sched == null) return null;
		if(type.equals("CPU")) {
			return sched.getCurProcess();
		} else {
			return sched.getCurIO();
		}

	}

	public List<PCB> getCurQueue(String type) {
		if(sched == null) return null;
		//make new array lists so removing the current process doesn't modify the original
		if(type.equals("CPU")) {
			var temp = new ArrayList<PCB>(sched.getReadyQueue());
			temp.remove(sched.getCurProcess());
			return temp; 
		} else {
			var temp = new ArrayList<PCB>(sched.getCurIoQueue());
			temp.remove(sched.getCurIO());
			return temp;
		}
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

			stepScheduler();
		}
	}

	private synchronized void unpause() {
		notify();
	}

	public void pause() {
		paused = true;
	}

	public void play() {
		paused = false;

		switch (this.getState()) {
		case WAITING -> unpause();
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

	public void step() {
		if (!done && paused) {
			stepScheduler();
		}
	}

	public void loadProcessesFile(String filePath) throws FileNotFoundException {
		try (var sc = new Scanner(new File(filePath))) {
			allProcesses_copy = new ArrayList<>();
			int id = 0;
			String line;
			while (sc.hasNextLine()) {
				line = sc.nextLine();
				String[] arr = line.split(",\\s+");
				String name = arr[0];
				int arrtime = Integer.parseInt(arr[1]);
				int priority = Integer.parseInt(arr[2]);
				int[] burst = new int[arr.length - 3];// create an array for CPU/IO Bursts which can differ based on
															// length of CPU bursts/IO bursts
				int[] cpuBurst= new int[(burst.length/2)+1];
				int[] ioBurst = new int[burst.length/2];
				for (int i = 3; i < arr.length; i++) {
					burst[i - 3] = Integer.parseInt(arr[i]);
				}
				int cpuc=0,ioc=0;
				for(int i=0;i<burst.length;i++) {
					
					if(i%2==0) {
						cpuBurst[cpuc]=burst[i];
						cpuc++;
					}else {
						ioBurst[ioc]=burst[i];
						ioc++;
					}
				}
						
				PCB proc = new PCB(name, id++, arrtime, cpuBurst,ioBurst, priority);
				allProcesses_copy.add(proc);
				copyProcessList();
			}
		}
	}

	private void stepScheduler() {
		logEvents(sched.nextB());
		newData = true;

		status.setTime(sched.systemTime);
		status.setCpuUtilization(sched.cpuUtilization());
		status.setIoUtilization(sched.ioUtilization());
		status.setAverageTurnaroundTime(sched.getAvgTat());
		status.setAverageCpuWaitingTime(sched.getAvgWt());
		status.setAverageIoWaitingTime(sched.getAvgIoWt());
		status.setThroughput(sched.getThroughput());

		tableModel.refresh();
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

	// deep copy of allProcesses
	private void copyProcessList() {
		allProcesses.clear();
		for (var p : allProcesses_copy) {
			allProcesses.add(new PCB(p));
		}

		tableModel.setProcesses(allProcesses);
	}
}