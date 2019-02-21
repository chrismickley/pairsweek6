package com.techelevator.campground.model;

public interface ReservationDAO
{
	public void createReservation(Long siteId, String customerName);
}
