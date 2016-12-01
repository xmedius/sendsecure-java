package com.xmedius.sendsecure.helper;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Recipient {

	private String email;
	@SerializedName("first_name")
	private String firstName;
	@SerializedName("last_name")
	private String lastName;
	@SerializedName("company_name")
	private String companyName;
	@SerializedName("contact_methods")
	private List<ContactMethod> contactMethods;

	public Recipient(String email) {
		this.email = email;
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public List<ContactMethod> getContactMethods() {
		return contactMethods;
	}

	public void setContactMethods(List<ContactMethod> contactMethods) {
		this.contactMethods = contactMethods;
	}
}
