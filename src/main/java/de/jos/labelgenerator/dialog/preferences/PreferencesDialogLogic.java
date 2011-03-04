package de.jos.labelgenerator.dialog.preferences;

import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.jdesktop.application.Action;

import de.jos.labelgenerator.configuration.ApplicationConfiguration;
import de.jos.labelgenerator.configuration.Preferences;
import de.jos.labelgenerator.configuration.addressProvider.GMailAddressProvider;

public class PreferencesDialogLogic implements PreferencesDialogConstants {

	private PreferencesDialogPanel preferencesDialogPanel = null;

	private JDialog dialog = null;

	private ApplicationConfiguration applicationConfiguration = null;

	public PreferencesDialogLogic(JDialog dialog, ApplicationConfiguration applicationConfiguration) {
		this.dialog = dialog;
		this.applicationConfiguration = applicationConfiguration;
	}

	@Action(name = ACTION_CANCEL_BUTTON_PRESSED)
	public void cancelButtonPressed(final ActionEvent actionEvent) {
		System.out.println("cancelAddressDialog");
		dialog.setVisible(false);
		dialog.dispose();
	}

	@Action(name = ACTION_OK_BUTTON_PRESSED)
	public void okButtonPressed(final ActionEvent actionEvent) {
		if (applicationConfiguration != null && applicationConfiguration.getPreferences() != null
				&& preferencesDialogPanel != null) {
			final Preferences preferences = applicationConfiguration.getPreferences();
			preferences.setCheckboxFilesystemProvider(preferencesDialogPanel.getCheckboxFilesystemProvider()
					.isSelected());
			preferences.setCheckboxGMailProvider(preferencesDialogPanel.getCheckboxGMailProvider().isSelected());
			preferences.setGmailEmail(preferencesDialogPanel.getTextFieldGMailEmail().getText());
			preferences.setGmailGroup(preferencesDialogPanel.getComboBoxGMailGroup().getSelectedItem().toString());
		}

		dialog.setVisible(false);
		dialog.dispose();
	}

	@Action(name = ACTION_TEST_BUTTON_PRESSED)
	public void testButtonPressed(final ActionEvent actionEvent) throws Exception {
		System.out.println("test button");
		final String email = preferencesDialogPanel.getTextFieldGMailEmail().getText();
		final String password = preferencesDialogPanel.getTextFieldGMailPassword().getText();
		final GMailAddressProvider gmailProvider = new GMailAddressProvider(email, password);
		final boolean credentialsOk = gmailProvider.isLoginSuccessful();

		if (credentialsOk == false) {
			JOptionPane.showMessageDialog(dialog, "Credentials invalid.", "Error", JOptionPane.ERROR_MESSAGE);
		}

		// get the groups
		if (credentialsOk) {
			Map<String, String> contactGroups = gmailProvider.getContactGroups();
			for (Map.Entry<String, String> tmpEntry : contactGroups.entrySet()) {
				preferencesDialogPanel.getComboBoxGMailGroup().addItem(tmpEntry.getKey());
			}
			System.out.println(contactGroups);
		}

	}

	@Action(name = ACTION_GMAIL_PROVIDER_CHECKBOX_CLICKED)
	public void GMailProviderClicked(final ActionEvent actionEvent) {
		if (preferencesDialogPanel != null) {
			if (preferencesDialogPanel.getCheckboxGMailProvider().isSelected()) {
				preferencesDialogPanel.getTextFieldGMailEmail().setEnabled(true);
				preferencesDialogPanel.getTextFieldGMailPassword().setEnabled(true);
				preferencesDialogPanel.getComboBoxGMailGroup().setEnabled(true);
			} else {
				preferencesDialogPanel.getTextFieldGMailEmail().setEnabled(false);
				preferencesDialogPanel.getTextFieldGMailPassword().setEnabled(false);
				preferencesDialogPanel.getComboBoxGMailGroup().setEnabled(false);
			}
		}
	}

	public void setPreferencesDialogPanel(PreferencesDialogPanel preferencesDialogPanel) {
		this.preferencesDialogPanel = preferencesDialogPanel;
	}

	public void applyPreferencesToPanel() {
		if (preferencesDialogPanel != null && applicationConfiguration != null) {
			final Preferences preferences = applicationConfiguration.getPreferences();
			if (preferences != null) {
				preferencesDialogPanel.getCheckboxFilesystemProvider().setSelected(
						preferences.getCheckboxFilesystemProvider());
				preferencesDialogPanel.getCheckboxGMailProvider().setSelected(preferences.getCheckboxGMailProvider());
				preferencesDialogPanel.getTextFieldGMailEmail().setText(preferences.getGmailEmail());
				preferencesDialogPanel.getComboBoxGMailGroup().setSelectedItem(preferences.getGmailGroup());
				preferencesDialogPanel.getTextFieldGMailPassword().setText(preferences.getGmailPassword());
			}
		}
		GMailProviderClicked(null);
	}

}
