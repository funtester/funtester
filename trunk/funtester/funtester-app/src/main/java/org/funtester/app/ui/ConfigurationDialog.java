package org.funtester.app.ui;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
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
import javax.swing.JTextField;

import org.apache.commons.lang3.text.WordUtils;
import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppConfiguration;
import org.funtester.app.project.AppState;
import org.funtester.app.ui.common.DefaultEditingDialog;
import org.funtester.app.ui.common.DefaultFileChooser;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.util.IconAndTextListCellRenderer;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.validation.AppConfigurationValidator;
import org.funtester.common.util.PathType;
import org.funtester.common.util.Validator;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Configuration dialog
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ConfigurationDialog extends DefaultEditingDialog< AppConfiguration > {

	private static final long serialVersionUID = -8333324434192577564L;
	
	/* UI State */
	
	private final Collection< String > lookAndFeels;
	private final Vector< String > localeNames = new Vector< String >();
	private final Map< String, Icon > itemsToIconsMap = new HashMap< String, Icon >();
	private final Map< String, Locale > itemsToLocalesMap = new HashMap< String, Locale >();

	/* Widgets */
	
	private final JTextField vocabulary;
	private final JTextField profile;
	private final JTextField databaseDriver;
	private final JTextField plugin;
	private final JComboBox locale;
	private final JComboBox lookAndFeel;

	/**
	 * Create the dialog.
	 */
	public ConfigurationDialog(AppState appState) {
		
		//
		// UI State
		//
		
		final File referenceDirectory = new File( appState.getExecutionDirectory() );
		
		final Collection< Locale > locales = appState.getLocalesMap() != null
				? appState.getLocalesMap().keySet() : new ArrayList< Locale >();
		
		lookAndFeels = appState.getLookAndFeelMap() != null ? appState.getLookAndFeelMap().keySet() : null;
		
		//
		// Dialog
		//
		
		setTitle(Messages.getString("ConfigurationDialog.this.title")); //$NON-NLS-1$
		setIconImage( ImageUtil.loadImage( ImagePath.configurationIcon() ) );
		setBounds( 100, 100, 720, 390 );

		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.UNRELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
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
		
		JLabel lblDirectories = new JLabel(Messages.getString("ConfigurationDialog.lblDirectories.text")); //$NON-NLS-1$
		lblDirectories.setForeground(SystemColor.textInactiveText);
		lblDirectories.setIcon( ImageUtil.loadIcon( ImagePath.openIcon() ) );
		contentPanel.add(lblDirectories, "3, 4, left, default");
		
		
		JLabel lblVocabulary = new JLabel(Messages.getString("ConfigurationDialog.lblVocabulary.text")); //$NON-NLS-1$
		contentPanel.add(lblVocabulary, "3, 6, right, default");
		
		vocabulary = new JTextField();
		vocabulary.setName("vocabulary");
		vocabulary.setColumns(10);
		contentPanel.add(vocabulary, "5, 6, fill, default");
		
		JButton vocabularyButton = new JButton("...");
		vocabularyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultFileChooser.chooseDirectory( vocabulary, referenceDirectory, PathType.RELATIVE );
			}
		});
		vocabularyButton.setName("vocabularyButton");
		contentPanel.add(vocabularyButton, "7, 6");
		
		JLabel lblProfile = new JLabel(Messages.getString("ConfigurationDialog.lblProfile.text"));  //$NON-NLS-1$
		contentPanel.add(lblProfile, "3, 8, right, default");
		
		profile = new JTextField();
		profile.setName("profile");
		profile.setColumns(10);
		contentPanel.add(profile, "5, 8, fill, default");
		
		JButton profileButton = new JButton("...");
		profileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultFileChooser.chooseDirectory( profile, referenceDirectory, PathType.RELATIVE );
			}
		});
		profileButton.setName("profileButton");
		contentPanel.add(profileButton, "7, 8");
		
		JLabel lblDatabaseDriver = new JLabel(Messages.getString("ConfigurationDialog.lblDatabaseDriver.text")); //$NON-NLS-1$
		contentPanel.add(lblDatabaseDriver, "3, 10, right, default");
		
		databaseDriver = new JTextField();
		databaseDriver.setName( "databaseDriver" );
		databaseDriver.setColumns(10);
		contentPanel.add(databaseDriver, "5, 10, fill, default");
		
		JButton databaseDriverButton = new JButton("...");
		databaseDriverButton.setName( "databaseDriverButton" );
		databaseDriverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultFileChooser.chooseDirectory( databaseDriver, referenceDirectory, PathType.RELATIVE );
			}
		});
		contentPanel.add(databaseDriverButton, "7, 10");
		
		JLabel lblPlugin = new JLabel(Messages.getString("ConfigurationDialog.lblPlugin.text")); //$NON-NLS-1$
		contentPanel.add(lblPlugin, "3, 12, right, default");
		
		plugin = new JTextField();
		plugin.setName( "plugin" );
		contentPanel.add(plugin, "5, 12, fill, default");
		plugin.setColumns(10);
		
		JButton pluginButton = new JButton("...");
		pluginButton.setName("pluginButton");
		pluginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultFileChooser.chooseDirectory( plugin, referenceDirectory, PathType.RELATIVE );
			}
		});	
		contentPanel.add(pluginButton, "7, 12");
		
		JLabel lblLocale = new JLabel(Messages.getString("ConfigurationDialog.lblLocale.text")); //$NON-NLS-1$
		lblLocale.setIcon( ImageUtil.loadIcon( ImagePath.localeIcon() ) );
		contentPanel.add(lblLocale, "3, 16, right, default");
		
		mapLocales( locales );
		locale = new JComboBox( localeNames );
		locale.setName("locale");
		locale.setRenderer( new IconAndTextListCellRenderer< String >( itemsToIconsMap ) );	
		contentPanel.add(locale, "5, 16, fill, default");
		
		JLabel lblLookAndFeel = new JLabel(Messages.getString("ConfigurationDialog.lblLookAndFeel.text")); //$NON-NLS-1$
		lblLookAndFeel.setIcon( ImageUtil.loadIcon( ImagePath.themeIcon() ) );
		contentPanel.add(lblLookAndFeel, "3, 18, right, default");
		
		lookAndFeel = new JComboBox( lookAndFeels.toArray() );
		lookAndFeel.setName("lookAndFeel");
		contentPanel.add(lookAndFeel, "5, 18, fill, default");
	}


	@Override
	protected AppConfiguration createObject() {
		return new AppConfiguration();
	}


	@Override
	protected boolean populateObject() {
		
		AppConfiguration obj = getObject();
		
		obj.setVocabularyDirectory( vocabulary.getText() );
		obj.setProfileDirectory( profile.getText() );
		obj.setDatabaseDriverDirectory( databaseDriver.getText() );
		obj.setPluginDirectory( plugin.getText() );
		
		final Locale l = itemsToLocalesMap.get( locale.getSelectedItem() );
		obj.setLocaleLanguage( l.getLanguage() );
		obj.setLocaleCountry( l.getCountry() );
		
		obj.setLookAndFeelName( (String) lookAndFeel.getSelectedItem() );
		
		return true;
	}


	@Override
	protected void drawObject(AppConfiguration obj) {
		
		vocabulary.setText( obj.getVocabularyDirectory() );
		profile.setText( obj.getProfileDirectory() );
		databaseDriver.setText( obj.getDatabaseDriverDirectory() );
		plugin.setText( obj.getPluginDirectory() );
		
		final Locale l = new Locale( obj.getLocaleLanguage(), obj.getLocaleCountry() );
		locale.setSelectedItem( displayedTextForLocale( l ) );
		
		lookAndFeel.setSelectedItem( obj.getLookAndFeelName() );
	}


	@Override
	protected Validator< AppConfiguration > createValidator() {
		return new AppConfigurationValidator( lookAndFeels );
	}
	
	
	
	private void mapLocales(final Collection< Locale > locales) {
		localeNames.clear();
		itemsToIconsMap.clear();
		itemsToLocalesMap.clear();
		for ( Locale locale : locales ) {
			// Map the locale's info (that will be displayed to the user) to the icon
			String itemText = displayedTextForLocale( locale );
			localeNames.add( itemText );
			itemsToLocalesMap.put( itemText, locale );
			// Get the icon with the locale country
			Icon image = null;
			final String iconPath = ImagePath.countryFlagIcon( locale.getCountry().toLowerCase() );
			try {
				image = ImageUtil.loadIcon( iconPath );
			} catch (Exception e) {
				// image is still null -> no problem
			}
			itemsToIconsMap.put( itemText, image );
		}		
		Collections.sort( localeNames );
	}
	
	
	private String displayedTextForLocale(final Locale currentLocale) {
		String s = WordUtils.capitalize( currentLocale.getDisplayLanguage() );
		if ( ! currentLocale.getDisplayCountry().isEmpty() ) {
			s += " (" + currentLocale.getDisplayCountry() + ")";
		}
		return s;
	}	

}
