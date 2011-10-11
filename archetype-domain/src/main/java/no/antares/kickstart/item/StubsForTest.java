package no.antares.kickstart.item;

import java.text.ParseException;
import java.util.Date;

import no.antares.kickstart.util.DateUtil;


public class StubsForTest {

	public static Item testItem1() {
		return new Item(1, "Tommy");
	}

	public static Date date( String dateOnly ) {
		try {
			return DateUtil.string2date( dateOnly );
		} catch ( ParseException e ) {
			throw new RuntimeException( "Error parsing date " + dateOnly, e );
		}
	}

}
