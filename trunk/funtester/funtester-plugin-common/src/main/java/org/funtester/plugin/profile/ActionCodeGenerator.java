package org.funtester.plugin.profile;

import java.util.List;

import org.funtester.common.at.AbstractTestElement;

/**
 * Action code generator
 *
 * @author Thiago Delgado Pinto
 *
 */
public interface ActionCodeGenerator {

	boolean is(final String value);

	String generateCode(final List< AbstractTestElement > elements);

}
