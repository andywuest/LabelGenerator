package de.jos.labelgenerator.configuration.addressProvider;

import de.jos.labelgenerator.configuration.Address;

public class MockAddress implements Address {

	public String getCity() {
		return "Ostfildern";
	}

	public String getFamilyName() {
		return "Volker Rohm";
	}

	public String getGivenName() {
		return "";
	}

	public String getPostalCode() {
		return "73760";
	}

	public String getStreet() {
		return "Talwiesenweg 6";
	}

}
