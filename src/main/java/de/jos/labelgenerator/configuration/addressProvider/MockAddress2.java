package de.jos.labelgenerator.configuration.addressProvider;

import de.jos.labelgenerator.configuration.Address;

public class MockAddress2 implements Address {

	public String getCity() {
		return "Stuttgart";
	}

	public String getFamilyName() {
		return "Vision iT media Consult GmbH";
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
