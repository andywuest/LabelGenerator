package de.jos.labelgenerator.configuration.addressProvider;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gdata.client.Query;
import com.google.gdata.client.contacts.ContactQuery;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.Content;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.contacts.ContactGroupEntry;
import com.google.gdata.data.contacts.ContactGroupFeed;
import com.google.gdata.data.extensions.PostalAddress;
import com.google.gdata.data.extensions.StructuredPostalAddress;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class GMailAddressProvider {

	private static final Logger LOGGER = Logger.getLogger(GMailAddressProvider.class.getName());

	private String email = null;

	private String emailEncoded = null;

	private String password = null;

	private ContactsService contactsService = null;

	public GMailAddressProvider(String email, String password) {
		try {
			this.emailEncoded = URLEncoder.encode(email, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// IGNORE
		}
		this.email = email;
		this.password = password;
		contactsService = new ContactsService("exampleCo-exampleApp-1");
	}

	public boolean isLoginSuccessful() {
		boolean result = false;
		try {
			contactsService.setUserCredentials(email, password);
			result = true;
		} catch (AuthenticationException e) {
			LOGGER.log(Level.SEVERE, "Login to GMail with email {0} failed with Exception {1}", new Object[] { email,
					e.getMessage() });
			result = false;
		}
		return result;
	}

	public Map<String, String> getContactGroups() throws MalformedURLException, IOException, ServiceException {
		final Map<String, String> resultMap = new HashMap<String, String>();

		final URL feedUrl = new URL(String.format("https://www.google.com/m8/feeds/groups/%s/full", emailEncoded));
		final Query query = new Query(feedUrl);
		query.setMaxResults(999999);

		final ContactGroupFeed resultFeed = (ContactGroupFeed) contactsService.query(query, ContactGroupFeed.class);
		if (resultFeed != null) {
			for (ContactGroupEntry tmpContactGroupEntry : resultFeed.getEntries()) {
				final String name = tmpContactGroupEntry.getTitle().getPlainText();
				final String tmpId = tmpContactGroupEntry.getId();
				resultMap.put(name, tmpId);
			}
		}
		return resultMap;
	}

	public static List<ContactEntry> getContactsForGroup(ContactsService contactsService, String email, String groupId)
			throws MalformedURLException, IOException, ServiceException {
		final List<ContactEntry> result = new ArrayList<ContactEntry>();

		final URL feedUrl = new URL(String.format("https://www.google.com/m8/feeds/contacts/%s/base", email));
		ContactQuery query = new ContactQuery(feedUrl);
		query.setStringCustomParameter("group", groupId);

		final ContactFeed resultFeed = contactsService.getFeed(query, ContactFeed.class);
		if (resultFeed != null) {
			for (ContactEntry tmpContactEntry : resultFeed.getEntries()) {
				result.add(tmpContactEntry);
				System.out.println(tmpContactEntry.getId());
				Content content = tmpContactEntry.getContent();

				if (tmpContactEntry.hasName()) {
					System.out.println("\t" + tmpContactEntry.getName().getFamilyName().getValue());
					System.out.println("\t" + tmpContactEntry.getName().getGivenName().getValue());
				}

				System.out.println("\t" + tmpContactEntry.getTitle().getPlainText());

				if (tmpContactEntry.getGender() != null) {
					System.out.println("\t" + tmpContactEntry.getGender().getValue().toString());
				}
				System.out.println("\t" + tmpContactEntry.getImAddresses());

				if (tmpContactEntry.getPostalAddresses() != null) {
					for (PostalAddress address : tmpContactEntry.getPostalAddresses()) {
						System.out.println("\t" + address.getValue());
					}
				}

				if (tmpContactEntry.getStructuredPostalAddresses() != null) {
					for (StructuredPostalAddress address : tmpContactEntry.getStructuredPostalAddresses()) {
						// address.getStreet()
						if (address.hasPostcode()) {
							System.out.println(address.getPostcode().getValue());
						}
						System.out.println(address.getFormattedAddress().getValue());
						System.out.println("\t" + address.getCity());
					}
				}

			}
		}

		return result;
	}

	public static void maina(String args[]) {

		// Configure the logging mechanisms.
		Logger httpLogger = Logger.getLogger("com.google.gdata.client.http.HttpGDataRequest");
		httpLogger.setLevel(Level.ALL);
		Logger xmlLogger = Logger.getLogger("com.google.gdata.util.XmlParser");
		xmlLogger.setLevel(Level.ALL);
		// Create a log handler which prints all log events to the console.
		ConsoleHandler logHandler = new ConsoleHandler();
		logHandler.setLevel(Level.ALL);
		httpLogger.addHandler(logHandler);
		xmlLogger.addHandler(logHandler);

		ContactsService myService = new ContactsService("exampleCo-exampleApp-1");
		// try {

		String password = "";

		try {
			myService.setUserCredentials("andreas.wuest.freelancer@googlemail.com", password);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}

//		try {
//			String email = "andreas.wuest.freelancer%40googlemail.com";
//			String group = "LabelGenerator";

//			Map<String, String> contactGroups = getContactGroups(myService, email);
//			System.out.println(contactGroups);
//
//			if (contactGroups.containsKey("LabelGenerator")) {
//				getContactsForGroup(myService, email, contactGroups.get(group));
//			}
//		} catch (ServiceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		// } catch (AuthenticationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

}
