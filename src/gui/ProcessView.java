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
    setMinimumSize(new Dimension(87, 56));
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
    g.setColor(new Color(process.getName().hashCode()));
    g.fillRoundRect(0, 0, width, height, 18, 18);
    g.setColor(Color.GRAY);
    g.fillRoundRect(5, 5, width-10, height-10, 8, 8);

    g.setColor(Color.BLACK);
    g.drawString(process.getName(), 10, height/2+getFont().getSize()/2);

  }
}
