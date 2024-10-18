package cpuscheduler;

import java.util.ArrayList;
import java.util.List;

import cpuscheduler.ProcessEvent.type;

public abstract class SchedulingAlgorithm {
	protected String name; // scheduling algorithm name
	protected List<PCB> allProcs; // the initial list of processes
	protected List<PCB> readyQueue; // ready queue of ready processes
	protected List<PCB> finishedProcs; // list of terminated processes
	protected PCB curProcess; // current selected process by the scheduler
	protected int systemTime; // system time or simulation time steps
	protected int quantum;// Quantum Time for round robin
	protected int procCount = 0;// works as an index for process switching in round robin
	protected int procCountLast=0;//keeps track of last executed process useful for RR
	protected int ioCpuLast=0; //Keep track if the last executed process was IO or CPU Used for Event processing with round robin
	

	public SchedulingAlgorithm(String name, List<PCB> queue, int quantum) {
		this.name = name;
		this.allProcs = queue;
		this.quantum = quantum;
		this.readyQueue = new ArrayList<>();
		this.finishedProcs = new ArrayList<>();
	}

	public void schedule() {

		System.out.println("Scheduling algroithm " + name);

		while (!allProcs.isEmpty() || !readyQueue.isEmpty()) {
			nextB();
		}

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
		// remove the running processes from the all queue
		allProcs.removeAll(readyQueue);
		curProcess = pickNextProcess();
		//checks to see if the queue and process changed and create a log if they do
		if(name.equals("Round Robin")&&procCount!=procCountLast) {
			if (curProcess.getBurstIndex() % 2 == 0&&ioCpuLast==1)
				events.add(new ProcessEvent(curProcess,type.CPUQUEUE));
			if(curProcess.getBurstIndex()%2==1&&ioCpuLast==0)
				events.add(new ProcessEvent(curProcess,type.IOQUEUE));
			
		}
		print();
		if (curProcess.getStartTime() < 0)
			curProcess.setStartTime(systemTime);

		CPU.execute(curProcess, curProcess.getCpuBurst(), curProcess.getBurstIndex(), 1);
		if (curProcess.getBurstIndex() % 2 == 0)
			events.add(new ProcessEvent(curProcess, type.CPU));
		else
			events.add(new ProcessEvent(curProcess, type.IO));

		for (PCB proc : readyQueue) {
			if (proc != curProcess)
				proc.increaseWaitingTime(1);

		}
		if (curProcess.getBurstIndex() % 2 == 0) 
			ioCpuLast=0;
		else 
			ioCpuLast=1;
		
		systemTime++;
		// finish up process
		if (curProcess.getCpuBurst()[curProcess.getCpuBurst().length - 1] == 0) {
			curProcess.setFinishTime(systemTime);
			events.add(new ProcessEvent(curProcess, type.TERMINATED));
			if (readyQueue.size() == 1)
				events.add(new ProcessEvent(curProcess, type.DONE));
			readyQueue.remove(curProcess);
			finishedProcs.add(curProcess);
			System.out.printf("Process %s terminated at %d start time = %d TAT =%d WT = %d", curProcess.getName(),
					systemTime, curProcess.getStartTime(), curProcess.getTurnaroundTime(), curProcess.getWaitingTime());
		}
		// move the process to the next IO/CPU burst
		if (curProcess.getCpuBurst()[curProcess.getBurstIndex()] == 0) {
			curProcess.setBurstIndex(curProcess.getBurstIndex() + 1);
			if (curProcess.getBurstIndex() % 2 == 0)
				events.add(new ProcessEvent(curProcess, type.CPUQUEUE));
			else
				events.add(new ProcessEvent(curProcess, type.IOQUEUE));
		}
		procCountLast=procCount;//Update the most recently executed process
		System.out.println();
		return events;
	}

	// print simulation step
	public void print() {
		// add code to complete the method
		if (curProcess.getBurstIndex() % 2 == 0)
			System.out.printf("CPU %s\n", curProcess == null ? "Idle" : curProcess.getName());
		else
			System.out.printf("IO %s\n", curProcess == null ? "Idle" : curProcess.getName());
		for (PCB proc : readyQueue)
			System.out.println(proc);
	}
}
