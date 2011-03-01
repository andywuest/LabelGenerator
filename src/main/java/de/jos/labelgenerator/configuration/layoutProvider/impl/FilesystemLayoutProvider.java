package de.jos.labelgenerator.configuration.layoutProvider.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import de.jos.labelgenerator.Constants;
import de.jos.labelgenerator.configuration.Layout;
import de.jos.labelgenerator.configuration.addressProvider.FilesystemVCardAddressProvider;
import de.jos.labelgenerator.configuration.layoutProvider.LayoutProvider;

public class FilesystemLayoutProvider implements LayoutProvider {

	private static final Logger LOGGER = Logger.getLogger(FilesystemLayoutProvider.class.getName());

	private static final String SUFFIX_PROPERTIES = ".properties";

	private List<Layout> layouts = new ArrayList<Layout>();

	public FilesystemLayoutProvider() {

		final File layoutDirectory = Constants.FILE_DIRECTORY_LAYOUT_FILE;

		if (layoutDirectory.exists() && layoutDirectory.isDirectory()) {
			// get all files in directory
			final String[] fileNames = layoutDirectory.list(new PropertiesFilter());
			System.out.println(fileNames.length);

			for (String tmpFileName : fileNames) {

				try {
					final FileInputStream fin = new FileInputStream(new File(layoutDirectory.getAbsolutePath()
							+ Constants.SEPARATOR + tmpFileName));
					Properties properties = new Properties();
					properties.load(fin);
					layouts.add(new Layout(properties));
				} catch (FileNotFoundException e) {
					LOGGER.log(Level.SEVERE, "Could not find layout file {0} in directory {1} .", new Object[] {
							tmpFileName, layoutDirectory });
				} catch (IOException e) {
					LOGGER.log(Level.SEVERE, "Could not read layout file {0} from directory {1} .", new Object[] {
							tmpFileName, layoutDirectory });
				}
			}
		}
	}

	public List<Layout> getLayouts() {
		final List<Layout> layoutList = new ArrayList<Layout>();
		layoutList.addAll(layouts);
		return layoutList;
	}

	public static void main(String args[]) {
		new FilesystemVCardAddressProvider();
	}

	private static final class PropertiesFilter implements FilenameFilter {

		public boolean accept(File dir, String name) {
			if (StringUtils.endsWith(name, SUFFIX_PROPERTIES)) {
				return true;
			}
			return false;
		}

	}

}
