package cpuscheduler;

import java.util.ArrayList;
import java.util.List;

import cpuscheduler.PCB.stateEnum;
import cpuscheduler.ProcessEvent.type;

public abstract class SchedulingAlgorithm {
	protected String name; // scheduling algorithm name
	protected List<PCB> allProcs; // the initial list of processes
	protected List<PCB> readyQueue; // ready queue of ready processes
	protected List<PCB> finishedProcs; // list of terminated processes
	protected List<PCB> ioQueue;
	protected List<PCB> allProcStatic;// Static list of all processes used for GUI
	protected PCB curProcess; // current selected process by the scheduler
	protected PCB curIO;
	protected int systemTime; // system time or simulation time steps
	protected int quantum;// Quantum Time for round robin
	protected int procCount = 0;// works as an index for process switching in round robin
	protected int procCountLast = 0;
	protected PCB lastIO, lastCPU; // holds on to what the most recently executed IO/CPU process used in event
									// handling
	protected double cpuUse, ioUse;// holds info for how many frames cpu and IO are active for

	public SchedulingAlgorithm(String name, List<PCB> queue, int quantum) {
		this.name = name;
		this.allProcs = new ArrayList<>(queue);
		this.quantum = quantum;
		this.readyQueue = new ArrayList<>();
		this.finishedProcs = new ArrayList<>();
		this.ioQueue = new ArrayList<>();
		this.allProcStatic = queue;
	}

	public void schedule() {

		System.out.println("Scheduling algroithm " + name);

		while (!allProcs.isEmpty() || !readyQueue.isEmpty() || !ioQueue.isEmpty()) {
			nextB();
		}
		print();
		System.out.println(cpuUse + " " + ioUse);
		System.out.println("CPU utilization= " + cpuUtilization() + "%, IO Utilization= " + ioUtilization() + "%");

	}

	// Selects the next task using the appropriate scheduling algorithm
	public abstract PCB pickNextProcess();

