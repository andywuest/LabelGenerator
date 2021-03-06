package de.jos.labelgenerator;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.LocalStorage;
import org.jdesktop.application.SingleFrameApplication;

import de.jos.labelgenerator.beaninfo.BeanInfoUtil;
import de.jos.labelgenerator.configuration.ApplicationConfiguration;
import de.jos.labelgenerator.configuration.Preferences;
import de.jos.labelgenerator.formatter.CustomFormatter;

public class LabelGeneratorApp extends SingleFrameApplication {

	private static final Logger logger = Logger.getLogger(LabelGeneratorApp.class.getName());

	private static ApplicationConfiguration applicationConfiguration = null;

	private AppView appView = null;

	private void createDirectory(final File directory) {
		if (!directory.exists()) {
			directory.mkdir();
		}
	}

	private void prepareDirectories() {
		logger.log(Level.INFO, "preparing Directories....");
		// create necessary directories if they do not exist
		createDirectory(Constants.FILE_DIRECTORY_LABELGENERATOR);
		createDirectory(Constants.FILE_DIRECTORY_VCF_FILE);
		createDirectory(Constants.FILE_DIRECTORY_LAYOUT_FILE);
		createDirectory(Constants.FILE_DIRECTORY_TMP);
	}

	private void clearTempDirectory() {

		final FilenameFilter fileNameFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".pdf");
			}
		};

		// get all the files and delete them
		final String[] fileNames = Constants.FILE_DIRECTORY_TMP.list(fileNameFilter);
		for (String tmpFileName : fileNames) {
			final File file = new File(Constants.FILE_DIRECTORY_TMP + Constants.SEPARATOR + tmpFileName);
			logger.log(Level.INFO, "Deleting temporary file {0}", file.getAbsoluteFile());
		}
	}

	private void loadConfiguration() {
		final ApplicationContext applicationContext = getContext();
		final LocalStorage localStorage = applicationContext.getLocalStorage();

		try {
			applicationConfiguration = (ApplicationConfiguration) localStorage
					.load(Constants.APPLICATION_CONFIGURATION_FILE);
			logger.log(Level.INFO, String.format("Configuration loaded from directory %s ...", localStorage
					.getDirectory().getAbsolutePath()));
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Loading configuration failed ...", e);
		}
	}

	private void saveConfiguration() {
		final ApplicationContext applicationContext = getContext();
		final LocalStorage localStorage = applicationContext.getLocalStorage();

		try {
			localStorage.save(applicationConfiguration, Constants.APPLICATION_CONFIGURATION_FILE);
			logger.log(Level.INFO, "Configuration saved ...");
		} catch (IOException e) {
			logger.log(Level.SEVERE, String.format("Saving configuration to directory %s failed ...", localStorage
					.getDirectory().getAbsolutePath()), e);
		}
	}

	/**
	 * Provides static access to the application configuration. If the
	 * configuration is not yet fully initialized, it will be done before
	 * returning the configuration.
	 * 
	 * @return
	 */
	public static ApplicationConfiguration getApplicationConfiguration() {
		if (applicationConfiguration == null) {
			applicationConfiguration = new ApplicationConfiguration();
		}
		if (applicationConfiguration.getPreferences() == null) {
			applicationConfiguration.setPreferences(new Preferences());
		}

		return applicationConfiguration;
	}

	@Override
	protected void startup() {
		// mark transient attributes in BeanInfo for preference classes
		BeanInfoUtil.markTransientAttributes(Preferences.class);
		// prepare directories
		prepareDirectories();
		// clear temp directory
		clearTempDirectory();
		// load configuration
		loadConfiguration();

		appView = new AppView(this);

		show(appView);
	}

	@Override
	protected void ready() {
		super.ready();
	}

	@Override
	protected void shutdown() {
		super.shutdown();

		// saving configuration
		saveConfiguration();
	}

	public static void main(String[] args) {
		final CustomFormatter formatter = new CustomFormatter();
		final ConsoleHandler handler = new ConsoleHandler();

		// WORKAROUND does not work with Logger.GLOBAL_LOGGER_NAME
		final Logger globalLogger = Logger.getLogger("");
		globalLogger.setUseParentHandlers(false);
		final Handler[] handlers = globalLogger.getHandlers();
		for (Handler tmpHandler : handlers) {
			System.out.println("handler: " + tmpHandler);
			globalLogger.removeHandler(tmpHandler);
		}

		handler.setFormatter(formatter);
		globalLogger.addHandler(handler);

		Application.launch(LabelGeneratorApp.class, args);
	}

}