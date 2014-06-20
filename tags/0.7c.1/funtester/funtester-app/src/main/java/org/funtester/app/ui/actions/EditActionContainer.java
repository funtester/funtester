package org.funtester.app.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JFrame;

import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppConfiguration;
import org.funtester.app.project.AppConfigurationManager;
import org.funtester.app.project.AppState;
import org.funtester.app.repository.AppConfigurationRepository;
import org.funtester.app.repository.json.JsonAppConfigurationRepository;
import org.funtester.app.ui.AppConfigurationDialog;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.util.BaseAction;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.UIUtil;
import org.funtester.app.validation.AppConfigurationValidator;

/**
 * Container for the "Edit" actions.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class EditActionContainer {

//	private final JFrame owner;
//	private final String title;
	private final AppState state;	
	
	private Action editConfigurationAction = null;
	private Action editProfilesAction = null;
	private Action editVocabulariesAction = null;
	
	public EditActionContainer(
			JFrame owner,
			final String title,
			final AppState state
			) {
//		this.owner = owner;
//		this.title = title;
		this.state = state;	
	}
	
	
	public Action editConfigurationAction() {
		final String fileName = state.getConfigurationFile();
		AppConfigurationRepository repository = new JsonAppConfigurationRepository( fileName );
		
		AppConfiguration configuration = state.getConfiguration();
		
		Collection< Locale > locales = state.getLocalesMap().keySet();
		
		Map< String, String > lookAndFeelMap = state.getLookAndFeelMap();
		Collection< String > lookAndFeels = lookAndFeelMap != null ? lookAndFeelMap.keySet() : null;
		
		AppConfigurationValidator validator = new AppConfigurationValidator( lookAndFeels );
		
		AppConfigurationManager manager = new AppConfigurationManager(
				locales, lookAndFeels, validator, repository );
		
		return ( null == editConfigurationAction )
			? editConfigurationAction = new BaseAction()
				.withName( Messages.alt( "_MENU_EDIT_CONFIGURATION", "Configuration..." ) )
				.withListener( createConfigurationEditActionListener( configuration, manager ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.configurationIcon() ) )
			: editConfigurationAction;
	}
	
	public Action editProfilesAction() {
		return ( null == editProfilesAction )
				? editProfilesAction = new BaseAction()
					.withEnabled( false )
					.withName( Messages.alt( "_MENU_EDIT_PROFILES", "Profiles..." ) )
					//.withListener( new EditProfilesActionListener( owner, title, manager ) )
				: editProfilesAction;
	}
	
	public Action editVocabulariesAction() {
		return ( null == editVocabulariesAction )
				? editVocabulariesAction = new BaseAction()
					.withEnabled( false )
					.withName( Messages.alt( "_MENU_EDIT_VOCABULARIES", "Vocabularies..." ) )
					//.withListener( new EditVocabulariesActionListener( owner, title, manager ) )
				: editVocabulariesAction;
	}
	
	private ActionListener createConfigurationEditActionListener(
			final AppConfiguration configuration,
			final AppConfigurationManager manager			
			) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AppConfigurationDialog dlg = new AppConfigurationDialog( manager );
				UIUtil.centerOnScreen( dlg );
				dlg.draw( configuration );
				dlg.setVisible( true );
			}
		};
	}

}
