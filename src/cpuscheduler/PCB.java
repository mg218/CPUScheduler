package cpuscheduler;

public class PCB {

	// the representation of each process
	private String name; // process name
	private int id; // process id
	private int arrivalTime; // arrival time of the process
	private int[] cpuBurst; // CPU burst length in unit time
	private int[] ioBurst;
	private int priority; // priority level of the process
	// the stats of the process execution
	private int startTime, finishTime, turnaroundTime, waitingTime,ioWaitTime,ioFinishTime;//Time metrics
	private int burstIndex,ioBurstIndex;//points to which group of bursts is being executed
	private int exCount;//counts how many times a process has been executed. Used to compare to the quantum time in round robin
	private PCB.stateEnum state;//current state of a process
	

	// constructor
	public PCB(String name, int id, int arrivalTime, int[] cpuBurst, int[] ioBurst, int priority) {
		super();
		this.name = name;
		this.id = id;
		this.arrivalTime = arrivalTime;
		this.cpuBurst = cpuBurst;
		this.priority = priority;
		this.startTime = -1;
		this.finishTime = -1;
		this.burstIndex = 0;
		this.setIoBurst(ioBurst);
		this.setIoBurstIndex(0);
		this.setIoFinishTime(-1);
		this.exCount = 0;
		this.setState(PCB.stateEnum.NEW);
		
	}

	// create copy constructor
	public PCB(PCB otherPcb) {
		this(otherPcb.name, otherPcb.id, otherPcb.arrivalTime, new int[otherPcb.cpuBurst.length],
				new int[otherPcb.ioBurst.length], otherPcb.priority);

		System.arraycopy(otherPcb.cpuBurst, 0, this.cpuBurst, 0, otherPcb.cpuBurst.length);
		System.arraycopy(otherPcb.ioBurst, 0, this.ioBurst, 0, otherPcb.ioBurst.length);

	}
	
	public static enum stateEnum {
		NEW,
		READY,
		WAITING,
		RUNNING,
		TERMINATED
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int[] getCpuBurst() {
		return cpuBurst;
	}

	public void setCpuBurst(int[] cpuBurst) {
		this.cpuBurst = cpuBurst;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(int finishTime) {
		this.finishTime = finishTime;
		this.turnaroundTime = finishTime - this.arrivalTime;
	}

	public int getTurnaroundTime() {
		return turnaroundTime;
	}

	public void setTurnaroundTime(int turnaroundTime) {
		this.turnaroundTime = turnaroundTime;
	}

	public int getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}

	public int getIoWaitTime() {
		return ioWaitTime;
	}

	public void setIoWaitTime(int ioWaitTime) {
		this.ioWaitTime = ioWaitTime;
	}

	public int getBurstIndex() {
		return burstIndex;
	}

	public void setBurstIndex(int burstIndex) {
		this.burstIndex = burstIndex;
	}

	public void increaseWaitingTime(int burst) {
		// Increase the waitingTime variable with burst.
		this.waitingTime += burst;

	}
	public void increaseIoWaitingTime(int a) {
		this.waitingTime += a;
	}

	public int getIoBurstIndex() {
		return ioBurstIndex;
	}

	public void setIoBurstIndex(int ioBurstIndex) {
		this.ioBurstIndex = ioBurstIndex;
	}

	public int getIoFinishTime() {
		return ioFinishTime;
	}

	public void setIoFinishTime(int ioFinishTime) {
		this.ioFinishTime = ioFinishTime;
	}

	public int[] getIoBurst() {
		return ioBurst;
	}

	public void setIoBurst(int[] ioBurst) {
		this.ioBurst = ioBurst;
	}

	public void increaseExCount() {
		this.exCount++;
	}
	public int getExCount() {
		return exCount;
	}
	public void resetExCount() {
		exCount=0;
	}

	public PCB.stateEnum getState() {
		return state;
	}

	public void setState(PCB.stateEnum state) {
		this.state = state;
	}
	public int getResponseTime() {
		return startTime-arrivalTime;
	}

	@Override
	public String toString() {
		String outp = "PCB [name=" + name + ", id=" + id + ", arrivalTime= " + arrivalTime + ", Priority= " + priority
				+ ", ";
		// prints out all the bursts
		int ioc=0,cpuc=0;
		for (int i = 0; i < cpuBurst.length+ioBurst.length; i++) {
			if (i % 2 == 0) {
				// if the process is an even index it is a cpu burst
				outp += "CPUburst= " + cpuBurst[cpuc] + ", ";
				cpuc++;
			} else {
				// if the process is an odd index it is an IO burst
				outp += "IO Burst= " + ioBurst[ioc] + ", ";
				ioc++;
			}
		}
		outp += "]";

		return outp;
	}

}
