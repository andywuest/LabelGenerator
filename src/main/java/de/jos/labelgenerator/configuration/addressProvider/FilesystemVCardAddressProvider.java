package de.jos.labelgenerator.configuration.addressProvider;

import info.ineighborhood.cardme.io.CompatibilityMode;
import info.ineighborhood.cardme.vcard.VCard;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.jos.labelgenerator.Constants;
import de.jos.labelgenerator.configuration.Address;
import de.jos.labelgenerator.configuration.VCardAddress;
import de.jos.labelgenerator.vcard.VCardParser;

public class FilesystemVCardAddressProvider implements AddressProvider {

	private static final String SUFFIX_VCARD = ".vcf";

	private List<VCardAddress> vcards = new ArrayList<VCardAddress>();

	public FilesystemVCardAddressProvider() {

		final File vcardDirectory = Constants.FILE_DIRECTORY_VCF_FILE;

		if (vcardDirectory.exists() && vcardDirectory.isDirectory()) {
			// get all files in directory
			final String[] fileNames = vcardDirectory.list(new VCFFilter());
			System.out.println(fileNames.length);

			for (String tmpFileName : fileNames) {
				VCardParser vcardParser = new VCardParser();
				vcardParser.setCompatibilityMode(CompatibilityMode.RFC2426);

				VCard vCard = vcardParser
						.getVCard(vcardDirectory.getAbsolutePath() + Constants.SEPARATOR + tmpFileName);
				vcards.add(new VCardAddress(vCard));
			}
		}
	}

	public List<Address> getAddresses() {
		final List<Address> addressList = new ArrayList<Address>();
		addressList.addAll(vcards);
		return addressList;
	}

	public static void main(String args[]) {
		new FilesystemVCardAddressProvider();
	}

	private static final class VCFFilter implements FilenameFilter {

		public boolean accept(File dir, String name) {
			if (StringUtils.endsWith(name, SUFFIX_VCARD)) {
				return true;
			}
			return false;
		}

	}

}
