package com.techelevator.campground.model;

import java.util.List;

public interface ParkDAO
{
	public List<Park> listAllParks();

	public Park displayParkInfo(Long parkId);
}
