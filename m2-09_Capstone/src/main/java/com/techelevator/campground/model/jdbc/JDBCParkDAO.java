package com.techelevator.campground.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;

public class JDBCParkDAO implements ParkDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Park> listAllParks() {
		List<Park> parks = new ArrayList<>();
		String name = "SELECT park_id, name, location, establish_date, area, visitors, description FROM park ORDER BY name ASC";
		SqlRowSet results = jdbcTemplate.queryForRowSet(name);
		while (results.next()) {
			Park thePark = mapRowToPark(results);
			parks.add(thePark);
		}
		return parks;
	}

	@Override
	public Park displayParkInfo(Long parkId) {
		Park thePark = new Park();
		String name = "SELECT park_id, name, location, establish_date, area, visitors, description FROM park WHERE park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(name, parkId);
		while (results.next()) {
			thePark = mapRowToPark(results);
		}
		return thePark;
	}

	private Park mapRowToPark(SqlRowSet results) {
		Park thePark;
		thePark = new Park();
		thePark.setParkId(results.getLong("park_id"));
		thePark.setName(results.getString("name"));
		thePark.setLocation(results.getString("location"));
		thePark.setEstablishDate(results.getDate("establish_date"));
		thePark.setArea(results.getLong("area"));
		thePark.setVisitors(results.getLong("visitors"));
		thePark.setDescription(results.getString("description"));
		return thePark;
	}

}
