package de.jos.labelgenerator.configuration.layoutProvider.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.jos.labelgenerator.configuration.Layout;
import de.jos.labelgenerator.configuration.layoutProvider.LayoutProvider;

/**
 * Provider that provides Layouts from the classpath.
 * 
 * @author andy
 * 
 */
public class ClasspathLayoutProvider implements LayoutProvider {

	private static final Logger LOGGER = Logger.getLogger(ClasspathLayoutProvider.class.getName());

	public List<Layout> getLayouts() {
		final List<Layout> result = new ArrayList<Layout>();

		boolean found = true;
		int counter = 1;
		while (found) {
			final Properties properties = new Properties();
			try {
				final String templateName = getFileName(counter);
				final InputStream in = getInputStream(templateName);
				if (in != null) {
					// read the properties
					properties.load(in);
					// create template
					result.add(new Layout(properties));
					// increment counter
					counter++;
				} else {
					// set flag to end loop
					found = false;
				}
			} catch (IOException e) {
				LOGGER.log(Level.INFO, "Could not load template with id {0}.", counter);
				// set flag to end loop
				found = false;
			}
		}

		return result;
	}

	private String getFileName(int counter) {
		return String.format("layouts/layout_%s.properties", Integer.valueOf(counter));
	}

	private InputStream getInputStream(String fileName) {
		return ClasspathLayoutProvider.class.getClassLoader().getResourceAsStream(fileName);
	}

	public static void main(String args[]) {
		ClasspathLayoutProvider provider = new ClasspathLayoutProvider();
		System.out.println(provider.getLayouts());
	}

}
