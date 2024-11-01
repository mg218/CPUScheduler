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

  static final String TIME_STRING = "System Time: ";
  static final String ALGO_STRING = "Scheduler: ";
  static final String CPU_UTILIZATION_STRING = "CPU util%: ";
  static final String IO_UTILIZATION_STRING = "IO util%: ";
  
  public StatusBar() {
    super();

    setLayout(new GridLayout());

    setBackground(new Color(0xbbbbbb));
    var font = new Font(fontName, Font.PLAIN, 13);

    lblTime = new JLabel(TIME_STRING + "N/A");
    lblTime.setHorizontalAlignment(JLabel.CENTER);
    lblTime.setFont(font);
    add(lblTime);

    lblAlgo = new JLabel(ALGO_STRING + "FCFS");
    lblAlgo.setHorizontalAlignment(JLabel.CENTER);
    lblAlgo.setFont(font);
    add(lblAlgo);

    lblCpuUtil = new JLabel(CPU_UTILIZATION_STRING + "N/A");
    lblCpuUtil.setHorizontalAlignment(JLabel.CENTER);
    lblCpuUtil.setFont(font);
    add(lblCpuUtil);

    lblIoUtil = new JLabel(IO_UTILIZATION_STRING + "N/A");
    lblIoUtil.setHorizontalAlignment(JLabel.CENTER);
    lblIoUtil.setFont(font);
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
