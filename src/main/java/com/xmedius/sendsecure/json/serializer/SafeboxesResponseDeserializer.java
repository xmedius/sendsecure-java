package com.xmedius.sendsecure.json.serializer;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;

import com.xmedius.sendsecure.helper.Safebox;
import com.xmedius.sendsecure.json.SafeboxesResponse;

public class SafeboxesResponseDeserializer implements JsonDeserializer<SafeboxesResponse> {

	@Override
	public SafeboxesResponse deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
		JsonObject responseJson = json.getAsJsonObject();
		JsonArray safeboxesArray = responseJson.get("safeboxes").getAsJsonArray();
		List<Safebox> safeboxes = new ArrayList<Safebox>();
		Iterator<JsonElement> it = safeboxesArray.iterator();
		while(it.hasNext()){
			safeboxes.add(context.deserialize(it.next().getAsJsonObject().get("safebox"), Safebox.class));
		}
		SafeboxesResponse response = new SafeboxesResponse();
		response.setCount(responseJson.get("count").getAsInt());
		if (responseJson.get("previous_page_url").isJsonNull()) {
		    response.setPreviousPageUrl(null);
		} 
		else {
		    response.setPreviousPageUrl(responseJson.get("previous_page_url").getAsString());
		}
		if (responseJson.get("next_page_url").isJsonNull()) {
		    response.setNextPageUrl(null);
		} 
		else {
		    response.setNextPageUrl(responseJson.get("next_page_url").getAsString());
		}
		response.setSafeboxes(safeboxes);
		return response;
	}
}