package org.funtester.core.process.testing;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Atomic id generator was implemented as a wrapper for {@link AtomicLong}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class AtomicIdGenerator implements IdGenerator {
	
	private final AtomicLong gen;
	
	public AtomicIdGenerator(final long initialValue) {
		gen = new AtomicLong( initialValue );
	}
	
	/* (non-Javadoc)
	 * @see semantic.strategies.IdGenerator#generate()
	 */
	public long generate() {
		return gen.incrementAndGet();
	}
}
