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
  protected EventListenerList listenerList = new EventListenerList();

  // Columns
  private static final String[] columnNames = { "Name", "State", "Priority", "wait", "IOwait", "CPU Bursts", "IO Bursts", "turnaround", "Arrival", "Finish"};

  
  public ProcessTableModel() {
  }

  // sets the current process Objects to be viewed in the table
  public void setProcesses(List<PCB> processes) {
    this.processes = processes;
    fireTableChanged(new TableModelEvent(this));
  }

  // force a refresh of the table
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
      case 1 -> {return process.getState();}
      case 2 -> {return process.getPriority();}
      case 3 -> {return process.getWaitingTime();}
      case 4 -> {return process.getIoWaitTime();}
      case 5 -> {return arr2String(process.getCpuBurst());}
      case 6 -> {return arr2String(process.getIoBurst());}
      case 7 -> {return process.getTurnaroundTime();}
      case 8 -> {return process.getArrivalTime();}
      case 9 -> {
        var finishTime = process.getFinishTime();
        
        if(finishTime == -1) {
          return "N/A";
        }
        return finishTime;
      }
      default -> {return -999;}
    }
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    switch(columnIndex) {
      case 0,1 -> {return String.class;}
      case 2,3,4,5,6,7,8,9 -> {return Integer.class;}
      // 9 can be String or Integer
      default -> {return Object.class;}
    }
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

    return temp.toString().trim();
  }
}
