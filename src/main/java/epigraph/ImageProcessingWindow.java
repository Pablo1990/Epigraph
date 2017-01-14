package epigraph;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import fiji.util.gui.OverlayedImageCanvas;
import ij.ImagePlus;
import net.coobird.thumbnailator.Thumbnails;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Canvas;
import javax.swing.SwingConstants;

/**
 * 
 * @author Pablo Vicente-Munuera
 *
 */
public class ImageProcessingWindow extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int CANVAS_SIZE = 512;

	private JPanel contentPane;

	private ArrayList<GraphletImage> newGraphletImages;
	private JTextField tfImageName;

	private OverlayedImageCanvas canvas;

	private JButton btnCalculateGraphlets;

	private JButton btnCreateRoi;

	private JComboBox<String> cbSelectedShape;

	private JLabel lblRadius;

	private JSpinner inputRadiusNeigh;

	private JButton btnTestNeighbours;

	private JButton btnPickAColor;

	private JPanel colorPicked;

	private JLabel lblImageName;
	private JButton btnAddToTable;

	private GraphletImage newGraphletImage;

	private JComboBox<String> cbGraphletsMode;
	
	private JLabel legend;

	private JLabel Lsquares;
	private JLabel Lpentagons;
	private JLabel Lhexagons;
	private JLabel Lheptagons;
	private JLabel Loctogons;
	private JLabel lblShape;

	private boolean modeSelectionCells;

	private AbstractButton btnSelectCells;
	private JLabel LTitlePoligonDistr;

	/**
	 * Create the frame.
	 * 
	 * @param raw_img
	 * @param tableInfo
	 */
	public ImageProcessingWindow(ImagePlus raw_img, JTableModel tableInfo) {
		super();
		setModal(true);
		newGraphletImages = new ArrayList<GraphletImage>();
		modeSelectionCells = false;
		setBounds(100, 100, 972, 798);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(null);
		setContentPane(contentPane);

		ImagePlus imgToShow = new ImagePlus("", raw_img.getChannelProcessor());
		BufferedImage thumbnail = null;
		try {
			thumbnail = Thumbnails.of(imgToShow.getBufferedImage()).height(CANVAS_SIZE).width(CANVAS_SIZE)
					.asBufferedImage();
			imgToShow.setImage(thumbnail);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		canvas = new OverlayedImageCanvas(imgToShow);
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (modeSelectionCells) {
					if (newGraphletImage.addCellToSelected(e.getX(), e.getY()) == -1) {
						JOptionPane.showMessageDialog(canvas, "No cell selected");
					}
				}
			}
		});
		canvas.setLocation(199, 42);
		canvas.setShowCursorStatus(false);
		canvas.setShowAllROIs(false);
		canvas.setCustomRoi(false);
		canvas.setSize(CANVAS_SIZE, CANVAS_SIZE);

		newGraphletImage = new GraphletImage(raw_img);
		
		
		
		
		btnCalculateGraphlets = new JButton("Calculate graphlets!");
		btnCalculateGraphlets.setBounds(199, 596, 329, 49);
		btnCalculateGraphlets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (newGraphletImage.getDistanceGDDH() == -1) {
					newGraphletImage.runGraphlets(raw_img, cbSelectedShape.getSelectedIndex(),
							(int) inputRadiusNeigh.getValue(), (int) cbGraphletsMode.getSelectedIndex());
					btnAddToTable.setEnabled(true);
				}
				newGraphletImage.setLabelName(tfImageName.getText());
				newGraphletImage.setColor(colorPicked.getBackground());
			}
		});

		btnCreateRoi = new JButton("Create RoI");
		btnCreateRoi.setBounds(755, 392, 124, 25);
		btnCreateRoi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createROI(imgToShow);
			}
		});
		contentPane.setLayout(null);
		contentPane.add(canvas);
		contentPane.add(btnCreateRoi);
		contentPane.add(btnCalculateGraphlets);

		tfImageName = new JTextField();
		tfImageName.setBounds(755, 458, 146, 26);
		contentPane.add(tfImageName);
		tfImageName.setColumns(10);

		lblImageName = new JLabel("Image Name");
		lblImageName.setLabelFor(tfImageName);
		lblImageName.setBounds(754, 436, 113, 20);
		contentPane.add(lblImageName);

		colorPicked = new JPanel();
		colorPicked.setBackground(Color.BLACK);
		colorPicked.setBounds(755, 555, 113, 25);
		contentPane.add(colorPicked);

		btnPickAColor = new JButton("Pick a color");
		btnPickAColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				Color c = JColorChooser.showDialog(btnPickAColor.getParent(), "Choose a Color",
						colorPicked.getBackground());
				if (c != null) {
					colorPicked.setBackground(c);
					if (newGraphletImage != null) {
						newGraphletImage.setColor(c);
					}
				}

			}
		});
		btnPickAColor.setBounds(752, 510, 115, 29);
		contentPane.add(btnPickAColor);

		
		
		btnTestNeighbours = new JButton("Test Neighbours");
		btnTestNeighbours.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Lsquares.setText(newGraphletImage.testNeighbours(raw_img, cbSelectedShape.getSelectedIndex(),
						(int) inputRadiusNeigh.getValue(), imgToShow).get(0));
				Lpentagons.setText(newGraphletImage.testNeighbours(raw_img, cbSelectedShape.getSelectedIndex(),
						(int) inputRadiusNeigh.getValue(), imgToShow).get(1));
				Lhexagons.setText(newGraphletImage.testNeighbours(raw_img, cbSelectedShape.getSelectedIndex(),
						(int) inputRadiusNeigh.getValue(), imgToShow).get(2));
				Lheptagons.setText(newGraphletImage.testNeighbours(raw_img, cbSelectedShape.getSelectedIndex(),
						(int) inputRadiusNeigh.getValue(), imgToShow).get(3));
				Loctogons.setText(newGraphletImage.testNeighbours(raw_img, cbSelectedShape.getSelectedIndex(),
						(int) inputRadiusNeigh.getValue(), imgToShow).get(4));
				
				legend.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("/legend.jpg")).getImage()));
				LTitlePoligonDistr.setText("Tested polygon distribution:");
				canvas.repaint();

			}
		});
		btnTestNeighbours.setBounds(755, 180, 162, 29);
		contentPane.add(btnTestNeighbours);

		inputRadiusNeigh = new JSpinner();
		inputRadiusNeigh.setModel(new SpinnerNumberModel(3, 1, 25, 1));
		inputRadiusNeigh.setBounds(826, 42, 72, 26);
		contentPane.add(inputRadiusNeigh);

		lblRadius = new JLabel("Radius");
		lblRadius.setLabelFor(inputRadiusNeigh);
		lblRadius.setBounds(757, 45, 69, 20);
		contentPane.add(lblRadius);

		cbSelectedShape = new JComboBox<String>();
		cbSelectedShape.setModel(new DefaultComboBoxModel<String>(new String[] { "Circle", "Square" }));
		cbSelectedShape.setSelectedIndex(0);
		cbSelectedShape.setBounds(755, 124, 156, 26);
		contentPane.add(cbSelectedShape);

		btnAddToTable = new JButton("Add to table");
		btnAddToTable.setEnabled(false);
		btnAddToTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tfImageName.getText().isEmpty()) {
					JOptionPane.showMessageDialog(btnAddToTable.getParent(), "You should insert a name for the image");
				} else {
					newGraphletImage.setLabelName(tfImageName.getText());
					int result = JOptionPane.showConfirmDialog(btnAddToTable.getParent(), "Everything is ok?",
							"Confirm", JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.OK_OPTION)
						tableInfo.addImage(newGraphletImage);
				}
			}
		});
		btnAddToTable.setBounds(558, 606, 153, 29);
		contentPane.add(btnAddToTable);

		cbGraphletsMode = new JComboBox<String>();
		cbGraphletsMode.setModel(new DefaultComboBoxModel<String>(new String[] { "Total (25 graphlets)",
				"Total Partial (16 graphlets)", "Basic (9 graphlets)", "Basic Partial (7 graphlets) " }));
		cbGraphletsMode.setSelectedIndex(0);
		cbGraphletsMode.setBounds(755, 245, 162, 26);
		contentPane.add(cbGraphletsMode);

		Lsquares = new JLabel("");
		Lsquares.setHorizontalAlignment(SwingConstants.CENTER);
		Lsquares.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		Lsquares.setBounds(121, 118, 80, 16);
		contentPane.add(Lsquares);
		
		Lpentagons = new JLabel("");
		Lpentagons.setHorizontalAlignment(SwingConstants.CENTER);
		Lpentagons.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		Lpentagons.setBounds(121, 197, 80, 16);
		contentPane.add(Lpentagons);
		
		Lhexagons = new JLabel("");
		Lhexagons.setHorizontalAlignment(SwingConstants.CENTER);
		Lhexagons.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		Lhexagons.setBounds(121, 279, 80, 16);
		contentPane.add(Lhexagons);
		
		Lheptagons = new JLabel("");
		Lheptagons.setHorizontalAlignment(SwingConstants.CENTER);
		Lheptagons.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		Lheptagons.setBounds(121, 360, 80, 16);
		contentPane.add(Lheptagons);
		
		Loctogons = new JLabel("");
		Loctogons.setHorizontalAlignment(SwingConstants.CENTER);
		Loctogons.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		Loctogons.setBounds(121, 440, 80, 16);
		contentPane.add(Loctogons);
		
		legend = new JLabel("");
		legend.setBounds(36, 87, 80, 424);
		contentPane.add(legend);

		LTitlePoligonDistr = new JLabel("");
		LTitlePoligonDistr.setHorizontalAlignment(SwingConstants.CENTER);
		LTitlePoligonDistr.setBounds(15, 22, 169, 49);
		contentPane.add(LTitlePoligonDistr);
		
		

		lblShape = new JLabel("Shape");
		lblShape.setLabelFor(cbSelectedShape);
		lblShape.setBounds(755, 101, 56, 16);
		contentPane.add(lblShape);

		btnSelectCells = new JButton("Select cells");
		btnSelectCells.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (modeSelectionCells) {
					modeSelectionCells = false;
					btnSelectCells.setBackground(new Color(212, 208, 200));
				} else {
					modeSelectionCells = true;
					btnSelectCells.setBackground(Color.red);
				}
			}
		});
		btnSelectCells.setBounds(755, 343, 124, 25);
		contentPane.add(btnSelectCells);
		
		
		
		
		
		
		

		
	}

	private void createROI(ImagePlus imgToShow) {
		// TODO Auto-generated method stub

		// imgToShow.getChannelProcessor().drawRect(x, y, width, height);
		//
		// WaitForUserDialog wtd = new WaitForUserDialog("USER ROI
		// SELECTION","select a Roi");
		// wtd.show();
		// Roi newRoi = new Roi();
		// imgToShow.getChannelProcessor().drawRoi(newRoi);
	}
}
