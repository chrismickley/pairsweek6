package com.techelevator;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.jdbc.JDBCCampgroundDAO;

public class JDBCCampgroundDAOTest extends DAOIntegrationTest {

	private JDBCCampgroundDAO testing;
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() throws Exception {
		testing = new JDBCCampgroundDAO(getDataSource());
	}

	@Test
	public void listAllCampgroundsTest() {
		jdbcTemplate = new JdbcTemplate(getDataSource());
		SqlRowSet nextId;

		nextId = jdbcTemplate.queryForRowSet("SELECT count(*) FROM park");
		nextId.next();
		Long nextParkId = nextId.getLong(1) + 1;

		nextId = jdbcTemplate.queryForRowSet("SELECT count(*) FROM campground");
		nextId.next();
		Long nextCampgroundId = nextId.getLong(1) + 1;
		Long nextCampgroundIdPlusOne = nextCampgroundId + 1;

		jdbcTemplate.execute(
				"INSERT INTO park(park_id, name, location, establish_date, area, visitors, description) VALUES (" +
						nextParkId +
						",'Crazy Park', 'Ohio','1968-01-15', '54321', 9999999, 'Something for description.')");
		jdbcTemplate.execute(
				"INSERT INTO campground(campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee) VALUES(" +
						nextCampgroundId + ", " + nextParkId + ", 'Some Park', '01', '11', 25.00)");
		jdbcTemplate.execute(
				"INSERT INTO campground(campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee) VALUES(" +
						nextCampgroundIdPlusOne + ", " + nextParkId + ",  'Someother Park', '01', '11', 25.00)");

		SqlRowSet result = jdbcTemplate.queryForRowSet("SELECT count(*) as total from campground WHERE park_id = ?",
				nextParkId);
		result.next();

		List<Campground> campgroundList = testing.listAllCampgrounds(nextParkId);

		assertEquals(result.getInt("total"), campgroundList.size());
	}
}
