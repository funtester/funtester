package org.funtester.app.ui.testing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.util.BaseTableModel;
import org.funtester.app.ui.util.JTableUtil;
import org.funtester.common.report.TestCaseExecution;
import org.funtester.common.report.TestExecutionReport;
import org.funtester.common.report.TestMethodExecution;
import org.funtester.common.report.TestSuiteExecution;
import org.funtester.common.util.ProcessingListener;
import org.funtester.common.util.TimeConverter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Test execution report dialog
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class TestExecutionReportDialog extends JDialog {
	
	public class ReportItem {
		public long id;
		public String testClassName;
		public String testMethodName;
		public long timeInMillis;
		public String status;	
		public TestMethodExecution testMethod; // References to get more info
	}
	
	
	private class TableWithToolTip extends JTable {
		
		private static final long serialVersionUID = 4126207886547768526L;
		private final List< Integer > columnsWithToolTip;
		
		public TableWithToolTip(Integer [] columnsWithToolTip) {
			super();		
			this.columnsWithToolTip =
				new ArrayList< Integer >( Arrays.asList( columnsWithToolTip ) );
		}

		/**
		 * Base on example shown in
		 * http://docs.oracle.com/javase/tutorial/uiswing/components/table.html#celltooltip
		 */
		public String getToolTipText(MouseEvent e) {
			java.awt.Point p = e.getPoint();
			int rowIndex = rowAtPoint( p );
			int colIndex = columnAtPoint( p );
			int realColumnIndex = convertColumnIndexToModel( colIndex );
			if ( columnsWithToolTip.contains( realColumnIndex ) ) {
				return getModel().getValueAt( rowIndex, realColumnIndex ).toString();	
			}			
			return super.getToolTipText( e );
		}
	}
	
	
	private class TestListSelectionListener implements ListSelectionListener {
		
		private JTable table;

		public TestListSelectionListener(JTable table) {
			if ( null == table ) {
				throw new IllegalArgumentException( "table should not be null." );
			}
			this.table = table;
		}
		
		public void valueChanged(final ListSelectionEvent e) {
			if ( e.getValueIsAdjusting() ) {
				return;
			}		
			ListSelectionModel model = (ListSelectionModel) e.getSource();
			
			final int idColumnIndex = 0;
			final int rowIndex = model.getMinSelectionIndex();
			if ( rowIndex < 0 ) {
				return;
			}			
			//System.out.println( "row: " + rowIndex + " col: " + ID_COLUMN_INDEX );
			Long id;
			try {
				id = (Long) table.getModel().getValueAt( rowIndex, idColumnIndex );
			} catch (final Exception ex) {
				try {
					id = Long.parseLong( 
						( (String) table.getModel().getValueAt( rowIndex, idColumnIndex ) ) );
				} catch (final Exception ex2) {
					return;
				}
			}
			selectReportItemById( id ); // id can be null
		}
		
	}
	
	private class TableSelectionClearerFocusAdapter extends FocusAdapter {
		private final JTable table;
		
		public TableSelectionClearerFocusAdapter(final JTable table) {
			super();
			this.table = table; 
		}
		@Override
		public void focusLost(final FocusEvent e) {
			table.clearSelection();
		}
	}
	
	
	
	private static final long serialVersionUID = -3799802945340836128L;
		
	private final String totalTitle;
	private final String passTitle;
	private final String skipTitle;
	private final String failTitle;
	private final String errorTitle;
	private final String unknownTitle;
	
	private final TestExecutionReport report;
	private final List< ReportItem > totalList;
	private ReportItem selectedReportItem = null;
	
	private final JTable totalTable;
	private final JTable passTable;
	private final JTable skipTable;
	private final JTable failTable;
	private final JTable errorTable;
	private final JTable unknownTable;
	private final JPanel contentPanel = new JPanel();
	private final JTextPane selectedItemField;
	
	

	/**
	 * Create the dialog.
	 * 
	 * @param owner		the owner frame.
	 * @param report	the report to be shown.
	 * @param listener	a listener to show progress. Can be null.
	 */
	public TestExecutionReportDialog(
			final JFrame owner,
			final TestExecutionReport report,
			ProcessingListener listener
			) {
		
		super( owner ); // non modal
		setTitle(Messages.getString("TestExecutionReportDialog.this.title")); //$NON-NLS-1$
		
		if ( null == report ) {
			throw new IllegalArgumentException( "report should not be null" );
		}
		this.report = report;

		if ( listener != null ) {
			listener.started( Messages.alt( "_EXECUTION_REPORT_PREPARING", "Preparing the execution report..." ) );
		}		
		
		this.totalTitle = Messages.alt( "_TOTAL", "Total" );
		this.passTitle = Messages.alt( "_PASSED", "Passed" );
		this.skipTitle = Messages.alt( "_SKIPPED", "Skipped" );
		this.failTitle = Messages.alt( "_FAILURES", "Failures" );
		this.errorTitle = Messages.alt( "_ERRORS", "Errors" );
		this.unknownTitle = Messages.alt( "_UNKNOWN", "Unknown results" );
		
		totalList = new ArrayList< ReportItem >();
		final List< ReportItem > passList = new ArrayList< ReportItem >();
		final List< ReportItem > skipList = new ArrayList< ReportItem >();
		final List< ReportItem > failList = new ArrayList< ReportItem >();
		final List< ReportItem > errorList = new ArrayList< ReportItem >();
		final List< ReportItem > unknownList = new ArrayList< ReportItem >();
		
		ReportItem slowestItem = null;
		ReportItem fastestItem = null;
		long slowestTime = Long.MIN_VALUE;
		long fastestTime = Long.MAX_VALUE;
		
		long id = 0;
		for ( TestSuiteExecution suite : this.report.getSuites() ) {
			for ( TestCaseExecution testCase : suite.getTestCases() ) {
				for ( TestMethodExecution testMethod : testCase.getMethods() ) {
					if ( testMethod.isForConfiguration() ) {
						continue;
					}					
					ReportItem item = new ReportItem();
					item.id = ++id;
					item.testMethod = testMethod; // Reference
					item.testClassName = testCase.getClassName();
					item.testMethodName = testMethod.getName();
					item.timeInMillis = testMethod.getTimeInMillis();
					item.status = testMethod.getStatus().toString();
					// Add to total list
					totalList.add( item );
					// Add to other lists depending on the status
					switch ( testMethod.getStatus() ) {
						case PASS: passList.add( item ); break;
						case SKIP: skipList.add( item ); break;
						case FAIL: failList.add( item ); break;
						case ERROR: errorList.add( item ); break;
						default: { // UNKNOWN
							unknownList.add( item ); break;
						}
					}
					// Computing fastest
					if ( item.timeInMillis < fastestTime ) {
						fastestItem = item;
						fastestTime = item.timeInMillis;
					}
					// Computing slowest
					if ( item.timeInMillis > slowestTime ) {
						slowestItem = item;
						slowestTime = item.timeInMillis;
					}					
				}
			}
		}
		
		
		final int columnCount = 5;
		final String[] columnNames = {
			Messages.alt( "_EXECUTION_REPORT_COLUMN_ID", "Id" ),
			Messages.alt( "_EXECUTION_REPORT_COLUMN_CLASS", "Class" ),
			Messages.alt( "_EXECUTION_REPORT_COLUMN_METHOD", "Method" ),
			Messages.alt( "_EXECUTION_REPORT_COLUMN_TIME", "Time (ms)" ),
			Messages.alt( "_EXECUTION_REPORT_COLUMN_STATUS", "Status" ),
		};
		
		final Integer[] columnsWithTooltips = { 1, 2 };
		
		final int[] columnMinWidths = {
			40, 250, 450, 75, 60
		};
		
		final int[] columnMaxWidths = {
			50, 1000, 1000, 150, 150
		};
		
		final BaseTableModel< ReportItem > totalTableModel = createTableModel(
				totalList, columnCount, columnNames );
		
		final BaseTableModel< ReportItem > passTableModel = createTableModel(
				passList, columnCount, columnNames );
		
		final BaseTableModel< ReportItem > skipTableModel = createTableModel(
				skipList, columnCount, columnNames );
		
		final BaseTableModel< ReportItem > failTableModel = createTableModel(
				failList, columnCount, columnNames );		
		
		final BaseTableModel< ReportItem > errorTableModel = createTableModel(
				errorList, columnCount, columnNames );
		
		final BaseTableModel< ReportItem > unknownTableModel = createTableModel(
				unknownList, columnCount, columnNames );		
		
		setBounds( 100, 100, 1022, 725 );
		getContentPane().setLayout( new BorderLayout() );
		getContentPane().add( contentPanel, BorderLayout.CENTER );
		
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		buttonPane.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
		getContentPane().add( buttonPane, BorderLayout.SOUTH );
		
		final ActionListener closeAL = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible( false );
				dispose();
			}
		};
		
		JButton closeButton = new JButton( Messages.getString("TestExecutionReportDialog.closeButton.text") ); //$NON-NLS-1$
		closeButton.setName("closeButton");
		closeButton.addActionListener( closeAL );
		closeButton.setActionCommand( "Cancel" );
		buttonPane.add( closeButton );
		
		contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("758px:grow"),},
			new RowSpec[] {
				RowSpec.decode("215px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.PARAGRAPH_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));

		JPanel chartContentPanel = new JPanel();
		chartContentPanel.setBackground(Color.WHITE);
		contentPanel.add( chartContentPanel, "1, 1, fill, fill" );
		chartContentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.MIN_COLSPEC,
				ColumnSpec.decode("500px:grow"),
				FormFactory.MIN_COLSPEC,},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JPanel chartPanel = createChartPanel(
				this.report.getTotalPassed(),
				this.report.getTotalSkipped(),
				this.report.getTotalFailures(),
				this.report.getTotalErrors(),
				this.report.getTotalUnknown()
				);
		
		chartContentPanel.add( chartPanel, "2, 2, fill, fill" );
		
		JPanel resultPanel = new JPanel();
		contentPanel.add(resultPanel, "1, 3, fill, fill");
		resultPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("left:4dlu"),
				ColumnSpec.decode("left:default"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.UNRELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.UNRELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("right:16dlu:grow"),
				ColumnSpec.decode("right:default"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JButton btnAbout = new JButton(Messages.getString("TestExecutionReportDialog.btnAbout.text")); //$NON-NLS-1$
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAbout();
			}
		});

		JLabel lblTestCount = new JLabel(Messages.getString("TestExecutionReportDialog.lblTestCount.text")); //$NON-NLS-1$
		resultPanel.add(lblTestCount, "2, 2");
		
		JLabel lblTestCountValue = new JLabel( String.valueOf( this.report.getTotalTests() ) );
		resultPanel.add(lblTestCountValue, "4, 2");
		
		JLabel lblTime = new JLabel(Messages.getString("TestExecutionReportDialog.lblTime.text")); //$NON-NLS-1$
		resultPanel.add(lblTime, "8, 2, right, default");
		
		JLabel lblFormattedTime = new JLabel( formattedTime( this.report.getTimeInMillis() ) );		
		resultPanel.add(lblFormattedTime, "10, 2");
		
		JLabel lblTimeInMillis = new JLabel( timeInMillis( this.report.getTimeInMillis() )  );
		lblTimeInMillis.setForeground(SystemColor.textInactiveText);
		resultPanel.add(lblTimeInMillis, "12, 2, left, default");
		
		JLabel lblAverageTime = new JLabel(Messages.getString("TestExecutionReportDialog.lblAverageTime.text")); //$NON-NLS-1$
		resultPanel.add(lblAverageTime, "8, 4, right, default");
		
		JLabel lblFormattedAverageTime = new JLabel( formattedTime( this.report.averageTimeInMillis() ) );		
		resultPanel.add(lblFormattedAverageTime, "10, 4");
		
		JLabel lblAverageTimeInMillis = new JLabel( timeInMillis( this.report.averageTimeInMillis() ) );
		lblAverageTimeInMillis.setForeground(SystemColor.textInactiveText);
		resultPanel.add(lblAverageTimeInMillis, "12, 4");		

		btnAbout.setName("aboutButton");
		resultPanel.add(btnAbout, "23, 2");
		
		JLabel lblSlowestTime = new JLabel(Messages.getString("TestExecutionReportDialog.lblSlowestTime.text")); //$NON-NLS-1$
		resultPanel.add(lblSlowestTime, "14, 2, right, default");
		
		JLabel lblFormattedSlowestTime = new JLabel( slowestItem != null ? formattedTime( slowestItem.timeInMillis ) : "?" );
		resultPanel.add(lblFormattedSlowestTime, "16, 2");
		
		JLabel lblSlowestTimeInMillis = new JLabel( slowestItem != null ? timeInMillis( slowestItem.timeInMillis ) : "?" );
		lblSlowestTimeInMillis.setForeground(SystemColor.textInactiveText);
		resultPanel.add(lblSlowestTimeInMillis, "18, 2");
		
		JLabel lblSlowestId = new JLabel( slowestItem != null ? " id=" + String.valueOf( slowestItem.id ) : "?" );
		lblSlowestId.setForeground(SystemColor.textInactiveText);
		resultPanel.add(lblSlowestId, "20, 2");
		
		JLabel lblFastestTime = new JLabel(Messages.getString("TestExecutionReportDialog.lblFastestTime.text")); //$NON-NLS-1$
		resultPanel.add(lblFastestTime, "14, 4, right, default");
		
		JLabel lblFormattedFastestTime = new JLabel( fastestItem != null ? formattedTime( fastestItem.timeInMillis ) : "?" );
		resultPanel.add(lblFormattedFastestTime, "16, 4");
		
		JLabel lblFastestTimeInMillis = new JLabel( fastestItem != null ? timeInMillis( fastestItem.timeInMillis ) : "?" );
		lblFastestTimeInMillis.setForeground(SystemColor.textInactiveText);
		resultPanel.add(lblFastestTimeInMillis, "18, 4");
		
		JLabel lblFastestId = new JLabel( fastestItem != null ? " id=" + String.valueOf( fastestItem.id ) : "?" );
		lblFastestId.setForeground(SystemColor.textInactiveText);
		resultPanel.add(lblFastestId, "20, 4");
		
		JLabel lblTip = new JLabel(Messages.getString("TestExecutionReportDialog.lblTip.text")); //$NON-NLS-1$
		lblTip.setHorizontalAlignment(SwingConstants.CENTER);
		lblTip.setForeground(SystemColor.inactiveCaptionText);
		contentPanel.add(lblTip, "1, 5");
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPanel.add(tabbedPane, "1, 7, fill, fill");
		
		JPanel totalPanel = new JPanel();
		final String totalTabTitle = this.totalTitle + " (" + this.report.getTotalTests() + ")";
		tabbedPane.addTab( totalTabTitle, null, totalPanel, null);
		totalPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane totalScrollPane = new JScrollPane();
		totalPanel.add(totalScrollPane);
		
		totalTable = new TableWithToolTip( columnsWithTooltips );		
		totalTable.setModel( totalTableModel );		
		totalTable.setFillsViewportHeight(true);
		totalTable.setName("totalTable");
		totalScrollPane.setViewportView(totalTable);
		
		JPanel passPanel = new JPanel();
		final String passedTabTitle = this.passTitle + " (" + this.report.getTotalPassed() + ")";
		tabbedPane.addTab( passedTabTitle, null, passPanel, null);
		passPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane passScrollPane = new JScrollPane();
		passPanel.add(passScrollPane);
		
		passTable = new TableWithToolTip( columnsWithTooltips );
		passTable.setModel( passTableModel );
