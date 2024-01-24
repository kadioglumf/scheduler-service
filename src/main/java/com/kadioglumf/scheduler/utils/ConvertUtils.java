package com.kadioglumf.scheduler.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvertUtils {

    private static final Logger log = LoggerFactory.getLogger(ConvertUtils.class);

    public static <T> T convertJsonDataToObject(String jsonDta, Class<T> clazz) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(jsonDta, clazz);
        } catch (JsonProcessingException ex) {
            ex.getStackTrace();
            log.error("convertJsonDataToObject method exception: ", ex);
        }
        return null;
    }

    public static String convertObjectToJsonData(Object value) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            ex.getStackTrace();
            log.error("convertJsonDataToObject method exception: ", ex);
        }
        return null;
    }
}
