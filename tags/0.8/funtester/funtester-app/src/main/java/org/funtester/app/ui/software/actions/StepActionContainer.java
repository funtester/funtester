package org.funtester.app.ui.software.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.Action;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.software.ActionStepDialog;
import org.funtester.app.ui.software.DocStepDialog;
import org.funtester.app.ui.software.OracleStepDialog;
import org.funtester.app.ui.software.UseCaseCallStepDialog;
import org.funtester.app.ui.util.BaseAction;
import org.funtester.app.ui.util.BaseTableModel;
import org.funtester.app.ui.util.CRUDActionContainer;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.app.ui.util.UIUtil;
import org.funtester.core.profile.StepKind;
import org.funtester.core.software.ActionStep;
import org.funtester.core.software.DocStep;
import org.funtester.core.software.Element;
import org.funtester.core.software.Flow;
import org.funtester.core.software.OracleStep;
import org.funtester.core.software.Software;
import org.funtester.core.software.Step;
import org.funtester.core.software.UseCase;
import org.funtester.core.software.UseCaseCallStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action container for a {@link Step}.
 *
 * TODO: Refactor the showXXXStep methods.
 *
 * @author Thiago Delgado Pinto
 *
 */
public class StepActionContainer extends CRUDActionContainer< Step >{

	private static final Logger logger =
			LoggerFactory.getLogger( StepActionContainer.class );

	private Action newDocStepAction;
	private Action newActionStepAction;
	private Action newUseCaseCallStepAction;
	private Action newOracleStepAction;
	private Action cloneAction;
	private Action editAction;
	private Action removeAction;
	private Action upAction;
	private Action downAction;

	private ActionListener afterMoveUpActionListener;
	private ActionListener afterMoveDownActionListener;

	private final Software software;
	private Flow flow;

