package com.xmedius.sendsecure.json;

import java.util.List;

import com.xmedius.sendsecure.helper.Safebox;

import com.google.gson.annotations.SerializedName;

/**
 * Class SafeboxesResponse builds an object containing the server response to Client.getSafeboxes method.
 */
public class SafeboxesResponse {

	private Integer count;
	@SerializedName("previous_page_url")
	private String previousPageUrl;
	@SerializedName("next_page_url")
	private String nextPageUrl;
	private List<Safebox> safeboxes;

	public List<Safebox> getSafeboxes() {
		return safeboxes;
	}

	public void setSafeboxes(List<Safebox> safeboxes) {
		this.safeboxes = safeboxes;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getPreviousPageUrl() {
		return previousPageUrl;
	}

	public void setPreviousPageUrl(String previousPageUrl) {
		this.previousPageUrl = previousPageUrl;
	}

	public String getNextPageUrl() {
		return nextPageUrl;
	}

	public void setNextPageUrl(String nextPageUrl) {
		this.nextPageUrl = nextPageUrl;
	}
}
