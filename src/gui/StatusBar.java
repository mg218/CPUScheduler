package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.*;

public class StatusBar extends JPanel {
	private static final long serialVersionUID = 4092409503485793479L;

  private String fontName = "Comic Sans MS";
  
  JLabel lblTime;
  JLabel lblAlgo;
  JLabel lblCpuUtil;
  JLabel lblIoUtil;

  JLabel lbltat;
  JLabel lblavgcpuwait;
  JLabel lblavgiowait;
  JLabel lblthroughput;

  static final String TIME_STRING = "System Time: ";
  static final String ALGO_STRING = "Scheduler: ";
  static final String CPU_UTILIZATION_STRING = "CPU util%: ";
  static final String IO_UTILIZATION_STRING = "IO util%: ";

  static final String TAT_STRING = "Turn Around: ";
  static final String AVG_CPUWAIT_STRING = "CPU Wait: ";
  static final String AVG_IOWAIT_STRING = "IO Wait: ";
  static final String THROUGHPUT_STRING = "Throughput: ";

  
  public StatusBar() {
    super();

    setLayout(new GridLayout(2,4));

    setBackground(new Color(0xbbbbbb));
    var font = new Font(fontName, Font.PLAIN, 13);

    lblTime = new JLabel(TIME_STRING + "N/A");
    lblTime.setHorizontalAlignment(JLabel.CENTER);
    lblTime.setFont(font);
    add(lblTime);
    lblCpuUtil = new JLabel(CPU_UTILIZATION_STRING + "N/A");
    lblCpuUtil.setHorizontalAlignment(JLabel.CENTER);
    lblCpuUtil.setFont(font);
    add(lblCpuUtil);

    lblIoUtil = new JLabel(IO_UTILIZATION_STRING + "N/A");
    lblIoUtil.setHorizontalAlignment(JLabel.CENTER);
    lblIoUtil.setFont(font);
    add(lblIoUtil);

    lbltat = new JLabel(TAT_STRING + "N/A");
    lbltat.setHorizontalAlignment(JLabel.CENTER);
    lbltat.setFont(font);
    add(lbltat);

//2nd row here
    lblAlgo = new JLabel(ALGO_STRING + "FCFS");
    lblAlgo.setHorizontalAlignment(JLabel.CENTER);
    lblAlgo.setFont(font);
    add(lblAlgo);


    lblavgcpuwait = new JLabel(AVG_CPUWAIT_STRING + "N/A");
    lblavgcpuwait.setHorizontalAlignment(JLabel.CENTER);
    lblavgcpuwait.setFont(font);
    add(lblavgcpuwait);

    lblavgiowait = new JLabel(AVG_IOWAIT_STRING + "N/A");
    lblavgiowait.setHorizontalAlignment(JLabel.CENTER);
    lblavgiowait.setFont(font);
    add(lblavgiowait);

    lblthroughput = new JLabel(THROUGHPUT_STRING + "N/A");
    lblthroughput.setHorizontalAlignment(JLabel.CENTER);
    lblthroughput.setFont(font);
    add(lblthroughput);
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

  public void setThroughput(double throughput) {
    lblthroughput.setText(THROUGHPUT_STRING + String.format("%.2f", throughput));
  }

  public void setAverageTurnaroundTime(double avgTurnaroundTime) {
    var tatString = (avgTurnaroundTime == -1) ? "N/A" : String.format("%.2f", avgTurnaroundTime);
    lbltat.setText(TAT_STRING + tatString);
  }

  public void setAverageCpuWaitingTime(double avgWaitingTime) {
    lblavgcpuwait.setText(AVG_CPUWAIT_STRING + String.format("%.2f", avgWaitingTime));
  }

  public void setAverageIoWaitingTime(double avgWaitingTime) {
    lblavgiowait.setText(AVG_IOWAIT_STRING + String.format("%.2f", avgWaitingTime));
  }
}
