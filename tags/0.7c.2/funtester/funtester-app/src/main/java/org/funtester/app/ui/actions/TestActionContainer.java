package org.funtester.app.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;

import org.funtester.app.common.MessageListener;
import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppState;
import org.funtester.app.project.FileState;
import org.funtester.app.project.Project;
import org.funtester.app.project.ProjectListener;
import org.funtester.app.project.TestGenerationPerformer;
import org.funtester.app.repository.json.JsonTestExecutionReportRepository;
import org.funtester.app.ui.common.DefaultFileNameExtensionFilters;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.testing.TestExecutionDialog;
import org.funtester.app.ui.testing.TestExecutionReportDialog;
import org.funtester.app.ui.testing.TestGenerationDialog;
import org.funtester.app.ui.util.BaseAction;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.app.ui.util.SimpleFileChooser;
import org.funtester.app.ui.util.UIUtil;
import org.funtester.common.generation.TestGenerationConfiguration;
import org.funtester.common.report.TestExecutionReport;
import org.funtester.common.report.TestExecutionReportRepository;
import org.funtester.common.util.FileUtil;
import org.funtester.common.util.ProcessingListener;

/**
 * Container for the "Test" actions.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class TestActionContainer implements ProjectListener {
	
	private final JFrame owner;
	private final String title;
	private final AppState appState;
	private final TestGenerationPerformer performer;
	
	/** Run after editing the configuration */
	private Action generateAndRunAction = null;
	
	/** Just generate test code using a copy of the last configuration */
