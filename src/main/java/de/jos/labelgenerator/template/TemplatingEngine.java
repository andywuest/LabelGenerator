package de.jos.labelgenerator.template;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import de.jos.labelgenerator.configuration.UndefinedAddress;
import freemarker.cache.ClassTemplateLoader;
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
