package de.jos.labelgenerator;

import java.io.File;

public abstract class Constants {

	public static final String APPLICATION_CONFIGURATION_FILE = "appConfig.xml";

	public static final String SEPARATOR = System.getProperty("file.separator");

	public static final String DIRECTORY_LABELGENERATOR = System.getProperty("user.home") + SEPARATOR
			+ ".labelGenerator";

	public static final String DIRECTORY_TMP = System.getProperty("java.io.tmpdir") + SEPARATOR + "labelGenerator";

	public static final File FILE_DIRECTORY_LABELGENERATOR = new File(DIRECTORY_LABELGENERATOR);

	public static final String DIRECTORY_VCARDS = DIRECTORY_LABELGENERATOR + SEPARATOR + "vcards";

	public static final String DIRECTORY_LAYOUTS = DIRECTORY_LABELGENERATOR + SEPARATOR + "layouts";

	public static final File FILE_DIRECTORY_VCF_FILE = new File(DIRECTORY_VCARDS);

	public static final File FILE_DIRECTORY_LAYOUT_FILE = new File(DIRECTORY_LAYOUTS);

	public static final File FILE_DIRECTORY_TMP = new File(DIRECTORY_TMP);

}
