package de.jos.labelgenerator.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import de.jos.labelgenerator.components.button.ButtonLabel;
import de.jos.labelgenerator.configuration.Layout;
import de.jos.labelgenerator.utils.FontUtil;

public class PDFGenerator {

	private static final Logger logger = Logger.getLogger(PDFGenerator.class.getName());

	private File targetFile;

	public PDFGenerator(final File targetFile) {
		this.targetFile = targetFile;
	}

	public boolean generate(Layout layout, List<ButtonLabel> buttonLabelList, boolean drawGrid) {
		boolean result = false;

		// check if the given font is supported at all
		if (FontUtil.isFontSupported(layout.getFontName()) == false) {
			logger.log(Level.SEVERE,
					"Font {0} of selected Layout is not supported. The following fonts are supported {1}.",
					new Object[] { layout.getFontName(), FontUtil.getSupportedFonts().toString() });
			return result;
		}

		try {
			logger.log(Level.INFO, "Creating tmp PDF file in directory {0}", targetFile.getAbsolutePath());

			final Document document = new Document(PageSize.A4);
			final PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(targetFile));

			document.open();

			final PdfContentByte cb = writer.getDirectContent();

			final BaseFont font = BaseFont.createFont(layout.getFontName(), "UTF-8", false);

			int index = 0;
			for (int row = 0; row < layout.getRows(); row++) {
				for (int col = layout.getColumns() - 1; col >= 0; col--) {
					final ButtonLabel buttonLabel = buttonLabelList.get(buttonLabelList.size() - index - 1);
					String text = buttonLabel.getRenderedTemplate();

					logger.log(Level.SEVERE, "Text {0} : {1} ", new Object[] { Integer.valueOf(index),
							buttonLabel.getRenderedTemplate() });

					final int x = mmToPoints(layout.getMarginLeft() + col * layout.getWidthNext());
					final int y = mmToPoints(layout.getMarginTop() + row * layout.getHeightNext());
					final int width = mmToPoints(layout.getWidth());
					final int height = mmToPoints(layout.getHeight());
					final Rectangle r = getRectangle(x, y, width, height);

					// draw grid only when enabled!
					if (drawGrid == true) {
						cb.rectangle(r.getLeft(), r.getTop(), r.getWidth(), r.getHeight());
						cb.stroke();
						// do print the index number when no text is defined
						if (StringUtils.trimToNull(text) == null) {
							text = buttonLabel.getIndex().toString();
						}
					}

					final String[] lines = StringUtils.split(text, "\n");

					System.out.println(layout.getTextMarginTop());
					
					int offset = 0;
					if (lines != null && lines.length > 0) {
						for (String tmpLine : lines) {
							cb.beginText();
							cb.setFontAndSize(font, layout.getFontSize());
							cb.showTextAligned(PdfContentByte.ALIGN_LEFT, StringUtils.trim(tmpLine), x
									+ mmToPoints(layout.getTextMarginLeft().floatValue()), y
									+ height
									- (offset * (layout.getFontSize().floatValue() + layout.getTextMargin()
											.floatValue())) - mmToPoints(layout.getTextMarginTop().floatValue()), 0);
							cb.endText();
							offset++;
						}
					}

					index++;
				}
			}

			document.close();

			result = true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception occured creating PDF file.", e);
			e.printStackTrace();
		}

		return result;
	}

	private Rectangle getRectangle(int x, int y, int width, int height) {
		Rectangle r = new Rectangle(x, PageSize.A4.getHeight() - y + height, x + width, PageSize.A4.getHeight() - y);
		return r;
	}

	private int mmToPoints(double mm) {
		return (int) (72 * mm / 25.4);
	}

	private double pointsToMm(double points) {
		return (double) (points * 25.4 / 72.0);
	}

}
