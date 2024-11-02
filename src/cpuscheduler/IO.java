package cpuscheduler;

public class IO {
	// similar to CPU but only handles IO
	public static void execute(PCB process, int[] burst, int burstIndex, int burstAmount) {
		int[] bursts = burst;
		bursts[burstIndex] = bursts[burstIndex] - burstAmount;
		process.setIoBurst(bursts);

	}
}
