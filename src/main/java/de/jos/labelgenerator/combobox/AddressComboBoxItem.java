package de.jos.labelgenerator.combobox;

import de.jos.labelgenerator.configuration.Address;

public class AddressComboBoxItem extends AbstractComboBoxItem<Address> {

	public AddressComboBoxItem(Address item) {
		super(item);
	}

	@Override
	public String toString() {
		return getValue().getFamilyName();
	}

}
