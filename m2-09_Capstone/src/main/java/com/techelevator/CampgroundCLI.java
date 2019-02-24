package com.techelevator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.view.Menu;

public class CampgroundCLI {

//	private static final String MAIN_MENU_OPTION_PARKS = "View Parks";
//	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
//	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_OPTION_PARKS, MAIN_MENU_OPTION_EXIT };

	private static final String PARK_VIEW_CAMP = "View Campgrounds";
	private static final String PARK_RETURN_TO_PREVIOUS = "Return to Previous Screen";
	private static final String[] PARK_MENU_OPTIONS = new String[] { PARK_VIEW_CAMP, PARK_RETURN_TO_PREVIOUS };

	private static final String CAMP_SEARCH_RESERVATION = "Search for Available Reservation";
	private static final String CAMP_RETURN_TO_PREVIOUS = "Return to Previous Screen";
	private static final String[] CAMP_MENU_OPTIONS = new String[] { CAMP_SEARCH_RESERVATION, CAMP_RETURN_TO_PREVIOUS };

//	private static final String MENU_OPTION_RETURN_TO_MAIN = "Return to main menu";

	private Menu menu;
	private ReservationSystem reserveSystem;
	private DateFormat formatDate = new SimpleDateFormat("MM/dd/yyyy");

	public static void main(String[] args) {

		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource dataSource) {
		this.menu = new Menu(System.in, System.out);
		reserveSystem = new ReservationSystem(dataSource);
	}

	public void run() {
		while (true) {
//			System.out.println("\n--------------------------------------------------");
//			System.out.println("\n---Main Menu---");
//			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
//			switch (choice) {
//			case MAIN_MENU_OPTION_PARKS:
			parkListScreen();
//			case MAIN_MENU_OPTION_EXIT:
//				System.exit(0);
//			}
		}
	}

	public void parkListScreen() {
		System.out.println("\n--------------------------------------------------");
		System.out.println("\n---View Parks Interface---");
		System.out.println("Select park for further details");

		reserveSystem.listParkNames();

		System.out.println("Q) quit\n");
		String parkSelectIndex = getUserInput("Please choose an option");
		Long parkId = reserveSystem.convertParkIdToLong(parkSelectIndex);
				
		if (parkSelectIndex.equalsIgnoreCase("Q")) {
			System.exit(0);
		} else if (parkSelectIndex.matches("\\d+") && parkId > 0 &&
				parkId <= reserveSystem.listAllParks().size()) {
			
			parkInfoScreen(parkId);
		} else {
			System.out.println("\n*** not a valid option ***\n");
		}
	}

	public void parkInfoScreen(Long parkId) {
		System.out.println("\n--------------------------------------------------");
		System.out.println("\n---Park Information Screen---");
		
		reserveSystem.parkInfoOutput(parkId);
		
		System.out.println("\nSelect a command");
		String choice = (String) menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
		switch (choice) {
		case PARK_VIEW_CAMP:
			campgroundScreen(parkId);
		case PARK_RETURN_TO_PREVIOUS:
			parkListScreen();
		default:
			parkListScreen();
		}
	}

	public void campgroundScreen(Long parkId) {
		System.out.println("\n--------------------------------------------------");
		System.out.println("\nPark Campgrounds");

		reserveSystem.listCampNames(parkId);

		System.out.println("\nSelect a command");
		String choice = (String) menu.getChoiceFromOptions(CAMP_MENU_OPTIONS);
		switch (choice) {
		case CAMP_SEARCH_RESERVATION:
			System.out.println("\n--------------------------------------------------");
			System.out.println("\nSearch for Campground Reservation");

			reserveSystem.listCampNames(parkId);

			System.out.println();
			Date fromDate = null;
			Date toDate = null;

			String campgroundIndex = getUserInput("Which campground (enter 0 to cancel)?");
			if (campgroundIndex.matches("\\d+") && Integer.parseInt(campgroundIndex) == 0) {
				campgroundScreen(parkId);

			} else if (campgroundIndex.matches("\\d+") && Integer.parseInt(campgroundIndex) > 0 && Integer
					.parseInt(campgroundIndex) <= reserveSystem.listAllCampgrounds(parkId).size()) {

			} else {
				System.out.println("\n*** not a valid option ***\n");
				campgroundScreen(parkId);
			}
			try {
				formatDate.setLenient(false);

				String userFromDate = getUserInput("What is the arrival date?");
				fromDate = formatDate.parse(userFromDate);

				String userToDate = getUserInput("What is the departure date?");
				toDate = formatDate.parse(userToDate);

				reserveSystem.listAvailableSites(Integer.parseInt(campgroundIndex), fromDate, toDate, parkId);

			} catch (ParseException e) {
				System.out.println("\n*** not a valid option ***\n");
				campgroundScreen(parkId);
			}
			if (reserveSystem.getSiteList().size() < 1) {
				System.out.println("\n*** no available sites ***\n");
				campgroundScreen(parkId);
			}
			siteSelectionScreen(parkId, fromDate, toDate);

		case CAMP_RETURN_TO_PREVIOUS:
			parkInfoScreen(parkId);

		default:
			campgroundScreen(parkId);
		}
	}

	public void siteSelectionScreen(Long parkId, Date fromDate, Date toDate) {
		System.out.println("\n--------------------------------------------------");
		System.out.println("\nResults Matching Your Search Criteria");

		reserveSystem.listSites(reserveSystem.getSiteList());

		String siteNum = getUserInput("Which site should be reserved (enter 0 to cancel)?");
		Long siteNumValue = Long.parseLong(siteNum);
		
		if (siteNum.matches("\\d+") && siteNumValue == 0) {
			campgroundScreen(parkId);

		} else if (reserveSystem.getSiteNumToId().keySet().contains(siteNumValue)) {

		} else {
			System.out.println("\n*** not a valid option ***\n");
			campgroundScreen(parkId);
		}
		String reservationName = getUserInput("What name should the reservation be made under?");

		reserveSystem.createReservation(siteNumValue, reservationName, fromDate, toDate);

		System.out.println("The reservation has been made and the confirmation id is " +
				reserveSystem.getReservationId(siteNumValue, fromDate, toDate));
	}

	@SuppressWarnings("resource")
	private String getUserInput(String prompt) {
		System.out.print(prompt + " >>> ");
		return new Scanner(System.in).nextLine();
	}
}
