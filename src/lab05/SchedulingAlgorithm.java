package lab05;

import java.util.ArrayList;
import java.util.List;

public abstract class SchedulingAlgorithm {
     protected String name;		      //scheduling algorithm name
	protected List<PCB> allProcs;		//the initial list of processes
	protected List<PCB> readyQueue;	//ready queue of ready processes
	protected List<PCB> finishedProcs;	//list of terminated processes
	protected PCB curProcess; //current selected process by the scheduler
	protected int systemTime; //system time or simulation time steps
 
      public SchedulingAlgorithm(String name, List<PCB> queue) {
    	      this.name=name;
    	      this.allProcs = queue;
    	      this.readyQueue = new ArrayList<>();
    	      this.finishedProcs = new ArrayList<>();
      }
	
	public void schedule() {
		
		System.out.println("Scheduling algroithm "+name);
		
		while(!allProcs.isEmpty()||!readyQueue.isEmpty()) {
			System.out.println("System time: "+ systemTime);
			
			for(PCB proc:allProcs) {
				if(proc.getArrivalTime()>=systemTime) {
					readyQueue.add(proc);
				}
			}
			allProcs.removeAll(readyQueue);
			curProcess=pickNextProcess();
			print();
			if(curProcess.getStartTime()<0)
				curProcess.setStartTime(systemTime);
			
			CPU.execute(curProcess, 1);
			
			for(PCB proc:readyQueue) {
				if(proc != curProcess) proc.increaseWaitingTime(1);
				
			}
			systemTime++;
			if(curProcess.getCpuBurst()==0) {
				curProcess.setFinishTime(systemTime);
				readyQueue.remove(curProcess);
				finishedProcs.add(curProcess);
				System.out.printf("Process %s terminated at %d start time = %d TAT =%d WT = %d",
						curProcess.getName(), systemTime,curProcess.getStartTime(), curProcess.getTurnaroundTime(), curProcess.getWaitingTime());
			}
			
			System.out.println();
		}
		
		//  add code to complete the method
//		- Print the name of the scheduling algorithm
//		- while (allProcs is not empty or readyQueue is not empty) {
//		    - Print the current system time
//		    - Move arrived processes from allProcs to readyQueue (arrivalTime = systemTime)
//		    - curProcess = pickNextProcess() //call pickNextProcess() to choose next process
//		    - call print() to print simulation events: CPU, ready queue, ..
//		    - update the start time of the selected process (curProcess)
//		    - Call CPU.execute() to let the CPU execute 1 CPU burst unit time of curProcess
//		    - Increase 1 to the waiting time of other processes in the ready queue
//		    - Increase systemTime by 1
//		    - Check if the remaining CPU burst of curProcess = 0
//		        - Update finishTime of curProcess
//		        - remove curProcess from readyQueue
//		        - add curProcess to the finished queue (finishedProcs)
//		        - Print to the console a message displaying the process name, terminated time, 
//		                                               startTime, turnaroundTime, waitingTime
//		    - Print a new line

	}
	
	//Selects the next task using the appropriate scheduling algorithm
      public abstract PCB pickNextProcess();

      //print simulation step
      public void print() {
		//  add code to complete the method
    	  System.out.printf("CPU %s\n", curProcess==null ? "Idle" : curProcess.getName());
    	  for(PCB proc:readyQueue)
    		  System.out.println(proc);
      }
}
