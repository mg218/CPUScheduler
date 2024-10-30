package cpuscheduler;

public class CPU {
	// Is inputted an array of bursts with an index and decreases the amount by the
	// input burst
	public static void execute(PCB process, int[] burst, int burstIndex, int burstAmount) {
		int[] bursts = burst;
		bursts[burstIndex] = bursts[burstIndex] - burstAmount;
		process.increaseExCount();
		process.setCpuBurst(bursts);

	}

}
