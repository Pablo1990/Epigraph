package epigraph;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;



public class JPanelModel extends JPanel {
  private boolean DEBUG = false;

  public JPanelModel() {
    super(new GridLayout(1, 0));
    
    JPanel panel = new JPanel();
    panel.setBorder(new LineBorder(new Color(0, 0, 0)));
    add(panel);
    
    //Create buttons
    JButton btnVisualize = new JButton("Visualize");
    btnVisualize.setBounds(342, 255, 93, 29);
    btnVisualize.setBackground(Color.yellow);
    
    
    JButton btnOpenButton = new JButton("Open");
    btnOpenButton.setBounds(15, 255, 71, 29);
    panel.setLayout(null);
    
	//Create table and scroll pane  
    JTable table = new JTable(new JTableModel());
    table.setPreferredScrollableViewportSize(new Dimension(500, 70));
  //Set up renderer and editor for the Favorite Color column.
    table.setDefaultRenderer(Color.class,
                             new ColorRenderer(true));
    table.setDefaultEditor(Color.class,
            new JColorEditor());
    //Create the scroll pane and add the table to it.
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBounds(15, 27, 420, 196);
    //scrollPane.setPreferredSize(new Dimension(400, 200));
    
    
    
    panel.add(scrollPane);
    panel.add(btnOpenButton);
    panel.add(btnVisualize);
    
    
  }

  class JTableModel extends AbstractTableModel {
    private String[] columnNames = { "Color", "GDDH", "Label","Visualizing" };

   
    
    private Object[][] data = {
    		
        { Color.black, "Huml",  new Integer(3), new Boolean(true) },
        
        { Color.black, "Kathy",  new Integer(2),
            new Boolean(false) },
        { Color.black, "Sharon",  new Integer(20),
            new Boolean(true) },
        { Color.red, "Philip",  new Integer(10),
            new Boolean(false) } };
    
    
    
    

    public int getColumnCount() {
      return columnNames.length;
    }

    public int getRowCount() {
      return data.length;
    }

    public String getColumnName(int col) {
      return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
      return data[row][col];
    }

    /*
     * JTable uses this method to determine the default renderer/ editor for
     * each cell. If we didn't implement this method, then the last column
     * would contain text ("true"/"false"), rather than a check box.
     */
    public Class getColumnClass(int c) {
      return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's editable.
     */
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    /*
     * Don't need to implement this method unless your table's data can
     * change.
     */
    public void setValueAt(Object value, int row, int col) {
      if (DEBUG) {
        System.out.println("Setting value at " + row + "," + col
            + " to " + value + " (an instance of "
            + value.getClass() + ")");
      }

      data[row][col] = value;
      fireTableCellUpdated(row, col);

      if (DEBUG) {
        System.out.println("New value of data:");
        printDebugData();
      }
    }

    private void printDebugData() {
      int numRows = getRowCount();
      int numCols = getColumnCount();

      for (int i = 0; i < numRows; i++) {
        System.out.print("    row " + i + ":");
        for (int j = 0; j < numCols; j++) {
          System.out.print("  " + data[i][j]);
        }
        System.out.println();
      }
      System.out.println("--------------------------");
    }
  }

  /**
   * Create the GUI and show it. For thread safety, this method should be
   * invoked from the event-dispatching thread.
   */
  private static void createAndShowGUI() {
    //Make sure we have nice window decorations.
    JFrame.setDefaultLookAndFeelDecorated(true);

    //Create and set up the window.
    JFrame frame = new JFrame("TableDemo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //Create and set up the content pane.
    JPanelModel newContentPane = new JPanelModel();
    newContentPane.setOpaque(true); //content panes must be opaque
    frame.setContentPane(newContentPane);

    //Display the window.
    frame.pack();
    frame.setSize(500, 400);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    //Schedule a job for the event-dispatching thread:
    //creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }
}
