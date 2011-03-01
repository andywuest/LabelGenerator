package de.jos.labelgenerator.configuration;

public class Template {

	private String name = null;

	private String fileName = null;

	public Template() {
	}

	public Template(String name, String fileName) {
		this.name = name;
		this.fileName = fileName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