//	private Action justGenerateTestCodeAction = null;
	
	/** Just run the tests using a copy of the last configuration */
	private Action justRunTestsAction = null;
	
	/** Open a test execution report */
	private Action openReportAction = null;
	
	/**
	 * Create the action container.
	 * 
	 * @param owner
	 * @param title
	 * @param state
	 */
	public TestActionContainer(
			JFrame owner,
			final String title,
			final AppState state
			) {
		this.owner = owner;
		this.title = title;
		this.appState = state;
		this.performer = new TestGenerationPerformer( this.appState );
		
		updateActionsEnabledState();
	}
	
	public Action getGenerateAndRunAction() {
		return ( null == generateAndRunAction )
				? generateAndRunAction = new BaseAction()
					.withName( Messages.alt( "_MENU_TEST_GENERATE_AND_RUN", "Generate and Run..." ) )
					.withAcceleratorKey( KeyStroke.getKeyStroke( KeyEvent.VK_F9, 0 ) ) // F9
					.withIcon( ImageUtil.loadIcon( ImagePath.testRunIcon() ) )
					.withListener( new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							runAfterEditingConfiguration();
						}
					} )
				: generateAndRunAction;
	}
	
	/* TODO to enter in the future ?
	public Action getJustGenerateTestCodeAction() {
		return ( null == justGenerateTestCodeAction )
				? justGenerateTestCodeAction = new BaseAction()
					.withName( Messages.alt( "_MENU_TEST_JUST_GENERATE_TEST_CODE", "Generate Test Code (only)" ) )
					.withAcceleratorKey( KeyStroke.getKeyStroke( KeyEvent.VK_F8, 0 ) ) // F8
					//.withIcon( ImageUtil.loadIcon( ImagePath.testRunIcon() ) )
					.withListener( new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							justGenerateTestCode();
						}
					} )
				: justGenerateTestCodeAction;
	}
	*/

	public Action getJustRunTestsAction() {
		return ( null == justRunTestsAction )
				? justRunTestsAction = new BaseAction()
					.withName( Messages.alt( "_MENU_TEST_JUST_RUN_TESTS", "Run Tests (only)" ) )
					.withAcceleratorKey( KeyStroke.getKeyStroke( KeyEvent.VK_F9, KeyEvent.CTRL_MASK) ) // Ctrl + F9
					.withIcon( ImageUtil.loadIcon( ImagePath.testRunOnlyIcon() ) )
					.withListener( new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							justRunTests();
						}
					} )
				: justRunTestsAction;
	}
	
	public Action getOpenReportAction() {
		return ( null == openReportAction )
				? openReportAction = new BaseAction()
					.withName( Messages.alt( "_MENU_TEST_OPEN_REPORT", "Open Report..." ) )
					//.withAcceleratorKey( KeyStroke.getKeyStroke( KeyEvent.VK_F9, 0 ) )
					//.withIcon( ImageUtil.loadIcon( ImagePath.testRunIcon() ) )
					.withListener( new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							showTestExecutionReportFile( null );
						}
					} )
				: openReportAction;
	}

	@Override
	public void hasOpened(Project p) {
		updateActionsEnabledState();
	}

	@Override
	public void hasClosed(Project p) {
		updateActionsEnabledState();
	}

	@Override
	public void hasSaved(Project p) {
		// Do nothing
	}
	
	public void updateActionsEnabledState() {
		final boolean isClosed = ( FileState.CLOSED == appState.getProjectFileState() );
		
		final boolean hasUseCases = ! isClosed &&
				( appState.getProject().getSoftware().numberOfUseCases() > 0 ); // TODO refactor
		
		getGenerateAndRunAction().setEnabled( hasUseCases );
//		getJustGenerateTestCodeAction().setEnabled( ! isClosed );
		getJustRunTestsAction().setEnabled( hasUseCases );
		getOpenReportAction().setEnabled( true ); // Always ?
	}
	
	/*
	private void justGenerateTestCode() {
		runDirectly( true, false );
	}*/
	
	private void justRunTests() {
		runDirectly( false, true );
	}
	
	/**
	 * Run the last configuration directly.
	 */
	private void runDirectly(
			final boolean generateTestCode,
			final boolean runTests
			) {
		
		// Get the last project configuration
		TestGenerationConfiguration cfg = appState.getProject().getLastConfiguration();
		if ( null == cfg ) {
			String msg = Messages.alt( "_TEST_GENERATION_NONE_CONFIGURATION_TO_RUN",
					"None configuration to run." );
			MsgUtil.error( owner, msg, title );
			return;
		}
		
		// Create a copy of the configuration to run
		final TestGenerationConfiguration cfgCopy = cfg.newCopy();
		cfgCopy.setGenerateCode( generateTestCode );
		cfgCopy.setRun( runTests );
		
		// Run
		performConfiguration( cfgCopy );
	}
	
	
	/**
	 * Edit the last configuration and run.
	 */
	private void runAfterEditingConfiguration() {
		
		// Get the last project configuration
		TestGenerationConfiguration cfg = appState.getProject().getLastConfiguration();
		if ( null == cfg ) {
			// Create if null
			cfg = new TestGenerationConfiguration();
			appState.getProject().setLastConfiguration( cfg );
		}
		
		// Show to edit
		final boolean confirmed = editConfiguration( cfg );
		if ( ! confirmed ) {
			return;
		}
		
		performConfiguration( cfg );
	}

	
	class MyWorker extends SwingWorker< String, String >
			implements MessageListener //, Consumer
			{
		
		private final TestGenerationConfiguration cfg;
		private final MessageListener ml;
		
		public MyWorker(
				final TestGenerationConfiguration cfg,
				final MessageListener ml
				) {
			this.cfg = cfg;	
			this.ml = ml;
		}
	
		@Override
		protected String doInBackground() throws Exception {
			executePlugin( this, cfg );
			return "done";
		}
		
		@Override
		protected void process(List< String > lines) {
			for ( String line : lines ) {
				ml.published( line );
			}
		}
	
		@Override
		public void published(String message) {
			publish( message );
		}
	}
	
	
	private void performConfiguration(final TestGenerationConfiguration cfg) {
		
		final TestExecutionDialog dlg = new TestExecutionDialog( owner );
		UIUtil.centerOnScreen( dlg );
		//dlg.setAlwaysOnTop( true );
		dlg.setVisible( true ); // Non modal
		
		MyWorker worker = new MyWorker( cfg, dlg );
		worker.addPropertyChangeListener( dlg ); // Add the dialog as a listener
		worker.execute();
	}
	
	void executePlugin(
			final MessageListener listener,
			//final Consumer consumer,
			final TestGenerationConfiguration cfg
			) {
		final CollectingLogOutputStream outputStream = new CollectingLogOutputStream();
		outputStream.addMessageListener( listener );
		
		//PrintStream old = System.out;
		//StreamCapturer capturer = new StreamCapturer( "", consumer, old );
		//System.setOut( new PrintStream( capturer ) );
		
		try {
			performer.performConfiguration( cfg, outputStream );
			
			outputStream.removeMessageListener( listener );
			
			if ( cfg.getRun() ) { // Show the report
				showTestExecutionReportFile( cfg.getConvertedResultsFile() );
			}
			
		} catch (Exception e) {
			outputStream.removeMessageListener( listener );
			MsgUtil.error( owner, e.getLocalizedMessage(), title );
		}
		
		//System.setOut( old );		
	}
	
	
	/**
	 * Edit a given {@code TestGenerationConfiguration}.
	 * 
	 * @param cfg	the configuration to edit.
	 * @return		true if configured, false otherwise.
	 */
	private boolean editConfiguration(
			TestGenerationConfiguration cfg
			) {
		TestGenerationDialog dlg = new TestGenerationDialog( appState );
		UIUtil.centerOnScreen( dlg );
		if ( cfg != null ) {
			dlg.copyObject( cfg );
		}
		dlg.showObject();
		if ( ! dlg.isConfirmed() ) { return false; }
		cfg.copy( dlg.getObject() );
		return true;
	}

	

	/**
	 * Show a test execution report file.
	 * 
	 * @param testExecutionReportFilePath the test execution report file path.
	 */
	private void showTestExecutionReportFile(
			final String testExecutionReportFilePath
			) {
		
		final String filePath;
		
		if ( null == testExecutionReportFilePath || testExecutionReportFilePath.isEmpty()
				|| ( ! ( new File( testExecutionReportFilePath ) ).exists() ) ) {
			
			String dialogTitle = Messages.alt( "_OPEN_FILE",  "Open" );
			File currentDirectory = FileUtil.currentDirectory();
			
			filePath = SimpleFileChooser.chooseFile(
					owner, dialogTitle, currentDirectory,
					DefaultFileNameExtensionFilters.TEST_EXECUTION_REPORT, false );
			if ( null == filePath ) { return; }
			
		} else {
			filePath = testExecutionReportFilePath;
		}
		
		
		TestExecutionReportRepository repository = new JsonTestExecutionReportRepository( filePath );
		TestExecutionReport report;
		try {
			report = repository.first();
		} catch ( Exception e ) {
			MsgUtil.error( owner, e.getLocalizedMessage(), title );
			return;
		}
		
		ProcessingListener listener = null; // TODO create it
		TestExecutionReportDialog dlg = new TestExecutionReportDialog( owner, report, listener );
		UIUtil.centerOnScreen( dlg );
		dlg.setVisible( true );
	}

}