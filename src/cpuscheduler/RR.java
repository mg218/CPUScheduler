package cpuscheduler;

import java.util.List;

public class RR extends SchedulingAlgorithm {
	public RR(List<PCB> queue, int quantum) {
		super("Round Robin",queue,quantum);
	}
	
	public PCB pickNextProcess() {
		if(systemTime==0) {
			return readyQueue.get(0);
		}else {
			if(systemTime%quantum==0) {
				if(procCount<readyQueue.size()-1) {
					procCount++;
					return readyQueue.get(procCount);
				}else {
					procCount=0;
					return readyQueue.get(procCount);
				}
			}else {
				while(procCount>=readyQueue.size()) {
					procCount--;
				}
				return readyQueue.get(procCount);
			}
		}
	}

}
