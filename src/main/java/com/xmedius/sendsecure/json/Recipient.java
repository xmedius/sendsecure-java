package com.xmedius.sendsecure.json;

import com.google.gson.annotations.SerializedName;

/**
 * Class Recipient builds an object containing the informations of an existing recipient that matches the autocomplete search term.
 */
public class Recipient {

	private int id;
	private String type;
	@SerializedName("first_name")
	private String firstName;
	@SerializedName("last_name")
	private String lastName;
	private String email;
	@SerializedName("company_name")
	private String companyName;

	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getCompanyName() {
		return companyName;
	}
}
