package org.funtester.app.ui.software;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.DefaultButtons;
import org.funtester.core.software.Flow;
import org.funtester.core.software.Postcondition;
import org.funtester.core.software.UseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Precondition from postcondition dialog
 * 
 * @author Thiago Delgado Pinto
 *
 */
@SuppressWarnings("rawtypes")
public class PreconditionFromPostconditionDialog extends JDialog {

	private static final long serialVersionUID = -1722989107859845883L;
	private static final Logger logger = LoggerFactory.getLogger(
			PreconditionFromPostconditionDialog.class );
	
	private final Map< UseCase, Map< Flow, List< Postcondition >>> useCaseMap;
	private boolean confirmed = false;
	
	private final JPanel contentPanel = new JPanel();
	private final JComboBox useCase;
	private final JComboBox flow;
	private final JComboBox postcondition;

	public PreconditionFromPostconditionDialog(
			final Map< UseCase, Map< Flow, List< Postcondition >>> useCaseMap
			) {
		this.useCaseMap = useCaseMap;
		
		setTitle(Messages.getString("PreconditionFromPostconditionDialog.this.title")); // TODO i18n //$NON-NLS-1$
		setModal( true );
		setBounds( 100, 100, 450, 190 );
		getContentPane().setLayout( new BorderLayout() );
		contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		getContentPane().add( contentPanel, BorderLayout.CENTER );
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblUseCase = new JLabel(Messages.getString("PreconditionFromPostconditionDialog.lblUseCase.text")); //$NON-NLS-1$
		contentPanel.add(lblUseCase, "2, 2");
		
		useCase = new JComboBox();
		useCase.setName("useCase");
		useCase.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if ( e.getStateChange() != ItemEvent.SELECTED ) {
					return;
				}
				UseCase uc = (UseCase) e.getItem();
				drawFlows( useCaseMap.get( uc ).keySet() );
			}
		});
		contentPanel.add(useCase, "4, 2, fill, default");
		
		JLabel lblFlow = new JLabel(Messages.getString("PreconditionFromPostconditionDialog.lblFlow.text")); //$NON-NLS-1$
		contentPanel.add(lblFlow, "2, 4");
		
		flow = new JComboBox();
		flow.setName("flow");
		flow.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if ( e.getStateChange() != ItemEvent.SELECTED ) {
					return;
				}
				Flow f = (Flow) e.getItem();
				
				UseCase uc = (UseCase) useCase.getSelectedItem();
				if ( null == uc ) {
					logger.error( "selected use case is null when selecting the flow" );
					return;
				}
				
				//System.out.println( useCaseMap.get( uc ).get( f ) );
				
				drawPostconditions( useCaseMap.get( uc ).get( f ) );
			}
		});
		contentPanel.add(flow, "4, 4, fill, default");
		
		JLabel lblPostcondition = new JLabel(Messages.getString("PreconditionFromPostconditionDialog.lblPostcondition.text")); //$NON-NLS-1$
		contentPanel.add(lblPostcondition, "2, 6");
		
		postcondition = new JComboBox();
		postcondition.setName("postcondition");
		contentPanel.add(postcondition, "4, 6, fill, default");

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
		getContentPane().add( buttonPane, BorderLayout.SOUTH );

		JButton okButton = DefaultButtons.createOkButton();
		buttonPane.add( okButton );
		getRootPane().setDefaultButton( okButton );
		okButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible( false );
				confirmed = true;
			}
		} );

		JButton cancelButton = DefaultButtons.createCancelButton();
		buttonPane.add( cancelButton );
		final ActionListener cancelActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible( false );
			}
		};
		cancelButton.addActionListener( cancelActionListener );
		
		// Register the ESC key to call the cancelActionListener
		getRootPane().registerKeyboardAction(
				cancelActionListener,
				KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ),
				JComponent.WHEN_IN_FOCUSED_WINDOW
				);
		
		
		drawUseCases( useCaseMap.keySet() );
	}

	public Postcondition getPostcondition() {
		return (Postcondition) postcondition.getSelectedItem();
	}
	
	public void setPostcondition(Postcondition p) {
		if ( null == p ) { return; }
		
		final Flow f = p.getOwnerFlow();
		if ( null == f ) { return; }
		
		final UseCase uc = f.getUseCase();
		if ( null == uc ) { return; }
		
		useCase.setSelectedItem( uc );
		
		drawFlows( useCaseMap.get( uc ).keySet() );
		flow.setSelectedItem( f );
		
		drawPostconditions( useCaseMap.get( uc ).get( f ) );
		postcondition.setSelectedItem( p );
	}
	
	public boolean isConfirmed() {
		return confirmed ;
	}
	
	@SuppressWarnings("unchecked")
	private void drawUseCases(final Collection< UseCase > useCases) {
		useCase.setModel( new DefaultComboBoxModel(
				useCases.toArray( new UseCase[ 0 ] ) ) );
		
		useCase.setSelectedIndex( -1 ); // Important
		
		if ( useCase.getModel().getSize() == 1 ) {
			useCase.setSelectedIndex( 0 );	
		}
	}

	@SuppressWarnings("unchecked")
	private void drawFlows(final Collection< Flow > flows) {
		flow.setModel( new DefaultComboBoxModel(
				flows.toArray( new Flow[ 0 ] ) ) );

		flow.setSelectedIndex( -1 ); // Important
		
		if ( flow.getModel().getSize() == 1 ) {
			flow.setSelectedIndex( 0 );	
		}		
	}
	
	@SuppressWarnings("unchecked")
	private void drawPostconditions(
			final Collection< Postcondition > postconditions
			) { 
		postcondition.setModel( new DefaultComboBoxModel(
				postconditions.toArray( new Postcondition[ 0 ] ) ) );
	}	
}
