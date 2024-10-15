package cpuscheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//driver for the program will be ignored once the GUI is made
public class driver {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner inp = new Scanner(System.in);//For user input
		System.out.println("Please enter the file you want to use: ");
		String fileName = inp.nextLine();
		Scanner sc = new Scanner(new File(fileName));//For file reading
		System.out.println("Please choose the scheduling algorithm to use [FCFS,PSS,RR]: ");
		String alg = inp.nextLine();//First Line is the algorithm identifier
		List<PCB> allProcesses = new ArrayList<>();
		int id=0;
		String line;
		while(sc.hasNextLine()) {
			line = sc.nextLine();
			String[] arr = line.split(",\\s+");
			String name = arr[0];
			int arrtime= Integer.parseInt(arr[1]);
			int priority = Integer.parseInt(arr[2]);
			int[] cpuBurst = new int[arr.length-3];//create an array for CPU/IO Bursts which can differ based on length of CPU bursts/IO bursts
			for(int i=3;i<arr.length;i++) {
				cpuBurst[i-3]=Integer.parseInt(arr[i]);
			}
			PCB proc = new PCB(name, id++,arrtime, cpuBurst,priority);
			allProcesses.add(proc);
		}
		
		sc.close();
		inp.close();
		
		SchedulingAlgorithm sched =null;
		switch(alg) {
		case "FCFS" : sched = new FCFS(allProcesses); break;
		case "PSS" : sched = new PSS(allProcesses); break;
		default: System.err.println("not supported");
		}
		sched.schedule();

	}

}