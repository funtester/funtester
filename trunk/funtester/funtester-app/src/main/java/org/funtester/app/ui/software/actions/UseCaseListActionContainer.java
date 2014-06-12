package org.funtester.app.ui.software.actions;

import javax.swing.Action;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.funtester.app.ui.util.CommandEventListener;
import org.funtester.core.process.rule.DriverCache;
import org.funtester.core.software.Flow;
import org.funtester.core.software.Software;
import org.funtester.core.software.UseCase;

/**
 * Use case list action container
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class UseCaseListActionContainer
	implements TreeSelectionListener, CommandEventListener {
	
	private final UseCaseActionContainer useCaseAC;
	private final FlowActionContainer flowAC;
	
	public UseCaseListActionContainer(
			final Software software,
			final DriverCache driverCache
			) {
		useCaseAC = new UseCaseActionContainer( software, driverCache );
		flowAC = new FlowActionContainer( software );
	}
	
	public boolean addUseCaseCUDEventListener(UseCaseCUDEventListener l) {
		return useCaseAC.addListener( l );
	}
	
	public boolean removeUseCaseCUDEventListener(UseCaseCUDEventListener l) {
		return useCaseAC.removeListener( l );
	}
	
	public boolean addFlowCUDEventListener(FlowCUDEventListener listener) {
		return flowAC.addListener( listener );
	}
	
	public boolean removeFlowCUDEventListener(FlowCUDEventListener listener) {
		return flowAC.removeListener( listener );
	}
	
	public Action useCaseNewAction() {
		return useCaseAC.getNewAction();
	}
	
	public Action useCaseEditAction() {
		return useCaseAC.getEditAction();
	}
	
	public Action useCaseRemoveAction() {
		return useCaseAC.getRemoveAction();
	}
	
	public Action flowNewBasicFlowAction() {
		return flowAC.getNewBasicFlowAction();
	}
	
	public Action flowNewTerminateFlowAction() {
		return flowAC.getNewTerminateFlowAction();
	}
	
	public Action flowNewReturnableFlowAction() {
		return flowAC.getNewReturnableFlowAction();
	}
	
	public Action flowNewCancelatorFlowAction() {
		return flowAC.getNewCancelatorFlowAction();
	}
	
	public Action flowEditAction() {
		return flowAC.getEditAction();
	}
	
	public Action flowRemoveAction() {
		return flowAC.getRemoveAction();
	}
	
	
	public void updateActions(final Object object) {
		final boolean isUseCase = isUseCase( object );
		final boolean isFlow = isFlow( object );
		
		useCaseNewAction().setEnabled( true ); // always enabled
		useCaseEditAction().setEnabled( isUseCase );
		useCaseRemoveAction().setEnabled( isUseCase );
		
		boolean useCaseHasMoreThanOneFlow = false;
		if ( isUseCase ) {
			UseCase useCase = (UseCase) object;
			useCaseHasMoreThanOneFlow = useCase.numberOfFlows() > 0;
		} else if ( isFlow ) {
			UseCase useCase = ( (Flow) object ).getUseCase();
			if ( useCase != null ) {
				useCaseHasMoreThanOneFlow = useCase.numberOfFlows() > 0;
			}
		}
		
		flowNewBasicFlowAction().setEnabled( isUseCase && ! useCaseHasMoreThanOneFlow );
		flowNewTerminateFlowAction().setEnabled( isFlow || useCaseHasMoreThanOneFlow );
		flowNewReturnableFlowAction().setEnabled( isFlow || useCaseHasMoreThanOneFlow );
		flowNewCancelatorFlowAction().setEnabled( isFlow || useCaseHasMoreThanOneFlow );
		flowEditAction().setEnabled( isFlow );
		flowRemoveAction().setEnabled( isFlow );
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// If the item has been deleted...
		if ( null == e.getNewLeadSelectionPath() ) {
			return; // ...exit
		}
		// The last component is the selected one
		Object selected = e.getPath().getLastPathComponent();
		if ( null == selected ) {
			return;
		}
		
		//Object object = selected;
		
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selected;
		Object object = selectedNode.getUserObject();
		
		updateActions( object );
		
		if ( isUseCase( object ) ) {
			UseCase uc = (UseCase) object;
			useCaseAC.setUseCase( uc );
			flowAC.setSelectedUseCase( uc );
			flowAC.setSelectedFlow( null );
		} else if ( isFlow( object ) ) {
			//UseCase uc = (UseCase) e.getPath().getParentPath().getLastPathComponent();
			UseCase uc = (UseCase) ( (DefaultMutableTreeNode) selectedNode.getParent() ).getUserObject();
			
			flowAC.setSelectedUseCase( uc );
			flowAC.setSelectedFlow( (Flow) object );
		}
	}

	/**
	 * This method is called when the tree is double clicked.
	 */
	@Override
	public void execute(Object object) {
		
		Object selected = ( object instanceof DefaultMutableTreeNode )
				? ( (DefaultMutableTreeNode) object ).getUserObject()
				: object;
				
		if ( isUseCase( selected ) ) {
			useCaseEditAction().actionPerformed( null );
		} else if ( isFlow( selected ) ) {
			flowEditAction().actionPerformed( null );
		}
	}
	
	private boolean isUseCase(Object object) {
		return object instanceof UseCase;
	}
	
	private boolean isFlow(Object object) {
		return object instanceof Flow;
	}
}
