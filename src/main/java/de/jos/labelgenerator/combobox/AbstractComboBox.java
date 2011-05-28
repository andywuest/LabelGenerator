package de.jos.labelgenerator.combobox;

import javax.swing.JComboBox;

import com.google.common.collect.ImmutableCollection;

//import com.google.common.collect.ImmutableCollection;

@SuppressWarnings("serial")
public class AbstractComboBox<T extends AbstractComboBoxItem> extends JComboBox {

	@Override
	@SuppressWarnings("unchecked")
	public T getSelectedItem() {
		return (T) super.getSelectedItem();
	}

	public void addItems(ImmutableCollection<AbstractComboBoxItem> itemSet) {
		for (AbstractComboBoxItem tmpItem : itemSet) {
			super.addItem((T) tmpItem);
		}
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
