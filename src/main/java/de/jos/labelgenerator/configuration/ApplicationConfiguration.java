package de.jos.labelgenerator.configuration;

public class ApplicationConfiguration {

	private Template lastTemplate = null;

	private Layout lastLayout = null;

	public ApplicationConfiguration() {

	}

	public Template getLastTemplate() {
		return lastTemplate;
	}

	public void setLastTemplate(Template lastTemplate) {
		this.lastTemplate = lastTemplate;
	}

	public Layout getLastLayout() {
		return lastLayout;
	}

	public void setLastLayout(Layout lastLayout) {
		this.lastLayout = lastLayout;
	}

}
