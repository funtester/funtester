package org.funtester.app.i18n;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Control control = ResourceBundle.Control.getControl(
				ResourceBundle.Control.FORMAT_PROPERTIES );
		
		String baseName = "i18n/messages";
		
		ResourceBundle ptBrBundle = ResourceBundle.getBundle(
				baseName,
				new Locale( "pt", "BR" ),
				control
				);
		
		System.out.println( ptBrBundle.getString( "_YES" ) );
		
		
		ResourceBundle enUsBundle = ResourceBundle.getBundle(
				baseName,
				new Locale( "en", "US" ),
				control
				);
		
		System.out.println( enUsBundle.getString( "_YES" ) );
	}

}
