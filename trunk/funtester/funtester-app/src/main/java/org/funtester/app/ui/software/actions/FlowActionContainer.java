package org.funtester.app.ui.software.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.software.FlowDialog;
import org.funtester.app.ui.util.BaseAction;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.app.ui.util.TreeSingleSelectionKeeper;
import org.funtester.app.ui.util.UIUtil;
import org.funtester.common.util.Announcer;
import org.funtester.core.software.BasicFlow;
import org.funtester.core.software.CancelatorFlow;
import org.funtester.core.software.Flow;
import org.funtester.core.software.FlowType;
import org.funtester.core.software.ReturnableFlow;
import org.funtester.core.software.Software;
import org.funtester.core.software.TerminatorFlow;
import org.funtester.core.software.UseCase;

/**
 * Flow action container
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class FlowActionContainer
	extends TreeSingleSelectionKeeper< Flow > {

	private final Software software;
	private UseCase useCase;
	
	private Action newActivationFlowAction = null;
	private Action newBasicFlowAction = null;
	private Action newTerminateFlowAction = null;
	private Action newReturnableFlowAction = null;
	private Action newCancelatorFlowAction = null;	
	private Action editAction = null;
	private Action removeAction = null;
	
	private final Announcer< FlowCUDEventListener > cudAnnouncer =
			Announcer.to( FlowCUDEventListener.class );
	

	public FlowActionContainer(final Software software) {
		this.software = software;
	}
	
	public boolean addListener(FlowCUDEventListener l) {
		return cudAnnouncer.addListener( l );
	}
	
	public boolean removeListener(FlowCUDEventListener l) {
		return cudAnnouncer.removeListener( l );
	}	
	
	
	public void setSelectedUseCase(UseCase obj) {
		this.useCase = obj;
	}
	
	public void setSelectedFlow(Flow obj) {
		super.setSelectedObject( obj );
	}
	
	public Action getNewActivationFlowAction() {
		if ( null == newActivationFlowAction ) {
			newActivationFlowAction = new BaseAction()
				.withName( Messages.alt( "_ACTIVATION_FLOW_NEW", "New..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.flowNewIcon() ) )
				.withListener( createNewActionListener( FlowType.FLOW ) )
				;
		}
		return newActivationFlowAction;
	}
	
	public Action getNewBasicFlowAction() {
		if ( null == newBasicFlowAction ) {
			newBasicFlowAction = new BaseAction()
				.withName( Messages.alt( "_BASIC_FLOW_NEW", "New Basic Flow..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.flowNewIcon() ) )
				.withListener( createNewActionListener( FlowType.BASIC ) )
				;
		}
		return newBasicFlowAction;
	}
	
	public Action getNewTerminateFlowAction() {
		if ( null == newTerminateFlowAction ) {
			newTerminateFlowAction = new BaseAction()
				.withName( Messages.alt( "_TERMINATE_FLOW_NEW", "New Terminate Flow..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.flowNewIcon() ) )
				.withListener( createNewActionListener( FlowType.TERMINATOR ) )
				;
		}
		return newTerminateFlowAction;
	}
	
	public Action getNewReturnableFlowAction() {
		if ( null == newReturnableFlowAction ) {
			newReturnableFlowAction = new BaseAction()
				.withName( Messages.alt( "_RETURNABLE_FLOW_NEW", "New Returnable Flow..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.flowNewIcon() ) )
				.withListener( createNewActionListener( FlowType.RETURNABLE ) )
				;
		}
		return newReturnableFlowAction;
	}
	
	public Action getNewCancelatorFlowAction() {
		if ( null == newCancelatorFlowAction ) {
			newCancelatorFlowAction = new BaseAction()
				.withName( Messages.alt( "_CANCELATOR_FLOW_NEW", "New Cancelator Flow..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.flowNewIcon() ) )
				.withListener( createNewActionListener( FlowType.CANCELATOR ) )
				;
		}
		return newCancelatorFlowAction;
	}
	
	public Action getEditAction() {
		if ( null == editAction ) {
			editAction = new BaseAction()
				.withName( Messages.alt( "_FLOW_EDIT", "Edit Flow..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.flowEditIcon() ) )
				.withListener( createEditActionListener() )
				;
		}
		return editAction;
	}
	
	public Action getRemoveAction() {
		if ( null == removeAction ) {
			removeAction = new BaseAction()
				.withName( Messages.alt( "_FLOW_REMOVE", "Remove Flow..." ) )
				.withIcon( ImageUtil.loadIcon( ImagePath.flowRemoveIcon() ) )
				.withListener( createRemoveActionListener() )
				;
		}
		return removeAction;
	}
	
	//
	// PRIVATE
	//
	
	private ActionListener createNewActionListener(final FlowType flowType) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createFlow( flowType );
			}
		};
	}
	
	private ActionListener createEditActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( null == software || null == useCase ) { return; }
				if ( isSelectionEmpty() ) { return; }
				Flow obj = getSelectedObject();
				FlowDialog dlg = createDialog( obj );
				// Show the dialog
				UIUtil.centerOnScreen( dlg );
				dlg.setVisible( true );
				if ( dlg.isConfirmed() ) {
					obj.copy( dlg.getFlow() );
					// Announce
					//getAnnouncer().announce().updated( obj );
					cudAnnouncer.announce().updated( useCase, obj, useCase.indexOfFlow( obj ) );
				}
				dlg = null;
			}
		};
	}
	
	
	private ActionListener createRemoveActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( null == software || null == useCase ) { return; }
				if ( isSelectionEmpty() ) { return; }
				Flow obj = getSelectedObject();
				// Ask
				if ( ! MsgUtil.yesTo( null, Messages.alt( "_ASK_REMOVE", "Remove ?" ),
						obj.shortName() ) ) {
					return;
				}
				final int index = useCase.indexOfFlow( obj );
				// Remove
				useCase.removeFlow( obj );
				// Announce
				//getAnnouncer().announce().updated( useCase );
				cudAnnouncer.announce().deleted( useCase, obj, index );
			}
		};
	}
	
	private void createFlow(final FlowType flowType) {
		if ( null == software || null == useCase ) { return; }
		
//		Flow flow = ( useCase.numberOfFlows() < 1 )
//				? new BasicFlow( useCase ) : new TerminatorFlow( useCase, "" );
		
		Flow flow;
		switch ( flowType ) {
			case TERMINATOR	: flow = new TerminatorFlow( useCase, "" ); break;
			case CANCELATOR	: flow = new CancelatorFlow( useCase, "" ); break;
			case RETURNABLE	: flow = new ReturnableFlow( useCase, "" ); break;
			case BASIC		: flow = new BasicFlow( useCase ); break;
			default			: flow = new Flow( useCase ); break;
		}
		
		FlowDialog dlg = createDialog( flow );
		UIUtil.centerOnScreen( dlg );
		dlg.setVisible( true );
		if ( dlg.isConfirmed() ) {
			Flow obj = dlg.getFlow().newCopy();
			obj.setId( software.generateIdFor( Flow.class.getSimpleName() ) );
			//System.out.println( obj.getSteps().size() );
			// Add
			useCase.addFlow( obj );
			// Announce
			//getAnnouncer().announce().updated( obj );
			cudAnnouncer.announce().created( useCase, obj, useCase.indexOfFlow( obj ) );
		}
		dlg = null;
	}	
	
	private FlowDialog createDialog(Flow flow) {
		return new FlowDialog( software, useCase, flow );
	}	
}
