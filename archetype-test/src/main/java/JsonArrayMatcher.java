import org.codehaus.jettison.json.*;
import org.hamcrest.*;

/** 
 * Check http://code.google.com/p/jsonmatcher/
 * @author tommy skodje
 *
 */
public class JsonArrayMatcher extends TypeSafeMatcher<JSONArray> {
	final JSONArray expected;
	static final JsonArrayMatcher NULL_MATCHER = new JsonArrayMatcher( null ) {
		@Override public boolean matchesSafely( JSONArray actual ) {
			return (actual == null);
		}
		@Override public void describeTo( Description description ) {
			description.appendText( "null" );
		}
	};

	@Factory public static TypeSafeMatcher<JSONArray> matcher( JSONArray expectedValue ) {
		if ( expectedValue == null )
			return NULL_MATCHER;
		return new JsonArrayMatcher( expectedValue );
	}

	public JsonArrayMatcher( JSONArray expectedValue ) {
		super();
		this.expected = expectedValue;
	}

	@Override public boolean matchesSafely( JSONArray actual ) {
	    if (actual == expected) return true;
	    if (actual == null) return false;

	    if (actual.length() != expected.length()) return false;

	    try {
		    for (int i = 0; i < actual.length(); i++) {
		    	Object actualElem	= actual.get( i );
		    	Object expectedElem	= expected.get( i );
		    	boolean matches	= false;
		    	if ( actualElem instanceof JSONObject )
		    		matches	= JsonMatcher.matcher( (JSONObject) expectedElem ).matchesSafely( (JSONObject) actualElem );
		    	else if ( actualElem instanceof JSONArray )
		    		matches	= matcher( (JSONArray) expectedElem ).matchesSafely( (JSONArray) actualElem );
		    	else
		    		matches	= actualElem.equals( expectedElem );
		    	if ( ! matches )
		    		return false;
		    }
		    return true;
	    } catch ( JSONException je ) {
	    	throw new RuntimeException( je );
	    }
	}

	public void describeTo( Description description ) {
		description.appendText( expected.toString() );
	}

}
