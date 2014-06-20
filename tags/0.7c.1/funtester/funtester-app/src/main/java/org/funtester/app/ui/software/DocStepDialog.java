package org.funtester.app.ui.software;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.DefaultEditingDialog;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.validation.DocStepValidator;
import org.funtester.common.util.Validator;
import org.funtester.core.profile.Trigger;
import org.funtester.core.software.DocStep;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Dialog for editing a {@link DocStep}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DocStepDialog extends DefaultEditingDialog< DocStep > {

	private static final long serialVersionUID = -7914339646459573688L;
	
	private final JTextField sentence;
	private final JRadioButton system;
	private final JRadioButton actor;

	public DocStepDialog() {
		super();
		
		setIconImage( ImageUtil.loadImage( ImagePath.stepIcon() ) );
		setTitle(Messages.getString("DocStepDialog.this.title")); //$NON-NLS-1$
		setBounds( 100, 100, 596, 161 );
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
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
		
		JLabel lblTrigger = new JLabel(Messages.getString("DocStepDialog.lblTrigger.text")); //$NON-NLS-1$
		contentPanel.add(lblTrigger, "2, 2, right, default");
		
		final ActionListener setFocusAL = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setFocusOnText();
			}
		}; 
		
		actor = new JRadioButton( Messages.getString( "_ACTOR" ) );
		actor.setName("actor");
		actor.addActionListener( setFocusAL );
		contentPanel.add(actor, "4, 2");
		
		system = new JRadioButton( Messages.getString( "_SYSTEM" ) );
		system.setName("system");
		system.addActionListener( setFocusAL );
		contentPanel.add(system, "6, 2");
		
		final ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add( actor );
		buttonGroup.add( system );
		
		JLabel lblSentence = new JLabel(Messages.getString("DocStepDialog.lblSentence.text")); //$NON-NLS-1$
		contentPanel.add(lblSentence, "2, 4, right, default");
		
		sentence = new JTextField();
		sentence.setName( "sentence" );
		contentPanel.add(sentence, "4, 4, 5, 1, fill, default");
		sentence.setColumns(10);
	}

	@Override
	protected DocStep createObject() {
		return new DocStep();
	}
	
	@Override
	protected boolean populateObject() {
		DocStep obj = getObject();
		obj.setSentence( sentence.getText() );
		if ( system.isSelected() ) {
			obj.setTrigger( Trigger.SYSTEM );
		} else {
			obj.setTrigger( Trigger.ACTOR );
		}
		return true;
	}

	@Override
	protected void drawObject(final DocStep obj) {
		sentence.setText( obj.getSentence() );
		if ( obj.trigger().equals( Trigger.SYSTEM ) ) {
			system.setSelected( true );
		} else {
			actor.setSelected( true );
		}
		
		setFocusOnText();
	}
	
	private void setFocusOnText() {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				sentence.requestFocusInWindow();
			}
		} );
	}

	@Override
	protected Validator< DocStep > createValidator() {
		return new DocStepValidator();
	}
}
