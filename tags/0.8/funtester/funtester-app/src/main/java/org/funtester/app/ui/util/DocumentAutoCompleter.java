package org.funtester.app.ui.util;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

/**
 * Autocomplete the words typed in a text component based on a supplied word
 * list. The word list must be ordered.
 * 
 * This class was inspired on the TextArea example project available at
 * http://docs.oracle.com/javase/tutorial/uiswing/examples/components/TextAreaDemoProject/src/components/TextAreaDemo.java
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DocumentAutoCompleter implements DocumentListener {

	private static enum Mode {
		INSERT, COMPLETION
	};

	private static final String COMMIT_ACTION = "commit";
	
	private Mode mode = Mode.INSERT;
	private int minChars = 2;
	
	private final JTextComponent textField;	
	private final List< String > words = new ArrayList< String >();
	private final boolean caseSensitive;

	private int wordStart = 0;
	private int whereItStarts = 0;
	private String wordToReplace;

	/**
	 * Creates an auto completer.
	 * 
	 * @param field				the text component field.
	 * @param wordList			the word list (do not need to be sorted)
	 * @param caseSensitive		whether the word search is case sensitive (default false).
	 */
	public DocumentAutoCompleter(
			JTextComponent field,
			List< String > wordList,
			final boolean caseSensitive
			) {
		textField = field;
		
		this.words.addAll( wordList );
		if ( caseSensitive )
			Collections.sort( this.words );
		else
			Collections.sort( this.words, createCaseInsensitiveComparator() );
			
		this.caseSensitive = caseSensitive;
		
		registerTo( this.textField );
	}
	
	/**
	 * Creates a case insensitive auto completer.
	 * 
	 * @param field				the text component field.
	 * @param wordList			the word list (do not need to be sorted)
	 */
	public DocumentAutoCompleter(
			JTextComponent field,
			List< String > wordList
			) {
		this( field, wordList, false );
	}
	
	public void registerTo(JTextComponent field) {
		field.getDocument().addDocumentListener( this );
		field.getInputMap().put( KeyStroke.getKeyStroke( "ENTER" ), COMMIT_ACTION );
		field.getActionMap().put( COMMIT_ACTION, new CommitAction() );
	}
	
	public void unregisterTo(JTextComponent field) {
		field.getDocument().removeDocumentListener( this );
		field.getActionMap().remove( COMMIT_ACTION );
		field.getInputMap().remove( KeyStroke.getKeyStroke( "ENTER" ) );
	}
	
	/**
	 * Return the minimum of characters to start completing.
	 * @return
	 */
	public int getMinChars() {
		return minChars;
	}
	
	/**
	 * Define the minimum of characters to start completing.
	 * @param min
	 */
	public void setMinChars(final int min) {
		if ( min >= 1 ) {
			this.minChars = min;
		}
	}

	@Override
	public void insertUpdate(DocumentEvent ev) {
		if ( ev.getLength() != 1 ) {
			return;
		}

		int startOfTheChange = ev.getOffset();
		
		
		final String content;
		try {
			// old (working for JTextField only)
			// content = textField.getText( whereItStarts, startOfTheChange + 1 );
			
			// new version {
			whereItStarts = 0;
			if ( textField instanceof JTextArea ) {
				int lineStart = ( (JTextArea) textField ).getLineOfOffset( startOfTheChange );
				whereItStarts = ( (JTextArea) textField ).getLineStartOffset( lineStart );
				startOfTheChange -= whereItStarts;
			}
			// }
			
			
			content = textField.getText( whereItStarts, startOfTheChange + 1 );
			
		} catch ( BadLocationException e ) {
			e.printStackTrace();
			return;
		}

		// Find where the word starts, that is, whether there is a non character
		// before the startOfTheChange
		int firstNonLetter;
		final char SPACE = ' ';
		for ( firstNonLetter = startOfTheChange; firstNonLetter >= 0; firstNonLetter-- ) {
			//if ( !Character.isLetter( content.charAt( firstNonLetter ) ) ) {
			// mod version
			if ( content.charAt( firstNonLetter ) == SPACE ) {
				break;
			}
		}
		say( "" );
		say( "startOfTheChange: " + startOfTheChange );
		say( "firstNonLetter: " + firstNonLetter );
		
		if ( startOfTheChange - firstNonLetter < this.minChars ) {
			// Too few chars
			return;
		}
		say( "content: " + content );
		
		final String prefix = content.substring( firstNonLetter + 1 );
		say( "prefix: " + prefix );
		
		this.wordStart = firstNonLetter + 1;
		
		
		final int idx = Collections.binarySearch( words, prefix, createCaseInsensitiveComparator() );
		say( "idx: " + idx );
		if ( idx < 0 && -idx <= words.size() ) {
			final String currentWord = words.get( -idx - 1 );
			say( "currentWord: " + currentWord );
			//if ( match.startsWith( prefix ) ) {
			if ( 0 == compareStr( currentWord, prefix, prefix.length(), this.caseSensitive ) ) {
				// A completion is found
				final String completion = currentWord.substring( startOfTheChange - firstNonLetter );
				
				// mod version
				//final String completion = currentWord;
				this.wordToReplace = currentWord;
				
				
				say( "completion: " + completion );
				// We cannot modify Document from within notification,
				// so we submit a task that does the change later
				SwingUtilities.invokeLater(
					 //new CompletionTask( completion, startOfTheChange + 1 ) ); // old (working for JTextField only)
					new CompletionTask( completion, whereItStarts + startOfTheChange + 1 ) );
						
					
			}
		} else {
			// Nothing found
			mode = Mode.INSERT;
		}
	}
	
	Comparator< String > createCaseInsensitiveComparator() {
		return new Comparator< String >() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase( o2 );
			}
		};
				
	}
	
	/**
	 * Tries to compare part two strings.
	 * 
	 * @param first				the first string.
	 * @param second			the second string.
	 * @param length			the length to be compared.
	 * @param isCaseSensitive	whether the comparison is case sensitive.
	 * @return					0, 1 or -1, where
	 * 							0 means "first == second",
	 * 							1 means "first > second" and
	 * 							-1 means "first < second".
	 */
	private int compareStr(
			final String first,
			final String second,
			final int length,
			final boolean isCaseSensitive
			) {
		int max = length;
		if ( max > first.length() ) max = first.length();
		if ( max > second.length() ) max = second.length();
		
		if ( isCaseSensitive )
			return first.substring( 0, max ).compareTo( second.substring( 0, max ) );
		else
			return first.substring( 0, max ).compareToIgnoreCase( second.substring( 0, max ) );
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}
	
	
	private class CompletionTask implements Runnable {
		String completion;
		int position;

		CompletionTask(String completion, int position) {
			this.completion = completion;
			this.position = position;
		}

		public void run() {
			if ( textField instanceof JTextArea ) {
				( (JTextArea) textField ).insert( completion, position );
			} else {
				try {
					( (JTextField) textField ).getDocument().insertString( position, completion, null );
				} catch ( BadLocationException e ) {
					e.printStackTrace();
				}
			}
			final int MAX_CARET_POSITION = textField.getText().length();
			int newPos = position + completion.length();
			if ( newPos > MAX_CARET_POSITION ) newPos = MAX_CARET_POSITION;

			// Select from the end to the beginning of the completion
			textField.setCaretPosition( newPos );
			textField.moveCaretPosition( position );
			mode = Mode.COMPLETION;
		}
	}

	private class CommitAction extends AbstractAction {

		private static final long serialVersionUID = -644430465881510839L;

		public void actionPerformed(ActionEvent ev) {
			if ( mode == Mode.COMPLETION ) {
				/*
				// Just deselect the temporary text
				int pos = textField.getSelectionEnd();
				//textField.insert( " ", pos );
				//textField.setCaretPosition( pos + 1 );
				textField.setCaretPosition( pos );
				mode = Mode.INSERT;
				*/
								
				// mod version
				say( "word start: " + wordStart );
				int pos = textField.getSelectionEnd();
				//int beg = wordStart; // old (working for JTextField only)
				
				int beg = whereItStarts + wordStart;
				
				if ( beg < 0 ) beg = 0;
				if ( beg > pos ) beg = pos;
				textField.setCaretPosition( pos );
				textField.moveCaretPosition( beg );
				textField.replaceSelection( wordToReplace );
				
				mode = Mode.INSERT;
			} else {
				textField.replaceSelection( "\n" );
			}
		}
	}
	
	private void say(final Object obj) {
		//System.out.println( obj );
	}

}
