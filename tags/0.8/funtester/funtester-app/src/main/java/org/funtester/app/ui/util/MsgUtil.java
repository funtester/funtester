package org.funtester.app.ui.util;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * Message utility.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class MsgUtil {
	
	private MsgUtil() {}

	public static void info(
			final Component parent,
			final String msg,
			final String title
			) {
		JOptionPane.showMessageDialog( parent, msg, title,
				JOptionPane.INFORMATION_MESSAGE );
	}
	
	public static void error(
			final Component parent,
			final String msg,
			final String title
			) {
		JOptionPane.showMessageDialog( parent, msg, title,
				JOptionPane.ERROR_MESSAGE );
	}
	
	public static void warn(
			final Component parent,
			final String msg,
			final String title
			) {
		JOptionPane.showMessageDialog( parent, msg, title,
				JOptionPane.WARNING_MESSAGE );		
	}	
	
	public static boolean yesTo(
			final Component parent,
			final String msg,
			final String title
			) {
		return JOptionPane.showConfirmDialog( parent, msg, title,
				JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION;
	}
	
	public static int answerToYesNoCancel(
			final Component parent,
			final String msg,
			final String title
			) {
		return JOptionPane.showConfirmDialog( parent, msg, title,
				JOptionPane.YES_NO_CANCEL_OPTION );
	}
	
	public static boolean isYes(final int answer) {
		return answer == JOptionPane.YES_OPTION;
	}
	
	public static boolean isNo(final int answer) {
		return answer == JOptionPane.NO_OPTION;
	}
	
	public static boolean isCancel(final int answer) {
		return answer == JOptionPane.CANCEL_OPTION;
	}	
}
