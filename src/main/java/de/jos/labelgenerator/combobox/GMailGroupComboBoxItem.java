package de.jos.labelgenerator.combobox;

import de.jos.labelgenerator.configuration.GMailGroup;

public class GMailGroupComboBoxItem extends AbstractComboBoxItem<GMailGroup> {

	public GMailGroupComboBoxItem(final GMailGroup item) {
		super(item);
	}

	@Override
	public String toString() {
		return getValue().getName();
	}

}
