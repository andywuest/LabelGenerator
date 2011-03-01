package de.jos.labelgenerator.combobox;

import de.jos.labelgenerator.configuration.Layout;

public class LayoutComboBoxItem extends AbstractComboBoxItem<Layout> {

	public LayoutComboBoxItem(final Layout item) {
		super(item);
	}

	@Override
	public String toString() {
		return getValue().getName();
	}

}
