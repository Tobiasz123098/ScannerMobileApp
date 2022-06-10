package com.example.scannerqrapplication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;

import java.io.IOException;
import java.util.List;

public class JSONUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static <ANY> ANY mapToPojoList(Object json, TypeReference<ANY> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(json.toString(), typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't map json to pojo", e);
        }
    }

    public static <POJO> POJO mapToPojo(Object json, Class<POJO> pojoClass) {
        try {
            return OBJECT_MAPPER.readValue(json.toString(), pojoClass);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't map json to pojo", e);
        }
    }

}
