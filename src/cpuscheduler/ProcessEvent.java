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
}
