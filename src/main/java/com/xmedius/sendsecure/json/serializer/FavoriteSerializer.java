package com.xmedius.sendsecure.json.serializer;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.xmedius.sendsecure.helper.Favorite;

public class FavoriteSerializer extends CustomSerializer<Favorite> implements JsonSerializer<Favorite> {

    @Override
    public JsonElement serialize(final Favorite favorite, final Type type, final JsonSerializationContext context) {
        JsonObject jFavorite = new JsonObject();
        jFavorite.add("favorite", defaultSerialization(favorite));
        return jFavorite;
    }

}


