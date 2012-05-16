import org.hamcrest.*;

/** Matcher for model objects
 * @author Tommy Skodje
 */
public abstract class PropertyMatcher<T> extends TypeSafeMatcher<T> {
	final String expected;

	public PropertyMatcher(String expectedValue) {
		super();
		this.expected = expectedValue;
	}
	protected abstract String property( T actual );

	@Override
	public boolean matchesSafely(T actual) {
		return this.expected.equalsIgnoreCase( property( actual ) );
	}

	public void describeTo(Description description) {
		description.appendText( expected );
	}

}
