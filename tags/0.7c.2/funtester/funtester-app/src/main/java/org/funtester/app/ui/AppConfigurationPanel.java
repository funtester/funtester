package org.funtester.app.ui;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.text.WordUtils;
import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppConfiguration;
import org.funtester.app.ui.common.DefaultFileChooser;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.util.IconAndTextListCellRenderer;
import org.funtester.app.ui.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Application configuration panel
 *
 * @author Thiago Delgado Pinto
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class AppConfigurationPanel extends JPanel {

	private static final long serialVersionUID = -8786493360891272884L;
	
	private final Logger logger = LoggerFactory.getLogger( AppConfigurationPanel.class );
	
	private final JTextField vocabulary;
	private final JTextField profile;
	private final JTextField databaseDriver;
	private final JTextField plugin;
	private final JComboBox locale;
	private final JComboBox lookAndFeel;
	
	private final Vector< String > items = new Vector< String >();
	private final Map< String, Icon > itemsToIconsMap = new HashMap< String, Icon >();
	private final Map< String, Locale > itemsToLocalesMap = new HashMap< String, Locale >();
	
	public AppConfigurationPanel(
			final Collection< Locale > locales,
			final Collection< String > lookAndFeels
			) {
		if ( null == locales ) throw new IllegalArgumentException( "locales" );
		if ( null == lookAndFeels ) throw new IllegalArgumentException( "lookAndFeels" );
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JPanel directoriesPanel = new JPanel();
		add(directoriesPanel, "2, 2, fill, fill");
		directoriesPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("max(49dlu;default)"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		final Icon dirIcon = ImageUtil.loadIcon( ImagePath.openIcon() );
		
		JLabel lblDirectories = new JLabel(Messages.getString("AppConfigurationPanel.lblDirectories.text")); //$NON-NLS-1$
		lblDirectories.setForeground(SystemColor.textInactiveText);
		lblDirectories.setIcon( dirIcon );
		directoriesPanel.add(lblDirectories, "1, 2");
		
		JLabel lblVocabulary = new JLabel(Messages.getString("AppConfigurationPanel.lblVocabulary.text")); //$NON-NLS-1$
		add(lblVocabulary, "2, 4, right, default");
		
		vocabulary = new JTextField();
		vocabulary.setName("vocabularyDirectory");
		vocabulary.setColumns(10);
		add(vocabulary, "4, 4, fill, default");
		
		JButton vocabularyButton = new JButton("...");
		vocabularyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultFileChooser.chooseDirectory( vocabulary );
			}
		});
		vocabularyButton.setName("vocabularyButton");
		add(vocabularyButton, "6, 4");
		
		JLabel lblProfile = new JLabel(Messages.getString("AppConfigurationPanel.lblProfile.text")); //$NON-NLS-1$
		add(lblProfile, "2, 6, right, default");
		
		profile = new JTextField();
		profile.setName("profileDirectory");
		profile.setColumns(10);
		add(profile, "4, 6, fill, default");
		
		JButton profileButton = new JButton("...");
		profileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultFileChooser.chooseDirectory( profile );
			}
		});
		profileButton.setName("profileButton");
		add(profileButton, "6, 6");
		
		JLabel lblDatabaseDriver = new JLabel(Messages.getString("AppConfigurationPanel.lblDatabaseDriver.text")); //$NON-NLS-1$
		add(lblDatabaseDriver, "2, 8, right, default");
		
		databaseDriver = new JTextField();
		databaseDriver.setName( "databaseDriver" );
		databaseDriver.setColumns(10);
		add(databaseDriver, "4, 8, fill, default");
		
		JButton jdbcDriverButton = new JButton("...");
		jdbcDriverButton.setName( "jdbcDriverButton" );
		jdbcDriverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultFileChooser.chooseDirectory( databaseDriver );
			}
		});		
		add(jdbcDriverButton, "6, 8");
		
		JLabel lblPlugin = new JLabel(Messages.getString("AppConfigurationPanel.lblPlugin.text")); //$NON-NLS-1$
		add(lblPlugin, "2, 10, right, default");
		
		plugin = new JTextField();
		plugin.setName( "plugin" );
		add(plugin, "4, 10, fill, default");
		plugin.setColumns(10);
		
		JButton pluginButton = new JButton("...");
		pluginButton.setName("driverButton");
		pluginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultFileChooser.chooseDirectory( plugin );
			}
		});	
		add(pluginButton, "6, 10");
		
		JLabel lblLocale = new JLabel(Messages.getString("AppConfigurationPanel.lblLocale.text"));
		lblLocale.setIcon( ImageUtil.loadIcon( ImagePath.localeIcon() ) );
		add(lblLocale, "2, 14, right, default");
		
		mapLocales( locales );
		locale = new JComboBox( items );
		locale.setName("locale");
		locale.setRenderer( new IconAndTextListCellRenderer< String >( itemsToIconsMap ) );	
		add(locale, "4, 14, fill, default");
		
		JLabel lblLookAndFeel = new JLabel(Messages.getString("AppConfigurationPanel.lblLookAndFeel.text")); //$NON-NLS-1$
		lblLookAndFeel.setIcon( ImageUtil.loadIcon( ImagePath.themeIcon() ) );
		add(lblLookAndFeel, "2, 16, right, default");
		
		lookAndFeel = new JComboBox( lookAndFeels.toArray() );
		lookAndFeel.setName("lookAndFeel");
		add(lookAndFeel, "4, 16, fill, default");
	}
	
	
	private void mapLocales(final Collection< Locale > locales) {
		items.clear();
		itemsToIconsMap.clear();
		itemsToLocalesMap.clear();
		for ( Locale locale : locales ) {
			// Map the locale's info (that will be displayed to the user) to the icon
			String itemText = displayedTextForLocale( locale );
			items.add( itemText );
			itemsToLocalesMap.put( itemText, locale );
			// Get the icon with the locale country
			Icon image = null;
			final String iconPath = ImagePath.countryFlagIcon( locale.getCountry().toLowerCase() );
			try {
				image = ImageUtil.loadIcon( iconPath );
			} catch (Exception e) {
				logger.warn( "country icon not found: " + iconPath );
				// image is still null -> no problem
			}
			itemsToIconsMap.put( itemText, image );
		}		
		Collections.sort( items );
	}
	
	private String displayedTextForLocale(final Locale currentLocale) {
		String s = WordUtils.capitalize( currentLocale.getDisplayLanguage() );
		if ( ! currentLocale.getDisplayCountry().isEmpty() ) {
			s += " (" + currentLocale.getDisplayCountry() + ")";
		}
		return s;
	}	

	public void draw(final AppConfiguration configuration) {
		vocabulary.setText( configuration.getVocabularyDirectory() );
		profile.setText( configuration.getProfileDirectory() );
		databaseDriver.setText( configuration.getDatabaseDriverDirectory() );
		plugin.setText( configuration.getPluginDirectory() );
		
		Locale l = new Locale( configuration.getLocaleLanguage(),
				configuration.getLocaleCountry() );
		locale.setSelectedItem( displayedTextForLocale( l ) );
		
		lookAndFeel.setSelectedItem( configuration.getLookAndFeelName() );
	}
	
	public AppConfiguration get() {
		Locale l = itemsToLocalesMap.get( locale.getSelectedItem() );
		return AppConfiguration.createWith(
				l.getLanguage(),
				l.getCountry(),
				(String) lookAndFeel.getSelectedItem(),
				vocabulary.getText(),
				profile.getText(),
				databaseDriver.getText(),
				plugin.getText(),
				"" // for now
				);
	}
}