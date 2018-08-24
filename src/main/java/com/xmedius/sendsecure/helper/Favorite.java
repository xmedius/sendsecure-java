package com.xmedius.sendsecure.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class Favorite represents the attributes of a Favorite Account
 */
public class Favorite {

	@Expose(serialize = false, deserialize = true)
	@SerializedName("created_at")
	private Date createdAt;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("updated_at")
	private Date updatedAt;
	@Expose(serialize = false, deserialize = true)
	private int id;
	@Expose
	private String email;
	@Expose
	@SerializedName("first_name")
	private String firstName;
	@Expose
	@SerializedName("last_name")
	private String lastName;
	@Expose
	@SerializedName("company_name")
	private String companyName;
	@Expose
	@SerializedName("order_number")
	private int orderNumber;
	@Expose
	@SerializedName("contact_methods")
	private List<ContactMethod> contactMethods = new ArrayList<ContactMethod>();

	public Favorite(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
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

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public List<ContactMethod> getContactMethods() {
		return contactMethods;
	}

	public void prepareToDestroyContactMethods(List<Integer> contactMethodIds) {
		for(ContactMethod contactMethod: this.contactMethods) {
			if(contactMethodIds.contains(contactMethod.getId())) {
				contactMethod.destroyContact();
			}
		}
	}
}
