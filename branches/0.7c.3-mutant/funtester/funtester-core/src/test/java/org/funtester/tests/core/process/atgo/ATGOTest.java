package org.funtester.tests.core.process.atgo;

import org.funtester.common.Importance;
import org.funtester.core.process.atgo.ATGO;
import org.funtester.core.process.atgo.UseCaseOption;
import org.funtester.core.process.atgo.criteria.AllCriteria;
import org.funtester.core.process.atgo.criteria.ImportanceEqualToCriteria;
import org.funtester.core.process.atgo.criteria.ImportanceEqualToOrHigherThanCriteria;
import org.funtester.core.process.atgo.criteria.InvertCriteria;
import org.funtester.core.process.atgo.criteria.NoneCriteria;
import org.funtester.core.software.BasicFlow;
import org.funtester.core.software.Flow;
import org.funtester.core.software.UseCase;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test the {@link ATGO} class.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ATGOTest {
	
	private ATGO atgo;
	
	private ATGO createATGO() {
		UseCase useCase = new UseCase();
		Flow flowA = new BasicFlow();
		flowA.setImportance( Importance.HIGH );
		useCase.addFlow( flowA );
		
		UseCaseOption uco = new UseCaseOption( useCase );
		uco.setSelected( false );
		
		ATGO atgo = new ATGO();
		atgo.addUseCaseOption( uco );
		return atgo;
	}
	
	@BeforeMethod
	private void setUp() {
		atgo = createATGO();
	}
	
	@Test
	public void selectWithImportanceEqualToCorrectly() {
		
		Assert.assertEquals( 0, atgo.selectedUseCaseOptions() );
		
		atgo.select( new ImportanceEqualToCriteria( Importance.HIGH ) );
		
		Assert.assertEquals( 1, atgo.selectedUseCaseOptions() );
	}
	
	
	@Test
	public void selectWithImportanceEqualToOrHigherThanCorrectly() {
		
		Assert.assertEquals( 0, atgo.selectedUseCaseOptions() );
		
		atgo.select( new ImportanceEqualToOrHigherThanCriteria( Importance.MEDIUM ) );
		
		Assert.assertEquals( 1, atgo.selectedUseCaseOptions() );
	}

	
	@Test
	public void selectAllCorreclty() {
		
		Assert.assertEquals( 0, atgo.selectedUseCaseOptions() );
		
		atgo.select( new AllCriteria() );
		
		Assert.assertEquals( 1, atgo.selectedUseCaseOptions() );
	}
	
	@Test
	public void selectNoneCorreclty() {
		
		Assert.assertEquals( 0, atgo.selectedUseCaseOptions() );
		
		atgo.select( new NoneCriteria() );
		
		Assert.assertEquals( 0, atgo.selectedUseCaseOptions() );
	}
	
	
	@Test
	public void selectInvertedCorreclty() {
		
		Assert.assertEquals( 0, atgo.selectedUseCaseOptions() );
		
		atgo.select( new InvertCriteria() );
		
		Assert.assertEquals( 1, atgo.selectedUseCaseOptions() );
		
		atgo.select( new InvertCriteria() );
		
		Assert.assertEquals( 0, atgo.selectedUseCaseOptions() );
	}
}
