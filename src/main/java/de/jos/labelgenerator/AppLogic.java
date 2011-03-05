package de.jos.labelgenerator;

import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;

import de.jos.labelgenerator.combobox.LayoutComboBoxItem;
import de.jos.labelgenerator.components.button.ButtonLabel;
import de.jos.labelgenerator.configuration.ApplicationConfiguration;
import de.jos.labelgenerator.configuration.Layout;
import de.jos.labelgenerator.configuration.layoutProvider.impl.ClasspathLayoutProvider;
import de.jos.labelgenerator.configuration.layoutProvider.impl.FilesystemLayoutProvider;
import de.jos.labelgenerator.dialog.address.AddressDialogController;
import de.jos.labelgenerator.dialog.main.MainDialogConstants;
import de.jos.labelgenerator.dialog.preferences.PreferencesDialogController;
import de.jos.labelgenerator.pdf.PDFGenerator;

/**
 * TODO move to dialog/main
 * 
 * @author andy
 * 
 */
public class AppLogic implements MainDialogConstants {

	private static final Logger LOGGER = Logger.getLogger(AppLogic.class.getName());

	private AppView view = null;

	private List<Layout> layouts = new ArrayList<Layout>();

	private List<ButtonLabel> buttonLabelList = new ArrayList<ButtonLabel>();

	public AppLogic(final AppView view) {
		this.view = view;

		// get predefined layouts from classpath and from defined directory.
		layouts.addAll(new ClasspathLayoutProvider().getLayouts());
		layouts.addAll(new FilesystemLayoutProvider().getLayouts());
	}

	/**
	 * TODO use static configuration !!
	 * 
	 * @param applicationConfiguration
	 */
	public void initializeComponentsWithConfiguration(ApplicationConfiguration applicationConfiguration) {
		if (applicationConfiguration != null) {
			// this.applicationConfiguration.setLastLayout(applicationConfiguration.getLastLayout());
			// this.applicationConfiguration.setLastTemplate(applicationConfiguration.getLastTemplate());
			// this.applicationConfiguration.setPreferences(applicationConfiguration.getPreferences());

			// update the comboBoxes
			view.getMainPanel().getComboBoxLayout().selectItemWithText(
					new LayoutComboBoxItem(applicationConfiguration.getLastLayout()));
			// TODO das tut noch nicht richtig trigger rebuilding of layout
			layoutItemChanged(null);
		}

	}

	@org.jdesktop.application.Action
	public void preferences() {
		System.out.println("pref in logic !");
		final PreferencesDialogController preferencesController = new PreferencesDialogController(getView().getFrame());
		preferencesController.showDialog();
	}

	public AppView getView() {
		return view;
	}

	public void setView(AppView view) {
		this.view = view;
	}

	@Action(name = ACTION_PRINT)
	public Task print(final ActionEvent actionEvent) {
		System.out.println("print !!");
		return new PrintPdfTask(view.getApplication());
	}

	private final class PrintPdfTask extends Task<Void, Void> {

		public PrintPdfTask(Application application) {
			super(application);
		}

		@Override
		protected Void doInBackground() throws Exception {
			final LayoutComboBoxItem comboBoxItem = (LayoutComboBoxItem) view.getMainPanel().getComboBoxLayout()
					.getSelectedItem();

			final File pdfFile = File.createTempFile("labelGenerator", ".pdf", Constants.FILE_DIRECTORY_TMP);

			if (comboBoxItem != null) {
				final PDFGenerator pdfGenerator = new PDFGenerator(pdfFile);
				final boolean drawGrid = view.getMainPanel().getCheckBoxDrawGrid().isSelected();
				// generate pdf and only open it if it was successful
				if (pdfGenerator.generate(comboBoxItem.getValue(), buttonLabelList, drawGrid) == true) {
					try {
						if (Desktop.isDesktopSupported()) {
							Desktop.getDesktop().open(pdfFile);
						} else {
							LOGGER.log(Level.WARNING, "Desktop is not supported. Cannot open PDF File.");
						}

					} catch (IOException e) {
						LOGGER.log(Level.SEVERE, "Opening the generated PDF failed.", e);
					}
				}
			}
			return null;
		}

	}

	public List<Layout> getLayouts() {
		return layouts;
	}

	@Action(name = ACTION_LAYOUT_CHANGED)
	public void layoutItemChanged(final ActionEvent actionEvent) {
		System.out.println("layoutItemChanged");

		// remove all components
		view.getMainPanel().getPanelLayoutPreview().removeAll();

		// create new components
		final LayoutComboBoxItem selectedLayoutItem = view.getMainPanel().getComboBoxLayout().getSelectedItem();
		final Layout selectedLayout = selectedLayoutItem.getValue();

		// update the texts
		view.getMainPanel().getLabelHeightValue().setText(selectedLayout.getHeight() + " mm");
		view.getMainPanel().getLabelWidthValue().setText(selectedLayout.getWidth() + " mm");
		view.getMainPanel().getLabelMarginLeftValue().setText(selectedLayout.getMarginLeft() + " mm");
		view.getMainPanel().getLabelMarginTopValue().setText(selectedLayout.getMarginTop() + " mm");
		view.getMainPanel().getLabelHeightNextValue().setText(selectedLayout.getHeight() + " mm");
		view.getMainPanel().getLabelWidthNextValue().setText(selectedLayout.getWidth() + " mm");

		view.getMainPanel().getLabelFontValue().setText(
				selectedLayout.getFontName() + " " + selectedLayout.getFontSize() + "pt");

		view.getMainPanel().getLabelTextMarginLeftValue().setText(selectedLayout.getTextMarginLeft() + "");
		view.getMainPanel().getLabelTextMarginTopValue().setText(selectedLayout.getTextMarginTop() + "");
		view.getMainPanel().getLabelTextMarginValue().setText(selectedLayout.getTextMargin() + "");

		final GridLayout gridLayout = new GridLayout(selectedLayout.getRows(), selectedLayout.getColumns());
		gridLayout.setHgap(10);
		gridLayout.setVgap(10);

		view.getMainPanel().getPanelLayoutPreview().setLayout(gridLayout);

		int numberButtons = selectedLayout.getRows() * selectedLayout.getColumns();

		buttonLabelList.clear();

		// add buttons to the preview panel
		for (int i = 0; i < numberButtons; i++) {
			final ButtonLabel button = new ButtonLabel(i);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final AddressDialogController dialogController = new AddressDialogController(view.getFrame(),
							button);
					dialogController.showDialog();
				}
			});

			view.getMainPanel().getPanelLayoutPreview().add(button);

			buttonLabelList.add(button);
		}

		view.getMainPanel().getPanelLayoutPreview().revalidate();
		view.getMainPanel().getPanelLayoutPreview().repaint();

		// update the applicationConfiguration
		final ApplicationConfiguration configuration = LabelGeneratorApp.getApplicationConfiguration();
		configuration.setLastLayout(selectedLayout);
	}

}
