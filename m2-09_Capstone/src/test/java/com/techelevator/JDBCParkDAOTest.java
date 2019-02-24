package com.techelevator;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.jdbc.JDBCParkDAO;

public class JDBCParkDAOTest extends DAOIntegrationTest {
	
	private JDBCParkDAO testing;
	private JdbcTemplate jdbcTemplate;
	private DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

	@Before
	public void setUp() throws Exception {
		testing = new JDBCParkDAO(getDataSource());
	}

	@Test
	public void listAllParksTest() {
		jdbcTemplate = new JdbcTemplate(getDataSource());

		SqlRowSet result = jdbcTemplate.queryForRowSet("SELECT count(*) as total from park");
		result.next();

		List<Park> parkList = testing.listAllParks();

		assertEquals(result.getInt("total"), parkList.size());
	}

	@Test
	public void displayParkInfoTest() throws Exception {
		Date establishDate = formatDate.parse("1968-01-15");
		jdbcTemplate = new JdbcTemplate(getDataSource());

		SqlRowSet nextId = jdbcTemplate.queryForRowSet("SELECT count(*) FROM park");
		nextId.next();

		Long nextParkId = nextId.getLong(1) + 1;

		String statement = "INSERT INTO park(park_id, name, location, establish_date, area, visitors, description) VALUES (" +
				nextParkId + ", 'Crazy Park', 'Ohio','1968-01-15', 54321, 9999999, 'Something for description.')";
		jdbcTemplate.execute(statement);

		Park thePark = new Park();
		thePark.setParkId(nextParkId);
		thePark.setName("Crazy Park");
		thePark.setLocation("Ohio");
		thePark.setEstablishDate(establishDate);
		thePark.setArea((long) 54321);
		thePark.setVisitors((long) 9999999);
		thePark.setDescription("Something for description.");

		assertParksAreEqual(thePark, testing.displayParkInfo(nextParkId));
	}

	private void assertParksAreEqual(Park expected, Park actual) {
		assertEquals(expected.getParkId(), actual.getParkId());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getLocation(), actual.getLocation());
		assertEquals(expected.getEstablishDate(), actual.getEstablishDate());
		assertEquals(expected.getArea(), actual.getArea());
		assertEquals(expected.getVisitors(), actual.getVisitors());
		assertEquals(expected.getDescription(), actual.getDescription());
	}
}
