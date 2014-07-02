package org.funtester.common.util;

/**
 * Analyze command-line arguments.
 *
 * @author Thiago Delgado Pinto
 *
 */
public final class ArgUtil {
	
	private ArgUtil() {}

	/**
	 * Return the argument index inside the arguments array.
	 * 
	 * @param args			the application's parameters.
	 * @param argsToCheck	the arguments to check the index.
	 * @return				the argument index or -1 if not found.
	 */
	public static int argumentIndex(
			final String[] args,
			final String[] argsToCheck
			) {
		final String PREFIXES[] = { "-", "--", "/" };
		int i = 0;
		for ( String a : args ) {
			for ( String atc : argsToCheck ) {
				for ( String pre : PREFIXES ) { 
					if ( a.equalsIgnoreCase( pre + atc ) ) {
						return i;
					}
				}
			}
			++i;
		}
		return -1;
	}
}
