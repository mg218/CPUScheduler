package lab05;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
//priority sorting
public class PSS extends SchedulingAlgorithm {
	

	public PSS(List<PCB> queue) {
		super("Prioity Scheduling", queue);
		// TODO Auto-generated constructor stub
	}

	@Override
	public PCB pickNextProcess() {
		// TODO Auto-generated method stub
		
		//sort ready queue in order of priority
		Collections.sort(readyQueue, Comparator.comparingInt(PCB::getPriority));
		//return top procrss
		return readyQueue.get(0);
	}

}