	public List<ProcessEvent> nextB() {
		List<ProcessEvent> events = new ArrayList<>();
		System.out.println("System time: " + systemTime);

		for (PCB proc : allProcs) {
			if (proc.getArrivalTime() <= systemTime) {
				readyQueue.add(proc);
				events.add(new ProcessEvent(proc, type.CREATED));

			}
		}
		// picks the process if its the first process in the cpu
		if (!readyQueue.isEmpty() && (systemTime == 0 || curProcess == null)) {
			allProcs.removeAll(readyQueue);
			curProcess = pickNextProcess();
		}
		if (!ioQueue.isEmpty() && systemTime == 0)
			curIO = ioQueue.get(0);

		print();
		if (curProcess != null)
			if (curProcess.getStartTime() < 0)
				curProcess.setStartTime(systemTime);
		// if the CPU starts executing a new process create an event
		if (!readyQueue.isEmpty() && curProcess != lastCPU && systemTime == 1)
			events.add(new ProcessEvent(curProcess, type.CPU));

		if (!readyQueue.isEmpty()) {
			CPU.execute(curProcess, curProcess.getCpuBurst(), curProcess.getBurstIndex(), 1);
			cpuUse++;
		}
		if (ioQueue.isEmpty() == false) {
			IO.execute(curIO, curIO.getIoBurst(), curIO.getIoBurstIndex(), 1);
			ioUse++;
		}

		for (PCB proc : readyQueue) {
			if (proc != curProcess)
				proc.increaseWaitingTime(1);
		}
		for (PCB proc : ioQueue) {
			if (proc != curIO)
				proc.increaseIoWaitingTime(1);
		}

		systemTime++;
		// sets the previously executed process to be compared to the current for event purposes
		if (curIO != null)
			lastIO = curIO;
		if (curProcess != null)
			lastCPU = curProcess;
		// handles IO finishing/moving processes back to readyQueue
		if (ioQueue.isEmpty() == false) {
			if (curIO.getIoBurst()[curIO.getIoBurst().length - 1] == 0) {
				curIO.setIoFinishTime(systemTime);
				ioQueue.remove(curIO);
				readyQueue.add(curIO);
				events.add(new ProcessEvent(curIO, type.CPUQUEUE));
				System.out.println("IO for process " + curIO.getName() + " terminated at " + curIO.getIoFinishTime());
			} else if (curIO.getIoBurst()[curIO.getIoBurstIndex()] == 0) {
				curIO.setIoBurstIndex(curIO.getIoBurstIndex() + 1);
				ioQueue.remove(curIO);
				readyQueue.add(curIO);
				events.add(new ProcessEvent(curIO, type.CPUQUEUE));
			}
		}

		// finish up process
		if (curProcess != null)
			if (!readyQueue.isEmpty()) {
				if (curProcess.getCpuBurst()[curProcess.getCpuBurst().length - 1] == 0) {
					curProcess.setFinishTime(systemTime);
					events.add(new ProcessEvent(curProcess, type.TERMINATED));
					curProcess.setState(stateEnum.TERMINATED);
					if (readyQueue.size() == 1 && ioQueue.isEmpty())
						events.add(new ProcessEvent(curProcess, type.DONE));
					readyQueue.remove(curProcess);
					finishedProcs.add(curProcess);
					System.out.printf("Process %s terminated at %d start time = %d TAT =%d WT = %d",
							curProcess.getName(), systemTime, curProcess.getStartTime(), curProcess.getTurnaroundTime(),
							curProcess.getWaitingTime());
				} else if (curProcess.getBurstVal() == 0) {
					curProcess.setBurstIndex(curProcess.getBurstIndex() + 1);
					readyQueue.remove(curProcess);
					curProcess.resetExCount();
					ioQueue.add(curProcess);
					events.add(new ProcessEvent(curProcess, type.IOQUEUE));
				}

			}
		// pick next process if there was a previous process
		if (!readyQueue.isEmpty()) {
			allProcs.removeAll(readyQueue);
			curProcess = pickNextProcess();
			if (name.equals("Round Robin")) {
				if (lastCPU != curProcess && readyQueue.contains(lastCPU))
					events.add(new ProcessEvent(lastCPU, type.INTERRUPTED));
			}
		} else {
			curProcess = null;
		}
		if (!ioQueue.isEmpty())
			curIO = ioQueue.get(0);
		else
			curIO = null;
		if (!readyQueue.isEmpty() && curProcess != lastCPU) {
			events.add(new ProcessEvent(curProcess, type.CPU));
			curProcess.setState(stateEnum.RUNNING);
		}
		// if the IO starts executing a new process create an event
		if (!ioQueue.isEmpty() && curIO != lastIO)
			events.add(new ProcessEvent(curIO, type.IO));

//		procCountLast = procCount;// Update the most recently executed process

		for (PCB p : readyQueue) {
			if (p != curProcess)
				p.setState(stateEnum.READY);
		}
		for (PCB p : ioQueue) {
			if (p != curIO)
				p.setState(stateEnum.WAITING);
		}

		System.out.println();

		return events;
	}

	// print simulation step
	public void print() {
		// add code to complete the method
		if (!readyQueue.isEmpty())
			System.out.printf("CPU %s\n", curProcess == null ? "Idle" : curProcess.getName());
		if (ioQueue.isEmpty() == false)
			System.out.printf("IO %s\n", curProcess == null ? "Idle" : curIO.getName());
		for (PCB proc : readyQueue)
			System.out.println(proc);
		System.out.println();
		System.out.println("IO");
		if (ioQueue.isEmpty() == false) {
			for (PCB procs : ioQueue)
				System.out.println(procs);
		}
	}

	public PCB getCurProcess() {
		return curProcess;
	}

	public List<PCB> getReadyQueue() {
		return readyQueue;
	}

	public PCB getCurIO() {
		return curIO;
	}

	public List<PCB> getCurIoQueue() {
		return ioQueue;
	}

	public double ioUtilization() {
		return (ioUse / systemTime) * 100;
	}

	public double cpuUtilization() {
		return (cpuUse / systemTime) * 100;
	}

	public double getAvgTat() {
		double total = 0;
		if (finishedProcs.size() == 0)
			return -1.0;
		for (PCB p : finishedProcs) {
			total += p.getTurnaroundTime();
		}
		return total / finishedProcs.size();
	}

	// average wait time for a list of processes
	public double getAvgWt() {
		double total = 0;
		for (PCB p : allProcStatic) {
			total += p.getWaitingTime();
		}
		return total / allProcStatic.size();
	}

	public double getAvgIoWt() {
		double total = 0;
		for (PCB p : allProcStatic) {
			total += p.getIoWaitTime();
		}
		return total / allProcStatic.size();
	}

	// returns throughput based on finished processes over the current time
	public double getThroughput() {
		double fin = finishedProcs.size(), st = systemTime;
		return fin / st;
	}
}
