package org.funtester.app.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppInfo;
import org.funtester.app.project.AppState;
import org.funtester.app.project.PluginInfo;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.util.BaseTableModel;
import org.funtester.app.ui.util.CRUDTablePanel;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.JTableUtil;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.common.util.TextFileUtil;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * About dialog.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 4564381642832492859L;

	private static final String VERSION_FILE_NAME = "release-notes.txt";
	private static final String ISSUES_FILE_NAME = "known-issues.txt";
	private static final String LICENSE_FILE_NAME = "LICENSE.txt";

	private final JPanel contentPanel = new JPanel();
	private JTextField pluginAuthors;
	private JTextField pluginName;
	private JTextField pluginSite;
	private JTextField pluginId;
	private JTextField pluginVersion;
	private JTextField pluginDescription;

	public AboutDialog(final AppState appState) {
		
		final AppInfo appInfo = appState.getAppInfo();
		
		setIconImage( ImageUtil.loadImage( ImagePath.aboutIcon() ) );
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setTitle(Messages.getString("AboutDialog.this.title")); //$NON-NLS-1$
		setBounds( 100, 100, 663, 545 );
		getContentPane().setLayout( new BorderLayout() );
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		getContentPane().add( contentPanel, BorderLayout.CENTER );
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPanel.add(tabbedPane, BorderLayout.CENTER);
		
		//
		// INFORMATION PANEL
		//
		
		JPanel informationPanel = new JPanel();
		informationPanel.setName("aboutPanel");
		informationPanel.setBackground(Color.WHITE);
		tabbedPane.addTab( Messages.getString( "AboutDialog.informationPanel.title" ) , null, informationPanel, null);
		informationPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(28dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel logo = new JLabel("");
		logo.setHorizontalAlignment(SwingConstants.CENTER);
		logo.setIcon( ImageUtil.loadIcon( ImagePath.logoImage() ) );
		logo.setBorder(null);
		informationPanel.add(logo, "1, 4");
		
		final String version = String.format( Messages.alt( "_VERSION", "Version %s" ),
				appInfo.getVersion() );
		JLabel lblVersion = new JLabel( version );
		lblVersion.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblVersion.setForeground(Color.GRAY);
		lblVersion.setHorizontalAlignment(SwingConstants.CENTER);
		informationPanel.add(lblVersion, "1, 8");
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		informationPanel.add(panel, "1, 10, fill, fill");
		
		final String webSite = appInfo.getSiteURL();
		final String link = String.format( "<html><a href=\"%s\" >%s</a></html>", webSite, webSite );
		JLabel website = new JLabel( link );
		website.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				openWebSite( webSite );	
			}
		});
		panel.add(website);
		website.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		website.setForeground(Color.BLUE);
		website.setHorizontalAlignment(SwingConstants.CENTER);
		
		//
		// VERSION NOTES PANEL
		//
		
		JPanel versionPanel = new JPanel();
		versionPanel.setBackground(Color.WHITE);
		tabbedPane.addTab( Messages.getString( "AboutDialog.versionPanel.title" ), null, versionPanel, null);
		versionPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.UNRELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,}));
		
		JScrollPane versionNotesScrollPane = new JScrollPane();
		versionNotesScrollPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		versionPanel.add(versionNotesScrollPane, "3, 3, fill, fill");
		
		JTextPane versionNotes = new JTextPane();
		versionNotes.setFont(new Font("Consolas", Font.PLAIN, 13));
		versionNotes.setEditable(false);
		versionNotes.setForeground(Color.DARK_GRAY);
		versionNotes.setBorder(null);
		versionNotesScrollPane.setViewportView(versionNotes);
		
		loadTextFileTo( versionNotes, VERSION_FILE_NAME );
		
		
		//
		// KNOWN ISSUES PANEL
		//
		
		JPanel knownIssuesPanel = new JPanel();
		knownIssuesPanel.setBackground(Color.WHITE);
		tabbedPane.addTab( Messages.getString( "AboutDialog.knownIssuesPanel.title" ), null, knownIssuesPanel, null);
		knownIssuesPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.UNRELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,}));
		
		JScrollPane issuesScrollPane = new JScrollPane();
		issuesScrollPane.setName( "issuesScrollPane" );
		knownIssuesPanel.add(issuesScrollPane, "3, 3, fill, fill");
		
		JTextPane issues = new JTextPane();
		issues.setForeground(Color.DARK_GRAY);
		issues.setEditable(false);
		issues.setFont(new Font("Consolas", Font.PLAIN, 13));
		issues.setName( "issues" );
		issuesScrollPane.setViewportView(issues);
		
		loadTextFileTo( issues, ISSUES_FILE_NAME );
		
		
		//
		// PLUGIN
		//
		
		final String linkedIn = "http://www.linkedin.com/pub/thiago-delgado-pinto/44/bb8/bb6";
		final String authorWebProfileContent = String.format( "<html> <a href=\"%s\" >LinkedIn Profile</a> </html>", linkedIn );
		
		JPanel pluginPanel = new JPanel();
		pluginPanel.setBackground(Color.WHITE);
		tabbedPane.addTab( Messages.getString( "AboutDialog.pluginPanel.title" ), null, pluginPanel, null);
		pluginPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JPanel pluginContentPanel = new JPanel();
		pluginContentPanel.setBackground(Color.WHITE);
		pluginPanel.add(pluginContentPanel, "2, 2, fill, fill");
		pluginContentPanel.setLayout(new BorderLayout(0, 0));
		
		final BaseTableModel< PluginInfo > pluginTM = createPlugInInfoTableModel(
				appState.getPluginMap().keySet() );
		
		CRUDTablePanel pluginTablePanel = new CRUDTablePanel();
		pluginTablePanel.getScrollPane().setMinimumSize(new Dimension(50, 50));
		pluginTablePanel.setMinimumSize(new Dimension(50, 50));
		pluginTablePanel.getTable().setForeground(Color.DARK_GRAY);
		pluginTablePanel.getTable().setFillsViewportHeight(true);
		pluginTablePanel.getTable().setAutoscrolls(false);
		pluginTablePanel.getScrollPane().setBackground(Color.WHITE);
		pluginTablePanel.setBackground(Color.WHITE);
		pluginContentPanel.add(pluginTablePanel, BorderLayout.CENTER);
		pluginTablePanel.setTableModel( pluginTM );
		
		JTableUtil.adjustPreferredColumnWidths( pluginTablePanel.getTable(),
				new int [] { 30, 150, 50, 150, 100 } );
		JTableUtil.adjustMaxColumnWidths( pluginTablePanel.getTable(),
				new int [] { 30 } );
		
		pluginTablePanel.addListSelectionListener( new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if ( e.getValueIsAdjusting() ) { return; }
				final int index = e.getFirstIndex();
				if ( index < 0 ) { return; }
				PluginInfo pluginInfo = pluginTM.itemAt( index );
				drawPluginInfo( pluginInfo );
			}
		} );
		
		JPanel pluginInfoPanel = new JPanel();
		pluginInfoPanel.setForeground(Color.LIGHT_GRAY);
		pluginInfoPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		pluginPanel.add(pluginInfoPanel, "2, 4");
		pluginInfoPanel.setBackground(Color.WHITE);
		pluginInfoPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(158dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.UNRELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("16px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblPluginName = new JLabel(Messages.getString("AboutDialog.lblPluginName.text")); //$NON-NLS-1$
		lblPluginName.setForeground(Color.GRAY);
		pluginInfoPanel.add(lblPluginName, "2, 2, left, top");
		
		pluginName = new JTextField();
		pluginName.setEditable(false);
		pluginName.setForeground(Color.DARK_GRAY);
		pluginName.setBorder(new LineBorder(Color.LIGHT_GRAY));
		pluginName.setText("Name");
		pluginInfoPanel.add(pluginName, "4, 2, fill, default");
		pluginName.setColumns(10);
		
		JLabel lblPluginVersion = new JLabel(Messages.getString("AboutDialog.lblPluginVersion.text")); //$NON-NLS-1$
		lblPluginVersion.setForeground(Color.GRAY);
		pluginInfoPanel.add(lblPluginVersion, "6, 2, right, default");
		
		pluginVersion = new JTextField();
		pluginVersion.setEditable(false);
		pluginVersion.setForeground(Color.DARK_GRAY);
		pluginVersion.setText("Version");
		pluginVersion.setBorder(new LineBorder(Color.LIGHT_GRAY));
		pluginInfoPanel.add(pluginVersion, "8, 2, fill, default");
		pluginVersion.setColumns(10);
		
		JLabel lblPluginDescription = new JLabel(Messages.getString("AboutDialog.lblPluginDescription.text")); //$NON-NLS-1$
		lblPluginDescription.setForeground(Color.GRAY);
		pluginInfoPanel.add(lblPluginDescription, "2, 4, right, default");
		
		pluginDescription = new JTextField();
		pluginDescription.setEditable(false);
		pluginDescription.setText("Description");
		pluginDescription.setForeground(Color.DARK_GRAY);
		pluginDescription.setColumns(10);
		pluginDescription.setBorder(new LineBorder(Color.LIGHT_GRAY));
		pluginInfoPanel.add(pluginDescription, "4, 4, 5, 1, fill, default");
		
		JLabel lblPluginSite = new JLabel(Messages.getString("AboutDialog.lblPluginSite.text")); //$NON-NLS-1$
		lblPluginSite.setForeground(Color.GRAY);
		pluginInfoPanel.add(lblPluginSite, "2, 6, left, default");
		
		pluginSite = new JTextField();
		pluginSite.setEditable(false);
		pluginSite.setForeground(Color.DARK_GRAY);
		pluginSite.setBorder(new LineBorder(Color.LIGHT_GRAY));
		pluginSite.setText("Site");
		pluginInfoPanel.add(pluginSite, "4, 6, fill, default");
		pluginSite.setColumns(10);
		
		JLabel lblPluginId = new JLabel(Messages.getString("AboutDialog.lblPluginId.text")); //$NON-NLS-1$
		lblPluginId.setForeground(Color.GRAY);
		pluginInfoPanel.add(lblPluginId, "6, 6, left, default");
		
		pluginId = new JTextField();
		pluginId.setEditable(false);
		pluginId.setForeground(Color.DARK_GRAY);
		pluginId.setBorder(new LineBorder(Color.LIGHT_GRAY));
		pluginId.setText("Id");
		pluginInfoPanel.add(pluginId, "8, 6, fill, default");
		pluginId.setColumns(10);
		
		JLabel lblPluginAuthors = new JLabel(Messages.getString("AboutDialog.lblPluginAuthors.text")); //$NON-NLS-1$
		lblPluginAuthors.setForeground(Color.GRAY);
		pluginInfoPanel.add(lblPluginAuthors, "2, 8, left, default");
		
		pluginAuthors = new JTextField();
		pluginAuthors.setEditable(false);
		pluginAuthors.setForeground(Color.DARK_GRAY);
		pluginAuthors.setBorder(new LineBorder(Color.LIGHT_GRAY));
		pluginAuthors.setText("Authors");
		pluginInfoPanel.add(pluginAuthors, "4, 8, 5, 1, fill, default");
		pluginAuthors.setColumns(10);
		
		//
		// LICENSE
		//

		JPanel licensePanel = new JPanel();
		licensePanel.setName("licencePanel");
		licensePanel.setBackground(Color.WHITE);
		tabbedPane.addTab( Messages.getString( "AboutDialog.licensePanel.title" ), null, licensePanel, null);
		licensePanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.UNRELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,}));
		
		JScrollPane licenseScrollPane = new JScrollPane();
		licensePanel.add(licenseScrollPane, "3, 3, fill, fill");
		
		JTextPane licence = new JTextPane();
		licence.setEditable(false);
		licence.setFont(new Font("Consolas", Font.PLAIN, 13));
		licenseScrollPane.setViewportView(licence);
		licence.setForeground(Color.GRAY);
		licence.setBorder(null);
		
		loadTextFileTo( licence, LICENSE_FILE_NAME );
		
		//
		// CONTRIBUTORS PANEL
		//
		
		JPanel contributorsPanel = new JPanel();
		contributorsPanel.setBackground(Color.WHITE);
		tabbedPane.addTab( Messages.getString( "AboutDialog.contributorsPanel.title" ), null, contributorsPanel, null);
		contributorsPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.UNRELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.UNRELATED_GAP_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JPanel authorPanel = new JPanel();
		authorPanel.setBorder(new TitledBorder(null, Messages.getString("AboutDialog.authorPanel.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
		authorPanel.setBackground(Color.WHITE);
		contributorsPanel.add(authorPanel, "5, 3, 5, 1, fill, fill");
		authorPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.UNRELATED_GAP_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.UNRELATED_GAP_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.UNRELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,}));
		
		JLabel authorPhoto = new JLabel("");
		authorPanel.add(authorPhoto, "4, 3");
		authorPhoto.setIcon( ImageUtil.loadIcon( ImagePath.authorImage() ) );
		authorPhoto.setBorder(new LineBorder(Color.LIGHT_GRAY, 3));
		
		JPanel panelAuthorInfo = new JPanel();
		authorPanel.add(panelAuthorInfo, "8, 3, default, fill");
		panelAuthorInfo.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panelAuthorInfo.setBackground(Color.WHITE);
		panelAuthorInfo.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel authorName = new JLabel("Thiago Delgado Pinto");
		authorName.setForeground(Color.DARK_GRAY);
		authorName.setFont(new Font("Tahoma", Font.BOLD, 14));
		panelAuthorInfo.add(authorName, "2, 2");
		
		JLabel authorEmail = new JLabel("thiago@funtester.org");
		authorEmail.setIconTextGap(6);
		authorEmail.setIcon( ImageUtil.loadIcon( ImagePath.emailIcon() ) );
		authorEmail.setForeground(Color.DARK_GRAY);
		panelAuthorInfo.add(authorEmail, "2, 4");
		
		JLabel authorAddress = new JLabel("Nova Friburgo, RJ - Brasil");
		authorAddress.setIconTextGap(6);
		authorAddress.setIcon( ImageUtil.loadIcon( ImagePath.countryFlagIcon( "br" ) ) );
		authorAddress.setForeground(Color.DARK_GRAY);
		panelAuthorInfo.add(authorAddress, "2, 6");
		
		JLabel authorWebProfile = new JLabel( authorWebProfileContent );
		authorWebProfile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		authorWebProfile.setIcon( ImageUtil.loadIcon( ImagePath.linkedinIcon() ) );
		authorWebProfile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				openWebSite( linkedIn );
			}
		});
		panelAuthorInfo.add(authorWebProfile, "2, 8, left, default");
		
		JLabel lblFeedback = new JLabel(Messages.getString("AboutDialog.lblFeedback.text")); //$NON-NLS-1$
		lblFeedback.setForeground(Color.GRAY);
		lblFeedback.setFont(new Font("Tahoma", Font.ITALIC, 13));
		panelAuthorInfo.add(lblFeedback, "2, 12");
		
		JLabel lblMailContributor = new JLabel(Messages.getString("AboutDialog.lblMailContributor.text")); //$NON-NLS-1$
		lblMailContributor.setFont(new Font("Tahoma", Font.BOLD, 13));
		contributorsPanel.add(lblMailContributor, "5, 9");
		
		JLabel mainContributor = new JLabel("Thiago Delgado Pinto");
		contributorsPanel.add(mainContributor, "9, 9, left, default");
		
		JLabel lblOtherContributors = new JLabel(Messages.getString("AboutDialog.lblOtherContributors.text")); //$NON-NLS-1$
		lblOtherContributors.setFont(new Font("Tahoma", Font.BOLD, 13));
		contributorsPanel.add(lblOtherContributors, "5, 11, default, top");
		
		JTextArea otherContributors = new JTextArea();
		otherContributors.setEditable(false);
		otherContributors.setFont( mainContributor.getFont() );
		otherContributors.setText("None :(  Be the first !");
		contributorsPanel.add(otherContributors, "9, 11, fill, fill");
		
		// BUTTONS
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(Color.WHITE);
		getContentPane().add( buttonPane, BorderLayout.SOUTH );
		buttonPane.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("482px:grow"),
				FormFactory.BUTTON_COLSPEC,
				FormFactory.UNRELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JButton closeButton = new JButton( Messages.getString("AboutDialog.closeButton.text") ); //$NON-NLS-1$
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		closeButton.setActionCommand( "" );
		buttonPane.add( closeButton, "2, 2, fill, top" );
		getRootPane().setDefaultButton( closeButton );

		
		//
		// Select the first row
		//
		if ( pluginTablePanel.getTable().getRowCount() > 0 ) {
			pluginTablePanel.getTable().setRowSelectionInterval( 0, 0 );
		}
	}

	private BaseTableModel< PluginInfo > createPlugInInfoTableModel(
			final Set< PluginInfo > plugins
			) {
		
		final String columns[] = {
				"#",
				Messages.alt( "AboutDialog.lblPluginName.text", "Name" ).replaceAll( ":", "" ),
				Messages.alt( "AboutDialog.lblPluginVersion.text", "Version" ).replaceAll( ":", "" ),
				Messages.alt( "AboutDialog.lblPluginId.text", "Id" ).replaceAll( ":", "" )
				};
		
		return new BaseTableModel< PluginInfo >(
				columns.length,
				plugins,
				columns
				) {
			
			private static final long serialVersionUID = -4873396121637899434L;

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch ( columnIndex ) {
					case 0: return rowIndex + 1;
					case 1: return itemAt( rowIndex ).getName();
					case 2: return itemAt( rowIndex ).getVersion();
					case 3: return itemAt( rowIndex ).getId();
					default: return "";
				}
			}
		};
	}


	private void openWebSite(final String url) {
		try {
			Desktop.getDesktop().browse( new URI( url ) );
		} catch ( Exception e1 ) {
			MsgUtil.error( null, e1.getLocalizedMessage(), getTitle() );
		}
	}

	
	private void drawPluginInfo(PluginInfo p) {
		pluginName.setText( p.getName() );
		pluginVersion.setText( p.getVersion() );
		pluginDescription.setText( p.getDescription() );
		pluginSite.setText( p.getWebsite() );
		pluginId.setText( p.getId() );
		pluginAuthors.setText( p.getAuthors() );
	}
	
	
	private void loadTextFileTo(JTextPane versionNotes, String fileName) {
		try {
			versionNotes.setText( TextFileUtil.loadContent( fileName ).toString() );
			versionNotes.select( 1, 1 ); // Scroll to the beginning of the file
		} catch ( Exception e ) {
			// Do not throw
			String msg = String.format(
					Messages.alt( "_FILE_NOT_FOUND", "File not found: %s" ),
					VERSION_FILE_NAME );
			versionNotes.setText( msg );
		}
	}
}
