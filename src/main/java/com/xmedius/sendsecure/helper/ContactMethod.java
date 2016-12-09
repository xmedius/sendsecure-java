package com.xmedius.sendsecure.helper;

import com.google.gson.annotations.SerializedName;

/**
 * Class ContactMethod builds an object to create a phone number destination
 */
public class ContactMethod {

	public enum DestinationType
	{
		@SerializedName("home_phone") HOME_PHONE,
		@SerializedName("cell_phone") CELL_PHONE,
		@SerializedName("office_phone") OFFICE_PHONE,
		@SerializedName("other_phone") OTHER_PHONE
	}

	public ContactMethod(String destination, DestinationType destinationType) {
		this.destination = destination;
		this.destinationType = destinationType;
	}

	@SerializedName("destination_type")
	private DestinationType destinationType;
	private String destination;

	public DestinationType getDestinationType() {
		return destinationType;
	}

	public void setDestinationType(DestinationType destinationType) {
		this.destinationType = destinationType;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
}
