package de.jos.labelgenerator.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.itextpdf.text.pdf.BaseFont;

public class FontUtil {

	private static final List<String> SUPPORTED_FONTS = new ArrayList<String>() {
		{
			add(BaseFont.COURIER);
			add(BaseFont.COURIER_BOLD);
			add(BaseFont.COURIER_OBLIQUE);
			add(BaseFont.COURIER_BOLDOBLIQUE);
			add(BaseFont.HELVETICA);
			add(BaseFont.HELVETICA_BOLD);
			add(BaseFont.HELVETICA_OBLIQUE);
			add(BaseFont.HELVETICA_BOLDOBLIQUE);
			add(BaseFont.SYMBOL);
			add(BaseFont.TIMES_ROMAN);
			add(BaseFont.TIMES_BOLD);
			add(BaseFont.TIMES_ITALIC);
			add(BaseFont.TIMES_BOLDITALIC);
			add(BaseFont.ZAPFDINGBATS);
		}
	};

	public static boolean isFontSupported(String fontName) {
		return SUPPORTED_FONTS.contains(StringUtils.trimToEmpty(fontName));
	}

	public static final List<String> getSupportedFonts() {
		return Collections.unmodifiableList(SUPPORTED_FONTS);
	}

}
