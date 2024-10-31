package gui;

import java.io.Serializable;
import java.util.List;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import cpuscheduler.PCB;

public class ProcessTableModel implements TableModel, Serializable {
  private static final long serialVersionUID = 999999999L;

  private List<PCB> processes;

  // Columns
  private String[] columnNames = { "Name", "Arrival", "Priority", "CPU Bursts", "IO Bursts"};

  protected EventListenerList listenerList = new EventListenerList();
  
  public ProcessTableModel() {
  }

  public void setProcesses(List<PCB> processes) {
    this.processes = processes;
    fireTableChanged(new TableModelEvent(this));
  }

  public void refresh() {
    fireTableChanged(new TableModelEvent(this));
  }

  public int getRowCount() {
    return (processes != null) ? processes.size() : 0;
  }

  public int getColumnCount() {
    return columnNames.length;
  }

  public String getColumnName(int col) {
    return columnNames[col];
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    var process = processes.get(rowIndex);

    switch(columnIndex) {
      case 0 -> {return process.getName();}
      case 1 -> {return process.getArrivalTime();}
      case 2 -> {return process.getPriority();}
      case 3 -> {return arr2String(process.getCpuBurst());}
      case 4 -> {return arr2String(process.getIoBurst());}
      default -> {return -1;}
    }
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return Object.class;
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return false; // no cell editable
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    // see function above
    throw new UnsupportedOperationException("Table not editable");
  }

  @Override
  public void addTableModelListener(TableModelListener l) {
    listenerList.add(TableModelListener.class, l);
  }

  @Override
  public void removeTableModelListener(TableModelListener l) {
    listenerList.remove(TableModelListener.class, l);
  }

  //copied from AbstractTableModel's implmentation
  public void fireTableChanged(TableModelEvent e) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
        if (listeners[i]==TableModelListener.class) {
            ((TableModelListener)listeners[i+1]).tableChanged(e);
        }
    }
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
