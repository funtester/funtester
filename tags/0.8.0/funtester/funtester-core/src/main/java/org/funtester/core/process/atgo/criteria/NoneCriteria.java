package org.funtester.core.process.atgo.criteria;

import org.funtester.common.util.criteria.Criteria;
import org.funtester.core.process.atgo.UseCaseOption;

/**
 * NoneCriteria
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class NoneCriteria implements Criteria< UseCaseOption > {

	@Override
	public boolean matches(final UseCaseOption obj) {
		return false;
	}

}
