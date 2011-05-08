package de.jos.labelgenerator.pdf;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFamily;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

import de.jos.labelgenerator.Constants;
import de.jos.labelgenerator.components.button.ButtonLabel;
import de.jos.labelgenerator.configuration.Layout;
import de.jos.labelgenerator.configuration.addressProvider.MockAddress;
import de.jos.labelgenerator.configuration.addressProvider.MockAddress2;
import de.schlichtherle.truezip.file.TFile;

/**
 * Required a OpenOffice instance started as service:
 * 
 * <pre>
 *   soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard
 * </pre>
 * 
 * The binary can be found in
 * 
 * <pre>
 *   C:\Program Files (x86)\OpenOffice.org 3\program
 * </pre>
 * 
 * @author andy
 * 
 */
public class PDFGeneratorOpenOffice implements PDFGenerator {

	private static final Logger LOGGER = Logger.getLogger(PDFGeneratorOpenOffice.class.getName());

	private File outputFile = null;

	public PDFGeneratorOpenOffice(final File outputFile) {
		this.outputFile = outputFile;
		LOGGER.log(Level.INFO, "preparing Directories....");
		// create necessary directories if they do not exist
		createDirectory(Constants.FILE_DIRECTORY_LABELGENERATOR);
		createDirectory(Constants.FILE_DIRECTORY_VCF_FILE);
		createDirectory(Constants.FILE_DIRECTORY_LAYOUT_FILE);
		createDirectory(Constants.FILE_DIRECTORY_TMP);
	}

	/**
	 * TODO pass the template file to use to the method.
	 */
	public boolean generate(Layout layout, List<ButtonLabel> buttonLabelList, boolean drawGrid) {

		File tmpTemplateOdtFile = null;

		try {
			// delete the tmp file
			tmpTemplateOdtFile = File.createTempFile("template", ".odt", Constants.FILE_DIRECTORY_TMP);
			final File tmpOriginalTemplateConentXmlFile = File.createTempFile("content", ".xml",
					Constants.FILE_DIRECTORY_TMP);

			// and copy the template to the tmpTemplateOdtFile
			TFile.cp(PDFGeneratorOpenOffice.class.getClassLoader().getResourceAsStream(
					"layouts/odt/avery_zweckform_3474.odt"), tmpTemplateOdtFile);

			// copy the content.xml from the zip to the
			// tmpOriginalTemplateContentXmlFile
			TFile contentXmlInOdt = new TFile(tmpTemplateOdtFile.getCanonicalPath() + Constants.SEPARATOR
					+ "content.xml");
			TFile.cp(contentXmlInOdt, tmpOriginalTemplateConentXmlFile);

			// now perform the stylesheet transformation with the
			// tmpOriginalTemplateConentXmlFile
			final StylesheetTransformation transformation = new StylesheetTransformation();
			final String contentXmlAfterTransformation = transformation.generate(null, buttonLabelList);
			writeStringToFile(tmpOriginalTemplateConentXmlFile, contentXmlAfterTransformation);

			// and copy the result back to the tmpTemplateOdtFile
			TFile.cp_p(tmpOriginalTemplateConentXmlFile, contentXmlInOdt);

			// unmount to make sure file is written
			TFile.umount();

			// convert the odt file to pdf
			LOGGER.log(Level.INFO, "Converting file {0} to PDF.", tmpTemplateOdtFile);

			// convert file to pdf
			convertOdtToPdf(tmpTemplateOdtFile);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO fix exception handling
			e.printStackTrace();
		}

		// TODO Auto-generated method stub
		return false;
	}

	private void convertOdtToPdf(File sourceOdtFile) throws IOException {
		// connect to an OpenOffice.org instance running on port 8100
		OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
		connection.connect();

		// creater converter
		DocumentConverter converter = new OpenOfficeDocumentConverter(connection);

		// create a PDF DocumentFormat (as normally configured in
		// document-formats.xml)
		DocumentFormat customPdfFormat = new DocumentFormat("Portable Document Format", "application/pdf", "pdf");
		customPdfFormat.setExportFilter(DocumentFamily.TEXT, "writer_pdf_Export");

		// now set our custom options
		final Map<String, Object> pdfOptions = new HashMap<String, Object>();
		pdfOptions.put("EncryptFile", Boolean.FALSE);
		customPdfFormat.setExportOption(DocumentFamily.TEXT, "FilterData", pdfOptions);

		// convert
		converter.convert(sourceOdtFile, outputFile, customPdfFormat);

		// close the connection
		connection.disconnect();
	}

	public static void main(String args[]) {
		PDFGeneratorOpenOffice pdfGenerator = new PDFGeneratorOpenOffice(new File("c:\\temp\\test.pdf"));

		List<ButtonLabel> buttonLabels = new ArrayList<ButtonLabel>();
		ButtonLabel buttonLabel = new ButtonLabel(4);
		buttonLabel.setAddress(new MockAddress2());

		buttonLabels.add(buttonLabel);

		buttonLabel = new ButtonLabel(2);
		buttonLabel.setAddress(new MockAddress());

		buttonLabels.add(buttonLabel);

		pdfGenerator.generate(null, buttonLabels, false);
	}

	private void createDirectory(final File directory) {
		if (!directory.exists()) {
			directory.mkdir();
		}
	}

	/**
	 * Writes the fileContent to the given fileName. Throws an IOException if an
	 * error occured, else true.
	 * 
	 * @param targetFile
	 * @param fileContent
	 * @return
	 * @throws IOException
	 */
	private void writeStringToFile(File targetFile, String fileContent) throws IOException {
		final FileWriter fileWriter = new FileWriter(targetFile, false);
		fileWriter.write(fileContent);
		fileWriter.close();
	}

}
