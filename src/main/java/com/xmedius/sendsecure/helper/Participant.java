package com.xmedius.sendsecure.helper;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class Participant represent the settings a Participant
 */
public class Participant {

	@Expose(serialize = false, deserialize = true)
	private String id;
	@Expose
	private String email;
	@Expose
	@SerializedName("first_name")
	private String firstName;
	@Expose
	@SerializedName("last_name")
	private String lastName;
	@Expose(serialize = false, deserialize = true)
	private String type;
	@Expose(serialize = false, deserialize = true)
	private String role;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("guest_options")
	private GuestOptions guestOptions;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("message_read_count")
	private int messageReadCount;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("message_total_count")
	private int messageTotalCount;

	public Participant(String email) {
    	this.email = email;
        this.guestOptions = new GuestOptions();
	}

	public String getId() {
    	return id;
	}

	public String getType() {
		return type;
	}

	public String getRole() {
		return role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public GuestOptions getGuestOptions() {
		return guestOptions;
	}

	public void setGuestOptions(GuestOptions guestOptions) {
		this.guestOptions = guestOptions;
	}

	public void addContactMethod(ContactMethod contact) {
		this.guestOptions.getContactMethods().add(contact);
	}

	public int getMessageReadCount() {
		return messageReadCount;
	}

	public int getMessageTotalCount() {
		return messageTotalCount;
	}

	public void prepareToDestroyContactMethods(List<Integer> contactMethodIds) {
		for(ContactMethod contactMethod: this.guestOptions.getContactMethods()) {
			if(contactMethodIds.contains(contactMethod.getId())) {
				contactMethod.destroyContact();
			}
		}
	}
}
