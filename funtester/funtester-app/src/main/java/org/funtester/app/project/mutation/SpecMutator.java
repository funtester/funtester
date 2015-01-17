package org.funtester.app.project.mutation;

import org.funtester.core.software.UseCase;

public interface SpecMutator {

	String mnemonic();

	boolean mutate(UseCase useCase);

}
