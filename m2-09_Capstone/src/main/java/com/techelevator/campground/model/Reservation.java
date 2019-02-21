package com.techelevator.campground.model;

import java.time.LocalDate;

public class Reservation
{
	private Long reservationId;
	private Long siteId;
	private String customerName;
	private LocalDate fromDate;
	private LocalDate toDate;
	private LocalDate createDate;

	public Long getReservationId()
	{
		return reservationId;
	}

	public void setReservationId(Long reservationId)
	{
		this.reservationId = reservationId;
	}

	public Long getSiteId()
	{
		return siteId;
	}

	public void setSiteId(Long siteId)
	{
		this.siteId = siteId;
	}

	public String getCustomerName()
	{
		return customerName;
	}

	public void setCustomerName(String name)
	{
		this.customerName = name;
	}

	public LocalDate getFromDate()
	{
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate)
	{
		this.fromDate = fromDate;
	}

	public LocalDate getToDate()
	{
		return toDate;
	}

	public void setToDate(LocalDate toDate)
	{
		this.toDate = toDate;
	}

	public LocalDate getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(LocalDate createDate)
	{
		this.createDate = createDate;
	}
}
