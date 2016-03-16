package org.funtester.app.project;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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
			// Freemarker configuration
			BeansWrapper wrapper = new BeansWrapper();
			wrapper.setSimpleMapWrapper( true );
			cfg.setObjectWrapper( wrapper );
			Map< String, Object > data = new HashMap< String, Object >();
			data.put( "software", software );

			final String freeMarkerTemplatePath = "templates/freemarker/";
			final String htmlTemplatePath = "templates/html/";
			final String targetPath = "tmp/";

			// CSS Template processing
			Template cssTemplate = cfg.getTemplate(
					freeMarkerTemplatePath + "Style.ftl" );

			final String CSS_FILE = "style.css";
			final String CSS_SOURCE_PATH = htmlTemplatePath + CSS_FILE;
			Writer cssFile = new FileWriter( new File( CSS_SOURCE_PATH ) );

			cssTemplate.process( data, cssFile );
			cssFile.flush();
			cssFile.close();

			// HTML UseCase Description Template processing
			Template useCaseDescriptionTemplate = cfg.getTemplate(
					freeMarkerTemplatePath + "PrintPreview.ftl" );

			Writer useCaseDescriptionFile = new FileWriter( new File(
					targetPath + "UseCaseDescription.html" ) );

			useCaseDescriptionTemplate.process( data, useCaseDescriptionFile );
			useCaseDescriptionFile.flush();
			useCaseDescriptionFile.close();

			// HTML UseCase Diagram Template processing
			Template useCaseDiagramTemplate = cfg.getTemplate(
					freeMarkerTemplatePath + "UseCaseDiagram.ftl" );

			Writer useCaseDiagramFile = new FileWriter( new File(
					targetPath + "UseCaseDiagram.html" ) );

			useCaseDiagramTemplate.process( data, useCaseDiagramFile );
			useCaseDiagramFile.flush();
			useCaseDiagramFile.close();

			final String USE_CASE_DESCRIPTION_FILE = "UseCaseDescription.html";
			final String USE_CASE_DIAGRAM_FILE = "UseCaseDiagram.html";


			final String CSS1_FILE = "style.css";
			final String CSS2_FILE = "UDStyle.css";
			final String JS1_FILE = "UDCore.js";
			final String JS2_FILE = "UDModules.js";

			final String srcPath = FileUtil.currentDirectoryAsString() + "\\";


			FileUtil.copy( new File( htmlTemplatePath + CSS1_FILE ), new File( targetPath + CSS1_FILE ) );
			FileUtil.copy( new File( htmlTemplatePath + CSS2_FILE ), new File( targetPath + CSS2_FILE ) );
			FileUtil.copy( new File( htmlTemplatePath + JS1_FILE ), new File( targetPath + JS1_FILE ) );
			FileUtil.copy( new File( htmlTemplatePath + JS2_FILE ), new File( targetPath + JS2_FILE ) );

			//FileUtil.copy( new File( srcPath + USE_CASE_DESCRIPTION_FILE ), new File( targetPath + USE_CASE_DESCRIPTION_FILE ) );
			//FileUtil.copy( new File( srcPath + USE_CASE_DIAGRAM_FILE ), new File( targetPath + USE_CASE_DIAGRAM_FILE ) );

			// Show HTML generated files in default System Browser
			Desktop desktop = Desktop.getDesktop();
			desktop.browse( new URI( targetPath + "UseCaseDescription.html" ) );
			desktop.browse( new URI( targetPath + "UseCaseDiagram.html" ) );

		} catch ( Exception e ) {
			throw new DocGenerationException( e );
		}

	}

}
