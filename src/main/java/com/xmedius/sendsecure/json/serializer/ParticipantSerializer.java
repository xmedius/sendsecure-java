package com.xmedius.sendsecure.json.serializer;

import java.lang.reflect.Type;

import com.google.gson.JsonSerializer;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

import java.util.Map;

import com.xmedius.sendsecure.helper.Participant;
import com.xmedius.sendsecure.helper.GuestOptions;
import com.xmedius.sendsecure.json.serializer.CustomSerializer;

public class ParticipantSerializer extends CustomSerializer<Participant> implements JsonSerializer<Participant> {

    @Override
    public JsonElement serialize(final Participant participant, final Type type, final JsonSerializationContext context) {
        JsonObject serializedParticipant = defaultSerialization(participant).getAsJsonObject();

        GuestOptions options = participant.getGuestOptions();
        if(options != null){
            JsonObject serializedGuestOptions = context.serialize(options).getAsJsonObject();
            for(Map.Entry<String, JsonElement> entry: serializedGuestOptions.entrySet()) {
                serializedParticipant.add(entry.getKey(), entry.getValue());
            }
        }

        JsonObject jParticipant = new JsonObject();
        jParticipant.add("participant", serializedParticipant);
        return jParticipant;
    }

}
