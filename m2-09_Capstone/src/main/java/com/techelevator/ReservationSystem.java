package com.techelevator;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class ReservationSystem {

	private ParkDAO park;
	private CampgroundDAO camp;
	private SiteDAO site;
	private ReservationDAO reservation;
	private JdbcTemplate jdbcTemplate;
	private DateFormat formatDate = new SimpleDateFormat("MM/dd/yyyy");
	private BigDecimal siteCost;
	private Long siteId;
	private Long campgroundId;
	private Long parkId;
	private List<Site> siteList = new ArrayList<Site>();
	private Map<Long, Long> siteNumToId = new HashMap<Long, Long>();

	public ReservationSystem(DataSource dataSource) {
		this.park = new JDBCParkDAO(dataSource);
		this.camp = new JDBCCampgroundDAO(dataSource);
		this.site = new JDBCSiteDAO(dataSource);
		this.reservation = new JDBCReservationDAO(dataSource);
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void createReservation(Long siteNum, String customerName, Date fromDate, Date toDate) {
		siteList = site.listAvailableSites(campgroundId, fromDate, toDate);
		
		convertSiteNumToId();
		siteId = siteNumToId.get(siteNum);

		reservation.createReservation(siteId, customerName, fromDate, toDate);
	}

	public List<Site> listAvailableSites(int campgroundIndex, Date fromDate, Date toDate, Long parkId) {
		campgroundId = listAllCampgrounds(parkId).get(campgroundIndex - 1).getCampgroundId();

		siteCost = calculateCost(campgroundId, fromDate, toDate);
		if (siteCost.compareTo(new BigDecimal("0")) == 1) {
			siteList = site.listAvailableSites(campgroundId, fromDate, toDate);
		}
		else {
			siteList = new ArrayList<Site>();
		}
		convertSiteNumToId();
		return siteList;
	}

	public List<Campground> listAllCampgrounds(Long parkId) {
		return camp.listAllCampgrounds(parkId);
	}

	public List<Park> listAllParks() {
		return park.listAllParks();
	}

	public Park displayParkInfo(Long parkId) {
		return park.displayParkInfo(parkId);
	}

	public List<Site> getSiteList() {
		return siteList;
	}

	public Map<Long, Long> getSiteNumToId() {
		return siteNumToId;
	}
	
	public void convertSiteNumToId() {
		for (int i = 0; i < siteList.size(); i++) {
			siteNumToId.put(siteList.get(i).getSiteNumber(), siteList.get(i).getSiteId());
		}
	}
	
	public Long convertParkIdToLong(String parkSelectIndex) {
		parkId = park.listAllParks().get(Integer.parseInt(parkSelectIndex) - 1).getParkId();
		return parkId;
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

	public void parkInfoOutput(Long parkId) {
		Park parkInfo = displayParkInfo(parkId);
		String establishDate = formatDate.format(parkInfo.getEstablishDate());
		String area = String.format("%,d", parkInfo.getArea());
		String visitors = String.format("%,d", parkInfo.getVisitors());
		StringBuilder description = new StringBuilder(parkInfo.getDescription());

		System.out.println(parkInfo.getName() + " National Park");
		System.out.format("%-16s %s %s", "Location:", parkInfo.getLocation(), "\n");
		System.out.format("%-16s %s %s", "Established:", establishDate, "\n");
		System.out.format("%-16s %s %s", "Area:", area + " sq km", "\n");
		System.out.format("%-16s %s %s", "Annual Visitors:", visitors, "\n");
		System.out.println();

		int i = 0;
		while ((i = description.indexOf(" ", i + 80)) != -1) {
			description.replace(i, i + 1, "\n");
		}
		System.out.println(description);
	}

	public void listCampNames(Long parkId) {
//		int parkId = Integer.parseInt(parkSelect);
//		Long parkIdForCamp = Long.parseLong(parkSelect);
		Park parkInfo = displayParkInfo(parkId);
		List<Campground> camps = camp.listAllCampgrounds(parkId);

		Map<String, String> months = new HashMap<String, String>();
		months.put("01", "January");
		months.put("02", "February");
		months.put("03", "March");
		months.put("04", "April");
		months.put("05", "May");
		months.put("06", "June");
		months.put("07", "July");
		months.put("08", "August");
		months.put("09", "September");
		months.put("10", "October");
		months.put("11", "November");
		months.put("12", "December");

		System.out.println(parkInfo.getName() + " National Park Campgrounds\n");
		System.out.format("%-4s %-32s %-11s %-11s %s %s", "", "Name", "Open", "Close", "Daily Fee", "\n");

		if (camps.size() > 0) {
			for (int i = 0; i < camps.size(); i++) {
				System.out.format("%-4s %-32s %-11s %-11s %s %s", "#" + (i + 1), camps.get(i).getName(),
						months.get(camps.get(i).getOpenFromMM()), months.get(camps.get(i).getOpenToMM()),
						"$" + camps.get(i).getDailyFee().setScale(2), "\n");
			}
		} else {
			System.out.println("*** No Results ***");
		}
	}

	public void listSites(List<Site> siteList) {
		List<Site> sites = siteList;
		String utilityNa;
		String maxRvLength;
		String accessible;

		System.out.format("%-11s %-11s %-16s %-16s %-11s %s %s", "Site No.", "Max Occup.", "Accessible?",
				"Max RV Length", "Utility", "Cost", "\n");

		if (sites.size() > 0) {
			for (int i = 0; i < sites.size(); i++) {
				if (sites.get(i).isUtilities()) {
					utilityNa = "Yes";
				} else {
					utilityNa = "N/A";
				}
				if (sites.get(i).getMaxRVLength() == 0) {
					maxRvLength = "N/A";
				} else {
					maxRvLength = sites.get(i).getMaxRVLength().toString();
				}
				if (sites.get(i).isAccessible()) {
					accessible = "Yes";
				} else {
					accessible = "No";
				}
				System.out.format("%-11s %-11s %-16s %-16s %-11s %s %s", sites.get(i).getSiteNumber(),
						sites.get(i).getMaxOccupancy(), accessible, maxRvLength, utilityNa, "$" + siteCost, "\n");
			}
		} else {
			System.out.println("*** No Results ***");
		}
	}

	public Long getReservationId(Long siteNum, Date fromDate, Date toDate) {
		convertSiteNumToId();
		siteId = siteNumToId.get(siteNum);

		SqlRowSet confirmation = jdbcTemplate.queryForRowSet(
				"SELECT reservation_id FROM reservation WHERE site_id = ? AND to_date = ? and from_date = ?", siteId,
				toDate, fromDate);
		confirmation.next();
		return confirmation.getLong(1);
	}

}
