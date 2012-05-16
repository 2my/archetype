import java.util.Iterator;

import org.codehaus.jettison.json.*;
import org.hamcrest.*;

public class JsonMatcher extends TypeSafeMatcher<JSONObject> {
	final JSONObject expected;
	static final JsonMatcher NULL_MATCHER = new JsonMatcher( null ) {
		@Override public boolean matchesSafely( JSONObject actual ) {
			return (actual == null);
		}
		@Override public void describeTo( Description description ) {
			description.appendText( "null" );
		}
	};

	@Factory public static TypeSafeMatcher<JSONObject> matcher( JSONObject expectedValue ) {
		if ( expectedValue == null )
			return NULL_MATCHER;
		return new JsonMatcher( expectedValue );
	}

	public JsonMatcher( JSONObject expectedValue ) {
		super();
		this.expected = expectedValue;
	}

	@Override public boolean matchesSafely( JSONObject actual ) {
	    if (actual == expected) return true;
	    if ( (actual == null) ) return false;

	    try {
		    Iterator<?> i	= actual.keys();
		    while ( i.hasNext() ) {
		    	String key	= (String) i.next();
		    	Object actualValue	= actual.get( key );
		    	Object expectedValue	= expected.get( key );
		    	// System.out.println( key + ", " + expectedValue + ", " + actualValue );
		    	boolean matches	= false;
		    	if ( actualValue instanceof JSONObject )
		    		matches	= matcher( (JSONObject) expectedValue ).matchesSafely( (JSONObject) actualValue );
		    	else if ( actualValue instanceof JSONArray )
		    		matches	= JsonArrayMatcher.matcher( (JSONArray) expectedValue ).matchesSafely( (JSONArray) actualValue  );
		    	else
		    		matches	= actualValue.equals( expectedValue );
		    	if ( ! matches )
		    		return false;
		    }

		    if (actual.length() != expected.length()) return false;

		    return true;
	    } catch ( JSONException je ) {
	    	throw new RuntimeException( je );
	    }
	}

	public void describeTo( Description description ) {
		description.appendText( expected.toString() );
	}

}
