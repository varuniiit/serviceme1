package com.shashi.customer.db;

import com.j256.ormlite.field.DatabaseField;

public class RequestIdRecent {
	@DatabaseField(canBeNull = true)
	int requestId = 0;

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

}
