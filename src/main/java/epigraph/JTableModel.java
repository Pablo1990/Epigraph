/**
 * 
 */
package epigraph;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * @author Pedro Gomez-Galvez
 *
 */
class JTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] columnNames = { "Color", "Label", "GDDH", "GDDRV", "% Hexagons", "Kind" ,"Visualizing" };

	private ArrayList<BasicGraphletImage> allGraphletImages;
	private ArrayList<Boolean> listOfVisualizing;
	private ArrayList<String> listOfModes;

	private int[] rgbs;
	
	/**
	 * 
	 */
	public JTableModel() {
		super();
		allGraphletImages = new ArrayList<BasicGraphletImage>();
		listOfVisualizing = new ArrayList<Boolean>();
		listOfModes = new ArrayList<String>();
	}

	/**
	 * 
	 */
	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return allGraphletImages.size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	

	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return allGraphletImages.get(row).getColor();
		case 1:
			return allGraphletImages.get(row).getLabelName();
		case 2:
			return allGraphletImages.get(row).getDistanceGDDH();
		case 3:
			return allGraphletImages.get(row).getDistanceGDDRV();
		case 4:
			return allGraphletImages.get(row).getPercentageOfHexagons();
		case 5:
			return listOfModes.get(row);
		case 6:
			return listOfVisualizing.get(row);
		}
		return null;
	}

	/**
	 * JTable uses this method to determine the default renderer/ editor for
	 * each cell. If we didn't implement this method, then the last column would
	 * contain text ("true"/"false"), rather than a check box.
	 */
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/**
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {
		return true;
	}

	/**
	 * Don't need to implement this method unless your table's data can change.
	 */
	public void setValueAt(Object value, int row, int col) {
		switch (col) {
		case 0:
			allGraphletImages.get(row).setColor((Color) value);
			break;
		case 1:
			allGraphletImages.get(row).setLabelName((String) value);
			break;
		case 2:
			allGraphletImages.get(row).setDistanceGDDH((float) value);
			break;
		case 3:
			allGraphletImages.get(row).setDistanceGDDRV((float) value);
			break;
		case 4:
			allGraphletImages.get(row).setPercentageOfHexagons((float) value);
			break;
		case 5:
			listOfModes.set(row, (String) value);
		case 6:
			listOfVisualizing.set(row, (Boolean) value);
			break;
		}
		//Updating table
		fireTableCellUpdated(row, col);
	}

	/**
	 * @return the allGraphletImages
	 */
	public ArrayList<BasicGraphletImage> getAllGraphletImages() {
		return allGraphletImages;
	}

	/**
	 * @param allGraphletImages
	 *            the allGraphletImages to set
	 */
	public void setAllGraphletImages(ArrayList<BasicGraphletImage> allGraphletImages) {
		this.allGraphletImages = allGraphletImages;
	}

	/**
	 * @return the listOfVisualizing
	 */
	public ArrayList<Boolean> getListOfVisualizing() {
		return listOfVisualizing;
	}

	/**
	 * @param listOfVisualizing the listOfVisualizing to set
	 */
	public void setListOfVisualizing(ArrayList<Boolean> listOfVisualizing) {
		this.listOfVisualizing = listOfVisualizing;
	}

	/**
	 * 
	 * @param newImages
	 */
	public void addImages(ArrayList<BasicGraphletImage> newImages) {
		allGraphletImages.addAll(newImages);
		
		for (int i = 0; i < newImages.size(); i++){
			listOfVisualizing.add(true);
		}
		fireTableDataChanged();
	}
	
	public void addImage(BasicGraphletImage newImage, String graphletsMode){
		allGraphletImages.add(new BasicGraphletImage(newImage));
		listOfVisualizing.add(true);
		listOfModes.add(graphletsMode);

		fireTableDataChanged();
	}

}