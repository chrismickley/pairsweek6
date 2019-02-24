package com.techelevator;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Park;

public class ReservationSystemTest extends DAOIntegrationTest {

	ReservationSystem testing;
	JdbcTemplate jdbcTemplate;
	DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

	@Before
	public void setUp() throws Exception {
		testing = new ReservationSystem(getDataSource());
	}

	@Test
	public void calculateCostTest() throws Exception {
		Date fromDate = formatDate.parse("1986-01-16");
		Date toDate = formatDate.parse("1986-01-17");
		jdbcTemplate = new JdbcTemplate(getDataSource());
		SqlRowSet nextId;

		nextId = jdbcTemplate.queryForRowSet("SELECT count(*) FROM park");
		nextId.next();
		Long nextParkId = nextId.getLong(1) + 1;

		nextId = jdbcTemplate.queryForRowSet("SELECT count(*) FROM campground");
		nextId.next();
		Long nextCampgroundId = nextId.getLong(1) + 1;

		jdbcTemplate.execute(
				"INSERT INTO park(park_id, name, location, establish_date, area, visitors, description) VALUES (" +
						nextParkId +
						", 'Crazy Park', 'Ohio','1986-01-15', '54321', 9999999, 'Something for description.')");
		jdbcTemplate.execute(
				"INSERT INTO campground(campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee) VALUES(" +
						nextCampgroundId + ", " + nextParkId + ", 'Some Park', '01', '11', 25.00)");

		assertEquals(new BigDecimal("25.00"), testing.calculateCost(nextCampgroundId, fromDate, toDate));
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
	public void getReservationId() throws Exception {
		Date fromDate = formatDate.parse("1986-01-16");
		Date toDate = formatDate.parse("1986-01-17");
		jdbcTemplate = new JdbcTemplate(getDataSource());
		jdbcTemplate.execute(
				"INSERT INTO reservation(reservation_id, site_id, name, from_date, to_date) VALUES (2100000000, 47, 'Jimmy', '1986-01-16', '1986-01-17')");
		Long resId = (long) 2100000000;
		assertEquals(resId, testing.returnReservationId(47, fromDate, toDate));
	}
}
