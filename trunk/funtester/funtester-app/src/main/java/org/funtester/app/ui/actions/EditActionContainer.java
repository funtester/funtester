package org.funtester.app.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JFrame;

import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppState;
import org.funtester.app.repository.AppConfigurationRepository;
import org.funtester.app.repository.json.JsonAppConfigurationRepository;
import org.funtester.app.ui.ConfigurationDialog;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.util.BaseAction;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.MsgUtil;

/**
 * Container for the "Edit" actions.
 *
 * @author Thiago Delgado Pinto
 *
 */
public class EditActionContainer {

	private final JFrame owner;
	private final String title;
	private final AppState state;

	private Action editConfigurationAction = null;
	private Action editProfilesAction = null;
	private Action editVocabulariesAction = null;

	public EditActionContainer(
			JFrame owner,
			final String title,
			final AppState state
			) {
		this.owner = owner;
		this.title = title;
		this.state = state;
	}


	public Action editConfigurationAction() {
		return ( null == editConfigurationAction )
			? editConfigurationAction = new BaseAction()
				.withName( Messages.alt( "_MENU_EDIT_CONFIGURATION", "Configuration..." ) )
				//.withListener( createConfigurationEditActionListener() )
				.withListener( createNewConfigurationEditActionListener() )
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


	private ActionListener createNewConfigurationEditActionListener() {

		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigurationDialog dlg = new ConfigurationDialog( state );
				dlg.setObject( state.getConfiguration() );
				dlg.showObject();
				if ( ! dlg.isConfirmed() ) {
					return;
				}
				// Changing
				state.getConfiguration().copy( dlg.getObject() );
				dlg = null;

				// Saving
				AppConfigurationRepository repository = new JsonAppConfigurationRepository(
						state.getConfigurationFile() );
				try {
					repository.save( state.getConfiguration() );
				} catch ( Exception e1 ) {
					MsgUtil.error( owner, e1.getLocalizedMessage(), title );
				}
			}
		};

	}

}
