package com.techelevator;

import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.jdbc.JDBCParkDAO;
import com.techelevator.view.Menu;

public class CampgroundCLI {
	private ReservationSystem ressys;
	private static final String MAIN_MENU_OPTION_PARKS = "View Parks";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String SELECT_A_COMMAND_MENU = "Select a command:";
	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_OPTION_PARKS, MAIN_MENU_OPTION_EXIT };
	private static final String PARK_MENU_OPTION_1 = "1";
	private static final String PARK_MENU_OPTION_2 = "2";
	private static final String PARK_MENU_OPTION_3 = "3";
	private static final String[] PARK_MENU_OPTIONS = new String[] { PARK_MENU_OPTION_1, PARK_MENU_OPTION_2,
			PARK_MENU_OPTION_3 };
//change 

	private static final String MENU_OPTION_RETURN_TO_MAIN = "Return to main menu";
	private Menu menu;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		this.menu = new Menu(System.in, System.out);
	}

	public void run() {
		while (true) {
			printStr("Main Menu");
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			switch (choice) {
			case MAIN_MENU_OPTION_PARKS:
				handleListAllParks();
				continue;

			case MAIN_MENU_OPTION_EXIT:
				System.exit(0);
			}
		}
	}

	public void handleParks() {
//		printStr("View Parks Interface");
//		printStr("Select park for further details: ");
//		String choice = (String)menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
//		switch (choice) {
//		case PARK_MENU_1:
//			continue;
//		case PARK_MENU_2:
//			continue;
//		case PARK_MENU_3:
//			continue;
//		}
	}

	public void handleListAllParks() {
		printStr("View Parks Interface");
		printStr("Select park for further details: ");
		
		List<Park> allParks = ressys.listAllParks();
		listParks(allParks);
		String choice = (String) menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
		switch (choice) {
		case PARK_MENU_OPTION_1:
//		continue;
		case PARK_MENU_OPTION_2:
//			continue;
		case PARK_MENU_OPTION_3:
//			continue;
		}

	}

	private void listParks(List<Park> parks) {
		System.out.println();
		if (parks.size() > 0) {
			for (Park park : parks) {
				System.out.println(park.getName());
			}
		} else {
			printStr("\n*** No Results***");
		}
	}

	private void printStr(String text) {
		System.out.println(text);

	}

	@SuppressWarnings("resource")
	private String getUserInput(String prompt) {
		System.out.print(prompt + " >>> ");
		return new Scanner(System.in).nextLine();
	}
}
