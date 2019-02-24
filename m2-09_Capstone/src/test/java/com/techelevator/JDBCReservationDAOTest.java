package com.techelevator;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Reservation;
import com.techelevator.campground.model.jdbc.JDBCReservationDAO;

public class JDBCReservationDAOTest extends DAOIntegrationTest {

	private JDBCReservationDAO testing;
	private JdbcTemplate jdbcTemplate;
	private DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

	@Before
	public void setUp() throws Exception {
		testing = new JDBCReservationDAO(getDataSource());
	}

	@Test
	public void createReservationTest() throws ParseException {
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

		nextId = jdbcTemplate.queryForRowSet("SELECT count(*) FROM site");
		nextId.next();
		Long nextSiteId = nextId.getLong(1) + 1;

		String customerName = "Jimmy Smith";
		jdbcTemplate.execute(
				"INSERT INTO park(park_id, name, location, establish_date, area, visitors, description) VALUES (" +
						nextParkId +
						", 'Crazy Park', 'Ohio','1986-01-15', '54321', 9999999, 'Something for description.')");
		jdbcTemplate.execute(
				"INSERT INTO campground(campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee) VALUES(" +
						nextCampgroundId + ", " + nextParkId + ", 'Some Camp', '01', '11', 99.00)");
		jdbcTemplate.execute(
				"INSERT INTO site(site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) VALUES(" +
						nextSiteId + ", " + nextCampgroundId + ",  1, 9, false, 11, true)");
	
		testing.createReservation(nextSiteId, customerName, fromDate, toDate);
		
		nextId = jdbcTemplate.queryForRowSet("SELECT MAX(reservation_id) FROM reservation");
		nextId.next();
		Long nextReservationId = nextId.getLong(1);
		
		SqlRowSet checkReservations = jdbcTemplate.queryForRowSet("SELECT * FROM reservation WHERE reservation_id = ?",
				nextReservationId);
		checkReservations.next();
		SqlRowSet checkCreateDate = jdbcTemplate.queryForRowSet("SELECT create_date FROM reservation WHERE reservation_id = ?",
				nextReservationId);
		checkCreateDate.next();
		Reservation newReservation = mapRowToReservation(checkReservations);
		Reservation theReservation = setReservation(nextReservationId, nextSiteId, customerName, fromDate, toDate,
				checkCreateDate.getDate(1));

		assertReservationsAreEqual(theReservation, newReservation);
	}

	private void assertReservationsAreEqual(Reservation expected, Reservation actual) {
		assertEquals(expected.getReservationId(), actual.getReservationId());
		assertEquals(expected.getSiteId(), actual.getSiteId());
		assertEquals(expected.getCustomerName(), actual.getCustomerName());
		assertEquals(expected.getFromDate(), actual.getFromDate());
		assertEquals(expected.getToDate(), actual.getToDate());
		assertEquals(expected.getCreateDate(), actual.getCreateDate());
	}

	private Reservation setReservation(Long reservationId, Long siteId, String customerName, Date fromDate, Date toDate,
			Date createDate) {
		Reservation theReservation;
		theReservation = new Reservation();
		theReservation.setReservationId(reservationId);
		theReservation.setSiteId(siteId);
		theReservation.setCustomerName((customerName));
		theReservation.setFromDate(fromDate);
		theReservation.setToDate(toDate);
		theReservation.setCreateDate(createDate);
		return theReservation;
	}

	private Reservation mapRowToReservation(SqlRowSet results) {
		Reservation theReservation;
		theReservation = new Reservation();
		theReservation.setReservationId(results.getLong("reservation_id"));
		theReservation.setSiteId(results.getLong("site_id"));
		theReservation.setCustomerName(results.getString("name"));
		theReservation.setFromDate(results.getDate("from_date"));
		theReservation.setToDate(results.getDate("to_date"));
		theReservation.setCreateDate(results.getDate("create_date"));
		return theReservation;
	}

}
