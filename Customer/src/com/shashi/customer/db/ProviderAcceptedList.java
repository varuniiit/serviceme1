package com.shashi.customer.db;

import com.j256.ormlite.field.DatabaseField;

public class ProviderAcceptedList {

	@DatabaseField(canBeNull = false, generatedId = true)
	int id;
	@DatabaseField(canBeNull = true)
	String providerName;
	@DatabaseField(canBeNull = true)
	String customerAcceptedStatus;
	@DatabaseField(canBeNull = true)
	String requestId;
	@DatabaseField(canBeNull = true)
	String installationId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
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

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getInstallationId() {
		return installationId;
	}

	public void setInstallationId(String installationId) {
		this.installationId = installationId;
	}

}
