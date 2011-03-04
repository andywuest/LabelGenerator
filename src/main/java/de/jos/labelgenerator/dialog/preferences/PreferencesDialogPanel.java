package de.jos.labelgenerator.dialog.preferences;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.jos.labelgenerator.LabelGeneratorApp;
import de.jos.labelgenerator.dialog.AbstractPanel;

public class PreferencesDialogPanel extends AbstractPanel implements PreferencesDialogConstants {

	private JCheckBox checkboxFilesystemProvider = null;

	private JCheckBox checkboxGMailProvider = null;

	private JTextField textFieldGMailEmail = null;

	private JTextField textFieldGMailPassword = null;

	private JComboBox comboBoxGMailGroup = null;

	private JButton buttonOk = null;

	private JButton buttonCancel = null;

	private JButton buttonTest = null;

	private final ApplicationContext applicationContext = Application.getInstance(LabelGeneratorApp.class).getContext();

	private final ResourceMap resourceMap = applicationContext.getResourceMap(PreferencesDialogPanel.class);

	public PreferencesDialogPanel(ApplicationActionMap applicationActionMap) {
		super(applicationActionMap);
		setLayout(null);
		initComponents();
	}

	public void initComponents() {

		checkboxFilesystemProvider = new JCheckBox();
		checkboxFilesystemProvider.setText(resourceMap.getString("checkbox.filesystemProvider"));
		checkboxFilesystemProvider.setSelected(true);

		checkboxGMailProvider = new JCheckBox();
		checkboxGMailProvider.setText(resourceMap.getString("checkbox.GMailProvider"));
		checkboxGMailProvider.setAction(getAction(ACTION_GMAIL_PROVIDER_CHECKBOX_CLICKED));
		checkboxGMailProvider.setSelected(false);

		buttonOk = new JButton();
		buttonOk.setAction(getAction(ACTION_OK_BUTTON_PRESSED));
		buttonOk.setText(resourceMap.getString("button.ok"));

		buttonCancel = new JButton();
		buttonCancel.setAction(getAction(ACTION_CANCEL_BUTTON_PRESSED));
		buttonCancel.setText(resourceMap.getString("button.cancel"));

		buttonTest = new JButton();
		buttonTest.setAction(getAction(ACTION_TEST_BUTTON_PRESSED));
		buttonTest.setText(resourceMap.getString("button.test"));

		textFieldGMailEmail = new JTextField();
		textFieldGMailEmail.setEnabled(false);

		textFieldGMailPassword = new JTextField();
		textFieldGMailPassword.setEnabled(false);

		comboBoxGMailGroup = new JComboBox();
		comboBoxGMailGroup.setEnabled(false);

		final FormLayout layout = new FormLayout("4dlu, 100dlu, 4dlu, 100dlu, 4dlu, fill:default:grow, 4dlu", // column
				"4dlu, 20dlu, 4dlu, 20dlu, 4dlu, 20dlu, 4dlu, 20dlu, 4dlu, 20dlu, 4dlu, fill:p:grow, 20dlu, 4dlu"); // row

		final PanelBuilder builder = new PanelBuilder(layout, this);

		final CellConstraints cc = new CellConstraints();

		builder.nextRow();
		builder.nextColumn(1);
		builder.add(checkboxFilesystemProvider);
		builder.nextRow(2);
		builder.setColumn(2);
		// builder.nextColumn(1);
		builder.add(checkboxGMailProvider);
		builder.nextRow(2);

		builder.setColumn(4);
		builder.add(textFieldGMailEmail);
		builder.nextRow(2);

		builder.add(textFieldGMailPassword);
		builder.nextColumn(2);
		builder.add(buttonTest);
		builder.nextRow(2);

		builder.setColumn(4);
		builder.add(comboBoxGMailGroup);
		builder.nextRow(3);

		builder.setColumn(4);
		builder.add(ButtonBarFactory.buildRightAlignedBar(buttonCancel, buttonOk));
	}

	public JCheckBox getCheckboxFilesystemProvider() {
		return checkboxFilesystemProvider;
	}

	public JCheckBox getCheckboxGMailProvider() {
		return checkboxGMailProvider;
	}

	public JTextField getTextFieldGMailEmail() {
		return textFieldGMailEmail;
	}

	public JTextField getTextFieldGMailPassword() {
		return textFieldGMailPassword;
	}

	public JComboBox getComboBoxGMailGroup() {
		return comboBoxGMailGroup;
	}

}
