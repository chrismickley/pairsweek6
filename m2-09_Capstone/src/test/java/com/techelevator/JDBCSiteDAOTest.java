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

import com.techelevator.campground.model.Site;
import com.techelevator.campground.model.jdbc.JDBCSiteDAO;

public class JDBCSiteDAOTest extends DAOIntegrationTest {

	JDBCSiteDAO testing;
	JdbcTemplate jdbcTemplate;
	DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

	@Before
	public void setUp() throws Exception {
		testing = new JDBCSiteDAO(getDataSource());
	}

	@Test
	public void listAvailableSitesTest() throws Exception {
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
		Long nextSiteIdPlusOne = nextSiteId + 1;

		nextId = jdbcTemplate.queryForRowSet("SELECT count(*) FROM reservation");
		nextId.next();
		Long nextReservationId = nextId.getLong(1) + 1;

		jdbcTemplate.execute(
				"INSERT INTO park(park_id, name, location, establish_date, area, visitors, description) VALUES (" +
						nextParkId +
						", 'Crazy Park', 'Ohio','1986-01-15', '54321', 9999999, 'Something for description.')");
		jdbcTemplate.execute(
				"INSERT INTO campground(campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee) VALUES(" +
						nextCampgroundId + ", " + nextParkId + ", 'Some Park', '01', '11', 25.00)");
		jdbcTemplate.execute(
				"INSERT INTO site(site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) VALUES(" +
						nextSiteId + ", " + nextCampgroundId + ",  1, 6, false, 10, true)");
		jdbcTemplate.execute(
				"INSERT INTO site(site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) VALUES(" +
						nextSiteIdPlusOne + ", " + nextCampgroundId + ",  2, 10, true, 20, false)");
		jdbcTemplate.execute(
				"INSERT INTO reservation(reservation_id, site_id, name, from_date, to_date, create_date) VALUES(" +
						nextReservationId + ", " + nextSiteId +
						",  'Proud Family', '1986-01-15', '1986-01-18', '2019-02-20')");

		Site theSite = setSite(nextSiteId, nextCampgroundId, (long) 1, (long) 6, false, (long) 10, true);

		Site theOtherSite = setSite(nextSiteIdPlusOne, nextCampgroundId, (long) 2, (long) 10, true, (long) 20, false);

		SqlRowSet result = jdbcTemplate.queryForRowSet("SELECT * FROM site WHERE site_id = ?", nextSiteId);
		result.next();

		Site unavailableSite = mapRowToSite(result);

		List<Site> siteList = testing.listAvailableSites(nextCampgroundId, fromDate, toDate);

		// checks if excluded site exists
		assertSitesAreEqual(theSite, unavailableSite);

		// checks if included site is returned
		assertSitesAreEqual(theOtherSite, siteList.get(0));
	}

	private void assertSitesAreEqual(Site expected, Site actual) {
		assertEquals(expected.getSiteId(), actual.getSiteId());
		assertEquals(expected.getCampgroundId(), actual.getCampgroundId());
		assertEquals(expected.getSiteNumber(), actual.getSiteNumber());
		assertEquals(expected.getMaxOccupancy(), actual.getMaxOccupancy());
		assertEquals(expected.isAccessible(), actual.isAccessible());
		assertEquals(expected.getMaxRVLength(), actual.getMaxRVLength());
		assertEquals(expected.isUtilities(), actual.isUtilities());
	}

	private Site mapRowToSite(SqlRowSet results) {
		Site theSite;
		theSite = new Site();
		theSite.setSiteId(results.getLong("site_id"));
		theSite.setCampgroundId(results.getLong("campground_id"));
		theSite.setSiteNumber(results.getLong("site_number"));
		theSite.setMaxOccupancy(results.getLong("max_occupancy"));
		theSite.setAccessible(results.getBoolean("accessible"));
		theSite.setMaxRVLength(results.getLong("max_rv_length"));
		theSite.setUtilities(results.getBoolean("utilities"));
		return theSite;
	}

	private Site setSite(Long nextSiteId, Long nextCampgroundId, Long siteNumber, Long maxOccupancy, boolean accessible,
			Long maxRVLength, boolean utilities) {
		Site theSite;
		theSite = new Site();
		theSite.setSiteId((nextSiteId));
		theSite.setCampgroundId((nextCampgroundId));
		theSite.setSiteNumber((siteNumber));
		theSite.setMaxOccupancy((maxOccupancy));
		theSite.setAccessible((accessible));
		theSite.setMaxRVLength((maxRVLength));
		theSite.setUtilities((utilities));
		return theSite;
	}
}
