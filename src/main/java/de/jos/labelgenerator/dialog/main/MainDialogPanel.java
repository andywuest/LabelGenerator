package de.jos.labelgenerator.dialog.main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.jos.labelgenerator.LabelGeneratorApp;
import de.jos.labelgenerator.combobox.ComboBoxLayout;
import de.jos.labelgenerator.dialog.AbstractPanel;

@SuppressWarnings("serial")
public class MainDialogPanel extends AbstractPanel implements MainDialogConstants {

	private ComboBoxLayout comboBoxLayout = null;

	private JLabel labelLayout = null;

	private JPanel panelLayoutPreview = null;

	private JButton buttonPrint = null;

	private JCheckBox checkBoxDrawGrid = null;

	private JLabel labelWidth = null;
	private JLabel labelWidthValue = null;

	private JLabel labelHeight = null;
	private JLabel labelHeightValue = null;

	private JLabel labelMarginTop = null;
	private JLabel labelMarginTopValue = null;

	private JLabel labelMarginLeft = null;
	private JLabel labelMarginLeftValue = null;

	private JLabel labelFont = null;
	private JLabel labelFontValue = null;

	private JLabel labelWidthNext = null;
	private JLabel labelWidthNextValue = null;

	private JLabel labelHeightNext = null;
	private JLabel labelHeightNextValue = null;

	private JLabel labelTextMarginTop = null;
	private JLabel labelTextMarginTopValue = null;

	private JLabel labelTextMarginLeft = null;
	private JLabel labelTextMarginLeftValue = null;

	private JLabel labelTextMargin = null;
	private JLabel labelTextMarginValue = null;

	private final ApplicationContext applicationContext = Application.getInstance(LabelGeneratorApp.class).getContext();

	private final ResourceMap resourceMap = applicationContext.getResourceMap(MainDialogPanel.class);

	public MainDialogPanel(final ApplicationActionMap applicationActionMap) {
		super(applicationActionMap);
		setLayout(null);

		initComponents();
	}

	public void initComponents() {
		labelLayout = new JLabel("Layout");

		comboBoxLayout = new ComboBoxLayout();
		comboBoxLayout.setAction(getAction(ACTION_LAYOUT_CHANGED));

		labelWidth = new JLabel("Width : ");
		labelWidthValue = new JLabel("");

		labelHeight = new JLabel("Height : ");
		labelHeightValue = new JLabel("");

		labelMarginTop = new JLabel("Margin Top: ");
		labelMarginTopValue = new JLabel("");

		labelMarginLeft = new JLabel("Margin Left: ");
		labelMarginLeftValue = new JLabel("");

		labelFont = new JLabel("Font: ");
		labelFontValue = new JLabel("");

		labelWidthNext = new JLabel("Width Next: ");
		labelWidthNextValue = new JLabel("");

		labelHeightNext = new JLabel("Height Next: ");
		labelHeightNextValue = new JLabel("");

		labelTextMarginTop = new JLabel("Text margin top: ");
		labelTextMarginTopValue = new JLabel("");

		labelTextMarginLeft = new JLabel("Text margin left: ");
		labelTextMarginLeftValue = new JLabel("");

		labelTextMargin = new JLabel("Text margin: ");
		labelTextMarginValue = new JLabel("");

		panelLayoutPreview = new JPanel();

		buttonPrint = new JButton();
		buttonPrint.setAction(getAction(ACTION_PRINT));
		buttonPrint.setText(resourceMap.getString("button.print"));

		checkBoxDrawGrid = new JCheckBox();
		checkBoxDrawGrid.setText(resourceMap.getString("checkbox.drawGrid"));
		checkBoxDrawGrid.setSelected(false);

		final FormLayout layout = new FormLayout(
				"4dlu, fill:default, 4dlu, fill:default, 4dlu, fill:default:grow, 4dlu", // column
				"4dlu, 18dlu, 4dlu, 18dlu, 4dlu, 18dlu, 4dlu, 18dlu, 4dlu, 18dlu, 4dlu, 18dlu, 4dlu, 18dlu, 4dlu, 18dlu, 4dlu, 18dlu, 4dlu, 18dlu, 4dlu, 18dlu, 4dlu,fill:p:grow, 4dlu, 18dlu, 4dlu, 18dlu, 4dlu"); // row

		final PanelBuilder builder = new PanelBuilder(layout, this);

		final CellConstraints cc = new CellConstraints();

		builder.add(panelLayoutPreview, cc.xywh(6, 2, 1, 27));

		builder.add(checkBoxDrawGrid, cc.xyw(2, 26, 2));

		builder.add(buttonPrint, cc.xy(2, 28));

		builder.nextRow();

		addLabels(builder, labelLayout, comboBoxLayout);
		addLabels(builder, labelWidth, labelWidthValue);
		addLabels(builder, labelHeight, labelHeightValue);
		addLabels(builder, labelMarginTop, labelMarginTopValue);
		addLabels(builder, labelMarginLeft, labelMarginLeftValue);
		addLabels(builder, labelFont, labelFontValue);
		addLabels(builder, labelWidthNext, labelWidthNextValue);
		addLabels(builder, labelHeightNext, labelHeightNextValue);
		addLabels(builder, labelTextMarginLeft, labelTextMarginLeftValue);
		addLabels(builder, labelTextMarginTop, labelTextMarginTopValue);
		addLabels(builder, labelTextMargin, labelTextMarginValue);
	}

