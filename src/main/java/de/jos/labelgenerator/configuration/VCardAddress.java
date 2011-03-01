package de.jos.labelgenerator.configuration;

import info.ineighborhood.cardme.vcard.VCard;
import info.ineighborhood.cardme.vcard.features.AddressFeature;

import java.util.Iterator;

public class VCardAddress implements Address {

	private VCard vcard = null;

	public VCardAddress(final VCard vcard) {
		this.vcard = vcard;
	}

	public String getGivenName() {
		return vcard.getName().getGivenName();
	}

	public String getFamilyName() {
		return vcard.getName().getFamilyName();
	}

	public String getPostalCode() {
		Iterator<AddressFeature> addressIterator = vcard.getAddresses();

		while (addressIterator.hasNext()) {
			AddressFeature tmpAddressFeature = addressIterator.next();
			return tmpAddressFeature.getPostalCode();
		}

		return null;
	}

	public String getCity() {
		Iterator<AddressFeature> addressIterator = vcard.getAddresses();

		while (addressIterator.hasNext()) {
			AddressFeature tmpAddressFeature = addressIterator.next();
			return tmpAddressFeature.getLocality();
		}

		return null;
	}

	public String getStreet() {
		Iterator<AddressFeature> addressIterator = vcard.getAddresses();

		while (addressIterator.hasNext()) {
			AddressFeature tmpAddressFeature = addressIterator.next();
			return tmpAddressFeature.getStreetAddress();
		}

		return null;
	}

}
