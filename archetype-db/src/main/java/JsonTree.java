

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * @author tommy skodje
*/
public 	class JsonTree {
	final JSONObject json;
	final List<String> cursor	= new ArrayList<String>();

	public static JsonTree fromFile( File source ) throws IOException, JSONException {
		Validate.notNull( source );
		Validate.isTrue( source.exists() );
		String text	= FileUtils.readFileToString( source, "UTF-8" );
		return new JsonTree( text );
	}

	public JsonTree( JSONObject json ) {
		Validate.notNull( json );
		this.json = json;
	}

	public JsonTree( String text ) throws JSONException {
		Validate.notNull( text );
		this.json = new JSONObject( text );
	}

	/** Changes value under cursor */
	public JsonTree changeValueTo( Object newValue ) throws JSONException {
		for ( JsonNode node: match() )
			node.changeTo( newValue );
		return this.resetCursor();
	}

	public JsonTree changeValue( Change change ) throws JSONException {
		for ( JsonNode node: match() )
			node.changeTo( change.changedValue( node.value() ) );
		return this.resetCursor();
	}

	public List<Object> values() throws JSONException {
		List<Object> values	= new ArrayList<Object>();
		for ( JsonNode node: match() )
			values.add( node.value() );
		this.resetCursor();
		return values;
	}

	public static RegExpChange replace2( String regExp ) {
		return new RegExpChange( regExp, null );
	}
	public static ChangeBuilder replace( final String regExp ) {
		return new ChangeBuilder() {
			public Change with( Object replacement ) {
				final String replamentString	= (String) replacement;
				return new Change() {
					public Object changedValue( Object oldValue ) {
						if ( oldValue != null )	// could check for String
							return oldValue.toString().replaceAll( regExp, replamentString );
						return null;
					}
				};
			}
		};
	}
	interface ChangeBuilder {
		public Change with( Object nextParam );
	}
	static class RegExpChange implements Change {
		final String regExp;
		final String replacement;

		public RegExpChange( String regExp, String replacement ) {
			this.regExp	= regExp;
			this.replacement	= replacement;
		}
		public Object changedValue( Object oldValue ) {
			if ( oldValue != null )	// could check for String
				return oldValue.toString().replaceAll( regExp, replacement );
			return null;
		}
		public Change with( String replacement ) {
			if ( replacement == null )
				replacement	= "";
			return new RegExpChange( regExp, replacement );
		}
	}

	public static Change prepend( final String prefix ) {
		return new Change() {
			public Object changedValue( Object oldValue ) {
				if ( oldValue != null )	// could check for String
					return prefix + oldValue.toString();
				return prefix;
			}
		};
	}
	public static Change map( final Map<?,?> lookup ) {
		return new Change() {
			public Object changedValue( Object oldValue ) {
				return lookup.get( oldValue );
			}
		};
	}
	public static Change incrementalFrom( final long start ) {
		return new Change() {
			long current	= start;
			public Object changedValue( Object oldValue ) {
				return Long.valueOf( current++ );
			}
		};
	}
	private static final String NULL_STRING	= JSONObject.NULL.toString();
	public static Change removeNulls() {
		return new Change() {
			public Object changedValue( Object oldValue ) {
				if ( ( oldValue == null ) || JSONObject.NULL.equals( oldValue ) || NULL_STRING.equalsIgnoreCase( oldValue.toString().trim() ) )
					return null;
				return oldValue;
			}
		};
	}

	public static Change remove() {
		return new Change() {
			public Object changedValue( Object oldValue ) {
				return null;
			}
		};
	}

	interface Change {
		public Object changedValue( Object oldValue );
	}

	private List<JsonNode> match() throws JSONException {
		List<JsonNode> matches	= new ArrayList<JsonNode>();
		matches.add( new JsonNode( json, json, 0 ) );
		for ( String part: cursor ) {
			List<JsonNode> curr	= new ArrayList<JsonNode>();
			for ( JsonNode o: matches ) {
				if ( "*".equals( part ) )
					curr.addAll( o.children() );
				else
					curr.addAll( o.child( part ) );
			}
			matches	= curr;
		}
		return matches;
	}
	public JsonTree resetCursor() {
		cursor.clear();
		return this;
	}
	public JsonTree child( String name ) {
		cursor.add( name );
		return this;
	}
	public JsonTree match( String path ) {
		for ( String part: path.split( "/" ) )
			cursor.add( part );
		return this;
	}
	public JsonTree children() {
		cursor.add( "*" );
		return this;
	}

	public JsonTree toFile( File target ) throws IOException, JSONException {
		FileUtils.writeStringToFile( target, toString(), "UTF-8" );
		return this;
	}

	public String toString() {
		try {
			return json.toString( 2 );
		} catch ( JSONException e ) {
			throw new RuntimeException( e );
		}
	}



}
