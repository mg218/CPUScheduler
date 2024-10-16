package cpuscheduler;

public class ProcessEvent {
  public enum type {
    CREATED,
    CPU,
    CPUQUEUE,
    IO,
    IOQUEUE,
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
      case CPU:
      case IO:
        action = "entered " + this.event.toString();
        break;
      case CPUQUEUE:
      case IOQUEUE:
        action = "joined " + this.event.toString();
        break;
      case TERMINATED:
        action = "finished";
        break;
      case DONE:
      default:
        action = "";
    }

    return process.getName() + " has " + action;
  }
}
