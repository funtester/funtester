package org.funtester.app.ui.software;

import javax.swing.JPanel;

import org.funtester.core.software.ValueConfiguration;

/**
 * Panel for a {@link ValueConfiguration}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public abstract class VCPanel extends JPanel {

	private static final long serialVersionUID = 6611954489708550285L;

	public VCPanel() {}
	
	public abstract ValueConfiguration getValueConfiguration() throws Exception;
	public abstract void setValueConfiguration(ValueConfiguration obj);

}
