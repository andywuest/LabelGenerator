package de.jos.labelgenerator.combobox;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;

public abstract class AbstractComboBoxItem<T> {

	private T item = null;;

	private static final Function<AbstractComboBoxItem, String> getToStringFunction = new Function<AbstractComboBoxItem, String>() {
		public String apply(AbstractComboBoxItem from) {
			return from.toString();
		}
	};

	public static Ordering<AbstractComboBoxItem> nameOrdering = Ordering.natural().onResultOf(getToStringFunction);

	public AbstractComboBoxItem(T item) {
		this.item = item;
	}

	public abstract String toString();

	public T getValue() {
		return item;
	}

}
