package lab05;

public class CPU {
	public static void execute(PCB process, int cpuBurst) {
		//  add code to complete the method		
		process.setCpuBurst(process.getCpuBurst()-cpuBurst);
		//simulate the running time with Thread.sleep()
	}

}
