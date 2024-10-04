package lab05;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
//SJF- Shortest job first
public class SJF extends SchedulingAlgorithm {

		public SJF(List<PCB> queue) {
			super("SJF",queue);
		}
	@Override
	public PCB pickNextProcess() {
		//sorts the queue by the shortest first
		Collections.sort(readyQueue, Comparator.comparingInt(PCB::getCpuBurst));
		return readyQueue.get(0);
	}

	
}
