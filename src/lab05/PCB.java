package lab05;
public class PCB {
	
	// the representation of each process
	private String name;     // process name
	private int id;          // process id
	private int arrivalTime; // arrival time of the process
	private int cpuBurst;    // CPU burst length in unit time
	private int priority;    // priority level of the process
	// the stats of the process execution
	private int startTime, finishTime, turnaroundTime, waitingTime;

	// constructor
	public PCB(String name, int id, int arrivalTime, int cpuBurst, int priority) {
		super();
		this.name = name;
		this.id = id;
		this.arrivalTime = arrivalTime;
		this.cpuBurst = cpuBurst;
		this.priority = priority;
		this.startTime = -1;
		this.finishTime = -1;
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

	public int getCpuBurst() {
		return cpuBurst;
	}

	public void setCpuBurst(int cpuBurst) {
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
		this.turnaroundTime=finishTime-this.arrivalTime;
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
	public void increaseWaitingTime(int burst) {
		//Increase the waitingTime variable with burst.
		this.waitingTime+= burst;
		
	}
	@Override
	public String toString() {
		return "PCB [name=" + name + ", id=" + id + ", arrivalTime=" + arrivalTime + ", cpuBurst=" + cpuBurst
				+ ", priority=" + priority + "]";
	}

} 

