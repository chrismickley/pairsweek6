package com.techelevator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;
import com.techelevator.campground.model.ReservationDAO;
import com.techelevator.campground.model.Site;
import com.techelevator.campground.model.SiteDAO;
import com.techelevator.campground.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.campground.model.jdbc.JDBCParkDAO;
import com.techelevator.campground.model.jdbc.JDBCReservationDAO;
import com.techelevator.campground.model.jdbc.JDBCSiteDAO;

public class ReservationSystem implements ParkDAO, CampgroundDAO, SiteDAO, ReservationDAO {

	private JDBCParkDAO park;
	private JDBCCampgroundDAO camp;
	private JDBCSiteDAO site;
	private JDBCReservationDAO reservation;
	private JdbcTemplate jdbcTemplate;

	public ReservationSystem(DataSource dataSource) {
		this.park = new JDBCParkDAO(dataSource);
		this.camp = new JDBCCampgroundDAO(dataSource);
		this.site = new JDBCSiteDAO(dataSource);
		this.reservation = new JDBCReservationDAO(dataSource);
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void createReservation(Long siteId, String customerName, Date fromDate, Date toDate) {
		reservation.createReservation(siteId, customerName, fromDate, toDate);
	}

	@Override
	public List<Site> listAvailableSites(Long campgroundId, Date fromDate, Date toDate) {
		return site.listAvailableSites(campgroundId, fromDate, toDate);
	}

	@Override
	public List<Campground> listAllCampgrounds(Long parkId) {
		return camp.listAllCampgrounds(parkId);
	}

	@Override
	public List<Park> listAllParks() {
		return park.listAllParks();
	}

	@Override
	public Park displayParkInfo(Long parkId) {
		return park.displayParkInfo(parkId);
	}

	public BigDecimal calculateCost(Long campgroundId, Date fromDate, Date toDate) {
		BigDecimal cost;
		
		Long dateRange = (toDate.getTime() - fromDate.getTime()) / 86400000;
		
		SqlRowSet fee = jdbcTemplate.queryForRowSet("SELECT daily_fee FROM campground WHERE campground_id = ?",
				campgroundId);
		fee.next();
		
		cost = fee.getBigDecimal(1).multiply(new BigDecimal(dateRange));
		
		return cost.setScale(2);
	}

	public void listParkNames() {
		List<Park> parks = listAllParks();
		System.out.println();
		
		if (parks.size() > 0) {
			for (int i = 0; i < parks.size(); i++) {
				System.out.println((i + 1) + ") " + parks.get(i).getName());
			}
		} else {
			System.out.println("*** No Results ***");
		}
	}

	public void parkInfoOutput(String parkSelect) {
		Long parkId = Long.parseLong(parkSelect);
		Park parkInfo = displayParkInfo(parkId);
		
		System.out.println(parkInfo.getName() + " National Park");
		System.out.format("%-16s %s %s", "Location:", parkInfo.getLocation(), "\n");
		System.out.format("%-16s %s %s", "Established:", parkInfo.getEstablishDate(), "\n");
		System.out.format("%-16s %s %s", "Area:", parkInfo.getArea(), "\n");
		System.out.format("%-16s %s %s", "Annual Visitors:", parkInfo.getVisitors(), "\n");
		System.out.println();
		System.out.format("%-16s %s", parkInfo.getDescription(), "\n");
	}

	public void listCampNames(String parkSelect) {
		Long parkId = Long.parseLong(parkSelect);
		Park parkInfo = displayParkInfo(parkId);
		List<Campground> camps = camp.listAllCampgrounds(parkId);
		
		System.out.println(parkInfo.getName() + " National Park Campgrounds\n");
		System.out.format("%-4s %-32s %-11s %-11s %s %s", "", "Name", "Open", "Close", "Daily Fee", "\n");

		if (camps.size() > 0) {
			for (int i = 0; i < camps.size(); i++) {
				System.out.format("%-4s %-32s %-11s %-11s %s %s", "#" + (i + 1), camps.get(i).getName(),
						camps.get(i).getOpenFromMM(), camps.get(i).getOpenToMM(),
						"$" + camps.get(i).getDailyFee().setScale(2), "\n");
			}
		} else {
			System.out.println("*** No Results ***");
		}
	}

}
