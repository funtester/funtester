package org.funtester.app.project.export;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.funtester.app.project.Directories;
import org.funtester.app.project.DocGenerationException;
import org.funtester.app.project.DocGenerator;
import org.funtester.common.util.FileUtil;
import org.funtester.core.software.Software;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * HTML documentation generator.
 *
 * @author Thiago Delgado Pinto
 *
 */
public class HTMLDocGenerator implements DocGenerator {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.funtester.app.documentation.DocGenerator#generate(org.funtester.core
	 * .software.Software)
	 */
	@Override
	public void generate(Software software) throws DocGenerationException {

		Configuration cfg = new Configuration();

		try {
			// Freemarker configuration ----------------------------------------

			BeansWrapper wrapper = new BeansWrapper();
			wrapper.setSimpleMapWrapper( true );
			cfg.setObjectWrapper( wrapper );
			Map< String, Object > data = new HashMap< String, Object >();
			data.put( "software", software );

			final String freeMarkerTemplatePath =
					"templates" + File.separator + "freemarker"  + File.separator;
			final String htmlTemplatePath =
					"templates" + File.separator + "html" + File.separator;
			final String targetPath = "tmp" + File.separator;

			// HTML UseCase Description Template processing --------------------

			final String USE_CASE_DESCRIPTION_FILE = "UseCaseDescription.html";

			Template useCaseDescriptionTemplate = cfg.getTemplate(
					freeMarkerTemplatePath + "UseCaseDescription.ftl" );

			Writer useCaseDescriptionFile = new FileWriter( new File(
					targetPath + USE_CASE_DESCRIPTION_FILE ) );

			useCaseDescriptionTemplate.process( data, useCaseDescriptionFile );
			useCaseDescriptionFile.flush();
			useCaseDescriptionFile.close();

			// Copy CSS --------------------------------------------------------

			final String CSS1_FILE = "style.css";

			FileUtil.copy( new File( htmlTemplatePath + CSS1_FILE ),
					new File( targetPath + CSS1_FILE ) );

			// Show ------------------------------------------------------------

			URI uri = new URI( FileUtil.toURI(
				FileUtil.currentDirectoryAsString() + File.separator +
				targetPath + USE_CASE_DESCRIPTION_FILE
				) );

			Desktop desktop = Desktop.getDesktop();
			desktop.browse( uri );

		} catch ( Exception e ) {
			throw new DocGenerationException( e );
		}

	}

}
