package de.jos.labelgenerator.pdf;

import java.util.List;

import de.jos.labelgenerator.components.button.ButtonLabel;
import de.jos.labelgenerator.configuration.Layout;

public interface PDFGenerator {

	boolean generate(Layout layout, List<ButtonLabel> buttonLabelList, boolean drawGrid);

}
