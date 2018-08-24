package com.xmedius.sendsecure.json.serializer;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.xmedius.sendsecure.helper.Safebox;

public class CommitSafeboxDeserializer implements JsonDeserializer<Safebox>{

	private String[] securityOptionsProperties = {
			"reply_enabled", "group_replies", "retention_period_type", "retention_period_value", "retention_period_unit",
			"encrypt_message", "double_encryption", "expiration_value", "expiration_unit", "security_code_length",
			"code_time_limit", "allowed_login_attempts", "allow_remember_me", "allow_sms", "allow_voice", "allow_email",
			"two_factor_required", "auto_extend_value", "auto_extend_unit", "delete_content_on", "allow_manual_delete",
			"allow_manual_close", "encrypt_attachments", "consent_group_id" };

	@Override
	public Safebox deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
		JsonObject jsonObject = json.getAsJsonObject();
		JsonObject securityOptions = new JsonObject();
		for (String property : securityOptionsProperties) {
			if (jsonObject.has(property)) {
				securityOptions.add(property, jsonObject.get(property));
				jsonObject.remove(property);
			}
		}
		jsonObject.add("security_options", securityOptions);

		return new Gson().fromJson(jsonObject, Safebox.class);
	}
}
