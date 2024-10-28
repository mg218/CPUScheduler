package gui;

import java.awt.Color;

import javax.swing.*;

public class StatusBar extends JPanel {
	private static final long serialVersionUID = 4092409503485793479L;

  JLabel lblTime;
  JLabel lblAlgo;

  static final String TIME_STRING = "System Time: ";
  static final String ALGO_STRING = "Scheduler: ";
  
  public StatusBar() {
    super();

    setBackground(new Color(0xbbbbbb));

    lblTime = new JLabel(TIME_STRING + "N/A");
    add(lblTime);

    lblAlgo = new JLabel(ALGO_STRING + "FCFS");
    add(lblAlgo);
  }

  public void setTime(int time) {
    lblTime.setText(TIME_STRING + time);
  }

  public void setScheduler(String sched) {
    lblAlgo.setText(ALGO_STRING + sched);
  }

}
