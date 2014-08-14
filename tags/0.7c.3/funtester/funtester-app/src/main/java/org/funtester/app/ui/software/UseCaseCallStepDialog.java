package org.funtester.app.ui.software;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.DefaultEditingDialog;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.validation.UseCaseCallStepValidator;
import org.funtester.common.util.Validator;
import org.funtester.core.profile.StepKind;
import org.funtester.core.software.UseCase;
import org.funtester.core.software.UseCaseCallStep;
import org.funtester.core.vocabulary.ActionNickname;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Dialog for editing a {@link UseCase}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class UseCaseCallStepDialog extends
		DefaultEditingDialog< UseCaseCallStep > {

	private static final long serialVersionUID = -2634901864486766463L;
	
	private final JComboBox actionNickname;
	private final JComboBox useCase;

	public UseCaseCallStepDialog(
			final Collection< ActionNickname > nicknames,
			final Collection< UseCase > useCases
			) {
		setTitle(Messages.getString("UseCaseCallStepDialog.this.title")); //$NON-NLS-1$
		setIconImage( ImageUtil.loadImage( ImagePath.stepIcon() ) );
		setBounds( 100, 100, 453, 159 );
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblAction = new JLabel(Messages.getString("UseCaseCallStepDialog.lblAction.text")); //$NON-NLS-1$
		contentPanel.add(lblAction, "2, 2, right, default");
		
		
		actionNickname = new JComboBox(
			new DefaultComboBoxModel( filterNicknames( nicknames ) )
			);
		actionNickname.setName( "actionNickname" );
		contentPanel.add(actionNickname, "4, 2, fill, default");
		
		JLabel lblUsecase = new JLabel(Messages.getString("UseCaseCallStepDialog.lblUsecase.text")); //$NON-NLS-1$
		contentPanel.add(lblUsecase, "2, 4, right, default");
		
		useCase = new JComboBox( new DefaultComboBoxModel( useCases.toArray( new UseCase[ 0 ] )  ) );
		useCase.setName( "useCase" );
		contentPanel.add(useCase, "4, 4, fill, default");
	}

	@Override
	protected UseCaseCallStep createObject() {
		return new UseCaseCallStep();
	}

	@Override
	protected boolean populateObject() {
		UseCaseCallStep obj = getObject();
		obj.setActionNickname( (ActionNickname) actionNickname.getSelectedItem() );
		UseCase uc = (UseCase) useCase.getSelectedItem();
		obj.setUseCase( uc );
		if ( uc != null ) {
			obj.setReferencedUseCaseId( uc.getId() );
		} else {
			obj.setReferencedUseCaseId( 0 );
		}
		return true;
	}

	@Override
	protected void drawObject(final UseCaseCallStep obj) {
		actionNickname.setSelectedItem( obj.getActionNickname() );
		useCase.setSelectedItem( obj.getUseCase() );
		
		// Select the first action nickname if there is no selected item
		if ( null == actionNickname.getSelectedItem()
			&& actionNickname.getItemCount() == 1 ) {
			actionNickname.setSelectedIndex( 0 );
		}

		// Select the first use case if there is no selected item
		if ( null == useCase.getSelectedItem()
			&& useCase.getItemCount() == 1 ) {
			useCase.setSelectedIndex( 0 );
		}
	}

	@Override
	protected Validator< UseCaseCallStep > createValidator() {
		return new UseCaseCallStepValidator();
	}

	/**
	 * Return just the nicknames for a use case call.
	 *  
	 * @param nicknames	the nicknames to filter.
	 * @return			a filtered array of nicknames.
	 */
	private ActionNickname[] filterNicknames(
			final Collection< ActionNickname > nicknames
			) {
		List< ActionNickname > list = new ArrayList< ActionNickname >();
		for ( ActionNickname a : nicknames ) {
			if ( a.actionKind().equals( StepKind.USE_CASE_CALL ) ) {
				list.add( a );
			}
		}
		Collections.sort( list );
		return list.toArray( new ActionNickname[ 0 ] );
	}
}
