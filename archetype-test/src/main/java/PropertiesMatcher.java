import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.*;

/** Matcher for model objects
 * @author Tommy Skodje
 */
public abstract class PropertiesMatcher<T> extends TypeSafeMatcher<T> {
	final Object[] expected;

	public PropertiesMatcher(Object[] expectedValue) {
		super();
		this.expected = expectedValue;
	}
	protected abstract Object[] properties( T actual );

	@Override
	public boolean matchesSafely(T actual) {
		Object[] actuals	= properties( actual );
		return ArrayUtils.isEquals( expected, actuals );
		/*
		if ( expected.length != actuals.length )
			return false;
		for (int i = 0; i < actuals.length; i++) {
			if ( actuals[i] != null ) {
				if ( ! actuals[i].equals( expected[i] ) )
					return false;
			} else
				if ( expected[i] != null )
					return false;
		}
		return true;*/
	}

	public void describeTo(Description description) {
		String msg	= StringUtils.join( expected, "," );
		description.appendText( msg );
	}

}
