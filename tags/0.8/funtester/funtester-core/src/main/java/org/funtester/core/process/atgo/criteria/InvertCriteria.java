package org.funtester.core.process.atgo.criteria;

import org.funtester.common.util.criteria.Criteria;
import org.funtester.core.process.atgo.UseCaseOption;

/**
 * InvertCriteria
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class InvertCriteria implements Criteria< UseCaseOption > {

	@Override
	public boolean matches(final UseCaseOption obj) {
		return ! obj.isSelected();
	}

}
