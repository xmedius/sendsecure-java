package com.xmedius.sendsecure.helper;

import java.util.Date;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

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

	@Expose
	@SerializedName("destination_type")
	private DestinationType destinationType;
	@Expose
	private String destination;
	@Expose(serialize = false, deserialize = true)
	private boolean verified;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("created_at")
	private Date createdAt;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("updated_at")
	private Date updatedAt;
	@Expose
	private Integer id;
	@Expose(serialize = true, deserialize = false)
	@SerializedName("_destroy")
	private Boolean destroyContact;

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

	public boolean isVerified() {
		return verified;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdateddAt() {
		return updatedAt;
	}

	public Integer getId() {
		return id;
	}

	public void destroyContact() {
		this.destroyContact = true;
	}

}
