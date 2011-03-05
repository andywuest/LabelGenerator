package de.jos.labelgenerator.dialog.address;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;

import org.jdesktop.application.Action;

import de.jos.labelgenerator.LabelGeneratorApp;
import de.jos.labelgenerator.combobox.AddressComboBoxItem;
import de.jos.labelgenerator.combobox.TemplateComboBoxItem;
import de.jos.labelgenerator.components.button.ButtonLabel;
import de.jos.labelgenerator.configuration.Address;
import de.jos.labelgenerator.configuration.ApplicationConfiguration;
import de.jos.labelgenerator.configuration.UndefinedAddress;
import de.jos.labelgenerator.template.TemplatingEngine;

public class AddressPanelLogic implements AddressDialogConstants {

	final List<Address> addresses = new ArrayList<Address>();

	private AddressDialogPanel addressPanel = null;

	private JDialog dialog = null;

	private ButtonLabel selectedButton = null;

	private TemplatingEngine templateEngine = null;

	public AddressPanelLogic(JDialog dialog, ButtonLabel selectedButton) {
		this.dialog = dialog;
		this.selectedButton = selectedButton;
		templateEngine = new TemplatingEngine();
	}

	public void initializeComponentsWithConfiguration() {
		if (selectedButton.getAddress() != null) {
			addressPanel.getComboBoxAddress().selectItemWithText(new AddressComboBoxItem(selectedButton.getAddress()));
		}
		// preselect template if it has already been chosen for the button -
		// else use the
		// template from the configuration.
		if (selectedButton.getTemplate() != null) {
			addressPanel.getComboBoxTemplate().selectItemWithText(
					new TemplateComboBoxItem(selectedButton.getTemplate()));
		} else {
			final ApplicationConfiguration configuration = LabelGeneratorApp.getApplicationConfiguration();
			if (configuration.getLastTemplate() != null) {
				addressPanel.getComboBoxTemplate().selectItemWithText(
						new TemplateComboBoxItem(configuration.getLastTemplate()));
			}

		}
	}

	public AddressDialogPanel getAddressPanel() {
		return addressPanel;
	}

	public void setAddressPanel(AddressDialogPanel addressPanel) {
		this.addressPanel = addressPanel;
	}

	private String getRenderedTemplate() {
		String renderedTemplate = "";
		if (addressPanel != null) {
			final AddressComboBoxItem addressItem = addressPanel.getComboBoxAddress().getSelectedItem();
			if (addressItem != null) {
				final Address address = addressItem.getValue();

				if (!(address instanceof UndefinedAddress)) {
					final TemplateComboBoxItem templateItem = addressPanel.getComboBoxTemplate().getSelectedItem();
					if (templateItem != null) {
						renderedTemplate = templateEngine
								.renderTemplate(address, templateItem.getValue().getFileName());
					}
				}
			}
		}
		return renderedTemplate;
	}

	private void updateRenderedTempalte() {
		if (addressPanel != null) {
			addressPanel.getTextAreaAddress().setText(getRenderedTemplate());
		}
	}

	@Action(name = ACTION_TEMPLATE_CHANGED)
	public void templateChanged(final ActionEvent actionEvent) {
		updateRenderedTempalte();
	}

	@Action(name = ACTION_ADDRESS_CHANGED)
	public void addressChanged(final ActionEvent actionEvent) {
		updateRenderedTempalte();
	}

	@Action(name = ACTION_CANCEL_BUTTON_PRESSED)
	public void cancelButtonPressed(final ActionEvent actionEvent) {
		System.out.println("cancelAddressDialog");
		dialog.setVisible(false);
		dialog.dispose();
	}

	@Action(name = ACTION_OK_BUTTON_PRESSED)
	public void okButtonPressed(final ActionEvent actionEvent) {
		System.out.println("closeAddressDialog");

		String renderedTemplate = getRenderedTemplate();

		System.out.println("closeAddressDialog " + renderedTemplate);

		if (addressPanel != null) {
			// update the button with the selected template and address
			selectedButton.setTemplate(addressPanel.getComboBoxTemplate().getSelectedItem().getValue());
			selectedButton.setAddress(addressPanel.getComboBoxAddress().getSelectedItem().getValue());
			selectedButton.setText(selectedButton.getAddress().getFamilyName());
			selectedButton.setRenderedTemplate(renderedTemplate);
			// remember the template in the configuration
			final TemplateComboBoxItem selectedTemplate = addressPanel.getComboBoxTemplate().getSelectedItem();
			if (selectedTemplate != null) {
				final ApplicationConfiguration configuration = LabelGeneratorApp.getApplicationConfiguration();
				configuration.setLastTemplate(selectedTemplate.getValue());
			}
		}

		dialog.setVisible(false);
		dialog.dispose();
	}

	public List<Address> getAddresses() {
		return addresses;
	}

}
