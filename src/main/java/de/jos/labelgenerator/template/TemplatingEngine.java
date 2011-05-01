package de.jos.labelgenerator.template;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import de.jos.labelgenerator.configuration.Address;
import de.jos.labelgenerator.configuration.UndefinedAddress;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TemplatingEngine {

	private final Configuration cfg;

	public TemplatingEngine() {
		cfg = new Configuration();
		final TemplateLoader templateLoader = new ClassTemplateLoader(TemplatingEngine.class, "/templates");
		cfg.setTemplateLoader(templateLoader);
	}

	public String renderXsltTempalte(Address address, String templateString, Integer frameNumber) {
		String renderedTemplate = null;
		try {
			// TODO cleanup template loader
			StringTemplateLoader stringLoader = new StringTemplateLoader();
			stringLoader.putTemplate("xsltTemplate", templateString);
			cfg.setTemplateLoader(stringLoader);
			final Template template = cfg.getTemplate("xsltTemplate");

			// create datamodel
			Map<String, Object> datamodel = new HashMap<String, Object>();
			datamodel.put("address", address);
			datamodel.put("frameNumber", frameNumber);
			datamodel.put("leadingBlanks", Integer.valueOf(6));

			final ByteArrayOutputStream baout = new ByteArrayOutputStream();
			final Writer out = new OutputStreamWriter(baout);
			template.process(datamodel, out);
			out.flush();

			renderedTemplate = baout.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return renderedTemplate;
	}

	public String renderTemplate(Object model, String templateName) {
		String renderedTemplate = null;
		try {
			final Template template = cfg.getTemplate(templateName);
			final ByteArrayOutputStream baout = new ByteArrayOutputStream();
			final Writer out = new OutputStreamWriter(baout);

			template.process(model, out);
			out.flush();

			renderedTemplate = baout.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return renderedTemplate;
	}

	public static void main(String args[]) {
		TemplatingEngine engine = new TemplatingEngine();
		System.out.println(engine.renderTemplate(new UndefinedAddress(), "template_default.ftl"));
	}

}
