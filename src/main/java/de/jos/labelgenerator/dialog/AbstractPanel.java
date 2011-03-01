package de.jos.labelgenerator.dialog;

import javax.swing.JPanel;

import org.jdesktop.application.ApplicationActionMap;

/**
 * TODO move to other package
 * extends FormDebugPanel zu debuggen !!!
 * 
 * @author andy
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractPanel extends JPanel {

	private ApplicationActionMap applicationActionMap = null;

	public AbstractPanel(ApplicationActionMap applicationActionMap) {
		super();
		this.applicationActionMap = applicationActionMap;
	}

	public javax.swing.Action getAction(final String actionName) {
		if (getApplicationActionMap() != null) {
			return getApplicationActionMap().get(actionName);
		} else {
			return null;
		}
	}

	public ApplicationActionMap getApplicationActionMap() {
		return applicationActionMap;
	}

}
