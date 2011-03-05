package de.jos.labelgenerator.configuration;

public class Preferences {

	private String gmailEmail = null;

	private GMailGroup gmailGroup = null;

	private transient String gmailPassword = null;

	private boolean checkboxFilesystemProvider = true;

	private boolean checkboxGMailProvider = false;

	public Preferences() {
	}

	public String getGmailEmail() {
		return gmailEmail;
	}

	public void setGmailEmail(String gmailEmail) {
		this.gmailEmail = gmailEmail;
	}

	public GMailGroup getGmailGroup() {
		return gmailGroup;
	}

	public void setGmailGroup(GMailGroup gmailGroup) {
		this.gmailGroup = gmailGroup;
	}

	public String getGmailPassword() {
		return gmailPassword;
	}

	public void setGmailPassword(String gmailPassword) {
		this.gmailPassword = gmailPassword;
	}

	public boolean getCheckboxFilesystemProvider() {
		return checkboxFilesystemProvider;
	}

	public void setCheckboxFilesystemProvider(boolean checkboxFilesystemProvider) {
		this.checkboxFilesystemProvider = checkboxFilesystemProvider;
	}

	public boolean getCheckboxGMailProvider() {
		return checkboxGMailProvider;
	}

	public void setCheckboxGMailProvider(boolean checkboxGMailProvider) {
		this.checkboxGMailProvider = checkboxGMailProvider;
	}

}
