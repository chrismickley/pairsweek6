package com.techelevator.campground.model.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Site;
import com.techelevator.campground.model.SiteDAO;

public class JDBCSiteDAO implements SiteDAO {
	private JdbcTemplate jdbcTemplate;

	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<Site> listAvailableSites(Long campgroundId, Date fromDate, Date toDate) {
		List<Site> siteList = new ArrayList<>();
		String name = "SELECT DISTINCT * FROM site " +
				"JOIN campground ON site.campground_id = campground.campground_id " + "WHERE site.campground_id = ? " +
				"AND site_id NOT IN " + "(SELECT site.site_id FROM site " +
				"JOIN reservation ON reservation.site_id = site.site_id " +
				"WHERE (? > reservation.from_date AND ? < reservation.to_date) OR (? > reservation.from_date AND ? < reservation.to_date)) " +
				"ORDER BY daily_fee " + "LIMIT 5;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(name, campgroundId, fromDate, fromDate, toDate, toDate);
		while (results.next()) {
			Site theSite = mapRowToSite(results);
			siteList.add(theSite);
		}
		return siteList;
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

}
