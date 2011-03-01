package de.jos.labelgenerator.combobox;

import de.jos.labelgenerator.configuration.Template;

public class TemplateComboBoxItem extends AbstractComboBoxItem<Template> {

	public TemplateComboBoxItem(Template item) {
		super(item);
	}

	@Override
	public String toString() {
		return getValue().getName();
	}

}
