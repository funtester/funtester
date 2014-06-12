package org.funtester.plugin.code.java;

/**
 * Contains some useful methods to generate java code.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class JavaSyntax {

	/** Semicolon */
	public final String SC			= ";";
	/** End line */
	public final String ENDL		= "\n";
	/** Semicolon plus end line */
	public final String SCE			= SC + ENDL;
	public final String BLOCK_START	= "{";
	public final String BLOCK_END	= "}";
	/** Parentheses */
	public final String PAR			= "()";
	public final String DOT			= ".";
	public final String COMMA		= ",";
	/** Space */
	public final String SPC			= " ";
	public final String QUOTE		= "\"";
	
	private int level = 0;	
	
	public JavaSyntax() {
	}
	
	public String gen(final String content) {
		return levelStr() + content;
	}

	public String genPackage(final String name) {
		if ( name.isEmpty() ) {
			return "";
		}
		return "package " + name + SCE;
	}

	public String genImportStatic(final String libraryName) {
		return "import static " + libraryName + SCE;
	}
	
	public String genImport(final String libraryName) {
		return "import " + libraryName + SCE;
	}
	
	public String genClassStart(final String name) {
		String text = levelStr() + "public class " + name + SPC + BLOCK_START + ENDL;
		increaseLevel();
		return text;
	}
	
	public String genClassEnd() {
		return genBlockEnd();
	}
	
	public String genMethodEnd() {
		return genBlockEnd();
	}
	
	public String genBlockEnd() {		
		decreaseLevel();
		return levelStr() + BLOCK_END + ENDL;
	}
	
	public String genField(final String className, final String fieldName) {
		return levelStr() + "public " + className + SPC + fieldName + SCE;
	}

	public String genTestConfigurationMethodStart(
			final String annotation,
			final String name
			) {
		String text = levelStr() + annotation + ENDL +
			levelStr() + "public void " + name + PAR + SPC + BLOCK_START + ENDL;
		increaseLevel();
		return text;
	}
	
	public String genTestMethodStart(
			final String annotation,
			final String name
			) {
		String text = levelStr() + annotation + ENDL +
			levelStr() + "public void " + name + PAR + SPC + BLOCK_START + ENDL;
		increaseLevel();
		return text;
	}
	
	public String genMethodStart(final String returnType, final String name) {
		String text = levelStr() + "public " + returnType + SPC + name + PAR + SPC + BLOCK_START + ENDL;
		increaseLevel();
		return text;
	}
	
	public String genAttr(final String var, final String content) {
		return levelStr() + var + " = " + content;
	}
	
	public String genNew(final String clazz, final String param) {
		if ( param.isEmpty() ) {
			return "new " + clazz + "()";
		}
		return "new " + clazz + "( " + param + " )";
	}
	
	public String genReturn(final String whatToReturn) {
		return levelStr() + "return " + whatToReturn;
	}
	
	public String genCallWithoutArgs(final String object, final String method) {
		return levelStr() + object + DOT + method + PAR;
	}
	
	public String genCallWithArgs(final String object, final String method) {
		return levelStr() + object + DOT + method;
	}
	
	public String genComment(String text) {
		return levelStr() + "// " + text + ENDL;
	}
	
	public String genBlockCommentStart() {
		return levelStr() + "/**" + ENDL;
	}
	
	public String genBlockComment(String text) {
		return  levelStr() + " * " +  text + ENDL;
	}
	
	public String genBlockCommentEnd() {
		return levelStr() + " */" + ENDL;
	}

	public String levelStr() {
		StringBuilder b = new StringBuilder();
		for ( int i = 0; ( i < level ); ++i) {
			b.append( "\t" );
		}
		return b.toString();
	}
	
	public void increaseLevel() {
		level ++;
	}
	
	public void decreaseLevel() {
		level--;
	}		
	
}
