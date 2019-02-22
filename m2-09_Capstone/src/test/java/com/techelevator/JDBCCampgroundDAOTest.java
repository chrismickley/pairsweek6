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
	JDBCCampgroundDAO testing;
	JdbcTemplate jdbcTemplate;

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
		Long parkId = nextId.getLong(1) + 1;
		nextId = jdbcTemplate.queryForRowSet("SELECT count(*) FROM campground");
		nextId.next();
		Long campGroundId = nextId.getLong(1) + 1;
		jdbcTemplate.execute(
				"INSERT INTO park(park_id, name, location, establish_date, area, visitors, description) VALUES (" +
						parkId + ",'Crazy Park', 'Ohio','1968-01-15', '54321', 9999999, 'Something for description.')");
		jdbcTemplate.execute(
				"INSERT INTO campground(campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee) VALUES(" +
						campGroundId + ", " + parkId + ", 'Some Park', '01', '11', 25.00)");
		jdbcTemplate.execute(
				"INSERT INTO campground(campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee) VALUES(" +
						(campGroundId + 1) + ", " + parkId + ",  'Someother Park', '01', '11', 25.00)");
		List<Campground> campgroundList = testing.listAllCampgrounds(parkId);

		SqlRowSet result = jdbcTemplate.queryForRowSet("SELECT count(*) as total from campground WHERE park_id = ?",
				parkId);
		result.next();
		assertEquals(result.getInt("total"), campgroundList.size());
	}
}
