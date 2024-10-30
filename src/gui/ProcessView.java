package gui;

import javax.swing.*;
import java.awt.*;

import cpuscheduler.PCB;

public class ProcessView extends JPanel {
	private static final long serialVersionUID = 3109842930852032940L;

  private String fontName = "Comic Sans MS";
  private PCB process;

  public ProcessView() {
    super();
    setPreferredSize(new Dimension(100, 64));
    setMinimumSize(new Dimension(100, 64));
  }

  public ProcessView(PCB p) {
    this();
    
    process = p;
  }

  public void setProcess(PCB p) {
    process = p;
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if(process == null) return;

    var width = getSize().width;
    var height = getSize().height;
    
    var font = new Font(fontName, Font.PLAIN, height/4);
    g.setFont(font);
    g.setColor(Color.GRAY);
    g.fillRoundRect(0, 0, width, height, 10, 10);
    g.setColor(new Color(process.getName().hashCode()));
    g.drawRoundRect(1, 1, width-1, height-1, 10, 10);

    g.setColor(Color.BLACK);
    g.drawString(process.getName(), 10, height/4);

    g.drawString("CPU: " + createCPUBurstString(), 10, height/2);
    g.drawString("IO: " + createIOBurstString(), 10, 3*height/4);

  }

  private String createCPUBurstString() {
    return arr2String(process.getCpuBurst());
  }

  private String createIOBurstString() {
    return arr2String(process.getIoBurst());
  }

  private String arr2String(int[] arr) {
    var temp = new StringBuilder();

    for (var value : arr) {
      if(value != 0) {
        temp.append(value + " ");
      }
    }

    return temp.toString();
  }
}
