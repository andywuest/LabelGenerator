package de.jos.labelgenerator.combobox;

import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class AbstractComboBox<T extends AbstractComboBoxItem> extends JComboBox {

	@Override
	@SuppressWarnings("unchecked")
	public T getSelectedItem() {
		return (T) super.getSelectedItem();
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getItemAt(int index) {
		return (T) super.getItemAt(index);
	}

	public boolean selectItemWithText(T comboBoxItem) {
		boolean found = false;

		// look for the first match and select it
		for (int i = 0; i < this.getItemCount(); i++) {
			final T item = getItemAt(i);
			if (item.toString().equals(comboBoxItem.toString())) {
				this.setSelectedIndex(i);
				found = true;
				break;
			}
		}

		return found;
	}

}
