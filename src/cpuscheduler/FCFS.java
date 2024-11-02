package cpuscheduler;

import java.util.List;

//First come first serve
public class FCFS extends SchedulingAlgorithm {
	public FCFS(List<PCB> queue) {
		super("FCFS", queue, 0);
	}

	// picks and returns first in the ready queue
	public PCB pickNextProcess() {
		return readyQueue.get(0);
	}
}
