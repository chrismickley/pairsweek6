package com.techelevator.campground.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;

public class JDBCCampgroundDAO implements CampgroundDAO {
	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Campground> listAllCampgrounds(Long parkId) {
		List<Campground> campgrounds = new ArrayList<>();
		String name = "SELECT campground_id, name, open_from_mm, open_to_mm, daily_fee FROM campground WHERE park_id = ? ORDER BY name ASC;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(name, parkId);
		while (results.next()) {
			Campground theCampground = mapRowToCampground(results);
			campgrounds.add(theCampground);
		}
		return campgrounds;
	}

	private Campground mapRowToCampground(SqlRowSet results) {
		Campground theCampground;
		theCampground = new Campground();
		theCampground.setCampgroundId(results.getLong("campground_id"));
		theCampground.setName(results.getString("name"));
		theCampground.setOpenFromMM(results.getString("open_from_mm"));
		theCampground.setOpenToMM(results.getString("open_to_mm"));
		theCampground.setDailyFee(results.getBigDecimal("daily_fee"));
		return theCampground;
	}

}
