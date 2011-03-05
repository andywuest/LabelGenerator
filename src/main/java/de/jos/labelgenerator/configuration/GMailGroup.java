package de.jos.labelgenerator.configuration;

public class GMailGroup {

	private String name = null;

	private String url = null;

	public GMailGroup() {
	}

	public GMailGroup(final String name, final String url) {
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
