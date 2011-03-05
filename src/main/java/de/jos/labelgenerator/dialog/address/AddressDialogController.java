package de.jos.labelgenerator.dialog.address;

import java.awt.Container;
import java.awt.Frame;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;

import de.jos.labelgenerator.LabelGeneratorApp;
import de.jos.labelgenerator.components.button.ButtonLabel;
import de.jos.labelgenerator.configuration.Address;
import de.jos.labelgenerator.configuration.Template;
import de.jos.labelgenerator.configuration.UndefinedAddress;
import de.jos.labelgenerator.configuration.addressProvider.AddressProvider;
import de.jos.labelgenerator.configuration.addressProvider.MockAddressProvider;

public class AddressDialogController {

	private final List<Address> addresses = new ArrayList<Address>();

	private final List<Template> templates = new ArrayList<Template>();

	private JDialog dialog = null;

	private final ApplicationContext applicationContext = Application.getInstance(LabelGeneratorApp.class).getContext();

	public AddressDialogController(Frame parentFrame, ButtonLabel selectedButton) {
		// read the available addresses
		// final AddressProvider addressProvider = new
		// FilesystemVCardAddressProvider();
		final AddressProvider addressProvider = new MockAddressProvider();
		addresses.add(new UndefinedAddress());
		addresses.addAll(addressProvider.getAddresses());

		// TODO
		templates.add(new Template("Template 1", "template_default.ftl"));
		templates.add(new Template("Template 2", "template_default.ftl"));

		// ceate the dialog
		dialog = new JDialog(parentFrame, "test");

		// create the dialog logic
		final AddressPanelLogic addressPanelLogic = new AddressPanelLogic(dialog, selectedButton);

		// create the dialog main panel
		final AddressDialogPanel addressPanel = new AddressDialogPanel(applicationContext
				.getActionMap(addressPanelLogic), addresses, templates);

		// add panel to the logic
		addressPanelLogic.setAddressPanel(addressPanel);

		// set the previously configured values as default
		addressPanelLogic.initializeComponentsWithConfiguration();

		// define modal dialog
		dialog.setModal(true);
		dialog.setName("BlockingDialog");
		dialog.add(addressPanel);
		dialog.setBounds(getDialogRectangleRelativeToParent(dialog, 500, 300));
	}

	public void showDialog() {
		dialog.setVisible(true);
	}

	private Rectangle getDialogRectangleRelativeToParent(final JDialog dialog, int width, int height) {
		final Container parent = dialog.getParent();

		final Rectangle parentBounds = parent.getBounds();
		int x = parentBounds.x + ((parentBounds.width - width) / 2);
		int y = parentBounds.y + ((parentBounds.height - height) / 2);

		return new Rectangle(x, y, width, height);
	}

}
