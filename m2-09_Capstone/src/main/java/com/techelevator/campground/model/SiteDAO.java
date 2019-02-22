package com.techelevator.campground.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface SiteDAO
{
	public List<Site> listAvailableSites(Long campgroundId, Date fromDate, Date toDate);
}
