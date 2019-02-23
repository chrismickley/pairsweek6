package com.techelevator.campground.model;

import java.util.Date;

public class Reservation
{
	private Long reservationId;
	private Long siteId;
	private String customerName;
	private Date fromDate;
	private Date toDate;
	private Date createDate;

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

	public Date getFromDate()
	{
		return fromDate;
	}

	public void setFromDate(Date fromDate)
	{
		this.fromDate = fromDate;
	}

	public Date getToDate()
	{
		return toDate;
	}

	public void setToDate(Date toDate)
	{
		this.toDate = toDate;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}
}
