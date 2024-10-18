package cpuscheduler;

import java.util.List;

public class RR extends SchedulingAlgorithm {
	public RR(List<PCB> queue, int quantum) {
		super("Round Robin", queue, quantum);
	}

	public PCB pickNextProcess() {
		// check to see if its the first process
		if (systemTime == 0) {
			return readyQueue.get(0);
		} else {
			// If the system time goes over an interval of the quantum time
			if (systemTime % quantum == 0) {
				if (procCount < readyQueue.size() - 1) {// if there is another process after the current one
					procCount++;// go to the next one
					return readyQueue.get(procCount);
				} else {// if this the last process in the queue
					procCount = 0;// reset to first process
					return readyQueue.get(procCount);
				}
			} else {
				while (procCount >= readyQueue.size()) {// if the process counter is larger than the size of the queue
														// decrease the counter until its in bounds
					procCount--;
				}
				return readyQueue.get(procCount);
			}
		}
	}

}
