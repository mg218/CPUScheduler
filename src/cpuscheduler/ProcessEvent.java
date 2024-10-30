package cpuscheduler;

public class ProcessEvent {
  public enum type {
    CREATED,
    CPU,
    CPUQUEUE,
    IO,
    IOQUEUE,
    INTERRUPTED,
    TERMINATED,
    DONE
  };

  public ProcessEvent.type event;
  public PCB process;

  public ProcessEvent(PCB p, ProcessEvent.type t) {
    event = t;
    process = p;
  }

  //create tostring method
  @Override
  public String toString() {
    String action;
    switch(this.event) {
      case CPU, IO:
        action = "entered " + this.event.toString();
        break;
      case CPUQUEUE, IOQUEUE:
        action = "joined " + this.event.toString();
        break;
      case INTERRUPTED:
        action = "was Interrupted";
        break;
      case DONE:
      default:
        action = this.event.toString();
    }

    return process.getName() + " has " + action;
  }
}
