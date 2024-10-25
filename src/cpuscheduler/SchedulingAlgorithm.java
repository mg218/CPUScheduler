package cpuscheduler;

import java.util.ArrayList;
import java.util.List;

import cpuscheduler.ProcessEvent.type;

public abstract class SchedulingAlgorithm {
	protected String name; // scheduling algorithm name
	protected List<PCB> allProcs; // the initial list of processes
	protected List<PCB> readyQueue; // ready queue of ready processes
	protected List<PCB> finishedProcs; // list of terminated processes
	protected List<PCB> ioQueue;
	protected PCB curProcess; // current selected process by the scheduler
	protected PCB curIO;
	protected int systemTime; // system time or simulation time steps
	protected int quantum;// Quantum Time for round robin
	protected int procCount = 0;// works as an index for process switching in round robin
	protected int procCountLast = 0;// keeps track of last executed process useful for RR
	protected int ioCpuLast = 0; // Keep track if the last executed process was IO or CPU Used for Event
									// processing with round robin

	public SchedulingAlgorithm(String name, List<PCB> queue, int quantum) {
		this.name = name;
		this.allProcs = queue;
		this.quantum = quantum;
		this.readyQueue = new ArrayList<>();
		this.finishedProcs = new ArrayList<>();
		this.ioQueue = new ArrayList<>();
	}

	public void schedule() {

		System.out.println("Scheduling algroithm " + name);

		while (!allProcs.isEmpty() || !readyQueue.isEmpty()||!ioQueue.isEmpty()) {
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
		if(!readyQueue.isEmpty()) {
			allProcs.removeAll(readyQueue);
			curProcess = pickNextProcess();
		}
		if(ioQueue.isEmpty()==false)
			curIO = ioQueue.get(0);
		// checks to see if the queue and process changed and create a log if they do
		

		print();
		if (curProcess.getStartTime() < 0)
			curProcess.setStartTime(systemTime);
		
		if(!readyQueue.isEmpty())
			CPU.execute(curProcess, curProcess.getCpuBurst(), curProcess.getBurstIndex(), 1);
		if (ioQueue.isEmpty() == false)
			IO.execute(curIO, curIO.getIoBurst(), curIO.getIoBurstIndex(), 1);

		for (PCB proc : readyQueue) {
			if (proc != curProcess)
				proc.increaseWaitingTime(1);

		}

		systemTime++;
		if (ioQueue.isEmpty() == false) {
			if (curIO.getIoBurst()[curIO.getIoBurst().length - 1] == 0) {
				curIO.setIoFinishTime(systemTime);
				ioQueue.remove(curIO);
				readyQueue.add(curIO);
				events.add(new ProcessEvent(curIO, type.CPUQUEUE));
				System.out.println("IO for process " + curIO.getName() + " terminated at " + curIO.getIoFinishTime());
			}else if (curIO.getIoBurst()[curIO.getIoBurstIndex()] == 0) {
				curIO.setIoBurstIndex(curIO.getIoBurstIndex() + 1);
				ioQueue.remove(curIO);
				readyQueue.add(curIO);
			}
		}
		

		// finish up process
		if(!readyQueue.isEmpty()) {
			if (curProcess.getCpuBurst()[curProcess.getCpuBurst().length - 1] == 0) {
				curProcess.setFinishTime(systemTime);
				events.add(new ProcessEvent(curProcess, type.TERMINATED));
				if (readyQueue.size() == 1)
					events.add(new ProcessEvent(curProcess, type.DONE));
				readyQueue.remove(curProcess);
				finishedProcs.add(curProcess);
				System.out.printf("Process %s terminated at %d start time = %d TAT =%d WT = %d", curProcess.getName(),
						systemTime, curProcess.getStartTime(), curProcess.getTurnaroundTime(), curProcess.getWaitingTime());
			}else if (curProcess.getCpuBurst()[curProcess.getBurstIndex()] == 0) {
				curProcess.setBurstIndex(curProcess.getBurstIndex() + 1);
				readyQueue.remove(curProcess);
				ioQueue.add(curProcess);
				events.add(new ProcessEvent(curProcess, type.IOQUEUE));
			}
		
		}
		procCountLast = procCount;// Update the most recently executed process
		
		System.out.println();
		System.out.println(readyQueue.size()+" "+ioQueue.size());
		return events;
	}

	// print simulation step
	public void print() {
		// add code to complete the method
			System.out.printf("CPU %s\n", curProcess == null ? "Idle" : curProcess.getName());
			if(ioQueue.isEmpty()==false)
				System.out.printf("IO %s\n", curProcess == null ? "Idle" : curIO.getName());
		for (PCB proc : readyQueue)
			System.out.println(proc);
		System.out.println();
		System.out.println("IO");
		if(ioQueue.isEmpty()==false) {
			for(PCB procs: ioQueue)
				System.out.println(procs);
		}
	}
}
