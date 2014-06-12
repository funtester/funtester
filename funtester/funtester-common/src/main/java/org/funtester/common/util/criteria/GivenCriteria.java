package org.funtester.common.util.criteria;

public abstract class GivenCriteria< OT, CT > implements Criteria< CT > {

	private final OT object;
	
	public GivenCriteria(final OT obj) {
		this.object = obj;
	}

	public OT getObject() {
		return object;
	}

	public abstract boolean matches(CT obj);
	
}
