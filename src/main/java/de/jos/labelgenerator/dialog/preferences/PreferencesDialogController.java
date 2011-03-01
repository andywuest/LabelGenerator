package de.jos.labelgenerator.dialog.preferences;

import java.awt.Container;
import java.awt.Frame;
import java.awt.Rectangle;

import javax.swing.JDialog;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;

import de.jos.labelgenerator.LabelGeneratorApp;

public class PreferencesDialogController {

	private JDialog dialog = null;

	private final ApplicationContext applicationContext = Application.getInstance(LabelGeneratorApp.class).getContext();

	public PreferencesDialogController(Frame parentFrame) {

		// ceate the dialog
		dialog = new JDialog(parentFrame, "preferences");

		// application configuration
		final PreferencesDialogLogic logic = new PreferencesDialogLogic(dialog, null);

		final PreferencesDialogPanel preferencesDialogPanel = new PreferencesDialogPanel(applicationContext
				.getActionMap(logic));

		logic.setPreferencesDialogPanel(preferencesDialogPanel);

		// define modal dialog
		dialog.setModal(true);
		// dialog.setName("");
		dialog.add(preferencesDialogPanel);
		dialog.setBounds(getDialogRectangleRelativeToParent(dialog, 500, 300));
	}

	public void showDialog() {
		dialog.setVisible(true);
	}

	/**
	 * TODO move to base class
	 * 
	 * @param dialog
	 * @param width
	 * @param height
	 * @return
	 */
	private Rectangle getDialogRectangleRelativeToParent(final JDialog dialog, int width, int height) {
		final Container parent = dialog.getParent();

		final Rectangle parentBounds = parent.getBounds();
		int x = parentBounds.x + ((parentBounds.width - width) / 2);
		int y = parentBounds.y + ((parentBounds.height - height) / 2);

		return new Rectangle(x, y, width, height);
	}

}
