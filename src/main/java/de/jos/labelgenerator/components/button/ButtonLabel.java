package de.jos.labelgenerator.components.button;

import javax.swing.JButton;

import de.jos.labelgenerator.configuration.Address;
import de.jos.labelgenerator.configuration.Template;

@SuppressWarnings("serial")
public class ButtonLabel extends JButton {

	private Address address = null;

	private Template template = null;

	private String renderedTemplate = null;

	public ButtonLabel() {
		super(" [ empty ] ");
	}
	
	public ButtonLabel(int index) {
		super(Integer.valueOf(index).toString());
		setRenderedTemplate(String.valueOf(index));
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public String getRenderedTemplate() {
		return renderedTemplate;
	}

	public void setRenderedTemplate(String renderedTemplate) {
		this.renderedTemplate = renderedTemplate;
	}

}
