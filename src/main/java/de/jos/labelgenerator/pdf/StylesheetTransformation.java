package de.jos.labelgenerator.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import de.jos.labelgenerator.components.button.ButtonLabel;
import de.jos.labelgenerator.configuration.Layout;
import de.jos.labelgenerator.configuration.addressProvider.MockAddress;
import de.jos.labelgenerator.configuration.addressProvider.MockAddress2;
import de.jos.labelgenerator.template.TemplatingEngine;

public class StylesheetTransformation {

	private static final Logger logger = Logger.getLogger(PDFGenerator.class.getName());

	private static final ClassLoader classLoader = StylesheetTransformation.class.getClassLoader();

	private static final String XSLT_FILENAME = "xslt/replacement_template.xsl";

	private static final String TEMPLATE_TO_MAKE_DYNAMIC = "layouts/odt/template.xml";

	private File targetFile;

	private TemplatingEngine templateEngine = new TemplatingEngine();

	public StylesheetTransformation(final File targetFile) {
		this.targetFile = targetFile;
	}

	public boolean generate(Layout layout, List<ButtonLabel> buttonLabelList) throws Exception {

		// read the xslt file
		final String originalStylesheet = readFileFromResourceInString(XSLT_FILENAME);
		final String originalTemplate = readFileFromResourceInString(TEMPLATE_TO_MAKE_DYNAMIC);

		System.out.println(originalStylesheet);

		final TransformerFactory transformerFactory = TransformerFactory.newInstance();

		String workingTemplate = originalTemplate;
		// now start applying the velocity placeholders for each address
		for (ButtonLabel tmpButtonLabel : buttonLabelList) {

			// replace address, frameNumber, leadingBlanks
			final String renderedXslt = templateEngine.renderXsltTempalte(tmpButtonLabel.getAddress(),
					originalStylesheet, tmpButtonLabel.getIndex());
			System.out.println("rendered xlst:" + renderedXslt);

			final Transformer transformer = transformerFactory.newTransformer(new StreamSource(
					new ByteArrayInputStream(renderedXslt.getBytes())));

			// perform stylesheet transformation
			final ByteArrayOutputStream bout = new ByteArrayOutputStream();
			transformer.transform(new StreamSource(new ByteArrayInputStream(workingTemplate.getBytes())),
					new StreamResult(bout));
			workingTemplate = bout.toString("UTF-8");
		}

		System.out.println(workingTemplate);

		return false;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		List<ButtonLabel> buttonLabels = new ArrayList<ButtonLabel>();
		ButtonLabel buttonLabel = new ButtonLabel(4);
		buttonLabel.setAddress(new MockAddress2());

		buttonLabels.add(buttonLabel);

		buttonLabel = new ButtonLabel(2);
		buttonLabel.setAddress(new MockAddress());

		buttonLabels.add(buttonLabel);

		StylesheetTransformation transformation = new StylesheetTransformation(null);
		transformation.generate(null, buttonLabels);
	}

	/**
	 * Read content from a file and put it into a String.
	 * 
	 * @param fileName 
	 * @return
	 * @throws IOException
	 */
	private String readFileFromResourceInString(String fileName) throws IOException {
		final InputStream inputStream = classLoader.getResourceAsStream(fileName);
		final InputStreamReader reader = new InputStreamReader(inputStream);

		// read content
		final StringBuilder sb = new StringBuilder();
		final char[] buffer = new char[1024];
		int len = -1;
		while ((len = reader.read(buffer)) != -1) {
			sb.append(new String(buffer, 0, len));
		}

		return sb.toString();
	}

}
