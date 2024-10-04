package lab05;

import java.util.List;
//First come first server
public class FCFS extends SchedulingAlgorithm {
      public FCFS(List<PCB> queue) {
		super("FCFS", queue);
	}


      public PCB pickNextProcess() {
    	  return readyQueue.get(0);
      }
}
