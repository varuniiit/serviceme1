package com.shashi.customer.db;

import com.j256.ormlite.field.DatabaseField;

public class CustomerDatabase {

	@DatabaseField(generatedId = true, canBeNull = false)
	int id;
	@DatabaseField(canBeNull = true)
	String serviceList;
	@DatabaseField(canBeNull = true)
	String locationToService;
	@DatabaseField(canBeNull = true)
	String timeToService;
	@DatabaseField(canBeNull = true)
	String installationId;
	@DatabaseField(canBeNull = true)
	String requestId;

	public CustomerDatabase() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getServiceList() {
		return serviceList;
	}

	public void setServiceList(String serviceList) {
		this.serviceList = serviceList;
	}

	public String getLocationToService() {
		return locationToService;
	}

	public void setLocationToService(String locationToService) {
		this.locationToService = locationToService;
	}

	public String getTimeToService() {
		return timeToService;
	}

	public void setTimeToService(String timeToService) {
		this.timeToService = timeToService;
	}

	public String getInstallationId() {
		return installationId;
	}

	public void setInstallationId(String installationId) {
		this.installationId = installationId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id=").append(id);
		sb.append(", ").append(serviceList);
		sb.append(", ").append(timeToService);
		sb.append(", ").append(locationToService);
		return sb.toString();
	}
}
