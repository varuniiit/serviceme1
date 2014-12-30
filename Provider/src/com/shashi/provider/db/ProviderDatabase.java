package com.shashi.provider.db;

import com.j256.ormlite.field.DatabaseField;

//@Table(name = "WorkerNotification")

public class ProviderDatabase {

	@DatabaseField(generatedId = true, canBeNull = false)
	int id;
	@DatabaseField(canBeNull = true)
	String customerName;
	@DatabaseField(canBeNull = true)
	String timeToService;
	@DatabaseField(canBeNull = true)
	String locationToService;
	@DatabaseField(canBeNull = true)
	String providerAcceptedStatus;
	@DatabaseField(canBeNull = true)
	String customerAcceptedStatus;
	@DatabaseField(canBeNull = true)
	String requestId;
	@DatabaseField(canBeNull = true)
	String installationId;

	public ProviderDatabase() {
		// TODO Auto-generated constructor stub
	}

	public String getInstallationId() {
		return installationId;
	}

	public void setInstallationId(String installationId) {
		this.installationId = installationId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getTimeToService() {
		return timeToService;
	}

	public void setTimeToService(String timeToService) {
		this.timeToService = timeToService;
	}

	public String getLocationToService() {
		return locationToService;
	}

	public void setLocationToService(String locationToService) {
		this.locationToService = locationToService;
	}

	public String getProviderAcceptedStatus() {
		return providerAcceptedStatus;
	}

	public void setProviderAcceptedStatus(String acceptedStatus) {
		this.providerAcceptedStatus = acceptedStatus;
	}

	public String getCustomerAcceptedStatus() {
		return customerAcceptedStatus;
	}

	public void setCustomerAcceptedStatus(String customerAcceptedStatus) {
		this.customerAcceptedStatus = customerAcceptedStatus;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String customerId) {
		this.requestId = customerId;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id=").append(id);
		sb.append(", ").append(customerName);
		sb.append(", ").append(timeToService);
		sb.append(", ").append(locationToService);
		sb.append(", ").append(providerAcceptedStatus);
		return sb.toString();
	}
}
