package cpuscheduler;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SJF extends SchedulingAlgorithm {

	public SJF(List<PCB> queue) {
		super("Shortest Job First", queue, 0);
	}

	public PCB pickNextProcess() {

		// sorts based on the current CPU burst
		Collections.sort(readyQueue, Comparator.comparingInt(PCB::getBurstVal));

		// return top procrss
		return readyQueue.get(0);
	}

}
