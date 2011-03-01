package de.jos.labelgenerator.dialog.preferences;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;

import org.jdesktop.application.Action;

import de.jos.labelgenerator.configuration.ApplicationConfiguration;

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
		dialog.setVisible(false);
		dialog.dispose();
	}

	@Action(name = ACTION_GMAIL_PROVIDER_CHECKBOX_CLICKED)
	public void GMailProviderClicked(final ActionEvent actionEvent) {
		if (preferencesDialogPanel != null) {
			if (preferencesDialogPanel.getCheckboxGMailProvider().isSelected()) {
				preferencesDialogPanel.getTextFieldGMailEmail().setEnabled(true);
				preferencesDialogPanel.getTextFieldGMailPassword().setEnabled(true);
			} else {
				preferencesDialogPanel.getTextFieldGMailEmail().setEnabled(false);
				preferencesDialogPanel.getTextFieldGMailPassword().setEnabled(false);
			}
		}
	}

	public void setPreferencesDialogPanel(PreferencesDialogPanel preferencesDialogPanel) {
		this.preferencesDialogPanel = preferencesDialogPanel;
	}

}