	public StepActionContainer(
			BaseTableModel< Step > tableModel,
			Software software
			) {
		super( tableModel );
		this.software = software;
	}

	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
	}

	public void registerAfterMoveUpActionListener(ActionListener l) {
		this.afterMoveUpActionListener = l;
	}

	public void registerAfterMoveDownActionListener(ActionListener l) {
		this.afterMoveDownActionListener = l;
	}

	public Action getNewDocStepAction() {
		if ( null == newDocStepAction ) {
			newDocStepAction = new BaseAction()
				.withName( Messages.getString( "_NEW" ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.stepNewIcon() ) )
				.withListener( createNewDocStepActionListener() )
				;
		}
		return newDocStepAction;
	}

	public Action getNewActionStepAction() {
		if ( null == newActionStepAction ) {
			newActionStepAction = new BaseAction()
				.withName( Messages.getString( "_NEW" ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.stepNewIcon() ) )
				.withListener( createNewActionStepActionListener() )
				;
		}
		return newActionStepAction;
	}

	public Action getNewUseCaseCallStepAction() {
		if ( null == newUseCaseCallStepAction ) {
			newUseCaseCallStepAction = new BaseAction()
				.withName( Messages.getString( "_NEW" ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.stepNewIcon() ) )
				.withListener( createNewUseCaseCallStepActionListener() )
				;
		}
		return newUseCaseCallStepAction;
	}

	public Action getNewOracleStepAction() {
		if ( null == newOracleStepAction ) {
			newOracleStepAction = new BaseAction()
				.withName( Messages.getString( "_NEW" ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.stepNewIcon() ) )
				.withListener( createNewOracleStepActionListener() )
				;
		}
		return newOracleStepAction;
	}

	public Action getCloneAction() {
		if ( null == cloneAction ) {
			cloneAction = new BaseAction()
				.withName( Messages.alt( "_CLONE", "Clone" ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.stepCloneIcon() ) )
				.withListener( createCloneActionListener() )
				;
		}
		return cloneAction;
	}

	public Action getEditAction() {
		if ( null == editAction ) {
			editAction = new BaseAction()
				.withName( Messages.alt( "_EDIT", "Edit..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.stepEditIcon() ) )
				.withListener( createEditActionListener() )
				;
		}
		return editAction;
	}

	public Action getRemoveAction() {
		if ( null == removeAction ) {
			removeAction = new BaseAction()
				.withName( Messages.alt( "_REMOVE", "Remove..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.stepRemoveIcon() ) )
				.withListener( createRemoveActionListener() )
				;
		}
		return removeAction;
	}

	public Action getUpAction() {
		if ( null == upAction ) {
			upAction = new BaseAction()
				.withName( Messages.alt( "_UP", "Up" ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.stepUpIcon() ) )
				.withListener( createUpActionListener() )
				;
		}
		return upAction;
	}

	public Action getDownAction() {
		if ( null == downAction ) {
			downAction = new BaseAction()
				.withName( Messages.alt( "_DOWN", "Down" ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.stepDownIcon() ) )
				.withListener( createDownActionListener() )
				;
		}
		return downAction;
	}

	// ACTION LISTENERS

	private ActionListener createNewDocStepActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showDocStep( true );
			}
		};
	}


	private ActionListener createNewActionStepActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showActionStep( true );
			}
		};
	}

	private ActionListener createNewUseCaseCallStepActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showUseCaseCallStep( true );
			}
		};
	}

	private ActionListener createNewOracleStepActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showOracleStep( true );
			}
		};
	}

	/**
	 * Edit the current step, according to the step kind.
	 * @return
	 */
	private ActionListener createEditActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( null == getFlow() || null == getSelectedObject() ) { return; }
				Step obj = getSelectedObject();

				// DocStep
				if ( StepKind.DOC.equals( obj.kind() ) ) {
					showDocStep( false );
				} else if ( StepKind.ACTION.equals( obj.kind() ) ) {
					showActionStep( false );
				} else if ( StepKind.USE_CASE_CALL.equals( obj.kind() ) ) {
					showUseCaseCallStep( false );
				} else if ( StepKind.ORACLE.equals( obj.kind() ) ) {
					showOracleStep( false );
				} else {
					// Not supported type. Do nothing.
					logger.warn( "Unsuported step kind: " + obj.kind() );
				}

			}
		};
	}

	private ActionListener createCloneActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( null == getFlow() || getSelectedIndex() < 0 ) { return; }
				final int index = getSelectedIndex();
				final int cloneIndex = getFlow().cloneStepAt( index );

				// Generate a new id for the clone
				Step step = getFlow().stepAt( cloneIndex );
				step.setId( software.generateIdFor( Step.class.getSimpleName() ) );

				getTableModel().inserted( cloneIndex );
			}
		};
	}

	private ActionListener createRemoveActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( null == getFlow() || null == getSelectedObject() ) { return; }
				// Ask
				if ( ! MsgUtil.yesTo( null, Messages.alt( "_ASK_REMOVE", "Remove ?" ),
						getSelectedObject().shortName() ) ) {
					return;
				}
				getTableModel().remove( getSelectedIndex() );
			}
		};
	}


	private ActionListener createUpActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( null == getFlow() || getSelectedIndex() < 0 ) { return; }
				final int oldIndex = getSelectedIndex();
				final int newIndex = getFlow().moveStepUp( oldIndex );
				getTableModel().moved( oldIndex, newIndex );
				if ( afterMoveUpActionListener != null ) {
					afterMoveUpActionListener.actionPerformed( e );
				}
			}
		};
	}

	private ActionListener createDownActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( null == getFlow() || getSelectedIndex() < 0 ) { return; }
				final int oldIndex = getSelectedIndex();
				final int newIndex = getFlow().moveStepDown( oldIndex );
				getTableModel().moved( oldIndex, newIndex );
				if ( afterMoveDownActionListener != null ) {
					afterMoveDownActionListener.actionPerformed( e );
				}
			}
		};
	}

	private void showDocStep(final boolean isNew) {
		if ( null == getFlow() || ( ! isNew && null == getSelectedObject() ) ) { return; }

		DocStepDialog dlg = new DocStepDialog();
		UIUtil.centerOnScreen( dlg );

		DocStep obj = (isNew ) ? new DocStep( getFlow() ) : (DocStep) getSelectedObject();
		dlg.copyObject( obj );
		dlg.showObject();
		if ( ! dlg.isConfirmed() ) { return; }

		if ( isNew ) {
			obj = dlg.getObject().newCopy();
			obj.setId( software.generateIdFor( Step.class.getSimpleName() ) );
			getTableModel().add( obj );
		} else {
			obj.copy( dlg.getObject() );
			getTableModel().updated( getSelectedIndex() );
		}
	}

	private void showActionStep(final boolean isNew) {
		if ( null == getFlow() || ( ! isNew && null == getSelectedObject() ) ) { return; }

		final UseCase useCase = getFlow().getUseCase();

		ActionStepDialog dlg = new ActionStepDialog(
				useCase, software.getVocabulary().getNicknames() );
		UIUtil.centerOnScreen( dlg );

		ActionStep obj = ( isNew ) ? new ActionStep( getFlow() ) : (ActionStep) getSelectedObject();
		dlg.copyObject( obj );
		dlg.showObject();
		if ( ! dlg.isConfirmed() ) { return; }

		if ( isNew ) {
			obj = dlg.getObject().newCopy();
			obj.setId( software.generateIdFor( Step.class.getSimpleName() ) );

			getTableModel().add( obj );
		} else {
			obj.copy( dlg.getObject() );
			getTableModel().updated( getSelectedIndex() );
		}

		// Add the elements to the use case
		final Collection< Element > newElements = dlg.getNewElements();
		for ( Element element : newElements ) {
			element.setId( software.generateIdFor( Element.class.getSimpleName() ) );
			element.setUseCase( useCase );

			// If the action is editable, so the element
			if ( obj.getActionNickname() != null ) {
				org.funtester.core.profile.Action action =
						obj.getActionNickname().getAction();
				if ( action != null && action.getMakeElementsEditable() ) {
					element.setEditable( true );
				}
			}

			useCase.addElement( element );
		}
		/*
		System.out.println( "Elementos do Caso de Uso: " + useCase.getElements() );
		System.out.println( "Elementos do Passo: " + obj.getElements() );
		System.out.println( "Casos de uso contém todos os elementos do passo: " + useCase.getElements().containsAll( obj.getElements() ) );
		final Element stepIE = (Element) obj.getElements().get( 0 );
		final String eName = stepIE.getName();
		for ( Element ie : useCase.getElements() ) {
			if ( ie.getName().equalsIgnoreCase( eName ) ) {
				System.out.println( "UseCase element: " + ie.asJSON() );
				System.out.println( "Step element: " + ie.asJSON() );
			}
		}
		*/
	}


	private void showUseCaseCallStep(final boolean isNew) {
		if ( null == getFlow() || ( ! isNew && null == getSelectedObject() ) ) { return; }

		UseCaseCallStepDialog dlg = new UseCaseCallStepDialog(
				software.getVocabulary().getNicknames(),
				software.getUseCases()
				);
		UIUtil.centerOnScreen( dlg );

		UseCaseCallStep obj = (isNew ) ? new UseCaseCallStep( getFlow() ) : (UseCaseCallStep) getSelectedObject();
		dlg.copyObject( obj );
		dlg.showObject();
		if ( ! dlg.isConfirmed() ) { return; }

		if ( isNew ) {
			obj = dlg.getObject().newCopy();
			obj.setId( software.generateIdFor( Step.class.getSimpleName() ) );
			getTableModel().add( obj );
		} else {
			obj.copy( dlg.getObject() );
			getTableModel().updated( getSelectedIndex() );
		}
	}

	private void showOracleStep(final boolean isNew) {
		if ( null == getFlow() || ( ! isNew && null == getSelectedObject() ) ) { return; }

		final UseCase useCase = getFlow().getUseCase();

		OracleStepDialog dlg = new OracleStepDialog(
			useCase,
			software.getVocabulary().getNicknames()
			);
		UIUtil.centerOnScreen( dlg );

		OracleStep obj = (isNew ) ? new OracleStep( getFlow() ) : (OracleStep) getSelectedObject();
		dlg.copyObject( obj );
		dlg.showObject();
		if ( ! dlg.isConfirmed() ) { return; }

		if ( isNew ) {
			obj = dlg.getObject().newCopy();
			obj.setId( software.generateIdFor( Step.class.getSimpleName() ) );
			getTableModel().add( obj );
		} else {
			obj.copy( dlg.getObject() );
			getTableModel().updated( getSelectedIndex() );
		}

		// Add the elements to the use case
		final Collection< Element > newElements = dlg.getNewElements();
		for ( Element ie : newElements ) {
			ie.setId( software.generateIdFor( Element.class.getSimpleName() ) );
			ie.setUseCase( useCase );

			useCase.addElement( ie );
		}
	}
}
