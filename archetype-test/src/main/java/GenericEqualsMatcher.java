import org.hamcrest.*;

/** Matcher for model objects
 * @author Tommy Skodje
 */
class GenericEqualsMatcher<T> extends TypeSafeMatcher<T> {
	final T expected;

	public GenericEqualsMatcher(T expectedValue) {
		super();
		this.expected = expectedValue;
	}

	@Override
	public boolean matchesSafely(T actual) {
		return this.expected.equals( actual );
	}

	public void describeTo(Description description) {
		description.appendText( expected.toString() );
	}

}
