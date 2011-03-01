package de.jos.labelgenerator.configuration;

public class UndefinedAddress implements Address {

	public String getFamilyName() {
		return "--undefined--";
	}

	public String getGivenName() {
		return "";
	}

	public String getPostalCode() {
		return "";
	}

	public String getCity() {
		return "";
	}

	public String getStreet() {
		return "";
	}

}
