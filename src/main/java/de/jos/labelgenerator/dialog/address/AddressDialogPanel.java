package de.jos.labelgenerator.dialog.address;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.jos.labelgenerator.LabelGeneratorApp;
import de.jos.labelgenerator.combobox.AddressComboBoxItem;
import de.jos.labelgenerator.combobox.ComboBoxAddress;
import de.jos.labelgenerator.combobox.ComboBoxTemplate;
import de.jos.labelgenerator.combobox.TemplateComboBoxItem;
import de.jos.labelgenerator.configuration.Address;
import de.jos.labelgenerator.configuration.Template;
import de.jos.labelgenerator.dialog.AbstractPanel;

@SuppressWarnings("serial")
public class AddressDialogPanel extends AbstractPanel implements AddressDialogConstants {

	private JLabel labelAddress = null;

	private JLabel labelTemplate = null;

	private ComboBoxAddress comboBoxAddress = null;

	private ComboBoxTemplate comboBoxTemplate = null;

	private JButton buttonOk = null;

	private JButton buttonCancel = null;

	private JScrollPane scrollPaneAddress = null;

	private JTextArea textAreaAddress = null;

	private List<AddressComboBoxItem> addressItems = new ArrayList<AddressComboBoxItem>();

	private List<TemplateComboBoxItem> templateItems = new ArrayList<TemplateComboBoxItem>();

	private final ApplicationContext applicationContext = Application.getInstance(LabelGeneratorApp.class).getContext();

	private final ResourceMap resourceMap = applicationContext.getResourceMap(AddressDialogPanel.class);

	public AddressDialogPanel(ApplicationActionMap applicationActionMap, List<Address> address, List<Template> templates) {
		super(applicationActionMap);
		setLayout(null);
		for (Address tmpAddress : address) {
			addressItems.add(new AddressComboBoxItem(tmpAddress));
		}
		for (Template tmpTemplate : templates) {
			templateItems.add(new TemplateComboBoxItem(tmpTemplate));
		}
		initComponents();
	}

	public void initComponents() {

		labelAddress = new JLabel(resourceMap.getString("label.address") + ":");

		labelTemplate = new JLabel(resourceMap.getString("label.template") + ":");

		comboBoxAddress = new ComboBoxAddress();
		comboBoxAddress.setAction(getAction(ACTION_ADDRESS_CHANGED));
		for (AddressComboBoxItem tmpAddressItem : addressItems) {
			comboBoxAddress.addItem(tmpAddressItem);
		}

		comboBoxTemplate = new ComboBoxTemplate();
		comboBoxTemplate.setAction(getAction(ACTION_TEMPLATE_CHANGED));
		for (TemplateComboBoxItem tmpTemplateItem : templateItems) {
			comboBoxTemplate.addItem(tmpTemplateItem);
		}

		// textarea for level data
		textAreaAddress = new JTextArea();

		scrollPaneAddress = new JScrollPane(textAreaAddress);

		buttonOk = new JButton();
		buttonOk.setAction(getAction(ACTION_OK_BUTTON_PRESSED));
		buttonOk.setText(resourceMap.getString("button.ok"));

		buttonCancel = new JButton();
		buttonCancel.setAction(getAction(ACTION_CANCEL_BUTTON_PRESSED));
		buttonCancel.setText(resourceMap.getString("button.cancel"));

		final FormLayout layout = new FormLayout("4dlu, 40dlu, fill:default:grow, 4dlu", // column
				"4dlu, 20dlu, 4dlu, 20dlu, 4dlu, fill:p:grow, 4dlu, 20dlu, 4dlu"); // row

		final PanelBuilder builder = new PanelBuilder(layout, this);

		final CellConstraints cc = new CellConstraints();

		builder.nextRow();
		builder.nextColumn(1);
		builder.add(labelAddress);
		builder.nextColumn(1);
		builder.add(comboBoxAddress);
		builder.nextRow(2);

		builder.setColumn(1);
		builder.nextColumn(1);
		builder.add(labelTemplate);
		builder.nextColumn(1);
		builder.add(comboBoxTemplate);
		builder.nextRow(2);

		builder.setColumn(1);
		builder.nextColumn(1);
		builder.add(scrollPaneAddress, cc.xywh(2, 6, 2, 1));
		builder.nextRow(2);

		builder.setColumn(1);
		builder.nextColumn(2);
		builder.add(ButtonBarFactory.buildRightAlignedBar(buttonCancel, buttonOk));
	}

	public ComboBoxAddress getComboBoxAddress() {
		return comboBoxAddress;
	}

	public JTextArea getTextAreaAddress() {
		return textAreaAddress;
	}

	public ComboBoxTemplate getComboBoxTemplate() {
		return comboBoxTemplate;
	}

}
