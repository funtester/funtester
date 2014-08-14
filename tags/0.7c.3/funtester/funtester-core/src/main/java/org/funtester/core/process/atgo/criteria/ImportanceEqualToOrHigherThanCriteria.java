package org.funtester.core.process.atgo.criteria;

import org.funtester.common.Importance;
import org.funtester.common.util.criteria.GivenCriteria;
import org.funtester.core.process.atgo.UseCaseOption;

/**
 * ImportanceEqualToOrHigherThanCriteria
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ImportanceEqualToOrHigherThanCriteria
		extends GivenCriteria< Importance, UseCaseOption  > {

	public ImportanceEqualToOrHigherThanCriteria(final Importance obj) {
		super( obj );
	}

	@Override
	public boolean matches(final UseCaseOption obj) {
		return obj.importanceIs( getObject() )
			|| obj.importanceHigherThan( getObject() );
	}

}
