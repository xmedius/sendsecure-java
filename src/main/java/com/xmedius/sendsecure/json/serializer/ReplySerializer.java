package com.xmedius.sendsecure.json.serializer;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.xmedius.sendsecure.helper.Reply;

public class ReplySerializer extends CustomSerializer<Reply> implements JsonSerializer<Reply> {

	@Override
	public JsonElement serialize(Reply reply, Type type, JsonSerializationContext context) {
		JsonObject jReply = new JsonObject();
		jReply.add("safebox", defaultSerialization(reply));
		return jReply;
	}

}
