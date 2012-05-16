
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.filter.IColumnFilter;

/** NB! assuming table has columns "DATO" and "SIST_OPPDATERT" - should be more general
 * @author tommysk
*/
public class StdTable {
	public static final StdTable TABLE_NAME	= new StdTable(
			"TABLE_NAME",
			"COL_1, COL_2"
		);


	final String name;
	final String columns;
	final String from;
	final String to;
	final String updated;

	private StdTable( String tableName, String columns ) {
		this( tableName, columns, null, null, null );
	}

	private StdTable( String tableName, String columns, String fromDate, String toDate, String updated ) {
		this.name	= tableName;
		this.columns	= columns;
		this.from	= fromDate;
		this.to	= toDate;
		this.updated	= updated;
	}
	public StdTable from( String date ) {
		return new StdTable( name, columns, date, to, updated );
	}
	public StdTable to( String date ) {
		return new StdTable( name, columns, from, date, updated );
	}
	public StdTable updated_since( String date ) {
		return new StdTable( name, columns, from, to, date );
	}

	public String name() {
		return name;
	}

	public IColumnFilter includedColumnsFilter() {
    	final Collection<String> pks	= Arrays.asList( columns.split( ", " ) );
    	return new IColumnFilter() {
    		@Override public boolean accept( String tableName, Column column ) {
    			return 
    				StringUtils.equals( StdTable.this.name, tableName )
    				&& pks.contains( column.getColumnName().toUpperCase() )
    			;
    		}
    	};
    }

	public String delete() {
		String sql	= String.format( "delete from %s ", name );
		return sql + conditions();
	}

	public String select() {
		String sql	= String.format( "select %s from %s ", columns, name );
		sql	= sql + conditions();
		sql	= sql + String.format( "order by %s ", columns );
		return sql;
	}

	private String conditions() {
		List<String> expressions	= new ArrayList<String>(  );
		if ( ! StringUtils.isBlank( from ) )
			expressions.add( String.format( "'%s' <= DATO ", from ) );
		if ( ! StringUtils.isBlank( to ) )
			expressions.add( String.format( "DATO <= '%s' ", to ) );
		if ( ! StringUtils.isBlank( updated ) )
			expressions.add( String.format( "'%s' <= SIST_OPPDATERT ", updated ) );

		if ( expressions.size() == 0 )
			return "";
		else
			return "where " + StringUtils.join( expressions, "and " );
	}

}
