package com.techelevator;

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

//	private static final String SITE_WHICH_CAMPGROUND = "Which campground (enter 0 to cancel)?";
//	private static final String SITE_ARRIVAL_DATE = "What is the arrival date?";
//	private static final String SITE_DEPARTURE_DATE = "What is the departure date?";
//	private static final String[] SITE_MENU_OPTIONS = new String[] { SITE_WHICH_CAMPGROUND, SITE_ARRIVAL_DATE,
//			SITE_DEPARTURE_DATE };
//
//	private static final String RESERVE_WHICH_SITE = "Which site should be reserved (enter 0 to cancel)?";
//	private static final String RESERVE_NAME = "What name should the reservation be made under";
//	private static final String[] RESERVE_MENU_OPTIONS = new String[] { RESERVE_WHICH_SITE, RESERVE_NAME };

//	private static final String MENU_OPTION_RETURN_TO_MAIN = "Return to main menu";

	private Menu menu;
	private ReservationSystem reserveSystem;

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
//			System.out.println("--------------------------------------------------");
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
		System.out.println("--------------------------------------------------");
		System.out.println("\n---View Parks Interface---");
		System.out.println("Select park for further details");
		reserveSystem.listParkNames();
		System.out.println("Q) quit\n");
		String choice = getUserInput("Please choose an option");
		if (choice.equalsIgnoreCase("Q")) {
			System.exit(0);
		}
		else if (Integer.parseInt(choice) <= reserveSystem.listAllParks().size()) {
			parkInfoScreen(choice);
		} else {
			System.out.println("\n*** "+choice+" is not a valid option ***\n");
		}
	}

	public void parkInfoScreen(String parkId) {
		System.out.println("--------------------------------------------------");
		System.out.println("\n---Park Information Screen---");
		reserveSystem.parkInfoOutput(parkId);
		System.out.println("\nSelect a command");
		String choice = (String) menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
		switch (choice) {
		case PARK_VIEW_CAMP:
			campgroundScreen(parkId);
		case PARK_RETURN_TO_PREVIOUS:
			parkListScreen();
		}
	}

	public void campgroundScreen(String parkId) {
		System.out.println("--------------------------------------------------");
		System.out.println("\nPark Campgrounds\n");
		reserveSystem.listCampNames(parkId);
		System.out.println("\nSelect a command");
		String choice = (String) menu.getChoiceFromOptions(CAMP_MENU_OPTIONS);
		switch (choice) {
		case CAMP_SEARCH_RESERVATION:
			String campgroundId = getUserInput("Which campground (enter 0 to cancel)?");
			if (Integer.parseInt(campgroundId) == 0) {
				parkInfoScreen(parkId);
			}
			String fromDate = getUserInput("What is the arrival date?");
			String toDate = getUserInput("What is the departure date?");
			siteSelectionScreen();
		case CAMP_RETURN_TO_PREVIOUS:
			parkInfoScreen(parkId);
		}
	}

	public void siteSelectionScreen() {
		System.out.println("--------------------------------------------------");
		System.out.println("\nSearch for Campground Reservation\n");
//		String choice = (String) menu.getChoiceFromOptions(SITE_MENU_OPTIONS);
//		switch (choice) {
//		case SITE_WHICH_CAMPGROUND:
//			reservationScreen();
//		case SITE_ARRIVAL_DATE:
//			continue;
//		case SITE_DEPARTURE_DATE:
//			continue;
//		case 0:
//			continue
//		}
	}

	public void reservationScreen() {
		System.out.println("--------------------------------------------------");
		System.out.println("\nResults Matching Your Search Criteria");
//		String choice = (String) menu.getChoiceFromOptions(RESERVE_MENU_OPTIONS);
//		switch (choice) {
//		case RESERVE_WHICH_SITE:
//			continue;
//		case RESERVE_NAME:
//			continue;
//		case 0:
//			continue;
//		}
	}

	@SuppressWarnings("resource")
	private String getUserInput(String prompt) {
		System.out.print(prompt + " >>> ");
		return new Scanner(System.in).nextLine();
	}
}
