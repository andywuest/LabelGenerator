package de.jos.labelgenerator.dialog.main;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;

import de.jos.labelgenerator.AppLogic;
import de.jos.labelgenerator.LabelGeneratorApp;

public class MainDialogMenu extends JMenu {

	private final String[] menuActionNames = { "preferences", "---", "quit" };

	private final String menuName = "menu.file";

	private final ApplicationContext applicationContext = Application.getInstance(LabelGeneratorApp.class).getContext();

	private final ResourceMap resourceMap = applicationContext.getResourceMap(MainDialogMenu.class);

	public MainDialogMenu(AppLogic appLogic) {
		setName(menuName);
		setText(resourceMap.getString(menuName));
		for (final String actionName : menuActionNames) {
			if (actionName.equals("---")) {
				add(new JSeparator());
			} else {

				Action action = getAction(appLogic, actionName);
				System.out.println("text: " + action.getValue("text"));

				System.out.println("actionName : " + actionName + "" + action);
				final JMenuItem menuItem = new JMenuItem();
				menuItem.setAction(getAction(appLogic, actionName));
				menuItem.setName(actionName);
				add(menuItem);
			}
		}
	}

	private Action getAction(final Object action, final String actionName) {
		final ApplicationActionMap actionMap = applicationContext.getActionMap(action);
		return actionMap.get(actionName);
	}

}
