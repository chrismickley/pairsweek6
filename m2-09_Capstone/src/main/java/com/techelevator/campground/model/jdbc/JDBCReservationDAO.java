package com.techelevator.campground.model.jdbc;

import java.util.Date;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.campground.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {
	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void createReservation(Long siteId, String customerName, Date fromDate, Date toDate) {
		String statement = "INSERT INTO reservation(site_id, name, from_date, to_date) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(statement, siteId, customerName, fromDate, toDate);
	}
	
}
