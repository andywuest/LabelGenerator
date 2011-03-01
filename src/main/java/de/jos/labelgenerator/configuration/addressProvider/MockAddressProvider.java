package de.jos.labelgenerator.configuration.addressProvider;

import java.util.ArrayList;
import java.util.List;

import de.jos.labelgenerator.configuration.Address;

public class MockAddressProvider implements AddressProvider {

	public List<Address> getAddresses() {
		final List<Address> result = new ArrayList<Address>();
		result.add(new MockAddress());
		result.add(new MockAddress2());
		return result;
	}

}