//		passTable.setBackground(new Color(232, 255, 232)); // Very light green
		passTable.setName("passTable");
		passTable.setFillsViewportHeight(true);
		passScrollPane.setViewportView(passTable);
		
		JPanel skipPanel = new JPanel();
		final String skippedTabTitle = this.skipTitle + " (" + this.report.getTotalSkipped() + ")";
		tabbedPane.addTab( skippedTabTitle, null, skipPanel, null);
		skipPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane skipScrollPane = new JScrollPane();
		skipPanel.add(skipScrollPane);
		
		skipTable = new TableWithToolTip( columnsWithTooltips );
		skipTable.setModel( skipTableModel );
		skipTable.setName("skipTable");
		skipTable.setFillsViewportHeight(true);
//		skipTable.setBackground(new Color(221, 219, 255)); // Very light blue
		skipScrollPane.setViewportView(skipTable);
		
		JPanel failPanel = new JPanel();
		final String failTabTitle = this.failTitle + " (" + this.report.getTotalFailures() + ")";
		tabbedPane.addTab( failTabTitle, null, failPanel, null);
		failPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane failScrollPane = new JScrollPane();
		failPanel.add(failScrollPane);
		
		failTable = new TableWithToolTip( columnsWithTooltips );
		failTable.setModel( failTableModel );
		failTable.setName("failTable");
		failTable.setFillsViewportHeight(true);
