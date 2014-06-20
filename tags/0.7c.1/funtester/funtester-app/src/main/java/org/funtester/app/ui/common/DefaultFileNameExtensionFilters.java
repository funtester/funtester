package org.funtester.app.ui.common;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.funtester.app.common.DefaultFileExtensions;
import org.funtester.app.i18n.Messages;

/**
 * Default {@link FileNameExtensionFilter}s.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class DefaultFileNameExtensionFilters {
	
	private DefaultFileNameExtensionFilters() {}
	

	public static final FileNameExtensionFilter PROJECT =
			new FileNameExtensionFilter(
					String.format( Messages.alt( "_PROJECT_FILE_EXTENSION_NAME",
							"FunTester Project file (*.%s)" ),
							DefaultFileExtensions.PROJECT ),
					new String[] { DefaultFileExtensions.PROJECT }
			);
	
	
	public static final FileNameExtensionFilter ABSTRACT_TESTS =
			new FileNameExtensionFilter(
					String.format( Messages.alt( "_ABSTRACT_TESTS_FILE_EXTENSION_NAME",
							"FunTester Abstract Tests file (*.%s)" ),
							DefaultFileExtensions.ABSTRACT_TESTS ),
					new String[] { DefaultFileExtensions.ABSTRACT_TESTS }
			);
	
	
	public static final FileNameExtensionFilter TEST_EXECUTION_REPORT =
			new FileNameExtensionFilter(
					String.format( Messages.alt( "_TEST_EXECUTION_REPORT_FILE_EXTENSION_NAME",
							"FunTester Test Execution Report file (*.%s)" ),
							DefaultFileExtensions.TEST_EXECUTION_REPORT ),
					new String[] { DefaultFileExtensions.TEST_EXECUTION_REPORT }
			);
	
	
	public static final FileNameExtensionFilter MANUAL =
			new FileNameExtensionFilter(
					String.format( Messages.alt( "_MANUAL_FILE_EXTENSION_NAME",
							"Portable Document File (*.%s)" ),
							DefaultFileExtensions.MANUAL ),
					new String[] { DefaultFileExtensions.MANUAL }
			);
	
	
	public static final FileNameExtensionFilter ANY = null; // It is really null
}
