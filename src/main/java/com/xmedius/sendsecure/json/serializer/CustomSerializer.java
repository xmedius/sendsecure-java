package com.xmedius.sendsecure.json.serializer;

import com.google.gson.JsonElement;
import com.google.gson.GsonBuilder;

public abstract class CustomSerializer<T> {

	protected GsonBuilder builder;

	protected JsonElement defaultSerialization(T element) {
		initializeBuilder();
		return builder.create().toJsonTree(element);
	}

	protected void initializeBuilder() {
		builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
	}
}
