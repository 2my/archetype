package no.antares.kickstart.item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import no.antares.kickstart.item.Item;
import no.antares.kickstart.item.ItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;


/** http://static.springsource.org/spring/docs/3.0.6.RELEASE/spring-framework-reference/html/jdbc.html
 * @author Tommy Skodje
*/
@Repository("itemRepository")
public class ItemRepositoryOracle implements ItemRepository {
  private static final String SQL = 
    "select * from dual"
	;

	protected final TransactionTemplate	txTemplate;
	private final SimpleJdbcTemplate		simpleJdbcTemplate;

	@Autowired public ItemRepositoryOracle( DataSource dataSource) {
		simpleJdbcTemplate = new SimpleJdbcTemplate( dataSource );
		DataSourceTransactionManager manager = new DataSourceTransactionManager( dataSource );
		txTemplate = new TransactionTemplate( manager );
	}

	public Item findById(Integer id) {
		return StubsForTest.testItem1();
		// return this.simpleJdbcTemplate.queryForObject( SQL, createMapper(), id );
	}

	public List< String > itemStatuses() {
		return Arrays.asList( "Succes", "Fail", "Epic success" );
	}

	// @Transactional(readOnly=false, propagation=Propagation.REQUIRES_NEW)
	public void update(final Item i) {
		txTemplate.execute(
			new TransactionCallbackWithoutResult() {
				@Override protected void doInTransactionWithoutResult(TransactionStatus txStatus) {
					updateWithoutTransaction( i );
				}
			}
		);
	}

	/** Need to run without transaction when testing */
	protected void updateWithoutTransaction(Item i) {
		/*
		this.simpleJdbcTemplate.update( SQL, i.getSalesValue(), i.getValuer(), i.getId() );
		this.simpleJdbcTemplate.update( SQL, i.getStatusDate(), i.getId() ); // TODO: Status...
		*/
	}

	private RowMapper< Item > createMapper() {
		return new RowMapper< Item >() {
			public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
				Integer id = rs.getInt( "id" );
				String name = rs.getString( "name" );
				return new Item( id, name );
			}
		};
	}

}
