package cpuscheduler;

import java.io.*;
import java.util.*;

public class PlayerThread extends Thread {

    private SchedulingAlgorithm sched = null;
    private int RRQuantum = 3;
    private int speedMS = 1000;
    private boolean paused;

    public List<PCB> allProcesses;

    public PlayerThread() {
        super();

        paused = false;
    }

    public void setScheduler(String sa) {
        //turn the string sa into a constructor of the respective scheduler
        switch (sa) {
            case "FCFS" ->
                sched = new FCFS(allProcesses);
            case "PS" ->
                sched = new PSS(allProcesses);
            case "RR" ->
                sched = new RR(allProcesses, RRQuantum);
            default ->
                throw new IllegalArgumentException("Invalid scheduler");
        }
    }

    public void setRRQuantum(int t) {
        RRQuantum = t;
        sched = new RR(allProcesses, RRQuantum);
    }

    public void setSpeed(int s) {
        if (s < 0) {
            throw new IllegalArgumentException("Speed cannot be negative");
        }

        speedMS = s;
    }

    @Override
    public synchronized void run() {

        while (!sched.allProcs.isEmpty() || !sched.readyQueue.isEmpty()) {
            sched.nextB();
            System.out.println("running scheduler");

            try {
                if (paused) {
                    wait();
                } else {
                    sleep((long) speedMS);
                }
            } catch (InterruptedException ex) {
                break;
            }
        }

    }

    public synchronized void play() {
        paused = false;
        switch (this.getState()) {
            case WAITING ->
                notify();
            case NEW -> {
                if (sched.allProcs != null) {
                    start();
                }
            }
            default ->
                throw new IllegalArgumentException("Unexpected value: " + this.getState());
        }

    }

    public void pause() {
        paused = true;
    }

    public void step() {
        if (sched == null) {
            throw new NullPointerException("Scheduler is null");
        }

        if (this.getState() != State.TERMINATED) {
            sched.nextB();
        }
    }

    public void loadProcessesFile(String filePath) throws FileNotFoundException {
        allProcesses = new ArrayList<>();

        try (var sc = new Scanner(new File(filePath))) {
            int id = 0;
            String line;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                String[] arr = line.split(",\\s+");
                String name = arr[0];
                int arrtime = Integer.parseInt(arr[1]);
                int priority = Integer.parseInt(arr[2]);
                int[] cpuBurst = new int[arr.length - 3];// create an array for CPU/IO Bursts which can differ based on
                // length of CPU bursts/IO bursts
                for (int i = 3; i < arr.length; i++) {
                    cpuBurst[i - 3] = Integer.parseInt(arr[i]);
                }
                PCB proc = new PCB(name, id++, arrtime, cpuBurst, priority);
                allProcesses.add(proc);
            }
        }
    }

}
