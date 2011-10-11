package no.antares.kickstart.item;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import no.antares.kickstart.item.Item;
import no.antares.kickstart.item.ItemRepository;
import no.antares.kickstart.item.ItemService;
import no.antares.kickstart.item.StubsForTest;
import no.antares.kickstart.util.DateUtil;

import org.junit.Test;
import org.mockito.Mockito;


public class ItemServiceTest {
	ItemRepository	mock	= Mockito.mock( ItemRepository.class );
	ItemService			sut		= new ItemService( mock );

	@Test public void testFindById() throws Exception {
		Item expected = StubsForTest.testItem1();
		Mockito.when( mock.findById( 1 ) ).thenReturn( expected );

		Item found = sut.findById( 1 );
		assertNotNull( found );
		assertEquals( expected.getName(), found.getName() );
	}

}
