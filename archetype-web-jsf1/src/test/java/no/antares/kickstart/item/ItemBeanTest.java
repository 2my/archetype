package no.antares.kickstart.item;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import no.antares.kickstart.item.ItemBean;
import no.antares.kickstart.item.Item;
import no.antares.kickstart.item.ItemService;
import no.antares.kickstart.item.StubsForTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class ItemBeanTest {

	final ItemService itemService = Mockito.mock(ItemService.class);
	final ItemBean	itemBean	= new ItemBean();

	@Before public void setUp() throws Exception {
		itemBean.setItemService( itemService );
	}

	@Test public void testSelecting() throws Exception {
		itemBean.setSelectedId( null );
		assertNull( itemBean.getSelectedId() );

		Item toReturn = StubsForTest.testItem1();
		Mockito.when( itemService.findById( 1 ) ).thenReturn( toReturn );
		itemBean.setSelectedId( 1 );
		assertNotNull( itemBean.getSelectedId() );
		assertEquals( toReturn.getName(), itemBean.getSelectedItem().getName() );

		Mockito.when( itemService.findById( -334455 ) ).thenReturn( null );
		itemBean.setSelectedId( -334455 );
		assertNull( itemBean.getSelectedItem() );
	}

	@Test public void testUpdating() throws Exception {
		Item toReturn = StubsForTest.testItem1();
		Mockito.when( itemService.findById( 1 ) ).thenReturn( toReturn );

		itemBean.setName( "Test name" );
		assertEquals( "Test name", itemBean.getName() );
	}
}
