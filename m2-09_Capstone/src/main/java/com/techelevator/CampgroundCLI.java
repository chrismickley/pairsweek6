package com.techelevator;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.view.Menu;

public class CampgroundCLI
{
	private Menu menu;

	public static void main(String[] args)
	{
		Menu menu = new Menu(System.in, System.out);
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource, menu);
		application.run();
	}

	public CampgroundCLI(DataSource datasource, Menu menu)
	{
		// create your DAOs here
		this.menu = menu;
	}

	public void run()
	{
		
	}
}
