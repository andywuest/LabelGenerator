package de.jos.labelgenerator.dialog.preferences;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.jdesktop.application.Action;

import com.google.common.collect.ImmutableSortedSet;

import de.jos.labelgenerator.LabelGeneratorApp;
import de.jos.labelgenerator.combobox.AbstractComboBoxItem;
import de.jos.labelgenerator.combobox.GMailGroupComboBoxItem;
import de.jos.labelgenerator.configuration.ApplicationConfiguration;
import de.jos.labelgenerator.configuration.GMailGroup;
import de.jos.labelgenerator.configuration.Preferences;
import de.jos.labelgenerator.configuration.addressProvider.GMailAddressProvider;

public class PreferencesDialogLogic implements PreferencesDialogConstants {

	private PreferencesDialogPanel preferencesDialogPanel = null;

	private JDialog dialog = null;

	public PreferencesDialogLogic(JDialog dialog) {
		this.dialog = dialog;
	}

	@Action(name = ACTION_CANCEL_BUTTON_PRESSED)
	public void cancelButtonPressed(final ActionEvent actionEvent) {
		System.out.println("cancelAddressDialog");
		dialog.setVisible(false);
		dialog.dispose();
	}

	@Action(name = ACTION_OK_BUTTON_PRESSED)
	public void okButtonPressed(final ActionEvent actionEvent) {
		if (preferencesDialogPanel != null) {
			final Preferences preferences = LabelGeneratorApp.getApplicationConfiguration().getPreferences();
			preferences.setCheckboxFilesystemProvider(preferencesDialogPanel.getCheckboxFilesystemProvider()
					.isSelected());
			preferences.setCheckboxGMailProvider(preferencesDialogPanel.getCheckboxGMailProvider().isSelected());
			preferences.setGmailEmail(preferencesDialogPanel.getTextFieldGMailEmail().getText());
			preferences.setGmailPassword(new String(preferencesDialogPanel.getPasswordFieldGMailPassword()
					.getPassword()));
			if (preferencesDialogPanel.getComboBoxGMailGroup().getSelectedItem() != null) {
				preferences.setGmailGroup(preferencesDialogPanel.getComboBoxGMailGroup().getSelectedItem().getValue());
			}
		}
		dialog.setVisible(false);
		dialog.dispose();
	}

	@Action(name = ACTION_TEST_BUTTON_PRESSED)
	public void testButtonPressed(final ActionEvent actionEvent) throws Exception {
		System.out.println("test button");
		final String email = preferencesDialogPanel.getTextFieldGMailEmail().getText();
		final String password = new String(preferencesDialogPanel.getPasswordFieldGMailPassword().getPassword());
		final GMailAddressProvider gmailProvider = new GMailAddressProvider(email, password);
		final boolean credentialsOk = gmailProvider.isLoginSuccessful();

		if (credentialsOk == false) {
			JOptionPane.showMessageDialog(dialog, "Credentials invalid.", "Error", JOptionPane.ERROR_MESSAGE);
		}

		// get the groups
		if (credentialsOk) {
			// remove all old groups
			preferencesDialogPanel.getComboBoxGMailGroup().removeAllItems();
			final List<GMailGroupComboBoxItem> comboBoxItems = new ArrayList<GMailGroupComboBoxItem>();
			final Map<String, String> contactGroups = gmailProvider.getContactGroups();
			for (final Map.Entry<String, String> tmpEntry : contactGroups.entrySet()) {
				final GMailGroup gmailGroup = new GMailGroup(tmpEntry.getKey(), tmpEntry.getValue());
				// add group
				comboBoxItems.add(new GMailGroupComboBoxItem(gmailGroup));
			}

			// add the sorted items to the comboBox
			// TODO improve generics here !
			preferencesDialogPanel.getComboBoxGMailGroup().addItems(
					ImmutableSortedSet.orderedBy(AbstractComboBoxItem.nameOrdering).addAll(comboBoxItems).build());

			// preselect the group from the configuration
			final ApplicationConfiguration configuration = LabelGeneratorApp.getApplicationConfiguration();
			final GMailGroup selectedGMailGroup = configuration.getPreferences().getGmailGroup();
			if (selectedGMailGroup != null) {
				preferencesDialogPanel.getComboBoxGMailGroup().selectItemWithText(
						new GMailGroupComboBoxItem(selectedGMailGroup));
			}
		}

	}

	@Action(name = ACTION_GMAIL_PROVIDER_CHECKBOX_CLICKED)
	public void GMailProviderClicked(final ActionEvent actionEvent) {
		if (preferencesDialogPanel != null) {
			boolean gmailProviderSelected = preferencesDialogPanel.getCheckboxGMailProvider().isSelected();
			preferencesDialogPanel.getTextFieldGMailEmail().setEnabled(gmailProviderSelected);
			preferencesDialogPanel.getPasswordFieldGMailPassword().setEnabled(gmailProviderSelected);
			preferencesDialogPanel.getComboBoxGMailGroup().setEnabled(gmailProviderSelected);
		}
	}

	public void setPreferencesDialogPanel(PreferencesDialogPanel preferencesDialogPanel) {
		this.preferencesDialogPanel = preferencesDialogPanel;
	}

}
