package org.funtester.app.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppInfo;
import org.funtester.app.project.AppState;
import org.funtester.app.project.FileState;
import org.funtester.app.project.Project;
import org.funtester.app.project.ProjectListener;
import org.funtester.app.ui.actions.EditActionContainer;
import org.funtester.app.ui.actions.FileActionContainer;
import org.funtester.app.ui.actions.HelpActionContainer;
import org.funtester.app.ui.actions.TestActionContainer;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.software.actions.ActorActionContainer;
import org.funtester.app.ui.software.actions.DatabaseConfigActionContainer;
import org.funtester.app.ui.software.actions.QueryConfigActionContainer;
import org.funtester.app.ui.software.actions.RegExActionContainer;
import org.funtester.app.ui.software.actions.UseCaseListActionContainer;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.ToolBarUtil;
import org.funtester.common.util.ProcessingListener;
import org.funtester.common.util.ProgressListener;
import org.funtester.core.software.Software;

/**
 * Main frame.
 *
 * TODO refactor
 *
 * @author Thiago Delgado Pinto
 *
 */
public class MainFrame extends JFrame implements
		ProgressListener, ProcessingListener, ProjectListener {

	private static final long serialVersionUID = -503786243009441515L;

	private JPanel contentPane;
	private JPanel topPanel;
	private JPanel centerPanel;
	private JProgressBar progressBar;
	private JMenuBar menuBar;
	private JMenu softwareMenu = null;
	private JToolBar fileToolBar;
	private JToolBar softwareToolBar = null;
	private JToolBar testToolBar = null;

	private ProjectPanel projectPanel = null; // Created on demand

	// Message list
	private final DefaultListModel listModel = new DefaultListModel();

	private final FileActionContainer fileAC;
	private final EditActionContainer editAC;
	private final TestActionContainer testAC;
	private final HelpActionContainer helpAC;

	private final AppState appState;
	private final String appName;


	public MainFrame(AppState appState) {

		if ( null == appState ) throw new IllegalArgumentException( "appState is null" );
		this.appState = appState;

		final AppInfo appInfo = this.appState.getAppInfo();
		this.appName = ( appInfo != null ) ? appInfo.getName() : "FunTester";
		setTitle( appName );

		setIconImage( ImageUtil.loadImage( ImagePath.mainIcon() ) );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setBounds( 100, 100, 450, 300 );

		// Create the content pane

		contentPane = new JPanel();
		contentPane.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		contentPane.setLayout( new BorderLayout( 0, 0 ) );
		setContentPane( contentPane );

		// Create the action containers

		final String title = getTitle();
		fileAC = new FileActionContainer( this, this.appState );
		editAC = new EditActionContainer( this, title, this.appState );
		testAC = new TestActionContainer( this, title, this.appState );
		helpAC = new HelpActionContainer( this, title, this.appState );

		fileAC.addListener( this ); // The {@code MainFrame} is listen now
		fileAC.addListener( testAC );

		// Create the panels inside the content panel

		createTopPanel( contentPane );
		centerPanel = createCenterPanel( contentPane );
		createBottomPanel( contentPane );

		// Maximize the window

		if ( appState != null ) { // << This IF statement was placed for not maximizing in design time
			setExtendedState( MAXIMIZED_BOTH );
			toFront();
		}
	}

	private JPanel createCenterPanel(JPanel parent) {
		JPanel centerPanel = new JPanel();
		centerPanel.setBorder( null );
		centerPanel.setLayout( new BorderLayout(0, 0) );
		centerPanel.setBackground( SystemColor.controlShadow );
		parent.add( centerPanel, BorderLayout.CENTER );
		return centerPanel;
	}

	private void createTopPanel(JPanel parent ) {
		topPanel = new JPanel();
		topPanel.setBorder( null );
		topPanel.setLayout( new BorderLayout(0, 0) );
		parent.add( topPanel, BorderLayout.NORTH );

		menuBar = createMenuBar( topPanel );
		createFileToolBar( topPanel );
	}

	private JMenuBar createMenuBar(JPanel parent) {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setName( "menuBar" );
		parent.add( menuBar, BorderLayout.NORTH );

		// File
		createFileMenu( menuBar );

		// Edit
		createEditMenu( menuBar );

		// Test
		createTestMenu( menuBar );

		// Help
		createHelpMenu( menuBar );


		// Create the "X" button to close
		JButton fileCloseButton = new JButton( fileAC.getFileCloseAction() );
		fileCloseButton.setName( "fileCloseButton" );
		fileCloseButton.setText( "" );
		fileCloseButton.setIcon( ImageUtil.loadIcon( ImagePath.closeIcon() ) );
		fileCloseButton.setContentAreaFilled( false );
		// Create a glue. After this point any item added to the menu will
		// be right aligned
		menuBar.add( Box.createHorizontalGlue() );
		// Add the button
		menuBar.add( fileCloseButton );

		return menuBar;
	}

	private void createFileMenu(JMenuBar menuBar) {
		JMenu fileMenu = new JMenu( Messages.getString("MainFrame.fileMenu.text") );  //$NON-NLS-1$
		fileMenu.setName( "fileMenu" );
		menuBar.add( fileMenu );

		JMenuItem newMenu = new JMenuItem( fileAC.getFileNewAction() );
		newMenu.setName( "newMenu" );
		fileMenu.add(newMenu);

		JMenuItem openMenu = new JMenuItem( fileAC.getFileOpenAction() );
		openMenu.setName( "openMenu" );
		fileMenu.add( openMenu );

		JMenuItem closeMenu = new JMenuItem( fileAC.getFileCloseAction() );
		closeMenu.setName( "closeMenu" );
		fileMenu.add( closeMenu );

		JSeparator separator1 = new JSeparator();
		fileMenu.add( separator1 );

		JMenuItem saveMenu = new JMenuItem( fileAC.getFileSaveAction() );
		saveMenu.setName( "saveMenu" );
		fileMenu.add( saveMenu );

		JMenuItem saveAsMenu = new JMenuItem( fileAC.getFileSaveAsAction() );
		saveAsMenu.setName( "saveAsMenu" );
		fileMenu.add( saveAsMenu );

		JSeparator separator2 = new JSeparator();
		fileMenu.add( separator2 );

		JMenu printMenu = new JMenu( fileAC.getFilePrintAction() );
		printMenu.setName( "printMenu" );
		fileMenu.add( printMenu );

		JMenu exportMenu = new JMenu( Messages.alt( "MainFrame.exportMenu.text", "Export As" ) );
		fileMenu.add(exportMenu);

		JMenuItem menuItem = new JMenuItem( fileAC.getFileExportAsHTMLAction() );
		exportMenu.add(menuItem);

		JSeparator separator3 = new JSeparator();
		fileMenu.add(separator3);

		JMenuItem exitMenu = new JMenuItem( fileAC.getFileExitAction() );
		exitMenu.setName( "exitMenu" );
		fileMenu.add( exitMenu );
	}

	private void createEditMenu(JMenuBar menuBar) {
		JMenu editMenu = new JMenu( Messages.getString("MainFrame.editMenu.text") );  //$NON-NLS-1$
		editMenu.setName( "editMenu" );
		menuBar.add( editMenu );

		JMenuItem configMenu = new JMenuItem( editAC.editConfigurationAction() );
		configMenu.setName( "configMenu" );
		editMenu.add( configMenu );

		JSeparator separator3 = new JSeparator();
		editMenu.add(separator3);

		JMenuItem profilesMenu = new JMenuItem( editAC.editProfilesAction() );
		profilesMenu.setName( "profilesMenu" );
		editMenu.add(profilesMenu);

		JMenuItem vocabulariesMenu = new JMenuItem( editAC.editVocabulariesAction() );
		vocabulariesMenu.setName( "vocabulariesMenu" );
		editMenu.add(vocabulariesMenu);
	}

	private JMenu createSoftwareMenu(UseCaseListActionContainer actionContainer) {
		JMenu softwareMenu = new JMenu( Messages.getString("MainFrame.softwareMenu.text") ); //$NON-NLS-1$
		softwareMenu.setName( "softwareMenu" );

		// USE CASE

		JMenuItem useCaseNewMenu = new JMenuItem( actionContainer.useCaseNewAction() );
		useCaseNewMenu.setName( "useCaseNewMenu" );
		useCaseNewMenu.setText( Messages.getString( "_USE_CASE_NEW" ) );
		softwareMenu.add(useCaseNewMenu);

		JMenuItem useCaseEditMenu = new JMenuItem( actionContainer.useCaseEditAction() );
		useCaseEditMenu.setName( "useCaseEditMenu" );
		useCaseEditMenu.setText( Messages.getString( "_USE_CASE_EDIT" ) );
		softwareMenu.add(useCaseEditMenu);

		JMenuItem useCaseRemoveMenu = new JMenuItem( actionContainer.useCaseRemoveAction() );
		useCaseRemoveMenu.setName( "useCaseRemoveMenu" );
		useCaseRemoveMenu.setText( Messages.getString( "_USE_CASE_REMOVE" ) );
		softwareMenu.add(useCaseRemoveMenu);

		/*
		// FLOW

		JSeparator flowSeparator = new JSeparator();
		softwareMenu.add(flowSeparator);

		JMenuItem flowNewMenu = new JMenuItem( actionContainer.flowNewAction() );
		flowNewMenu.setName( "flowNewMenu" );
		softwareMenu.add(flowNewMenu);

		JMenuItem flowEditMenu = new JMenuItem( actionContainer.flowEditAction() );
		flowEditMenu.setName( "flowEditMenu" );
		softwareMenu.add(flowEditMenu);

		JMenuItem flowRemoveMenu = new JMenuItem( actionContainer.flowRemoveAction() );
		flowRemoveMenu.setName( "flowRemoveMenu" );
		softwareMenu.add(flowRemoveMenu);
		*/

		// ACTOR

		JSeparator actorSeparator = new JSeparator();
		softwareMenu.add(actorSeparator);

		ActorActionContainer actorAC = projectPanel.getActorActionContainer();

		JMenuItem actorNewMenu = new JMenuItem( actorAC.newAction() );
		actorNewMenu.setName( "actorNewMenu" );
		actorNewMenu.setText( Messages.getString( "_ACTOR_NEW" ) );
		softwareMenu.add( actorNewMenu );

		JMenuItem actorEditMenu = new JMenuItem( actorAC.editAction() );
		actorEditMenu.setName( "actorEditMenu" );
		actorEditMenu.setText( Messages.getString( "_ACTOR_EDIT" ) );
		softwareMenu.add( actorEditMenu );

		JMenuItem actorRemoveMenu = new JMenuItem( actorAC.removeAction() );
		actorRemoveMenu.setName( "actorRemoveMenu" );
		actorEditMenu.setText( Messages.getString( "_ACTOR_REMOVE" ) );
		softwareMenu.add( actorRemoveMenu );

		// DATABASE

		JSeparator databaseSeparator = new JSeparator();
		softwareMenu.add(databaseSeparator);

		DatabaseConfigActionContainer databaseConfigAC = projectPanel.getDatabaseConfigActionContainer();

		JMenuItem databaseConfigNewMenu = new JMenuItem( databaseConfigAC.getNewAction() );
		databaseConfigNewMenu.setName( "databaseConfigNewMenu" );
		databaseConfigNewMenu.setText( Messages.getString( "_DATABASE_CONFIG_NEW" ) );
		softwareMenu.add( databaseConfigNewMenu );

		JMenuItem databaseConfigEditMenu = new JMenuItem( databaseConfigAC.getEditAction() );
		databaseConfigEditMenu.setName( "databaseConfigEditMenu" );
		databaseConfigEditMenu.setText( Messages.getString( "_DATABASE_CONFIG_EDIT" ) );
		softwareMenu.add( databaseConfigEditMenu );

		JMenuItem databaseConfigRemoveMenu = new JMenuItem( databaseConfigAC.gerRemoveAction() );
		databaseConfigRemoveMenu.setName( "databaseConfigRemoveMenu" );
		databaseConfigRemoveMenu.setText( Messages.getString( "_DATABASE_CONFIG_REMOVE" ) );
		softwareMenu.add( databaseConfigRemoveMenu );

		// QUERY

		JSeparator querySeparator = new JSeparator();
		softwareMenu.add(querySeparator);

		QueryConfigActionContainer queryConfigAC = projectPanel.getQueryConfigActionContainer();

		JMenuItem queryConfigNewMenu = new JMenuItem( queryConfigAC.getNewAction() );
		queryConfigNewMenu.setName( "queryConfigNewMenu" );
		queryConfigNewMenu.setText( Messages.getString( "_QUERY_CONFIG_NEW" ) );
		softwareMenu.add( queryConfigNewMenu );

		JMenuItem queryConfigEditMenu = new JMenuItem( queryConfigAC.getEditAction() );
		queryConfigEditMenu.setName( "queryConfigEditMenu" );
		queryConfigEditMenu.setText( Messages.getString( "_QUERY_CONFIG_EDIT" ) );
		softwareMenu.add( queryConfigEditMenu );

		JMenuItem queryConfigRemoveMenu = new JMenuItem( queryConfigAC.getRemoveAction() );
		queryConfigRemoveMenu.setName( "queryConfigRemoveMenu" );
		queryConfigRemoveMenu.setText( Messages.getString( "_QUERY_CONFIG_REMOVE" ) );
		softwareMenu.add( queryConfigRemoveMenu );

		// REGEX

		JSeparator regExSeparator = new JSeparator();
		softwareMenu.add(regExSeparator);

		RegExActionContainer regEx = projectPanel.getRegExActionContainer();

		JMenuItem regExNewMenu = new JMenuItem( regEx.newAction() );
		regExNewMenu.setName( "regExNewMenu" );
		regExNewMenu.setText( Messages.getString( "_REGEX_NEW" ) );
		softwareMenu.add( regExNewMenu );

		JMenuItem regExEditMenu = new JMenuItem( regEx.editAction() );
		regExEditMenu.setName( "regExEditMenu" );
		regExEditMenu.setText( Messages.getString( "_REGEX_EDIT" ) );
		softwareMenu.add( regExEditMenu );

		JMenuItem regExRemoveMenu = new JMenuItem( regEx.removeAction() );
		regExRemoveMenu.setName( "regExRemoveMenu" );
		regExRemoveMenu.setText( Messages.getString( "_REGEX_REMOVE" ) );
		softwareMenu.add( regExRemoveMenu );

		return softwareMenu;
	}

	private void createTestMenu(JMenuBar menuBar) {
		JMenu testMenu = new JMenu( Messages.getString("MainFrame.testMenu.text") ); //$NON-NLS-1$
		testMenu.setName( "testMenu" );
		menuBar.add( testMenu );

		JMenuItem runMenu = new JMenuItem( testAC.getGenerateAndRunAction() );
		runMenu.setName( "runMenu" );
		testMenu.add( runMenu );

//		JMenuItem justGenerateTestCodeMenu = new JMenuItem( testAC.getJustGenerateTestCodeAction() );
//		justGenerateTestCodeMenu.setName( "justGenerateTestCodeMenu" );
//		testMenu.add( justGenerateTestCodeMenu );

		JMenuItem justRunTestsMenu = new JMenuItem( testAC.getJustRunTestsAction() );
		justRunTestsMenu.setName( "justRunTestsMenu" );
		testMenu.add( justRunTestsMenu );

		testMenu.add( new JSeparator() );

		JMenuItem openReportMenu = new JMenuItem( testAC.getOpenReportAction() );
		openReportMenu.setName( "openReportMenu" );
		testMenu.add( openReportMenu );
	}

	private void createHelpMenu(JMenuBar menuBar) {
		JMenu helpMenu = new JMenu( Messages.getString("MainFrame.helpMenu.text") ); //$NON-NLS-1$
		helpMenu.setName( "helpMenu" );
		menuBar.add( helpMenu );

		JMenuItem manualMenu = new JMenuItem( helpAC.getOpenManualAction() );
		manualMenu.setName( "manualMenu" );
		helpMenu.add( manualMenu );

		JMenuItem webSiteMenu = new JMenuItem( helpAC.getGoToWebSiteAction() );
		webSiteMenu.setName( "webSiteMenu" );
		helpMenu.add(webSiteMenu);

		JSeparator separator1 = new JSeparator();
		helpMenu.add(separator1);

		JMenuItem versionMenu = new JMenuItem( helpAC.getCheckForNewVersionAction() );
		versionMenu.setName( "versionMenu" );
		helpMenu.add(versionMenu);

		JSeparator separator2 = new JSeparator();
		helpMenu.add(separator2);

		JMenuItem aboutMenu = new JMenuItem( helpAC.getShowAboutDialogAction() );
		aboutMenu.setName( "aboutMenu" );
		helpMenu.add( aboutMenu );
	}

	private JToolBar createFileToolBar(JPanel parent) {
		fileToolBar = new JToolBar();
		fileToolBar.setName( "ToolBar" );
		fileToolBar.setRollover(true);
		parent.add( fileToolBar, BorderLayout.SOUTH );

		JButton newButton = new JButton( fileAC.getFileNewAction() );
		newButton.setName( "newButton" );
		fileToolBar.add( newButton );

		JButton openButton = new JButton( fileAC.getFileOpenAction() );
		openButton.setName( "openButton" );
		fileToolBar.add( openButton );

		JButton saveButton = new JButton( fileAC.getFileSaveAction() );
		saveButton.setName( "saveButton" );
		fileToolBar.add( saveButton );

		testToolBar = createTestsToolBar( fileToolBar );

		// Remove the text from each button IF the icon is set.
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				ToolBarUtil.removeTextFromButtonsWithIcon( fileToolBar );
				ToolBarUtil.removeTextFromButtonsWithIcon( testToolBar );
			}
		} );

		return fileToolBar;
	}

	private JToolBar createTestsToolBar(JComponent parent) {
		JToolBar bar = new JToolBar();
		parent.add(bar);
		bar.setName( "Tests" );
		bar.setRollover( true );

		JButton runButton = new JButton( testAC.getGenerateAndRunAction() );
		runButton.setName( "runButton" );
		bar.add( runButton );

		JButton justRunTestsButton = new JButton( testAC.getJustRunTestsAction() );
		justRunTestsButton.setName( "justRunTestsButton" );
		bar.add( justRunTestsButton );

//		JButton justGenerateTestCodeButton = new JButton( testAC.getJustGenerateTestCodeAction() );
//		justGenerateTestCodeButton.setName( "justGenerateTestCodeButton" );
//		bar.add(justGenerateTestCodeButton);

		return bar;
	}

	private JToolBar createSoftwareToolBar(UseCaseListActionContainer actionContainer) {
		final JToolBar bar = new JToolBar();
		bar.setName( "Software" );
		bar.setRollover( true );

		// Use Case

		JButton useCaseNewButton = new JButton( actionContainer.useCaseNewAction() );
		useCaseNewButton.setName( "useCaseNewButton" );
		bar.add( useCaseNewButton );

		JButton useCaseEditButton = new JButton( actionContainer.useCaseEditAction() );
		useCaseEditButton.setName( "useCaseEditButton" );
		bar.add( useCaseEditButton );

		JButton useCaseRemoveButton = new JButton( actionContainer.useCaseRemoveAction() );
		useCaseRemoveButton.setName( "useCaseRemoveButton" );
		bar.add( useCaseRemoveButton );


		// Separator
		//JSeparator separator = new JSeparator( SwingConstants.VERTICAL );
		//softwareToolBar.add(separator);

		/*
		bar.addSeparator();

		// Flow

		JButton flowNewButton = new JButton( actionContainer.flowNewAction() );
		flowNewButton.setName( "flowNewButton" );
		bar.add( flowNewButton );

		JButton flowEditButton = new JButton( actionContainer.flowEditAction() );
		useCaseEditButton.setName( "flowEditButton" );
		bar.add( flowEditButton );

		JButton flowRemoveButton = new JButton( actionContainer.flowRemoveAction() );
		useCaseRemoveButton.setName( "flowRemoveButton" );
		bar.add( flowRemoveButton );
		*/

		// Remove the text from the buttons

		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				ToolBarUtil.removeTextFromButtonsWithIcon( bar );
			}
		} );

		return bar;
	}


	private JPanel createBottomPanel(JPanel parent) {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setVisible(false);
		bottomPanel.setPreferredSize(new Dimension(10, 110));
		bottomPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		bottomPanel.setLayout(new BorderLayout(0, 0));
		parent.add(bottomPanel, BorderLayout.SOUTH);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		bottomPanel.add(tabbedPane, BorderLayout.CENTER);

		JPanel messagesTab = new JPanel();
		messagesTab.setName( "messagesTab" );
		tabbedPane.addTab( "Messages", null, messagesTab, null);
		messagesTab.setLayout(new BorderLayout(0, 0));

		// Put the Close button for Substance Look and Feel (only)
		//messagesTab.putClientProperty( SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, Boolean.TRUE );

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		messagesTab.add(scrollPane, BorderLayout.CENTER);

		JTextArea messageList = new JTextArea();
		messageList.setEditable(false);
		messageList.setFont(new Font("Consolas", Font.PLAIN, 13));
		messageList.setForeground(Color.GRAY);
		messageList.setEnabled(false);
		scrollPane.setViewportView(messageList);
		messageList.setName( "messageList" );

		JPanel statusBarPanel = new JPanel();
		bottomPanel.add(statusBarPanel, BorderLayout.SOUTH);
		statusBarPanel.setLayout(new BorderLayout(0, 0));

		return bottomPanel;
	}

	private synchronized void showLog(final String msg, final Object ... args) {
		if ( args.length > 0 ) {
			listModel.addElement( String.format( msg, args ) );
		} else {
			listModel.addElement( msg );
		}
	}

	public synchronized void updateProgress(
			final int current, final int max, final String status) {
		progressBar.setVisible( true );
		progressBar.setIndeterminate( false );
		progressBar.setMaximum( max );
		progressBar.setValue( current );
		progressBar.updateUI();
		showLog( status );
		if ( current >= max ) {
			progressBar.setVisible( false );
		}
	}

	public synchronized void started(String details) {
		showLog( details );
		progressBar.setVisible( true );
		progressBar.updateUI();
		progressBar.setIndeterminate( true );
	}

	public synchronized void finished(String details) {
		progressBar.updateUI();
		progressBar.setIndeterminate( false );
		progressBar.setVisible( false );
		showLog( details );
	}

	// ProjectUIListener

	private void closeSoftware() {
		// Remove the software content
		if ( projectPanel != null ) {
			centerPanel.remove( projectPanel );
			projectPanel = null; // Delete
			centerPanel.updateUI();
		}

		// Remove the software menu
		if ( softwareMenu != null ) {
			menuBar.remove( softwareMenu );
			softwareMenu = null; // Delete
			menuBar.updateUI();
		}

		// Remove the software tool bar
		if ( softwareToolBar != null ) {
			fileToolBar.remove( softwareToolBar );
			softwareToolBar = null; // Delete
			fileToolBar.updateUI();
		}

		// Update the title
		updateTitle( "", "", false );
	}

	private void openSoftware(Software software) {
		// Add the software content
		projectPanel = new ProjectPanel( appState );
		centerPanel.add( projectPanel );
		centerPanel.updateUI();

		// Add the software menu
		final int POSITION_BEFORE_TEST_MENU = 2;
		softwareMenu = createSoftwareMenu( projectPanel.getUseCaseListActionContainer() );
		menuBar.add( softwareMenu, POSITION_BEFORE_TEST_MENU );
		menuBar.updateUI();

		// Add the software tool bar
		softwareToolBar = createSoftwareToolBar( projectPanel.getUseCaseListActionContainer() );
		fileToolBar.add( softwareToolBar );
		fileToolBar.updateUI();

		// Update the title
		updateTitle( software.getName(),
				appState.getProjectFileName(),
				appState.getProjectFileState().equals( FileState.DIRTY ) );
	}


	private void updateTitle(String softwareName, String path, boolean dirty) {
		final String title = new StringBuffer()
			.append( appName )
			.append( ( softwareName != null && ! softwareName.isEmpty() ) ? ( " - " + softwareName ) : "" )
			.append( ( path != null && ! path.isEmpty() ) ? " [" + path + "]" : "" )
			.append( dirty ? "*" : "" )
			.toString();
		setTitle( title );
	}

	@Override
	public void hasOpened(Project p) {
		closeSoftware();
		openSoftware( p.getSoftware() );
	}

	@Override
	public void hasClosed(Project p) {
		closeSoftware();
	}

	@Override
	public void hasSaved(Project p) {
		updateTitle( p.getSoftware().getName(), appState.getProjectFileName(), false );
	}
}
