package lab05;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SJF extends SchedulingAlgorithm {

		public SJF(List<PCB> queue) {
			super("SJF",queue);
		}
	@Override
	public PCB pickNextProcess() {
		// TODO Auto-generated method stub
		
		Collections.sort(readyQueue, Comparator.comparingInt(PCB::getCpuBurst));
		return readyQueue.get(0);
	}

	
}
