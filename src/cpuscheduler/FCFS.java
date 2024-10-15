package cpuscheduler;

import java.util.List;

//First come first server
public class FCFS extends SchedulingAlgorithm {
	public FCFS(List<PCB> queue) {
		super("FCFS", queue, 0);
	}

	public PCB pickNextProcess() {
		return readyQueue.get(0);
	}
}
