package de.jos.labelgenerator.combobox;

public abstract class AbstractComboBoxItem<T> {

	private T item = null;;

	public AbstractComboBoxItem(T item) {
		this.item = item;
	}

	public abstract String toString();

	public T getValue() {
		return item;
	}

}