	private void addLabels(PanelBuilder builder, JLabel label1, JComponent label2) {
		builder.setColumn(1);
		builder.nextColumn(1);
		builder.add(label1);
		builder.nextColumn(2);
		builder.add(label2);
		builder.nextRow(2);
	}

	public ComboBoxLayout getComboBoxLayout() {
		return comboBoxLayout;
	}

	public JPanel getPanelLayoutPreview() {
		return panelLayoutPreview;
	}

	public static void main(final String args[]) {
		try {
			// Method to test the Panel
			final JFrame frame = new JFrame();
			final MainDialogPanel panel = new MainDialogPanel(null);
			frame.setContentPane(panel);
			frame.setSize(1000, 600);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(final WindowEvent e) {
					System.exit(0);
				};
			});
			frame.setVisible(true);
		} catch (final Throwable exception) {
			exception.printStackTrace(System.out);
		}
	}

	public JLabel getLabelLayout() {
		return labelLayout;
	}

	public JButton getButtonPrint() {
		return buttonPrint;
	}

	public JLabel getLabelWidth() {
		return labelWidth;
	}

	public JLabel getLabelWidthValue() {
		return labelWidthValue;
	}

	public JLabel getLabelHeight() {
		return labelHeight;
	}

	public JLabel getLabelHeightValue() {
		return labelHeightValue;
	}

	public JLabel getLabelMarginTop() {
		return labelMarginTop;
	}

	public JLabel getLabelMarginTopValue() {
		return labelMarginTopValue;
	}

	public JLabel getLabelMarginLeft() {
		return labelMarginLeft;
	}

	public JLabel getLabelMarginLeftValue() {
		return labelMarginLeftValue;
	}

	public JLabel getLabelFont() {
		return labelFont;
	}

	public JLabel getLabelFontValue() {
		return labelFontValue;
	}

	public JLabel getLabelWidthNext() {
		return labelWidthNext;
	}

	public JLabel getLabelWidthNextValue() {
		return labelWidthNextValue;
	}

	public JLabel getLabelHeightNext() {
		return labelHeightNext;
	}

	public JLabel getLabelHeightNextValue() {
		return labelHeightNextValue;
	}

	public JLabel getLabelTextMarginTop() {
		return labelTextMarginTop;
	}

	public JLabel getLabelTextMarginTopValue() {
		return labelTextMarginTopValue;
	}

	public JLabel getLabelTextMarginLeft() {
		return labelTextMarginLeft;
	}

	public JLabel getLabelTextMarginLeftValue() {
		return labelTextMarginLeftValue;
	}

	public JLabel getLabelTextMarginValue() {
		return labelTextMarginValue;
	}

	public JCheckBox getCheckBoxDrawGrid() {
		return checkBoxDrawGrid;
	}

}
