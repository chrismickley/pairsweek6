package com.techelevator.campground.model;

import java.util.Date;

public interface ReservationDAO
{
	public void createReservation(Long siteId, String customerName, Date fromDate, Date toDate);
}
