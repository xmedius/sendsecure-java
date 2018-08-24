package com.xmedius.sendsecure.json;

import java.util.List;

/**
 * Class SearchRecipientResponse builds an object containing the list of recipients that match the autocomplete search term.
 */
public class SearchRecipientResponse {

	private List<Recipient> results;

	public List<Recipient> getResults() {
		return results;
	}
}
