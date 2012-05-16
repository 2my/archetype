import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;

public class JsonMatcherTest {

   	@Test public void test_1() throws Exception {
		JSONObject actual = new JSONObject( "{'a': [ { 'aa': 'AA1', 'bb': 'BB1' }, { 'aa': 'AA2', 'bb': 'BB2' } ] }" );
    	assertThat( actual, JsonMatcher.matcher( actual ) );
    	assertThat( actual, not( JsonMatcher.matcher( null ) ) );
    	// not supported: assertThat( null, JsonMatcher.matcher( null ) );

		JSONObject same = new JSONObject( "{'a': [ { 'aa': 'AA1', 'bb': 'BB1' }, { 'aa': 'AA2', 'bb': 'BB2' } ] }" );
    	assertThat( actual, JsonMatcher.matcher( same ) );

		JSONObject different = new JSONObject( "{'a': [ { 'aa': 'AA1', 'bb': 'BB1' }, { 'aa': 'DIFFERENT', 'bb': 'BB2' } ] }" );
    	assertThat( actual, JsonMatcher.matcher( different ) );
   	}

}
