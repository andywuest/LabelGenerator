package de.jos.labelgenerator.configuration.addressProvider;

import de.jos.labelgenerator.configuration.Address;

public class MockAddress implements Address {

	public String getCity() {
		return "Stuttgart";
	}

	public String getFamilyName() {
		return "Roland Rohm & Volker Rohm";
	}

	public String getGivenName() {
		return "";
	}

	public String getPostalCode() {
		return "70619";
	}

	public String getStreet() {
		return "Pfefferstr. 5";
	}

}
