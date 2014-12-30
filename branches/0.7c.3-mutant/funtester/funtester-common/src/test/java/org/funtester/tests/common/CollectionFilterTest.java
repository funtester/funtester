package org.funtester.tests.common;

import java.util.ArrayList;
import java.util.List;

import org.funtester.common.util.CollectionFilter;
import org.funtester.common.util.criteria.Criteria;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test the {@link CollectionFilter} class.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class CollectionFilterTest {
	
	private final Criteria< Integer > evenNumber = new Criteria< Integer >() {
		@Override
		public boolean matches(Integer obj) {
			return obj % 2 == 0;
		}
	};
	
	private List< Integer > items;
	
	@BeforeMethod
	public void setUp() {
		items = new ArrayList< Integer >();
		items.add( 1 );
		items.add( 2 );
		items.add( 3 );
		items.add( 4 );
		items.add( 5 );
	}
	
	@Test
	public void filterCorrectly() {
		List< Integer > even = ( new CollectionFilter() ).filter( items, evenNumber );
		Assert.assertTrue( even.get( 0 ).equals( 2 ) );
		Assert.assertTrue( even.get( 1 ).equals( 4 ) );
	}
	
	@Test
	public void findFirstCorrectly() {
		Integer obj = ( new CollectionFilter() ).first( items, evenNumber );
		Assert.assertTrue( obj.equals( 2 ) );
	}

}
