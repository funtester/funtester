package org.funtester.plugin.profile;

/**
 * Action recognizer
 *
 * @author Thiago Delgado Pinto
 *
 */
public class ActionRecognizer {

	/**
	 * Check whether the given value is equal to one of the other values.
	 *
	 * @param value
	 * @param values
	 * @return boolean
	 */
	private boolean is(final String value, final String ... values ) {
		for ( String v : values ) {
			if ( v.equalsIgnoreCase( value ) ) {
				return true;
			}
		}
		return false;
	}

	public boolean isAnswerCancel(final String value) {
		return is( value, "answer.cancel" );
	}

	public boolean isAnswerNo(final String value) {
		return is( value, "answer.no" );
	}

	public boolean isAnswerOk(final String value) {
		return  is( value, "answer.ok" );
	}

	public boolean isAnswerRetry(final String value) {
		return is( value, "answer.retry" );
	}

	public boolean isAnswerWithText(final String value) {
		return is( value, "answer.withText" );
	}

	public boolean isAnswerYes(final String value) {
		return is( value, "answer.yes" );
	}

	public boolean isCall(final String value) {
		return is( value, "call" );
	}

	public boolean isCheck(final String value) {
		return is( value, "check" );
	}

	public boolean isClick(final String value) {
		return is( value, "click" );
	}

	public boolean isClose(final String value) {
		return is( value, "close" );
	}

	public boolean isDeselect(final String value) {
		return is( value, "deselect" );
	}

	public boolean isDisable(final String value) {
		return is( value, "disable" );
	}

	public boolean isDoubleClick(final String value) {
		return is( value, "click.double", "double.click", "doubleclick" );
	}

	public boolean isDrag(final String value) {
		return is( value, "drag" );
	}

	public boolean isDrop(final String value) {
		return is( value, "drop" );
	}

	public boolean isEnable(final String value) {
		return is( value, "enable" );
	}

	public boolean isHide(final String value) {
		return is( value, "hide" );
	}

	public boolean isNavigateTo(final String value) {
		return is( value, "navigate.to", "navigate" );
	}

	public boolean isPress(final String value) {
		return is( value, "press" );
	}

	public boolean isSelect(final String value) {
		return is( value, "select" );
	}

	public boolean isSelectFirst(final String value) {
		return is( value, "select.first", "selectFirst" );
	}

	public boolean isSelectItems(final String value) {
		return is( value, "select.items", "selectItems" );
	}

	public boolean isShow(final String value) {
		return is( value, "show" );
	}

	public boolean isType(final String value) {
		return is( value, "type" );
	}

	public boolean isUncheck(final String value) {
		return is( value, "uncheck" );
	}

	public boolean isWait(final String value) {
		return is( value, "wait" );
	}

}