//		failTable.setBackground(new Color(255, 255, 193)); // Very light yellow
		failScrollPane.setViewportView(failTable);
		
		JPanel errorPanel = new JPanel();
		final String errorTabTitle = this.errorTitle + " (" + this.report.getTotalErrors() + ")";	
		tabbedPane.addTab( errorTabTitle, null, errorPanel, null);
		errorPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane errorScrollPane = new JScrollPane();
		errorPanel.add(errorScrollPane, BorderLayout.CENTER);
		
		errorTable = new TableWithToolTip( columnsWithTooltips );
		errorTable.setModel( errorTableModel );
		errorTable.setName("errorTable");
		errorTable.setFillsViewportHeight(true);
		errorScrollPane.setViewportView(errorTable);
		
		JPanel unknownPanel = new JPanel();
		final String unknownTabTitle = this.unknownTitle + " (" + this.report.getTotalUnknown() + ")";
		tabbedPane.addTab( unknownTabTitle, null, unknownPanel, null);
		unknownPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane unknownScrollPane = new JScrollPane();
		unknownPanel.add(unknownScrollPane, BorderLayout.CENTER);
		
		unknownTable = new TableWithToolTip( columnsWithTooltips );
		unknownTable.setModel( unknownTableModel );
		unknownTable.setName("unknownTable");
		unknownTable.setFillsViewportHeight(true);
		unknownScrollPane.setViewportView(unknownTable);
		
		JPanel selectionPanel = new JPanel();
		contentPanel.add(selectionPanel, "1, 9, fill, fill");
		selectionPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblSelectedItem = new JLabel(Messages.getString("TestExecutionReportDialog.lblSelectedItem.text")); //$NON-NLS-1$
		selectionPanel.add(lblSelectedItem, "2, 2");
		
		selectedItemField = new JTextPane();
		selectedItemField.setName("selectedItemField");
		selectedItemField.setEditable(false);
		selectedItemField.setContentType("text/html");
		selectedItemField.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
		selectedItemField.setBackground(SystemColor.control);
		selectionPanel.add(selectedItemField, "4, 2, fill, fill");
		
		JButton testInfoButton = new JButton(Messages.getString("TestExecutionReportDialog.testInfoButton.text")); //$NON-NLS-1$
		testInfoButton.setName("testInfoButton");
		selectionPanel.add(testInfoButton, "6, 2");
		testInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showTestInfo();
			}
		});
				
		
		final MouseAdapter tableDoubleClickMouseAdapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if ( e.getButton() == MouseEvent.BUTTON1
						&& e.getClickCount() == 2 ) {
					showTestInfo();
				} else {
					super.mouseClicked( e );
				}
			}
		};
		
		
		final JTable[] tables = {
			totalTable, passTable, skipTable, failTable, errorTable, unknownTable	
		};
		for ( JTable currentTable : tables ) {
			currentTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );	
			
			currentTable.addFocusListener( new TableSelectionClearerFocusAdapter( currentTable ) );
						
			BaseTableModel< ReportItem > currentTableModel = toBaseTableModel( currentTable );
			if ( null == currentTableModel ) {
				continue;
			}					
			// This is important to get the id as a Integer value
			currentTableModel.setColumnClass( 0, Long.class );
			
			currentTable.getSelectionModel().addListSelectionListener(
					new TestListSelectionListener( currentTable ) );
					
			currentTable.addMouseListener( tableDoubleClickMouseAdapter );		
			
			JTableUtil.adjustMinColumnWidths( currentTable, columnMinWidths );
			JTableUtil.adjustMaxColumnWidths( currentTable, columnMaxWidths );
		}		
		
		// Announce that is done
		if ( listener != null ) {
			listener.finished( Messages.alt( "_EXECUTION_REPORT_DONE", "Report done.") );
		}
		
		
		// Register the ESC key to call the cancelActionListener
		getRootPane().registerKeyboardAction(
				closeAL,
				KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ),
				JComponent.WHEN_IN_FOCUSED_WINDOW
				);
	}
	
	
	private String formattedTime(final long millis) {
		final int hours = TimeConverter.millisToHours( millis );
		final int minutes = TimeConverter.millisToMinutes( millis );
		final int seconds = TimeConverter.millisToSeconds( millis );
		return ( (hours > 0) ? hours + "h" : "" )
			+ ( (minutes > 0)? minutes + "m" : "" )
			+ seconds + "s";		
	}
	
	private String timeInMillis(final long millis) {
		return "(" + millis + "ms)";
	}


	@SuppressWarnings("unchecked")
	private BaseTableModel< ReportItem > toBaseTableModel(JTable currentTable) {
		if ( ! ( currentTable.getModel() instanceof BaseTableModel ) ) {
			return null;
		}
		return (org.funtester.app.ui.util.BaseTableModel< ReportItem > ) currentTable.getModel();
	}


	protected void showTestInfo() {
		if ( null == selectedReportItem ) {
			return;
		}
		ReportItemDialog dialog = new ReportItemDialog( this, selectedReportItem );
		dialog.setVisible( true );
	}


	/**
	 * Selects the report item with some id.
	 * 
	 * @param id	the item id (can be null).
	 */
	private void selectReportItemById(final Long id) {		
		final String selectOne = Messages.alt( "_EXECUTION_REPORT_SELECT_ITEM", "<select>" );
		if ( null == id || id < 0 || id > totalList.size() ) {
			selectedItemField.setText( selectOne );
			return;
		} 
		// Updates the select item on the screen
		selectedReportItem = reportItemWithId( id );
		if ( null == selectedReportItem ) {
			selectedItemField.setText( selectOne );
			return;			
		}
		final String text =
			"<span style=\"font-family:consolas;\" >"
			+ "<span style=\"color:gray;\" >"
			+ selectedReportItem.testClassName
			+ "</span>"				
			+ "."
			+ "<span style=\"color:purple;\" >"
			+ selectedReportItem.testMethodName
			+ "</span>"
			+ "</span>"	
			;
		selectedItemField.setText( text );
	}


	/**
	 * Gets the report item with a certain id.
	 * 
	 * @param id	the item id.
	 * @return		a {@link ReportItem} object.
	 */
	private ReportItem reportItemWithId(final long id) {
		// In the current version, the id is the index plus one, so just
		// subtract one from it and we have the index
		final int index = ( new Long( id - 1 ) ).intValue();
		try {
			return totalList.get( index );
		} catch (final Exception e) {
			return null;
		}
	}


	protected void showAbout() {
		ReportAboutDialog dialog = new ReportAboutDialog( this, report );
		dialog.setVisible( true );
	}


	/**
	 * @param list
	 * @param COLUMN_COUNT
	 * @param COLUMN_NAMES
	 * @return
	 */
	private BaseTableModel< ReportItem > createTableModel(
			final List< ReportItem > list, final int COLUMN_COUNT,
			final String[] COLUMN_NAMES) {
		final BaseTableModel< ReportItem > totalTableModel =
			new BaseTableModel< ReportItem >(
					COLUMN_COUNT,
					list,
					COLUMN_NAMES
					) {				
				private static final long serialVersionUID = 13874795L;

				@Override
				public Object getValueAt(int rowIndex, int columnIndex) {
					ReportItem item = itemAt( rowIndex );
					if ( null == item ) {
						return "";
					}
					switch ( columnIndex ) {
						case 0: return item.id;
						case 1: return item.testClassName;
						case 2: return item.testMethodName;
						case 3: return item.timeInMillis;
						case 4: return item.status;
						default: return "";
					}
				}
			};
		totalTableModel.setColumnClass( 0, Integer.class );
		return totalTableModel;
	}
	
	
	private JPanel createChartPanel(
			final int totalPassed,
			final int totalSkipped,
			final int totalFailures,
			final int totalErrors,
			final int totalUnknown
			) {
		PieDataset dataset = createPieDataSet(
				totalPassed, totalSkipped, totalFailures, totalErrors, totalUnknown );
		JFreeChart chart = createPieChart( dataset,
				Messages.alt( "_TEST_EXECUTION_RESULTS", "Results" ) );
		
		ChartPanel chartPanel = new ChartPanel( chart );
		chartPanel.setPreferredSize( new java.awt.Dimension( 500, 270 ) );
		return chartPanel;
	}

	private PieDataset createPieDataSet(
			final int totalPassed,
			final int totalSkipped,
			final int totalFailures,
			final int totalErrors,
			final int totalUnknown
			) {
		DefaultPieDataset ds = new DefaultPieDataset();
		ds.setValue( passTitle, totalPassed );
		if ( totalSkipped > 0 ) {
			ds.setValue( skipTitle, totalSkipped );
		}
		ds.setValue( failTitle, totalFailures );
		ds.setValue( errorTitle, totalErrors );
		if ( totalUnknown > 0 ) {
			ds.setValue( unknownTitle, totalUnknown );
		}
		return ds;
	}
	
	private JFreeChart createPieChart(
			final PieDataset dataset,
			final String title
			) {
		JFreeChart chart = ChartFactory.createPieChart3D(
				title,
				dataset,
				true, // include legend
				false, // include tooltips
				false // urls
				);

		// Adjusting title font
		chart.getTitle().setFont( new Font( "Arial", Font.BOLD, 16 ) );
		
		// Adjusting the chart plot
		PiePlot3D plot = ( PiePlot3D ) chart.getPlot();
		plot.setStartAngle( 290 );
		plot.setDirection( Rotation.CLOCKWISE );
		plot.setForegroundAlpha( 0.7f );
		plot.setBackgroundAlpha( 0.3f );
		
		// Adjusting messages
		plot.setNoDataMessage( Messages.alt( "_NO_DATA_AVAILABLE", "No data available" ) );
		
		// Adjusting the colors
		plot.setSectionPaint( passTitle, new Color( 127, 246, 127 ) ); // Green
		plot.setSectionPaint( skipTitle, new Color( 125, 124, 251 ) ); // Blue
		plot.setSectionPaint( failTitle, new Color( 250, 250, 124 ) ); // Yellow
		plot.setSectionPaint( errorTitle, new Color( 251, 125, 126 ) ); // Red
		plot.setSectionPaint( unknownTitle, new Color( 195, 195, 195 ) ); // Gray
		
		// Adjusting the labels
		PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator( 
			"{1} {0} ({2})", // {0} is label name, {1} is value, {2} is percentage
			new DecimalFormat( "0" ), new DecimalFormat( "0.00%" )
			); 
		plot.setLabelGenerator( labelGenerator );
		
		return chart;
	}
	
}
