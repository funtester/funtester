package org.funtester.app.ui.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;

/**
 * Clipboard utilities.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class ClipboardUtil {

	private ClipboardUtil() {}

	/**
	 * Copy a text to the clipboard.
	 * 
	 * @param text the text to be copied.
	 */
	public static void copyText(final String text) {
		copyText( text, null );
	}

	/**
	 * Copy a text to the clipboard and specify its owner.
	 * 
	 * @param text	the text to be copied.
	 * @param owner	the clipboard content owner.
	 */
	public static void copyText(final String text, final ClipboardOwner owner) {
		StringSelection selection = new StringSelection( text );
		clipboard().setContents( selection, owner );
	}

	/**
	 * Return a {@link Clipboard} object.
	 * @return
	 */
	public static Clipboard clipboard() {
		return Toolkit.getDefaultToolkit ().getSystemClipboard();
	}
}
