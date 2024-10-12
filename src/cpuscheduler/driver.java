package cpuscheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//driver for the program will be ignored once the GUI is made
public class driver {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(new File("src/proc.txt"));
		String alg = sc.nextLine();//First Line is the algorithm identifier
		List<PCB> allProcesses = new ArrayList<>();
		int id=0;
		String line;
		while(sc.hasNextLine()) {
			line = sc.nextLine();
			String[] arr = line.split(",\\s+");
			String name = arr[0];
			int arrtime= Integer.parseInt(arr[1]);
			int cpuBurst = Integer.parseInt(arr[2]);
			int priority = Integer.parseInt(arr[3]);
			PCB proc = new PCB(name, id++,arrtime, cpuBurst,priority);
			allProcesses.add(proc);
		}
		
		sc.close();
		
		SchedulingAlgorithm sched =null;
		switch(alg) {
		case "FCFS" : sched = new FCFS(allProcesses); break;
		case "SJF" : sched = new SJF(allProcesses); break;
		case "PSS" : sched = new PSS(allProcesses); break;
		default: System.err.println("not supported");
		}
		sched.schedule();

	}

}