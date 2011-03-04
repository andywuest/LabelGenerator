package de.jos.labelgenerator;

import java.awt.FlowLayout;

import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;

import de.jos.labelgenerator.combobox.LayoutComboBoxItem;
import de.jos.labelgenerator.configuration.Layout;
import de.jos.labelgenerator.dialog.main.MainDialogMenu;
import de.jos.labelgenerator.dialog.main.MainDialogPanel;

public class AppView extends FrameView {

	private JMenuBar menuBar = null;

	private MainDialogPanel mainPanel = null;

	private JPanel statusPanel = null;

	private JLabel statusLabel = null;

	private SingleFrameApplication application = null;

	private final AppLogic logic = new AppLogic(this);

	private final ApplicationContext applicationContext = Application.getInstance(LabelGeneratorApp.class).getContext();

	private final ResourceMap resourceMap = applicationContext.getResourceMap(AppView.class);
	
	public AppView(final SingleFrameApplication application) {
		super(application);
		this.application = application;
		initComponents();
	}

	private void initComponents() {
		menuBar = createMenuBar();
		mainPanel = new MainDialogPanel(application.getContext().getActionMap(logic));

		statusLabel = new JLabel(" Ready ...");

		statusPanel = new JPanel();
		statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		statusPanel.add(statusLabel);

		// update the comboBox
		for (Layout tmpLayout : logic.getLayouts()) {
			getMainPanel().getComboBoxLayout().addItem(new LayoutComboBoxItem(tmpLayout));
		}

		getMainPanel().getComboBoxLayout().setSelectedIndex(0);

		setMenuBar(menuBar);
		setComponent(mainPanel);
		setStatusBar(statusPanel);
	}

	private JMenu createMenu(final String menuName, final String[] actionNames) {
		final JMenu menu = new JMenu();
		menu.setName(menuName);
		menu.setText(resourceMap.getString(menuName));
		for (final String actionName : actionNames) {
			if (actionName.equals("---")) {
				menu.add(new JSeparator());
			} else {
				
				Action action = getAction(actionName);
				System.out.println("text: " +action.getValue("text"));
				
				System.out.println("actionName : " + actionName + "" + action);
				final JMenuItem menuItem = new JMenuItem();
				menuItem.setAction(getAction(actionName));
				menuItem.setName(actionName);
				//menuItem.setText(getText(menuName + "." + actionName));
				//menuItem.setIcon(null);
				menu.add(menuItem);
			}
		}
		return menu;
	}

	private JMenuBar createMenuBar() {
		menuBar = new JMenuBar();
		//final String[] fileMenuActionNames = { "preferences", "---", "quit" };
		//menuBar.add(createMenu("menu.file", fileMenuActionNames));
		menuBar.add(new MainDialogMenu(logic));
		return menuBar;
	}

	private Action getAction(final String actionName) {
		final ApplicationActionMap actionMap = application.getContext().getActionMap(logic);
		return actionMap.get(actionName);
	}

	/**
	 * Runs after the startup has completed and the GUI is up and ready. We show
	 * the first image here, rather than initializing it at startup time, so
	 * loading the first image doesn't impede getting the GUI visible.
	 */
	protected void ready() {
		System.out.println("ready !");
	}

	@Override
	public JMenuBar getMenuBar() {
		return menuBar;
	}

	public MainDialogPanel getMainPanel() {
		return mainPanel;
	}

	public JPanel getStatusPanel() {
		return statusPanel;
	}

	public JLabel getStatusLabel() {
		return statusLabel;
	}

	public AppLogic getLogic() {
		return logic;
	}

}
