package org.funtester.core.process.atgo;

import java.util.ArrayList;
import java.util.List;

import org.funtester.common.util.criteria.Criteria;
import org.funtester.core.software.UseCase;

/**
 * Abstract tests generation option
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ATGO {
	
	private String softwareName = "";
	private String testSuiteName = "";
	private List< UseCaseOption > useCaseOptions = new ArrayList< UseCaseOption >();
	
	public ATGO() {
	}

	public String getSoftwareName() {
		return softwareName;
	}

	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}

	public String getTestSuiteName() {
		return testSuiteName;
	}

	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}

	
	public List< UseCaseOption > getUseCaseOptions() {
		return useCaseOptions;
	}

	public void setUseCaseOptions(List< UseCaseOption > useCaseOptions) {
		this.useCaseOptions = useCaseOptions;
	}
	
	/**
	 * Return the option of a use case.
	 * 
	 * @param useCase
	 * @return
	 */
	public UseCaseOption optionOf(UseCase useCase) {
		for ( UseCaseOption o : useCaseOptions ) {
			if ( o.getUseCase().equals( useCase ) ) {
				return o;
			}
		}
		return null;
	}
	
	/**
	 * Return the number of selected {@code UseCaseOption}.
	 * @return
	 */
	public int selectedUseCaseOptions() {
		int count = 0;
		for ( UseCaseOption o : useCaseOptions ) {
			if ( o.isSelected() ) {
				++count;
			}
		}
		return count;
	}

	/**
	 * Add a {@code UseCaseOption}.
	 * 
	 * @param useCaseOption
	 * @return {@code true} if added.
	 */
	public boolean addUseCaseOption(UseCaseOption useCaseOption) {
		return useCaseOptions.add( useCaseOption );
	}

	/**
	 * Select the use case options that matches the given criteria.
	 * 
	 * @param criteria
	 */
	public void select(Criteria< UseCaseOption > criteria) {
		for ( UseCaseOption o : useCaseOptions ) {
			o.setSelected( criteria.matches( o ) );
		}
	}
	
}
