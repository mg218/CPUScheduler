package cpuscheduler;

import java.util.List;

public class RR extends SchedulingAlgorithm {
	public RR(List<PCB> queue, int quantum) {
		super("Round Robin", queue, quantum);
	}

	public PCB pickNextProcess() {
		// check to see if its the first process
		if (systemTime == 0||readyQueue.size()==0) {
			return readyQueue.get(0);
		} else {
			// If the system time goes over an interval of the quantum time
			if(lastCPU.getExCount()%quantum==0&&curProcess.getExCount()>0&&readyQueue.contains(lastCPU)) {//if the last executed process 
				readyQueue.remove(curProcess);//remove current process
				readyQueue.add(curProcess);
				return readyQueue.get(0);
			}else {//If last 
				return readyQueue.get(0);
			}
			
		}
	}

}
