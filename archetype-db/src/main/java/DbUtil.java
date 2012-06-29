

import java.io.File;

import no.antares.dbunit.*;
import no.antares.dbunit.converters.AlphaRemover;

/** A utility to export from / import to database
 * @author tommy skodje
*/
public class DbUtil {

	/** Quick export to json files */
    public final static void main( String[] args ) throws Exception {
		String driverClass	= "net.sourceforge.jtds.jdbc.Driver";
		String url	= "jdbc:jtds:sybase://URL";
		Db db	= new DbDataSource( driverClass, url, "user", "password", "" );
		// db.addDbUnitProperty( DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MsSqlDataTypeFactory() );
		DbWrapper dbWrapper	= new DbWrapper( db );

		if ( (0 < args.length) && "import".equalsIgnoreCase( args[0] ) ) {
			JsonDataSet jSet	= new JsonDataSet( JsonTree.fromFile( new File( "QUEUE_MSG.json" ) ).toString(), new AlphaRemover() );
			dbWrapper.refreshWithFlatJSON( jSet.wrap() );
			return;
		}

    	String sql	= "select * from TABLE where MSG_STATUS = 0";
		JsonTree json	= new JsonTree( dbWrapper.extractFlatJson( "QUEUE_MSG",  sql ) );
		json.toFile( new File( "QUEUE_MSG.json" ) );
	}


}
