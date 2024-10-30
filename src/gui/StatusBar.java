package gui;

import java.awt.Color;

import javax.swing.*;

public class StatusBar extends JPanel {
	private static final long serialVersionUID = 4092409503485793479L;

  JLabel lblTime;
  JLabel lblAlgo;
  JLabel lblCpuUtil;
  JLabel lblIoUtil;

  static final String TIME_STRING = "System Time: ";
  static final String ALGO_STRING = "Scheduler: ";
  static final String CPU_UTILIZATION_STRING = "CPU util%: ";
  static final String IO_UTILIZATION_STRING = "IO util%: ";
  
  public StatusBar() {
    super();

    setBackground(new Color(0xbbbbbb));

    lblTime = new JLabel(TIME_STRING + "N/A");
    add(lblTime);

    lblAlgo = new JLabel(ALGO_STRING + "FCFS");
    add(lblAlgo);

    lblCpuUtil = new JLabel(CPU_UTILIZATION_STRING + "N/A");
    add(lblCpuUtil);

    lblIoUtil = new JLabel(IO_UTILIZATION_STRING + "N/A");
    add(lblIoUtil);
  }

  public void setTime(int time) {
    lblTime.setText(TIME_STRING + time);
  }

  public void setScheduler(String sched) {
    lblAlgo.setText(ALGO_STRING + sched);
  }

  public void setCpuUtilization(double util) {
    lblCpuUtil.setText(CPU_UTILIZATION_STRING + String.format("%.2f", util));
  }

  public void setIoUtilization(double util) {
    lblIoUtil.setText(IO_UTILIZATION_STRING + String.format("%.2f", util));
  }

}
