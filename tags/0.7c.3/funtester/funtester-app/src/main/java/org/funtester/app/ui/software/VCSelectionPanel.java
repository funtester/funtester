package org.funtester.app.ui.software;

import java.awt.BorderLayout;
import java.awt.SystemColor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.funtester.app.ui.common.EnumTranslation;
import org.funtester.core.process.rule.DriverCache;
import org.funtester.core.software.BusinessRuleType;
import org.funtester.core.software.Element;
import org.funtester.core.software.Software;
import org.funtester.core.software.ValueConfiguration;
import org.funtester.core.software.ValueConfigurationCompatibility;
import org.funtester.core.software.ValueConfigurationKind;
import org.funtester.core.software.ValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Panel for a {@link ValueConfiguration}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class VCSelectionPanel extends JPanel {

	private static final long serialVersionUID = 1678965651810252512L;
	private static final Logger logger = LoggerFactory.getLogger( VCSelectionPanel.class );
	
	private final Map< BusinessRuleType, Map< ValueConfigurationKind, String > > brVCMap;
	private BusinessRuleType businessRuleType = null;
	
	private final JComboBox valueConfigurationKind;
	private final JPanel vcContainerPanel;
	
	private final SingleVCPanel singleVCPanel;
	private final MultiVCPanel multiVCPanel;
	private final ElementBasedVCPanel elementBasedVCPanel;
	private final RegExBasedVCPanel regExBasedVCPanel;
	private final QueryBasedVCPanel queryBasedVCPanel;	


	public VCSelectionPanel(
			final ValueType valueType,
			Collection< Element > elements,
			Software software,
			final DriverCache driverCache
			) {
		
		//
		// Configure a map with the compatible configurations
		//
		
		// A HashMap allows a null key. So the null key will have ALL the value configurations 
		brVCMap = new HashMap< BusinessRuleType, Map< ValueConfigurationKind, String > >();
		
		// Null as the brVCMap key
		Map< ValueConfigurationKind, String > nullKeyVCMap = new LinkedHashMap< ValueConfigurationKind, String >();
		for ( ValueConfigurationKind vcKind : ValueConfigurationKind.values() ) {
			nullKeyVCMap.put( vcKind, EnumTranslation.translationForItem(
					ValueConfigurationKind.class, vcKind ) );
		}
		brVCMap.put( null, nullKeyVCMap );
		
		// BusinessRuleTypes as the key
		for ( BusinessRuleType brType : BusinessRuleType.values() ) {
			
			Map< ValueConfigurationKind, String > vcMap = new HashMap< ValueConfigurationKind, String >();
			
			for ( ValueConfigurationKind vcKind :
				ValueConfigurationCompatibility.compatibleKinds( brType ) ) {
				
				vcMap.put( vcKind, EnumTranslation.translationForItem(
						ValueConfigurationKind.class, vcKind ) );
			}
			
			brVCMap.put( brType, vcMap );
		}
		
		
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		final List< String > valueConfigurationTypeValues =
				EnumTranslation.createList( ValueConfigurationKind.class );
		
		valueConfigurationKind = new JComboBox( new DefaultComboBoxModel(
				valueConfigurationTypeValues.toArray( new String[ 0 ] ) ) );
		valueConfigurationKind.setName("valueConfigurationKind");
		
		valueConfigurationKind.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if ( e.getStateChange() == ItemEvent.SELECTED ) {
					
					int vcKindIndex = valueConfigurationKind.getSelectedIndex();
					logger.debug( "vcKindIndex = " + vcKindIndex );
					if ( vcKindIndex < 0 ) { return; }
					
					ValueConfigurationKind vcKind =
							valueConfigurationKindAtIndex( vcKindIndex, businessRuleType );
					logger.debug( "vcKind = " + vcKind );
					
					VCPanel panel = valueConfigurationPanelForKind( vcKind );
					logger.debug(  "panel = " + ( panel != null ? panel.getClass().getSimpleName() : "null" ) );
					changePanelTo( panel );
				}
			}
		});
		
		add(valueConfigurationKind, "1, 1, fill, default");
		
		vcContainerPanel = new JPanel();
		vcContainerPanel.setName( "vcContainerPanel" );
		vcContainerPanel.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
		vcContainerPanel.setLayout(new BorderLayout(0, 0));
		
		add(vcContainerPanel, "1, 3, fill, fill");
		
		//
		// Create the value configuration panels
		//
		
		singleVCPanel = new SingleVCPanel();
		singleVCPanel.setName( "singleVCPanel" );
		
		multiVCPanel = new MultiVCPanel( valueType );
		multiVCPanel.setName( "multiVCPanel" );
		
		elementBasedVCPanel = new ElementBasedVCPanel( elements );
		elementBasedVCPanel.setName( "elementBasedVCPanel" );
		
		regExBasedVCPanel = new RegExBasedVCPanel( software );
		regExBasedVCPanel.setName( "regExBasedVCPanel" );
		
		queryBasedVCPanel = new QueryBasedVCPanel(
				valueType,
				elements,
				software,
				driverCache,
				software.getQueryConfigurations()
				);
		queryBasedVCPanel.setName( "queryBasedVCPanel" );
	}

	public void setBusinessRuleType(BusinessRuleType brType) {
		this.businessRuleType = brType;
		filterValueConfigurationKind( this.businessRuleType );
	}
	
	public ValueConfiguration getValueConfiguration() throws Exception {
		
		ValueConfigurationKind kind = valueConfigurationKindAtIndex(
				valueConfigurationKind.getSelectedIndex(),
				this.businessRuleType );
			
		VCPanel vcPanel = ( kind != null )
				? valueConfigurationPanelForKind( kind ) : null;
		
		ValueConfiguration vc = ( vcPanel != null )
				? vcPanel.getValueConfiguration() : null;
				
		return vc;
	}
	
	public void setValueConfiguration(ValueConfiguration obj) {
		
		ValueConfigurationKind kind = ( obj != null ) ? obj.kind() : null;

		if ( kind != null ) {
			valueConfigurationKind.setSelectedItem(
					EnumTranslation.translationForItem(
							ValueConfigurationKind.class, kind ) );
		} else {
			valueConfigurationKind.setSelectedIndex( -1 );
		}
		
		VCPanel panel = valueConfigurationPanelForKind( kind );
		if ( panel != null ) {
			panel.setValueConfiguration( obj );
		}
		changePanelTo( panel );
	}

	private void filterValueConfigurationKind(BusinessRuleType brType) {
		
		Collection< String > values = brVCMap.get( brType ).values();
		
		valueConfigurationKind.setModel( new DefaultComboBoxModel(
			values.toArray( new String[ 0 ] ) ) );
		
		// Clear selection
		valueConfigurationKind.setSelectedIndex( -1 );
		// Remove the current panel
		vcContainerPanel.removeAll();
		vcContainerPanel.updateUI();
		
		// Disable if empty
		boolean hasElements = valueConfigurationKind.getModel().getSize() > 0;
		valueConfigurationKind.setEnabled( hasElements );
		
		// Just one item, so choose it
		if ( valueConfigurationKind.getModel().getSize() == 1 ) {
			valueConfigurationKind.setSelectedIndex( 0 );
		}
	}

	private void changePanelTo(VCPanel panel) {
		vcContainerPanel.removeAll();
		if ( panel != null ) {
			vcContainerPanel.add( panel, BorderLayout.CENTER );
		}
		vcContainerPanel.updateUI();
	}	

	private VCPanel valueConfigurationPanelForKind(final ValueConfigurationKind kind) {
		if ( null == kind ) { return null; }
		switch ( kind ) {
			case SINGLE			: return singleVCPanel;
			case MULTI			: return multiVCPanel;
			case ELEMENT_BASED	: return elementBasedVCPanel;
			case REGEX_BASED	: return regExBasedVCPanel;
			case QUERY_BASED	: return queryBasedVCPanel;
			default				: return null; // no panel
		}
	}
	
	private ValueConfigurationKind valueConfigurationKindAtIndex(int index, BusinessRuleType brType) {
		int count = 0;
		ValueConfigurationKind vcKind;
		Iterator< ValueConfigurationKind > it = brVCMap.get( brType ).keySet().iterator();
		while ( it.hasNext() ) {
			vcKind = it.next();
			if ( index == count ) {
				return vcKind;
			}
			count++;
		}
		return null;
	}	
}
