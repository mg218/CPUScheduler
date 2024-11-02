package cpuscheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//driver for the program will be ignored once the GUI is made
public class driver {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner inp = new Scanner(System.in);// For user input
		System.out.println("Please enter the file you want to use: ");
		String fileName = inp.nextLine();
		Scanner sc = new Scanner(new File(fileName));// For file reading
		System.out.println("Please choose the scheduling algorithm to use [FCFS,PSS,RR]: ");
		String alg = inp.nextLine();// First Line is the algorithm identifier
		List<PCB> allProcesses = new ArrayList<>();
		int quant = 0;
		if (alg.equals("RR")) {
			System.out.println("Enter a quantum time for round robin execution");
			quant = Integer.parseInt(inp.nextLine());
		}
		int id = 0;
		String line;
		while (sc.hasNextLine()) {
			line = sc.nextLine();
			String[] arr = line.split(",\\s+");
			String name = arr[0];
			int arrtime = Integer.parseInt(arr[1]);
			int priority = Integer.parseInt(arr[2]);
			int[] burst = new int[arr.length - 3];// create an array for CPU/IO Bursts which can differ based on
														// length of CPU bursts/IO bursts
			int[] cpuBurst= new int[(burst.length/2)+1];
			int[] ioBurst = new int[burst.length/2];
			for (int i = 3; i < arr.length; i++) {
				burst[i - 3] = Integer.parseInt(arr[i]);
			}
			int cpuc=0,ioc=0;
			for(int i=0;i<burst.length;i++) {
				
				if(i%2==0) {
					cpuBurst[cpuc]=burst[i];
					cpuc++;
				}else {
					ioBurst[ioc]=burst[i];
					ioc++;
				}
			}
					
			PCB proc = new PCB(name, id++, arrtime, cpuBurst,ioBurst, priority);
			allProcesses.add(proc);
		}

		sc.close();
		inp.close();

		SchedulingAlgorithm sched = null;
		switch (alg) {
		case "FCFS":
			sched = new FCFS(allProcesses);
			break;
		case "PSS":
			sched = new PSS(allProcesses);
			break;
		case "RR":
			sched = new RR(allProcesses, quant);
			break;
		case "SJF":
			sched= new SJF(allProcesses);
			break;
		default:
			System.err.println("not supported");
		}
		sched.schedule();

	}

}