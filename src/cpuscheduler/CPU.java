package cpuscheduler;

public class CPU {
	public static void execute(PCB process, int cpuBurst) {	
		process.setCpuBurst(process.getCpuBurst()-cpuBurst);
		
	}

}
