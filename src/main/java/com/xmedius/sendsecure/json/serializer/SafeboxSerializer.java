package com.xmedius.sendsecure.json.serializer;

import java.lang.reflect.Type;

import com.google.gson.JsonSerializer;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;

import java.util.Map;

import com.xmedius.sendsecure.helper.Safebox;
import com.xmedius.sendsecure.helper.SecurityOptions;
import com.xmedius.sendsecure.helper.Participant;
import com.xmedius.sendsecure.json.serializer.CustomSerializer;
import com.xmedius.sendsecure.json.serializer.ParticipantSerializer;

import com.google.gson.Gson;

public class SafeboxSerializer extends CustomSerializer<Safebox> implements JsonSerializer<Safebox> {

  @Override
  public JsonElement serialize(final Safebox safebox, final Type type, final JsonSerializationContext context) {
    JsonObject serializedSafebox = defaultSerialization(safebox).getAsJsonObject();

    JsonArray recipients = new JsonArray();
    for(JsonElement participant: serializedSafebox.remove("participants").getAsJsonArray()){
      recipients.add(participant.getAsJsonObject().remove("participant"));
    }
    serializedSafebox.add("recipients", recipients);
    serializedSafebox.add("document_ids", new Gson().toJsonTree(safebox.getDocumentIds()));

    SecurityOptions options = safebox.getSecurityOptions();
    if(options != null){
        for(Map.Entry<String, JsonElement> entry: context.serialize(options).getAsJsonObject().entrySet()){
            serializedSafebox.add(entry.getKey(), entry.getValue());
        }
    }

    JsonObject jSafebox = new JsonObject();
    jSafebox.add("safebox", serializedSafebox);
    return jSafebox;
  }

  protected void initializeBuilder() {
    super.initializeBuilder();
    builder = builder.registerTypeAdapter(Participant.class, new ParticipantSerializer());
  }

}