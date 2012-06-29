

import java.util.*;

import org.codehaus.jettison.json.*;

public class JsonNode {
	final Object parent;
	final Object lookup;
	final Object node;
	public JsonNode(Object parent, Object node, Object lookup) {
		this.parent = parent;
		this.node = node;
		this.lookup = lookup;
	}
	public JsonNode spawn( Object child, Object lookup ) {
		return new JsonNode( node, child, lookup );
	}
	public Object value() {
		return node;
	}

	public void changeTo( Object value ) throws JSONException {
		if ( parent instanceof JSONObject ) {
			JSONObject jo	= (JSONObject) parent;
			String key	= (String) lookup;
			if ( value != null )
				jo.put( key, value );
			else
				jo.remove( key );
			// node	= value;
		} else if ( parent instanceof JSONArray ) {
			JSONArray ja	= (JSONArray) parent;
			Integer index	= (Integer) lookup;
			ja.put( index, value );
			// problematic with null? Cannot remove because then indexe lookup of other Nodes will bug
		}
	}
	public List<JsonNode> child( String name ) throws JSONException {
		List<JsonNode> result	= new ArrayList<JsonNode>();
		if ( node instanceof JSONObject ) {
			JSONObject jo	= (JSONObject) node;
			Object child	= jo.opt( name );
			if ( child != null )
				result.add( spawn( child, name ) );
		}
		return result;
	}
	public List<JsonNode> children() throws JSONException {
		List<JsonNode> result	= new ArrayList<JsonNode>();
		if ( node instanceof JSONObject ) {
			JSONObject jo	= (JSONObject) node;
			Iterator<?> i	= jo.keys();
			while ( i.hasNext() ) {
				String name	= i.next().toString();
				Object child	= jo.get( name );
				result.add( spawn( child, name ) );
			}
		} else if ( node instanceof JSONArray ) {
			JSONArray ja	= (JSONArray) node;
			for (int i = 0; i < ja.length(); i++) {
				Object child	= ja.get( i );
				result.add( spawn( child, i ) );
			}
		}
		return result;
	}
}
