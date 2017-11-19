package org.aaa.core.web.api.model.input.databinding.sinister;


import com.google.gson.*;
import org.aaa.core.web.api.model.JsonPolymorphicAdapter;
import org.aaa.core.web.api.model.ouput.DTO;
import org.aaa.core.web.common.http.exception.CustomHttpExceptions;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Type;

public class GsonPolymorphicAdapter implements JsonDeserializer<Serializable>, JsonSerializer<Serializable> {

    private static final String TYPE_PROPERTY_NAME = "__type__";

    @Override
    @SuppressWarnings("unchecked")
    public Serializable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        String typeAsString;
        Class<? extends Serializable> clazz;
        typeAsString = json.getAsJsonObject().get(TYPE_PROPERTY_NAME).getAsString();
        json.getAsJsonObject().remove(TYPE_PROPERTY_NAME);
        if(typeAsString == null)
            throw new CustomHttpExceptions.BadRequestException();

        try {
            typeAsString = getClass().getPackage().getName() + StringUtils.capitalize(typeAsString) + "Submission";
            clazz = (Class<? extends Serializable>) Class.forName(typeAsString);
        } catch(ClassNotFoundException | ClassCastException e) {
            throw new CustomHttpExceptions.BadRequestException();
        }
        return context.deserialize(json, clazz);
    }

    @Override
    public JsonElement serialize(Serializable serializable, Type type, JsonSerializationContext jsonSerializationContext) {
        Class<? extends Serializable> clazz;
        JsonElement jsonElement;

        clazz = serializable.getClass();
        jsonElement = jsonSerializationContext.serialize(serializable, clazz);
        jsonElement.getAsJsonObject().addProperty(TYPE_PROPERTY_NAME, clazz.getSimpleName().toLowerCase().replace(DTO.class.getSimpleName(), ""));

        return jsonElement;
    }
}
