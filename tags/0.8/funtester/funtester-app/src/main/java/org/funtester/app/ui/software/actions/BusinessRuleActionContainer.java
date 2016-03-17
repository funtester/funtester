package org.funtester.app.ui.software.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.Action;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.software.BusinessRuleDialog;
import org.funtester.app.ui.util.BaseAction;
import org.funtester.app.ui.util.BaseTableModel;
import org.funtester.app.ui.util.CRUDActionContainer;
import org.funtester.app.ui.util.MsgUtil;
import org.funtester.app.ui.util.UIUtil;
import org.funtester.core.process.rule.DriverCache;
import org.funtester.core.software.BusinessRule;
import org.funtester.core.software.Element;
import org.funtester.core.software.Software;
import org.funtester.core.software.ValueConfiguration;
import org.funtester.core.software.ValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action container for a {@link BusinessRule}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class BusinessRuleActionContainer
	extends CRUDActionContainer< BusinessRule > {
	
	private static Logger logger = LoggerFactory.getLogger( BusinessRuleActionContainer.class );

	private Action newAction;
	private Action editAction;
	private Action removeAction;
	
	private final Software software;
	private final Collection< Element > elements;
	private final DriverCache driverCache;
	private ValueType valueType = ValueType.STRING;
	
	public BusinessRuleActionContainer(
			BaseTableModel< BusinessRule > tableModel,
			final Software software,
			Collection< Element > elements,
			final DriverCache driverCache
			) {
		super( tableModel );
		this.software = software;
		this.elements = elements;
		this.driverCache = driverCache;
	}
	
	public ValueType getValueType() {
		return valueType;
	}	
	
	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}
	
	public Action getNewAction() {
		if ( null == newAction ) {
			newAction = new BaseAction()
				.withName( Messages.alt( "_NEW", "New..." ) )
				//.withIcon( ImageUtil.loadIcon( ImagePath.businessRuleNewIcon() ) )
				.withListener( new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						showBusinessRule( false );
					}
				} );
		}
		return newAction;
	}

	public Action getEditAction() {
		if ( null == editAction ) {
			editAction = new BaseAction()
				.withName( Messages.alt( "_EDIT", "Edit..." ) )
				//.withIcon( ImageUtil.loadIcon( ImagePath.businessRuleEditIcon() ) )
				.withListener( new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						showBusinessRule( true );
					}
				} );
		}
		return editAction;
	}
	
	public Action getRemoveAction() {
		if ( null == removeAction ) {
			removeAction = new BaseAction()
				.withName( Messages.alt( "_REMOVE", "Remove..." ) )
				//.withIcon( ImageUtil.loadIcon( ImagePath.businessRuleRemoveIcon() ) )
				.withListener( new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						removeBusinessRule();
					}
				} );
		}
		return removeAction;
	}


	private void showBusinessRule(boolean editMode) {
		BusinessRuleDialog dlg = new BusinessRuleDialog(
				valueType,
				getTableModel().getItems(),
				elements,
				software,
				driverCache
				);
		UIUtil.centerOnScreen( dlg );
		BusinessRule obj = ( editMode && ! isSelectionEmpty() ) ? getSelectedObject() : new BusinessRule();
		dlg.copyObject( obj );
		dlg.showObject();
		if ( ! dlg.isConfirmed() ) { return; }
		
		BusinessRule br = dlg.getObject();
		logger.debug( "Dialog business rule: " +  br );
		
		obj.copy( br );
		logger.debug(  "Copied business rule: " + obj );
		
		if ( editMode ) {
			getTableModel().updated( getSelectedIndex() );
		} else {
			obj.setId( software.generateIdFor( BusinessRule.class.getSimpleName() ) );
			getTableModel().add( obj ); // Add it
		}
		
		// Generate an id for the value configuration if not set
		if ( obj.getValueConfiguration() != null && 0 == obj.getValueConfiguration().getId() ) {
			obj.getValueConfiguration().setId(
					software.generateIdFor( ValueConfiguration.class.getSimpleName() ) );
		}
		dlg = null;
	}
	
	
	private void removeBusinessRule() {
		if ( isSelectionEmpty() ) { return; }
		
		// Confirm the action
		final String msg = Messages.alt( "_CONFIRM_REMOVE", "Remove ?" );
		final String title = Messages.alt( "_CONFIRMATION", "Confirmation" );
		if ( ! MsgUtil.yesTo( null, msg, title ) ) {
			return;
		}
		
		// Remove
		final int index = getSelectedIndex();
		getTableModel().remove( index );		
	}	
}
